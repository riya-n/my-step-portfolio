package com.google.sps.data;

/** Class to create Comment object. */
public final class Comment {
    private final String comment;
    private final long timestamp;

    public Comment(String comment, long timestamp) {
      this.comment = comment;
      this.timestamp = timestamp;
    }

    /** Returns comment. */
    public String getComment() {
        return this.comment;
    }

    /** Returns timestamp of when comment was created. */
    public long getTimestamp() {
        return this.timestamp;
    }
}
