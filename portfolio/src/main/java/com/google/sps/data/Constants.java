 package com.google.sps.data;

 import java.util.List;

/** Class to hold the constants used in this project. */
public final class Constants {
  
  private Constants() {}

  /** comments related */
  public static final String COMMENT_ENTITY = "Comment";
  public static final String COMMENT_PARAMETER = "comment";
  public static final String TIMESTAMP_PARAMETER = "timestamp";
  public static final String COMMENTS_LIMIT_PARAMETER = "commentsLimit";
  
  /** cuisine related */
  public static final String CUISINE_ENTITY = "CuisineVotes";
  public static final String CUISINE_PARAMETER = "cuisine";
  public static final String USERID_PARAMETER = "userId";

  /** enum for the list of cuisine options */
  public enum CuisineEnum {
    JAPANESE("Japanese"),
    INDONESIAN("Indonesian"),
    CHINESE("Chinese"),
    INDIAN("Indian"),
    THAI("Thai"),
    KOREAN("Korean"),
    VIETNAMESE("Vietnamese"),
    MALAY("Malay"),
    SINGAPOREAN("Singaporean");

    private final String localizedName;

    CuisineEnum(String localizedName){
      this.localizedName = localizedName;
    }

    public String getLocalizedName(){
      return this.localizedName;
    }
  }

  /** basketball court related */
  public static final String JSON_FILE_PATH = "///home/riyanar/step/portfolio/src/main/webapp/WEB-INF/basketball-courts-nyc.json";
  public static final String ACCESSIBLE_PROPERTY = "Accessible";
  public static final String IS_ACCESSIBLE = "Y";
  public static final String NAME_PROPERTY = "Name";
  public static final String LOCATION_PROPERTY = "Location";
  public static final String NUMCOURTS_PROPERTY = "Num_of_Courts";
  public static final String LAT_PROPERTY = "lat";
  public static final String LON_PROPERTY = "lon";
 
}
