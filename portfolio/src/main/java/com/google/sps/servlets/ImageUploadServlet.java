package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process the request using the file URL from Blobstore.
 */
@WebServlet("/upload-image")
public class ImageUploadServlet extends HttpServlet {
      private static final Logger log = LoggerFactory.getLogger(ImageUploadServlet.class);


    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        log.info("blobs: " + blobs);
        List<BlobKey> blobKeys = blobs.get("uploaded-image");
        log.info("blobkeySS: " + blobKeys);    
        if (blobKeys == null || blobKeys.isEmpty()) {
            log.info("empty called");
            response.sendRedirect("/comments.jsp");
        } else {
            log.info("else statement");
            log.info("theblobkey: " + blobKeys.get(0).getKeyString());
            response.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
        }



    // String imageUrl = getUploadedFileUrl(request, "uploaded-image");

    // response.setContentType("text/html;");
    // response.getWriter().println("<img src=\"" + imageUrl + "\" height=\"400\">");


  }

//   private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
//     BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//     Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
//     List<BlobKey> blobKeys = blobs.get(formInputElementName);

//     if (blobKeys == null || blobKeys.isEmpty()) {
//       return null;
//     }

//     BlobKey blobKey = blobKeys.get(0);

//     BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
//     if (blobInfo.getSize() == 0) {
//       blobstoreService.delete(blobKey);
//       return null;
//     }

//     ImagesService imagesService = ImagesServiceFactory.getImagesService();
//     ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

//     try {
//       URL url = new URL(imagesService.getServingUrl(options));
//       return url.getPath();
//     } catch (MalformedURLException e) {
//       return imagesService.getServingUrl(options);
//     }
//   }
}
