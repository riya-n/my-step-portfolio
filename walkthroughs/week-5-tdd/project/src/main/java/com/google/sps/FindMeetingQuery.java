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
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import com.google.gson.Gson;


public final class FindMeetingQuery {

  private static final Logger log = Logger.getLogger(FindMeetingQuery.class.getName());

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    int duration = (int) request.getDuration();
    
    List<TimeRange> unavailableTimes = new LinkedList<>();
    List<TimeRange> availableTimes = new LinkedList<>();

    if (startOfDay + duration > endOfDay) {
        return availableTimes;
    }

    if (events.isEmpty() || request.getAttendees().isEmpty()) {
        availableTimes.add(TimeRange.fromStartEnd(startOfDay, endOfDay, true));
        return availableTimes;
    }

    Set<String> attendees = new HashSet<>(request.getAttendees());

    for (Event event : events) {
        Set<String> eventAttendees = event.getAttendees();
        Set<String> intersection = new HashSet<>(eventAttendees);
        intersection.retainAll(attendees);
        if (intersection.size() > 0) {
            unavailableTimes.add(event.getWhen());
        }
    }

    if (unavailableTimes.size() == 0) {
        availableTimes.add(TimeRange.fromStartEnd(startOfDay, endOfDay, true));
        return availableTimes;
    }

    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

    int start = startOfDay;
    for (int i = 0; i < unavailableTimes.size(); i++) {
        TimeRange time = unavailableTimes.get(i);
        if (time.start() >= start + duration) {
            availableTimes.add(TimeRange.fromStartEnd(start, time.start(), false));
        }
        if (time.end() > start) {
            start = time.end();
        }
        if (i == unavailableTimes.size() - 1) {
            if (endOfDay >= start + duration) {
                availableTimes.add(TimeRange.fromStartEnd(start, endOfDay, true));
            }
        }
        
    }

    return availableTimes;
  }

}
