package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.BasketballCourtsDatastore;
import com.google.sps.data.BasketballCourt;

/** Returns the accessible basketball courts in New York City */
@WebServlet("/basketball-courts")
public final class BasketballCourtsServlet extends HttpServlet {
    
  BasketballCourtsDatastore basketballCourtsDatastore;
  List<BasketballCourt> accessibleCourts;

  @Override
  public void init() {
    basketballCourtsDatastore = new BasketballCourtsDatastore(getServletContext());
    accessibleCourts = basketballCourtsDatastore.getAccessibleCourts();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String json = new Gson().toJson(accessibleCourts);
    response.getWriter().println(json);
  }
}
