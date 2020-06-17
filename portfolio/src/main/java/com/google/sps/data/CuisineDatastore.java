package com.google.sps.data;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableMap;
import com.google.sps.data.AvailableCuisine;

/** Class that handles writing/reading cuisine data from the datastore. */
public final class CuisineDatastore {
    
  private static final Logger log = Logger.getLogger(CuisineDatastore.class.getName());

  public static final String CUISINE_ENTITY = "CuisineVotes";
  public static final String CUISINE_PARAMETER = "cuisine";
  public static final String USERID_PARAMETER = "userId";

  private CuisineDatastore() {}

  /** Method that retreives cuisine entities from the datastore and formats it. */
  public static Map<AvailableCuisine, Integer> fetchCuisineVotes() {
    Query query = new Query(CUISINE_ENTITY);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
      
    EnumMap<AvailableCuisine, Integer> cuisineVotes = new EnumMap<>(AvailableCuisine.class);

    for (AvailableCuisine cuisineId : AvailableCuisine.values()) {
      cuisineVotes.put(cuisineId, 0);
    }

    for (Entity entity : results.asIterable()) {
      String dbCuisineId = (String) entity.getProperty(CUISINE_PARAMETER);
      try {
        AvailableCuisine cuisineId = AvailableCuisine.getFromId(dbCuisineId);
        Integer votes = cuisineVotes.get(cuisineId);
        cuisineVotes.put(cuisineId, votes + 1);
      } catch (IllegalArgumentException e) {
        log.log(Level.SEVERE, "AvailableCuisine not found for id", e);
      }
    }

    ImmutableMap<AvailableCuisine, Integer> immuMap =  ImmutableMap.<AvailableCuisine, Integer>builder()
      .putAll(cuisineVotes).build(); 

    return immuMap;
  }

  /** Method that updates the cuisine vote in the datastore.
    Method throws {@link IllegalArgumentException} if the cuisine or userId is empty.
    {@code null} */
  public static void addCuisineVote(String userId, AvailableCuisine cuisineId) {
    if (userId.isEmpty()) {
      throw new IllegalArgumentException("userId should not be empty.");
    }

    Query.Filter filter = new Query.FilterPredicate(USERID_PARAMETER,
      Query.FilterOperator.EQUAL, userId);
    Query query = new Query(CUISINE_ENTITY).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    // need to handle PreparedQuery.TooManyResultsException?
    Entity entity = datastore.prepare(query).asSingleEntity();

    if (entity == null) {
      Entity cuisineVoteEntity = new Entity(CUISINE_ENTITY);
      cuisineVoteEntity.setProperty(USERID_PARAMETER, userId);
      cuisineVoteEntity.setProperty(CUISINE_PARAMETER, cuisineId.getId());
      datastore.put(cuisineVoteEntity);
    } else {
      entity.setProperty(CUISINE_PARAMETER, cuisineId.getId());
      datastore.put(entity);
    }
  }
}
