package com.google.sps.data;

/** Class used to store the json object from the basketball courts file. */
public final class BasketballCourt {
    private final String id;
    private final String name;
    private final String location;
    /** TODO: make this optional. numOfCourts field does not always exist (null). */
    private final Integer numOfCourts;
    private final double lat;
    private final double lon;

    public BasketballCourt(String id, String name, String location,
      Integer numOfCourts, double lat, double lon) {
      this.id = id;
      this.name = name;
      this.location = location;
      this.numOfCourts = numOfCourts;
      this.lat = lat;
      this.lon = lon;
    }

}
