package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.CuisineDatastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.sps.data.Constants;
import java.util.stream.Collectors;

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

  /** Class used to parse the json object in post request body. */
  public class CuisineVote {
    private String userId;
    private String cuisine;

    public String getUserId() {
      return this.userId;
    }

    public String getCuisine() {
      return this.cuisine;
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    CuisineVote cuisineVote = new Gson().fromJson(request.getReader(), CuisineVote.class);

    if (cuisineVote == null) {
      throw new IllegalArgumentException("cuisine vote shouldn't be null");
    }

    String userId = cuisineVote.getUserId();
    String cuisine = cuisineVote.getCuisine();

    try {
      CuisineDatastore.addCuisineVote(userId, cuisine.toLowerCase());
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      log.error(Constants.DOPOST_ILL_ARG_ERROR);
      response.setStatus(400);
      return;
    }

    response.sendRedirect("/visuals.html");
  }
}
