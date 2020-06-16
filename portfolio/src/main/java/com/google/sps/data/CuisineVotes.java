package com.google.sps.data;

import com.google.sps.data.AvailableCuisines;

/** Class used to store cuisineId, cuisineName, and votes. */
public final class CuisineVotes {
    private AvailableCuisines cuisine;
    private int votes;

    public CuisineVotes(AvailableCuisines cuisine) {
      this.cuisine = cuisine;
      this.votes = 0;
    }

    public void addVote() {
      this.votes = this.votes + 1;
    }

    public AvailableCuisines getCuisine() {
      return this.cuisine;
    }

    public int getVotes() {
      return this.votes;
    }
}
