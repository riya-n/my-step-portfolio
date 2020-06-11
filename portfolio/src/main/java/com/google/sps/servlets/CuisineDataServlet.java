package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.CuisineDatastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.sps.data.Constants;

@WebServlet("/cuisine-data")
public final class CuisineDataServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(CuisineDataServlet.class);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Map<String, Integer> cuisineVotes = CuisineDatastore.fetchCuisineVotes();

    Gson gson = new Gson();
    String json = gson.toJson(cuisineVotes);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    if (request.getParameter(Constants.CUISINE_PARAMETER) != null) {
      String cuisine = request.getParameter(Constants.CUISINE_PARAMETER);

      try {
        CuisineDatastore.addCuisineVote(cuisine);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        log.error(Constants.DOPOST_ILL_ARG_ERROR);
        response.setStatus(400);
        return;
      }
    }

    response.sendRedirect("/visuals.html");
  }
}
