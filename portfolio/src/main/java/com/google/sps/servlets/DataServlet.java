// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
import static com.google.sps.servlets.Constants.DEFAULT_COMMENTS_DISPLAYED;

/** Servlet that handles comments data. */
@WebServlet("/comments")
public class DataServlet extends HttpServlet {

  private int maxNumOfComments;

  public class CommentsApiResponse {
    private int maxNumOfComments;
    private List<Comment> comments;

    public CommentsApiResponse(int maxNumOfComments,
      List<Comment> comments) {
      this.maxNumOfComments = maxNumOfComments;
      this.comments = comments;
    }
  }

  public class Comment {
    private String comment;
    private long timestamp;

    public Comment(String comment, long timestamp) {
      this.comment = comment;
      this.timestamp = timestamp;
    }
  } 

  @Override
  public void init() {
    maxNumOfComments = DEFAULT_COMMENTS_DISPLAYED;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    
    CommentsApiResponse commentsData =
      new CommentsApiResponse(maxNumOfComments, comments);

    Gson gson = new Gson();
    String json = gson.toJson(commentsData);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();
    String maxNum = request.getParameter("number-of-comments");
    if (maxNum != null) {
      maxNumOfComments = Integer.parseInt(maxNum);
    }
    if (comment != null && !comment.isEmpty()) {
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      commentEntity.setProperty("timestamp", timestamp);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);

    }

    Comment newComment = new Comment(comment, timestamp);
    Gson gson = new Gson();
    String json = gson.toJson(newComment);
    response.setContentType("application/json;");
    response.getWriter().println(comment);
    response.sendRedirect("/comments.html");
  }

}
