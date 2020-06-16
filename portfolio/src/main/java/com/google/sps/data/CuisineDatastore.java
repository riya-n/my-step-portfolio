package com.google.sps.data;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableMap;
import com.google.sps.data.AvailableCuisines;
import com.google.sps.data.CuisineVotes;

/** Class that handles writing/reading cuisine data from the datastore. */
public final class CuisineDatastore {
  
  public static final String CUISINE_ENTITY = "CuisineVotes";
  public static final String CUISINE_PARAMETER = "cuisine";
  public static final String USERID_PARAMETER = "userId";

  private CuisineDatastore() {}

    /** Method that retreives cuisine entities from the datastore and formats it. */
    public static Map<AvailableCuisines, Integer> fetchCuisineVotes() {
      Query query = new Query(CUISINE_ENTITY);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      
      EnumMap<AvailableCuisines, Integer> cuisineVotes = new EnumMap<>(AvailableCuisines.class);

      for (AvailableCuisines cuisineId : AvailableCuisines.values()) {
        cuisineVotes.put(cuisineId, 0);
      }

      for (Entity entity : results.asIterable()) {
        String cuisine = (String) entity.getProperty(CUISINE_PARAMETER); //store the name of the enum (capitalized)
        AvailableCuisines cuisineId = AvailableCuisines.getFromId(cuisine);
        Integer votes = cuisineVotes.get(cuisineId);
        if (votes == null) {
          votes = 0;
        }
        cuisineVotes.put(cuisineId, votes + 1);
      }

      ImmutableMap<AvailableCuisines, Integer> immuMap =  ImmutableMap.<AvailableCuisines, Integer>builder()
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

      AvailableCuisines cuisineId = null;
      for (AvailableCuisines val : AvailableCuisines.values()) {
        if (val.getLocalizedName().equals(cuisine)) {
          cuisineId = val;
        }
      }

      if (cuisineId == null) {
        throw new IllegalArgumentException("cuisine is not from the list of valid cuisines");
      }

      Query.Filter filter = new Query.FilterPredicate(USERID_PARAMETER,
        Query.FilterOperator.EQUAL, userId);
      Query query = new Query(CUISINE_ENTITY).setFilter(filter);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      boolean updated = false;

      for (Entity entity : results.asIterable()) {
        entity.setProperty(CUISINE_PARAMETER, cuisineId.getId());
        datastore.put(entity);
        updated = true;
      }

      if (!updated) {
        Entity cuisineVoteEntity = new Entity(CUISINE_ENTITY);
        cuisineVoteEntity.setProperty(USERID_PARAMETER, userId);
        cuisineVoteEntity.setProperty(CUISINE_PARAMETER, cuisineId.getId());
        datastore.put(cuisineVoteEntity);
      }

    }
}
