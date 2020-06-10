package com.google.sps.data;

import java.util.Map;
import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableMap; 

/** Class that handles writing/reading cuisine data from the datastore. */
public final class CuisineDatastore {
    
    /** Method that retreives cuisine entities from the datastore and formats it. */
    public static Map<String, Integer> fetchCuisine() {
      Query query = new Query("Cuisine");
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      
      ImmutableMap.Builder<String, Integer> cuisineVotes = ImmutableMap.builder();
      for (Entity entity : results.asIterable()) {
        String cuisine = (String) entity.getProperty("cuisine");
        Long votes = (Long) entity.getProperty("votes");
        cuisineVotes.put(cuisine, votes.intValue());
      }

      return cuisineVotes.build();
    }

    /** Method that updates the cuisine vote in the datastore.
    Method throws {@link IllegalArgumentException} if the cuisine is empty or null */
    public static void updateCuisine(String cuisine) {
      if (cuisine.isEmpty() || cuisine == null) {
        throw new IllegalArgumentException("Cuisine should not be empty or null");
      }

      // TODO: filter directly by property
      Query query = new Query("Cuisine");
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      boolean bool = false;
      for (Entity entity : results.asIterable()) {
        if (entity.getProperty("cuisine").equals(cuisine)) {
          bool = true;
          Integer votes = ((Long) entity.getProperty("votes")).intValue();
          entity.setProperty("votes", votes + 1);
          datastore.put(entity);
          break;
        }
      }

      if (!bool) {
        Entity cuisineEntity = new Entity("Cuisine");
        cuisineEntity.setProperty("cuisine", cuisine);
        cuisineEntity.setProperty("votes", 1);
        datastore.put(cuisineEntity);
      }

    }
}