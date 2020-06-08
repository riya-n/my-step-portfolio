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
import static com.google.sps.servlets.Constants.DEFAULT_COMMENTS_DISPLAYED;
import com.google.sps.servlets.CommentsDatastore.Comment;

/** Servlet that handles comments data. */
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {

  private int maxNumOfComments;
  private CommentsDatastore commentsDatastore;

  public class CommentsApiResponse {
    private int maxNumOfComments;
    private List<Comment> comments;

    public CommentsApiResponse(int maxNumOfComments,
      List<Comment> comments) {
      this.maxNumOfComments = maxNumOfComments;
      this.comments = comments;
    }
  }

  @Override
  public void init() {
    maxNumOfComments = DEFAULT_COMMENTS_DISPLAYED;
    commentsDatastore = new CommentsDatastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    List<Comment> comments = commentsDatastore.fetchComments(maxNumOfComments);

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
    String maxNum = request.getParameter("number-of-comments");

    if (maxNum != null) {
      maxNumOfComments = Integer.parseInt(maxNum);
    }
    
    Comment newComment = commentsDatastore.addComment(comment, maxNumOfComments);

    Gson gson = new Gson();
    String json = gson.toJson(newComment);

    response.setContentType("application/json;");
    response.getWriter().println(json);
    response.sendRedirect("/comments.html");
  }

}
