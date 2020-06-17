package com.google.sps.data;

import java.util.OptionalInt;

/** Class used to store the json object from the basketball courts file.
  Doesn't require getter or setter methods because the class is just for the 
  basketball court to be returned as the api response. numOfCourts not always
  provided which is why its Optional. */
public final class BasketballCourt {
    private final String id;
    private final String name;
    private final String location;
    private final OptionalInt numOfCourts;
    private final double lat;
    private final double lon;

    public BasketballCourt(String id, String name, String location,
      OptionalInt numOfCourts, double lat, double lon) {
      this.id = id;
      this.name = name;
      this.location = location;
      this.numOfCourts = numOfCourts;
      this.lat = lat;
      this.lon = lon;
    }

}
