 package com.google.sps.data;

/** Class to hold the constants used in this project. */
public final class Constants {
  
  private Constants() {}

  /** comments related */
  public static String COMMENT_ENTITY = "Comment";
  public static String COMMENT_PARAMETER = "comment";
  public static String TIMESTAMP_PARAMETER = "timestamp";
  public static String COMMENTS_LIMIT_PARAMETER = "commentsLimit";
  public static String COMMENT_EMPTY_ERROR = "Comment should not be empty";
  
  /** cuisine related */
  public static String CUISINE_ENTITY = "Cuisine";
  public static String CUISINE_PARAMETER = "cuisine";
  public static String VOTES_PARAMETER = "votes";
  public static String CUISINE_EMPTY_ERROR = "Cuisine should not be empty";

  /** basketball court related */
  public static String JSON_FILE_PATH = "///home/riyanar/step/portfolio/src/main/webapp/WEB-INF/basketball-courts-nyc.json";
  public static String ACCESSIBLE_PROPERTY = "Accessible";
  public static String IS_ACCESSIBLE = "Y";
  public static String FILE_NOT_FOUND = "Basketall courts json file not found";

  /** generic errors */
  public static String DOPOST_ILL_ARG_ERROR = "Illegal Argument Exception caught in doPost method";
 
}
