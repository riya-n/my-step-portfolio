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
import com.google.gson.Gson;
import com.google.sps.data.CommentsDatastore;

/** Servlet that handles comments data. */
@WebServlet("/comments")
public final class CommentsServlet extends HttpServlet {

  /** Class that creates an object to hold the comments
  and limit on the number of comments displayed. */
  public final class CommentsApiResponse {
    private List<CommentsDatastore.Comment> comments;

    public CommentsApiResponse(List<CommentsDatastore.Comment> comments) {
      this.comments = comments;
    }

    /** Returns list of comments. */
    public List<CommentsDatastore.Comment> getComments() {
      return this.comments;
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer commentsLimit = Integer.parseInt(request.getParameter("commentsLimit"));

    List<CommentsDatastore.Comment> comments = CommentsDatastore.fetchComments(commentsLimit);

    CommentsApiResponse commentsData =
        new CommentsApiResponse(comments);

    Gson gson = new Gson();
    String json = gson.toJson(commentsData);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");

    try {
      CommentsDatastore.addComment(comment);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      response.setStatus(400);
    }

    response.sendRedirect("/comments.html");
  }

}
