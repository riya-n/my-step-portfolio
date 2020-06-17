package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.CuisineDatastore;
import com.google.sps.data.AvailableCuisine;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.Enum;

@WebServlet("/cuisine-data")
public final class CuisineDataServlet extends HttpServlet {
  
  private static final Logger log = Logger.getLogger(CuisineDataServlet.class.getName());

  public static final String CUISINEID_PARAMETER = "cuisineId";
  public static final String USERID_PARAMETER = "userId";

  /** Class that creates an object to hold the cuisineId, cuisineName,
    and the number of votes for that cuisine. */
  public final class CuisineApiResponse {
    private final AvailableCuisine cuisineId;
    private final String cuisineName;
    private final int votes;

    public CuisineApiResponse(AvailableCuisine cuisineId,
      String cuisineName, int votes) {
        this.cuisineId = cuisineId;
        this.cuisineName = cuisineName;
        this.votes = votes;
      }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Map<AvailableCuisine, Integer> cuisineVotes = CuisineDatastore.fetchCuisineVotes();
   
    List<CuisineApiResponse> cuisineVotesResponse = new ArrayList<>();
    for (AvailableCuisine cuisineId : cuisineVotes.keySet()) {
      cuisineVotesResponse.add(new CuisineApiResponse(cuisineId,
        cuisineId.getLocalizedName(), cuisineVotes.get(cuisineId)));
    }

    Gson gson = new Gson();
    String json = gson.toJson(cuisineVotesResponse);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    JsonObject cuisineVote = new Gson().fromJson(request.getReader(), JsonObject.class);

    if (cuisineVote.isJsonNull()) {
      log.severe("cuisine vote should not be null");
      response.setStatus(400);
      return;
    }

    JsonElement userIdEl = cuisineVote.get(USERID_PARAMETER);
    JsonElement cuisineEl = cuisineVote.get(CUISINEID_PARAMETER);

    if (userIdEl.isJsonNull() || cuisineEl.isJsonNull()) {
      log.severe("user id and cuisine id should not be null");
      response.setStatus(400);
      return;
    }

    String userId = userIdEl.getAsString();
    String cuisine = cuisineEl.getAsString();

    try {
      AvailableCuisine cuisineId = AvailableCuisine.getFromId(cuisine);
      CuisineDatastore.addCuisineVote(userId, cuisineId);
    } catch (IllegalArgumentException e) {
      log.log(Level.SEVERE,
        "availableCuisine not found from id or error in adding vote for cuisine", e);
      response.setStatus(400);
      return;
    }

    response.sendRedirect("/visuals.html");
  }
}
