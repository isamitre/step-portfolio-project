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

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing comments. */
@WebServlet("/new-comment")
public class NewCommentServlet extends HttpServlet {

  public static String getCurrentDate() {
    DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);  
    LocalDateTime now = LocalDateTime.now();  
    String output = dtf.format(now);
    return output;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String author = request.getParameter("name");
    // if (author.length() < 2) {
    //   System.out.println("Name too short");
    //   response.setContentType("text/html");
    //   response.getWriter().println("Please enter a name that has at least 2 characters");
    // }

    String commentText = request.getParameter("comment-box");
    // if (commentText.length() < 2) {
    //   System.out.println("Comment too short");
    //   response.setContentType("text/html");
    //   response.getWriter().println("Please enter a comment that has at least 2 characters");
    // }
    Document doc =
        Document.newBuilder().setContent(commentText).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    Entity com = new Entity("Comment");
    com.setProperty("author", author);
    com.setProperty("commentText", commentText);
    com.setProperty("date", getCurrentDate());
    com.setProperty("sentiment", score);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(com);
    
    response.sendRedirect("html/pages/SecretTalents.html");
  }


}
