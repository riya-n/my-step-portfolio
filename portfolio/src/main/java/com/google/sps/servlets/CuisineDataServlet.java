package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.CuisineDatastore;

@WebServlet("/cuisine-data")
public class CuisineDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Map<String, Integer> cuisineVotes = CuisineDatastore.fetchCuisine();

    Gson gson = new Gson();
    String json = gson.toJson(cuisineVotes);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String cuisine = request.getParameter("cuisine");

    try {
      CuisineDatastore.updateCuisine(cuisine);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      response.setStatus(400);
      return;
    }

    response.sendRedirect("/visuals.html");
  }
}
