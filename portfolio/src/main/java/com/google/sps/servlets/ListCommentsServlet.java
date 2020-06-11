package com.google.sps.servlets; 
 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/** Servlet responsible for listing c. */
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("date", SortDirection.DESCENDING);
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
 
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
     	String name = (String) entity.getProperty("name");
      String date = (String) entity.getProperty("date");
      String commentText = (String) entity.getProperty("comment");

      Comment comment = new Comment(name, date, commentText);
      comments.add(comment);
    }
 
    if(!comments.isEmpty()) {
      Gson gson = new Gson();
 
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(comments));
    } else {
      response.setContentType("text/html");
      response.getWriter().println("There are no comments to show.");
    }
  }
}
 

