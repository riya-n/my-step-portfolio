package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.ImmutableList; 

/** Class that handles writing/reading data from the datastore. */
public final class CommentsDatastore {

  /** Class to create Comment object. */
  public final static class Comment {
    private final String comment;
    private final long timestamp;

    public Comment(String comment, long timestamp) {
      this.comment = comment;
      this.timestamp = timestamp;
    }

    /** Returns comment. */
    public String getComment() {
        return this.comment;
    }

    /** Returns timestamp of when comment was created. */
    public long getTimestamp() {
        return this.timestamp;
    }
  } 
    
    /** Method that retreives comments from the datastore and formats it. */
    public static List<Comment> fetchComments(int maxNumOfComments) {
      Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      List<Entity> results = datastore.prepare(query)
        .asList(FetchOptions.Builder.withLimit(maxNumOfComments));
      
      List<Comment> comments = new ArrayList<>();
      for (Entity entity : results) {
        String comment = (String) entity.getProperty("comment");
        long timestamp = (long) entity.getProperty("timestamp");
        Comment newComment = new Comment(comment, timestamp);
        comments.add(newComment);
      }

      ImmutableList<Comment> immutComments =
        ImmutableList.<Comment>builder().addAll(comments).build(); 

      return immutComments;
    }

    /** Method that adds a comment to the datastore. */
    public static void addComment(String comment) {
      if (comment.isEmpty()) {
        throw new IllegalArgumentException();
      }
      
      long timestamp = System.currentTimeMillis();

      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      commentEntity.setProperty("timestamp", timestamp);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    /** Method that deletes all the comments currently in the datastore. */
    public static void deleteComments() {
      Query query = new Query("Comment");
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      for (Entity entity : results.asIterable()) {
        datastore.delete(entity.getKey());
      }
    }
}
