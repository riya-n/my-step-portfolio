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
import java.util.*;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> data;
  private List<Person> people;

  public class Person {
      String name;
      String birthday;

      Person(String name, String birthday) {
          this.name = name;
          this.birthday = birthday;
      }
  }

  @Override
  public void init() {
    data = new ArrayList<>();
    data.add("first message");
    data.add("second message");
    data.add("third message");

    people = new ArrayList<>();
    people.add(new Person("Riya", "22Nov"));
    people.add(new Person("Sana", "2Aug"));
    people.add(new Person("Mars", "8Mar"));
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String dataJson = gson.toJson(data);
    String peopleJson = gson.toJson(people);
    response.setContentType("application/json;");
    // response.setContentType("text/html;");
    response.getWriter().println(dataJson);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    data.add(comment);
    response.setContentType("text/html;");
    response.getWriter().println(comment);
    response.sendRedirect("/index.html");
  }

}
