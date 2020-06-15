package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.google.gson.Gson;
import com.google.sps.data.CommentsDatastore;
import com.google.sps.data.Constants;

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
    String commentsLimitStr = (String) request.getParameter(Constants.COMMENTS_LIMIT_PARAMETER);
    if (commentsLimitStr.isEmpty()) {
      throw new IllegalArgumentException("comments limit should not be empty");
    }

    Integer commentsLimit = Integer.parseInt(commentsLimitStr);

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
    String comment = request.getParameter(Constants.COMMENT_PARAMETER);

    try {
      CommentsDatastore.addComment(comment);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      response.setStatus(400);
      return;
    }

    response.sendRedirect("/comments.html");
  }

}
