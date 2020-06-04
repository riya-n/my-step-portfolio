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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
  final static Logger log = LoggerFactory.getLogger(DeleteDataServlet.class);

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    for (Entity key : results.asIterable()) {
      datastore.delete(key.getKey());
    }

    // response.setContentType("text/html;");
    // response.getWriter().println("");
    // response.sendRedirect("/index.html"); 
  }

}