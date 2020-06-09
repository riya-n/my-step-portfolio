package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.google.sps.data.CommentsDatastore.deleteComments;

/** Servlet that handles deletion of all comments. */
@WebServlet("/delete-comments")
public final class DeleteCommentsServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    deleteComments();

  }

}
