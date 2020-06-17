package com.google.sps.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.servlet.ServletContext;
import javax.servlet.*;
import java.util.Scanner;

/** Parses the basketball courts file and returns a list of basketball courts */
public final class BasketballCourtsDatastore {

  private static final Logger log = Logger.getLogger(BasketballCourtsDatastore.class.getName());
  
  public static final String JSON_FILE_PATH = "/WEB-INF/basketball-courts-nyc.json";
  public static final String ACCESSIBLE_PROPERTY = "Accessible";
  public static final String IS_ACCESSIBLE = "Y";
  public static final String ID_PROPERTY = "Prop_ID";
  public static final String NAME_PROPERTY = "Name";
  public static final String LOCATION_PROPERTY = "Location";
  public static final String NUMCOURTS_PROPERTY = "Num_of_Courts";
  public static final String LAT_PROPERTY = "lat";
  public static final String LON_PROPERTY = "lon";

  List<JsonObject> accessibleCourts;
  JsonArray allCourts;

  public BasketballCourtsDatastore(ServletContext context) {
    try(Reader reader = new InputStreamReader(context.getResourceAsStream(JSON_FILE_PATH))) {
      allCourts = new Gson().fromJson(reader, JsonArray.class);
      accessibleCourts = BasketballCourtsDatastore.getAccessibleCourtsList(allCourts);
    } catch (IOException e) {
      log.log(Level.SEVERE, "Error when closing reader", e);
    }
  }

  /** Returns accessibleCourts list */
  public List<JsonObject> getAccessibleCourts() {
    if (accessibleCourts == null) {
      getAccessibleCourtsList(allCourts);
    }
    return accessibleCourts;
  }

  /** Returns a list of the accessible basketball courts. If there is a bad entry, it should be
    ignored and the other entries should be returned. */
  public static List<JsonObject> getAccessibleCourtsList(JsonArray allCourts) {

    ImmutableList.Builder<JsonObject> accessibleCourts =
      new ImmutableList.Builder<JsonObject>();

    for (JsonElement court : allCourts) {
        JsonObject courtObj = court.getAsJsonObject();
        JsonElement accessibleEl = courtObj.get(ACCESSIBLE_PROPERTY);
        if (!accessibleEl.isJsonNull()) {
          String accessibleStr = accessibleEl.getAsString();
          if (accessibleStr.equals(IS_ACCESSIBLE)) {
            try {
              accessibleCourts.add(formatCourtJson(courtObj));
            } catch (IllegalArgumentException e) {
              log.log(Level.SEVERE, "IllegalArg caught when creating court object from json", e);
            }
          }
        }
    }

    return accessibleCourts.build();
  }

  /** Creates a basketball court object given a json object of that court.
    * Throws {@link IllegalArgumentException} if the fields (not including numOfCourts) are null
    * or if numOfCourts is not greater than zero. NumOfCourts is null for most entries likely because
    * that data wasn't reported, but these entries still need to be included so numOfCourts=null is valid. */
  private static JsonObject formatCourtJson(JsonObject jsonCourt) {
    JsonElement idJson = jsonCourt.get(ID_PROPERTY);
    JsonElement nameJson = jsonCourt.get(NAME_PROPERTY);
    JsonElement locationJson = jsonCourt.get(LOCATION_PROPERTY);
    JsonElement numOfCourtsJson = jsonCourt.get(NUMCOURTS_PROPERTY);
    JsonElement latJson = jsonCourt.get(LAT_PROPERTY);
    JsonElement lonJson = jsonCourt.get(LON_PROPERTY);

    if (idJson.isJsonNull() || nameJson.isJsonNull() || locationJson.isJsonNull() ||
      latJson.isJsonNull() || lonJson.isJsonNull()) {
      throw new IllegalArgumentException("basketball court value should not be null");
    }

    String id = idJson.getAsString();
    String name = nameJson.getAsString();
    String location = locationJson.getAsString();
    double lat = latJson.getAsDouble();
    double lon = lonJson.getAsDouble();
    Integer numOfCourts = null;
    if (!numOfCourtsJson.isJsonNull()) {
      numOfCourts = numOfCourtsJson.getAsNumber().intValue();
      if (numOfCourts <= 0) {
        throw new IllegalArgumentException("Number of courts should be greater than zero");
      }
    }
    
    JsonObject basketballCourt = new JsonObject();
    basketballCourt.addProperty("id", id);
    basketballCourt.addProperty("name", name);
    basketballCourt.addProperty("location", location);
    if (numOfCourts != null) {
      basketballCourt.addProperty("numOfCourts", numOfCourts);
    }
    basketballCourt.addProperty("lat", lat);
    basketballCourt.addProperty("lon", lon);

    return basketballCourt;
  }
}
