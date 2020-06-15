package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.BasketballCourts;

/** Returns the accessible basketball courts in New York City */
@WebServlet("/basketball-courts")
public class BasketballCourtsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    List<BasketballCourts.BasketballCourt> accessibleCourts = BasketballCourts.getAccessibleBasketballCourts();
    String json = new Gson().toJson(accessibleCourts);
    response.getWriter().println(json);
  }
}
