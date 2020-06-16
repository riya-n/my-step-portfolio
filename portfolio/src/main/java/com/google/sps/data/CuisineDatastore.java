package com.google.sps.data;

import java.util.Map;
import java.util.HashMap;
import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableMap;
import com.google.sps.data.Constants;
import com.google.sps.data.Constants.CuisineEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class that handles writing/reading cuisine data from the datastore. */
public final class CuisineDatastore {
    private static final Logger log = LoggerFactory.getLogger(CuisineDatastore.class);

  /** Class used to store cuisineId, cuisineName, and votes. */
  public static class CuisineVotes {
    private CuisineEnum cuisineId;
    private String cuisineName;
    private Integer votes;

    public CuisineVotes(CuisineEnum cuisineId, String cuisineName) {
      this.cuisineId = cuisineId;
      this.cuisineName = cuisineName;
      this.votes = 0;
    }

    public void addVote() {
      this.votes = this.votes + 1;
    }

    public CuisineEnum getCuisineId() {
      return this.cuisineId;
    }

    public String getCuisineName() {
      return this.cuisineName;
    }

    public Integer getVotes() {
      return this.votes;
    }
  }

    /** Method that retreives cuisine entities from the datastore and formats it. */
    public static Map<String, CuisineVotes> fetchCuisineVotes() {
      Query query = new Query(Constants.CUISINE_ENTITY);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      
      Map<String, CuisineVotes> cuisineVotes = new HashMap<>();
      for (CuisineEnum val : CuisineEnum.values()) {
        CuisineVotes cuisineObj = new CuisineVotes(val, val.getLocalizedName());
        cuisineVotes.put(val.getLocalizedName(), cuisineObj);
      }

      for (Entity entity : results.asIterable()) {
        String cuisine = (String) entity.getProperty(Constants.CUISINE_PARAMETER);
        CuisineVotes cuisineObj = cuisineVotes.get(cuisine);
        cuisineObj.addVote();
        cuisineVotes.replace(cuisine, cuisineObj);
      }

      ImmutableMap<String, CuisineVotes> immuMap =  ImmutableMap.<String, CuisineVotes>builder()
        .putAll(cuisineVotes).build(); 

      return immuMap;
    }

    /** Method that updates the cuisine vote in the datastore.
    Method throws {@link IllegalArgumentException} if the cuisine or userId is empty.
    {@code null} */
    public static void addCuisineVote(String userId, String cuisine) {
      if (cuisine.isEmpty() || userId.isEmpty()) {
        throw new IllegalArgumentException("cuisine and userId should not be empty.");
      }

      CuisineEnum cuisineId = null;
      for (CuisineEnum val : CuisineEnum.values()) {
        if (val.getLocalizedName().toLowerCase().equals(cuisine)) {
          cuisineId = val;
        }
      }

      if (cuisineId == null) {
        throw new IllegalArgumentException("cuisine is not from the list of valid cuisines");
      }

      Query.Filter filter = new Query.FilterPredicate(Constants.USERID_PARAMETER,
        Query.FilterOperator.EQUAL, userId);
      Query query = new Query(Constants.CUISINE_ENTITY).setFilter(filter);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      boolean updated = false;

      for (Entity entity : results.asIterable()) {
        entity.setProperty(Constants.CUISINE_PARAMETER, cuisineId.getLocalizedName());
        datastore.put(entity);
        updated = true;
      }

      if (!updated) {
        Entity cuisineVoteEntity = new Entity(Constants.CUISINE_ENTITY);
        cuisineVoteEntity.setProperty(Constants.USERID_PARAMETER, userId);
        cuisineVoteEntity.setProperty(Constants.CUISINE_PARAMETER, cuisineId.getLocalizedName());
        datastore.put(cuisineVoteEntity);
      }

    }
}
