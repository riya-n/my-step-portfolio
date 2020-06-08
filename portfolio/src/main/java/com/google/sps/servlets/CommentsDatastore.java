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

public class CommentsDatastore {

  public class Comment {
    private String comment;
    private long timestamp;

    public Comment(String comment, long timestamp) {
      this.comment = comment;
      this.timestamp = timestamp;
    }
  } 

    public CommentsDatastore() {}
    
    public List<Comment> fetchComments(int maxNumOfComments) {
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

      return comments;
    }

    public Comment addComment(String comment, int maxNumOfComments) {
      long timestamp = System.currentTimeMillis();

      if (comment != null && !comment.isEmpty()) {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("comment", comment);
        commentEntity.setProperty("timestamp", timestamp);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
      }

      Comment newComment = new Comment(comment, timestamp);

      return newComment;
    }

    public void deleteComments() {
      Query query = new Query("Comment");
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);
      for (Entity entity : results.asIterable()) {
        datastore.delete(entity.getKey());
      }
    }
}