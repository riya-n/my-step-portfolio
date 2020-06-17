package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.CuisineDatastore;
import com.google.sps.data.AvailableCuisines;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.Enum;

@WebServlet("/cuisine-data")
public final class CuisineDataServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(CuisineDataServlet.class.getName());

  /** Class that creates an object to hold the cuisineId, cuisineName,
    and the number of votes for that cuisine. */
  public final class CuisineApiResponse {
    private AvailableCuisines cuisineId;
    private String cuisineName;
    private int votes;

    public CuisineApiResponse(AvailableCuisines cuisineId,
      String cuisineName, int votes) {
        this.cuisineId = cuisineId;
        this.cuisineName = cuisineName;
        this.votes = votes;
      }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Map<AvailableCuisines, Integer> cuisineVotes = CuisineDatastore.fetchCuisineVotes();
   
    List<CuisineApiResponse> cuisineVotesResponse = new ArrayList<>();
    for (AvailableCuisines cuisineId : cuisineVotes.keySet()) {
      cuisineVotesResponse.add(new CuisineApiResponse(cuisineId,
        cuisineId.getLocalizedName(), cuisineVotes.get(cuisineId)));
    }

    Gson gson = new Gson();
    String json = gson.toJson(cuisineVotesResponse);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /** Class used to parse the json object in post request body. */
  public class UserCuisineVote {
    private String userId;
    private String cuisineId;

    public String getUserId() {
      return this.userId;
    }

    public String getCuisineId() {
      return this.cuisineId;
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    UserCuisineVote cuisineVote = new Gson().fromJson(request.getReader(), UserCuisineVote.class);

    if (cuisineVote == null) {
      log.severe("cuisine vote should not be null");
      response.setStatus(400);
      return;
    }

    String userId = cuisineVote.getUserId();
    String cuisine = cuisineVote.getCuisineId();

    try {
      AvailableCuisines cuisineId = AvailableCuisines.getFromId(cuisine);
      CuisineDatastore.addCuisineVote(userId, cuisineId);
    } catch (IllegalArgumentException e) {
      log.log(Level.SEVERE, "IllegalArg caught in CuisineDataServlet", e);
      response.setStatus(400);
      return;
    } catch (NullPointerException e) {
      log.log(Level.SEVERE, "NPE caught in CuisineDataServlet", e);
      response.setStatus(400);
      return; 
    }

    response.sendRedirect("/visuals.html");
  }
}
