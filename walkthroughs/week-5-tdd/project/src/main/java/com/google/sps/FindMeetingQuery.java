// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import com.google.gson.Gson;


public final class FindMeetingQuery {

  private static final Logger log = Logger.getLogger(FindMeetingQuery.class.getName());

  /**
   * Assume that eventslist is sorted by start of meeting time.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    // need to sort the events by start time
    
    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    ArrayList<Event> eventsInfo = new ArrayList<>(events);
    
    Collection<TimeRange> possibleTimes = new ArrayList<>();

    int meetingLength = (int) duration;
    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;

    log.info("eventsList: " + new Gson().toJson(events));
    log.info("meetingLength: " + meetingLength);
    if (meetingLength > TimeRange.WHOLE_DAY.duration()) {
        return possibleTimes;
    }

    Set<String> attendeesSet = new HashSet<>(attendees);
    // it should only include the events of the people going to this meeting
    for (int i = 0; i < eventsInfo.size(); i++) {
        Set<String> eventAttendees = eventsInfo.get(i).getAttendees();
        Set<String> set = new HashSet<>(eventAttendees);
        set.retainAll(attendeesSet);
        if (set.size() == 0) {
            eventsInfo.remove(i);
        }
    }

    //remove any overlapping events
    for (int i = 0; i < eventsInfo.size() - 1; i++) {
        if (eventsInfo.get(i).getWhen().contains(eventsInfo.get(i + 1).getWhen().end())) {
            eventsInfo.remove(i +1);
        }
    }

    if (eventsInfo.size() == 0) {
        possibleTimes.add(TimeRange.fromStartEnd(startOfDay, endOfDay, true));
    }

    for (int i = 0; i < eventsInfo.size(); i++) {
        if (i == 0) {
            int start = eventsInfo.get(i).getWhen().start();
            if (start >= startOfDay + meetingLength) {
                possibleTimes.add(TimeRange.fromStartEnd(startOfDay, start, false));
            }
        } 
        if (i == eventsInfo.size() - 1) {
            int start = eventsInfo.get(i).getWhen().end();
            if (endOfDay >= start + meetingLength) {
                possibleTimes.add(TimeRange.fromStartEnd(start, endOfDay, true));
            }
        } else {
            int start = eventsInfo.get(i).getWhen().end();
            int end = eventsInfo.get(i + 1).getWhen().start();
            if (end >= start + meetingLength) {
                possibleTimes.add(TimeRange.fromStartEnd(start, end, false));
            }
        }
    }

    return possibleTimes;
  }
}
