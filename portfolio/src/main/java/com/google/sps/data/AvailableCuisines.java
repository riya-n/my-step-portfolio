package com.google.sps.data;

/** enum for the list of cuisine options */
public enum AvailableCuisines {
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

    AvailableCuisines(String localizedName){
      this.localizedName = localizedName;
    }

    public String getLocalizedName(){
      return this.localizedName;
    }

    public String getId() {
      return this.name();
    }

    /** valueOf() throws IllegalArg and NPE */
    public static AvailableCuisines getFromId(String id)
      throws IllegalArgumentException, NullPointerException {
      return AvailableCuisines.valueOf(id);
    }
    
}
