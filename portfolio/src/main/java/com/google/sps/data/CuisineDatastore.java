package com.google.sps.data;

import java.util.Map;
import java.util.HashMap;
import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableMap;
import com.google.sps.data.Constants;

/** Class that handles writing/reading cuisine data from the datastore. */
public final class CuisineDatastore {
    
    /** Method that retreives cuisine entities from the datastore and formats it. */
    public static Map<String, Integer> fetchCuisineVotes() {
      Query query = new Query(Constants.CUISINE_ENTITY);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      
      Map<String, Integer> cuisineVotes = new HashMap<>();

      for (Entity entity : results.asIterable()) {
        String cuisine = (String) entity.getProperty(Constants.CUISINE_PARAMETER);
        Integer votes = cuisineVotes.get(cuisine);
        if (votes == null) {
          cuisineVotes.put(cuisine, 1);
        } else {
          cuisineVotes.replace(cuisine, votes + 1);
        }
      }

      ImmutableMap<String, Integer> immuMap =  ImmutableMap.<String, Integer>builder()
        .putAll(cuisineVotes).build(); 

      return immuMap;
    }

    /** Method that updates the cuisine vote in the datastore.
    Method throws {@link IllegalArgumentException} if the cuisine or userId is empty or null */
    public static void addCuisineVote(String userId, String cuisine) {
      if (cuisine.isEmpty() || userId.isEmpty()) {
        throw new IllegalArgumentException(Constants.CUISINE_EMPTY_ERROR);
      }

      Query.Filter filter = new Query.FilterPredicate(Constants.USERID_PARAMETER,
        Query.FilterOperator.EQUAL, userId);
      Query query = new Query(Constants.CUISINE_ENTITY).setFilter(filter);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      boolean updated = false;

      for (Entity entity : results.asIterable()) {
        entity.setProperty(Constants.CUISINE_PARAMETER, cuisine);
        datastore.put(entity);
        updated = true;
      }

      if (!updated) {
        Entity cuisineVoteEntity = new Entity(Constants.CUISINE_ENTITY);
        cuisineVoteEntity.setProperty(Constants.USERID_PARAMETER, userId);
        cuisineVoteEntity.setProperty(Constants.CUISINE_PARAMETER, cuisine);
        datastore.put(cuisineVoteEntity);
      }

    }
}
