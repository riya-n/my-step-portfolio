package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.google.gson.Gson;
import com.google.sps.data.CommentsDatastore;
import com.google.sps.data.Comment;
import java.util.logging.Logger;
import java.util.logging.Level;

/** Servlet that handles comments data. */
@WebServlet("/comments")
public final class CommentsServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CommentsServlet.class.getName());

  /** Class that creates an object to hold the comments
  and limit on the number of comments displayed. */
  public final class CommentsApiResponse {
    private final List<Comment> comments;

    public CommentsApiResponse(List<Comment> comments) {
      this.comments = comments;
    }

    /** Returns list of comments. */
    public List<Comment> getComments() {
      return this.comments;
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentsLimitStr = (String) request.getParameter(CommentsDatastore.COMMENTS_LIMIT_PARAMETER);
    
    if (commentsLimitStr == null || commentsLimitStr.isEmpty()) {
      log.severe("commentsLimitStr should not be null or empty");
      response.setStatus(400);
      return;
    }

    Integer commentsLimit;

    try {
      commentsLimit = Integer.parseInt(commentsLimitStr);
    } catch (NumberFormatException e) {
      log.log(Level.SEVERE, "commentsLimitStr cannot be parsed for Integer", e);
      response.setStatus(400);
      return;
    }

    List<Comment> comments = CommentsDatastore.fetchComments(commentsLimit);
    CommentsApiResponse commentsData = new CommentsApiResponse(comments);

    Gson gson = new Gson();
    String json = gson.toJson(commentsData);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter(CommentsDatastore.COMMENT_PARAMETER);

    if (comment == null) {
      log.severe("comment should not be null");
      response.setStatus(400);
      return;
    }

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
