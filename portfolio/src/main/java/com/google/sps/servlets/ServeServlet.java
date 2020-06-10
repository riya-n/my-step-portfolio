package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/serve")
public class ServeServlet extends HttpServlet {
      private static final Logger log = LoggerFactory.getLogger(ServeServlet.class);

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
            BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
            log.info("blobkey: " + blobKey);
            blobstoreService.serve(blobKey, response);
        }
}