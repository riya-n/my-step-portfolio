package com.google.sps.data;

import java.util.List;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.ImmutableList; 
import com.google.sps.data.Comment;

/** Class that handles writing/reading data from the datastore. */
public final class CommentsDatastore {

  public static final String COMMENT_ENTITY = "Comment";
  public static final String COMMENT_PARAMETER = "comment";
  public static final String TIMESTAMP_PARAMETER = "timestamp";
  public static final String COMMENTS_LIMIT_PARAMETER = "commentsLimit";

  private CommentsDatastore() {}
    
    /** Method that retreives comments from the datastore and formats it. */
    public static List<Comment> fetchComments(int commentsLimit) {
      Query query = new Query(COMMENT_ENTITY)
        .addSort(TIMESTAMP_PARAMETER, SortDirection.DESCENDING);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      List<Entity> results = datastore.prepare(query)
        .asList(FetchOptions.Builder.withLimit(commentsLimit));
      
      ImmutableList.Builder<Comment> comments = ImmutableList.builder();
      for (Entity entity : results) {
        String comment = (String) entity.getProperty(COMMENT_PARAMETER);
        long timestamp = (long) entity.getProperty(TIMESTAMP_PARAMETER);
        Comment newComment = new Comment(comment, timestamp);
        comments.add(newComment);
      }

      return comments.build();
    }

    /** Method that adds a comment to the datastore.
    Method throws {@link IllegalArgumentException} if the comment is empty */
    public static void addComment(String comment) {
      if (comment.isEmpty()) {
        throw new IllegalArgumentException("comment should not be empty");
      }
      
      long timestamp = System.currentTimeMillis();

      Entity commentEntity = new Entity(COMMENT_ENTITY);
      commentEntity.setProperty(COMMENT_PARAMETER, comment);
      commentEntity.setProperty(TIMESTAMP_PARAMETER, timestamp);

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
