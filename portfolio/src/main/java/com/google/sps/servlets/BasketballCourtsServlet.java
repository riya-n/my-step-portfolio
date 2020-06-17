package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.BasketballCourtsDatastore;

/** Returns the accessible basketball courts in New York City */
@WebServlet("/basketball-courts")
public final class BasketballCourtsServlet extends HttpServlet {
  
  private static final Logger log = Logger.getLogger(BasketballCourtsServlet.class.getName());
  public static final String JSON_FILE_PATH = "/WEB-INF/basketball-courts-nyc.json";
  List<JsonObject> accessibleCourts;

  @Override
  public void init() {
    try(Reader reader = new InputStreamReader(getServletContext().getResourceAsStream(JSON_FILE_PATH))) {
      JsonArray allCourts = new Gson().fromJson(reader, JsonArray.class);
      accessibleCourts = BasketballCourtsDatastore.getAccessibleBasketballCourts(allCourts);
    } catch (IOException e) {
      log.log(Level.SEVERE, "IOException caught when closing reader", e);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String json = new Gson().toJson(accessibleCourts);
    response.getWriter().println(json);
  }
}
