package com.google.sps.data;

import java.util.Map;
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
      
      ImmutableMap.Builder<String, Integer> cuisineVotes = ImmutableMap.builder();
      for (Entity entity : results.asIterable()) {
        String cuisine = (String) entity.getProperty(Constants.CUISINE_PARAMETER);
        Long votes = (Long) entity.getProperty(Constants.VOTES_PARAMETER);
        cuisineVotes.put(cuisine, votes.intValue());
      }

      return cuisineVotes.build();
    }

    /** Method that updates the cuisine vote in the datastore.
    Method throws {@link IllegalArgumentException} if the cuisine is empty or null */
    public static void addCuisineVote(String cuisine) {
      if (cuisine.isEmpty()) {
        throw new IllegalArgumentException(Constants.CUISINE_EMPTY_ERROR);
      }

      Query.Filter filter = new Query.FilterPredicate(Constants.CUISINE_PARAMETER,
        Query.FilterOperator.EQUAL, cuisine);
      Query query = new Query(Constants.CUISINE_ENTITY).setFilter(filter);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      boolean updated = false;

      for (Entity entity : results.asIterable()) {
        Integer votes = ((Long) entity.getProperty(Constants.VOTES_PARAMETER)).intValue();
        entity.setProperty(Constants.VOTES_PARAMETER, votes + 1);
        datastore.put(entity);
        updated = true;
      }

      if (!updated) {
        Entity cuisineEntity = new Entity(Constants.CUISINE_ENTITY);
        cuisineEntity.setProperty(Constants.CUISINE_PARAMETER, cuisine);
        cuisineEntity.setProperty(Constants.VOTES_PARAMETER, 1);
        datastore.put(cuisineEntity);
      }

    }
}
