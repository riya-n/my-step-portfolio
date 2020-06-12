package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.sps.data.Constants;

/** Returns the accessible basketball courts in New York City */
@WebServlet("/basketball-courts")
public class BasketballCourtsServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(BasketballCourtsServlet.class);

  Gson gson;
  JsonArray allCourts;
  JsonArray accessibleCourts;

  @Override
  public void init() {
    try {
      String path = Constants.JSON_FILE_PATH;
      BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

      gson = new Gson();
      allCourts = gson.fromJson(bufferedReader, JsonArray.class);
      accessibleCourts = new JsonArray();

      for (JsonElement court : allCourts) {
        JsonObject courtObj = court.getAsJsonObject();
        JsonElement accessibleEl = courtObj.get(Constants.ACCESSIBLE_PROPERTY);
        if (!accessibleEl.isJsonNull()) {
          String accessibleStr = accessibleEl.getAsString();
          if (accessibleStr.equals(Constants.IS_ACCESSIBLE)) {
            accessibleCourts.add(court);
          }
        }
      }
    } catch(FileNotFoundException e) {
      e.printStackTrace();
      log.info(Constants.FILE_NOT_FOUND);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String json = gson.toJson(accessibleCourts);
    response.getWriter().println(json);
  }
}
