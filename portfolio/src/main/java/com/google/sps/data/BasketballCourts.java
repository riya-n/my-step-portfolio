package com.google.sps.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.sps.data.Constants;

/** Parses the basketball courts file and returns a list of basketball courts */
public final class BasketballCourts {
  private static final Logger log = LoggerFactory.getLogger(BasketballCourts.class);

  /** Class used to store the json object from the basketball courts file. */
  public final static class BasketballCourt {
    private final String name;
    private final String location;
    private final Integer numOfCourts;
    private final Double lat;
    private final Double lon;

    public BasketballCourt(String name, String location,
      Integer numOfCourts, Double lat, Double lon) {
      this.name = name;
      this.location = location;
      this.numOfCourts = numOfCourts;
      this.lat = lat;
      this.lon = lon;
    }
  }

  /** Returns a list of the accessible basketball courts. */
  public static List<BasketballCourt> getAccessibleBasketballCourts() {
    List<BasketballCourt> accessibleCourts = new ArrayList<>();
    try {
      String path = Constants.JSON_FILE_PATH;
      BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

      Gson gson = new Gson();
      JsonArray allCourts = gson.fromJson(bufferedReader, JsonArray.class);

      for (JsonElement court : allCourts) {
        JsonObject courtObj = court.getAsJsonObject();
        JsonElement accessibleEl = courtObj.get(Constants.ACCESSIBLE_PROPERTY);
        if (!accessibleEl.isJsonNull()) {
          String accessibleStr = accessibleEl.getAsString();
          if (accessibleStr.equals(Constants.IS_ACCESSIBLE)) {
            accessibleCourts.add(createCourtObject(courtObj));
          }
        }
      }
    } catch(FileNotFoundException e) {
      e.printStackTrace();
      log.info("basketball courts file not found");
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      log.info("basketball court value should not be null");
    }

    return accessibleCourts;
  }

  /** Creates a basketball court object given a json object of that court. */
  private static BasketballCourt createCourtObject(JsonObject court) {
    JsonElement nameJson = court.get(Constants.NAME_PROPERTY);
    JsonElement locationJson = court.get(Constants.LOCATION_PROPERTY);
    JsonElement numOfCourtsJson = court.get(Constants.NUMCOURTS_PROPERTY);
    JsonElement latJson = court.get(Constants.LAT_PROPERTY);
    JsonElement lonJson = court.get(Constants.LON_PROPERTY);

    if (nameJson.isJsonNull() || locationJson.isJsonNull() || latJson.isJsonNull() || lonJson.isJsonNull()) {
      throw new IllegalArgumentException("basketball court value should not be null");
    }

    String name = nameJson.getAsString();
    String location = locationJson.getAsString();
    Double lat = latJson.getAsDouble();
    Double lon = lonJson.getAsDouble();
    Integer numOfCourts = null;
    if (!numOfCourtsJson.isJsonNull()) {
      numOfCourts = numOfCourtsJson.getAsNumber().intValue();
    }
    
    BasketballCourt basketballCourt = new BasketballCourt(name, location, numOfCourts, lat, lon);

    return basketballCourt;
  }
}
