package com.google.sps.data;

/** enum for the list of cuisine options */
public enum AvailableCuisine {
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

    AvailableCuisine(String localizedName){
      this.localizedName = localizedName;
    }

    public String getLocalizedName(){
      return this.localizedName;
    }

    public String getId() {
      return this.name();
    }

    /** 
    * Returns the cuisine matching the provided id.
    * @throw IllegalArgumentException if a cuisine
    * cannot be found for provided id.
    */
    public static AvailableCuisine getFromId(String id)
      throws IllegalArgumentException {
      return AvailableCuisine.valueOf(id);
    }
    
}
