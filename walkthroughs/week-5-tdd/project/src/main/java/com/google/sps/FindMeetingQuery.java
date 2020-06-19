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
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public final class FindMeetingQuery {

  /**
   * This method find the available times to schedule a meeting. It finds times that
   * all required attendees can make, and also tries to include optional attendees.
   * However, it either includes all the optional attendees or none of them.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    int durationMin = (int) request.getDuration();
    
    if (startOfDay + durationMin > endOfDay) {
        return ImmutableList.of();
    }

    if (events.isEmpty() || (request.getAttendees().isEmpty() && request.getOptionalAttendees().isEmpty())) {
        return ImmutableList.of(TimeRange.fromStartEnd(startOfDay, endOfDay, /* inclusive */ true));
    }

    List<TimeRange> availableTimes = new LinkedList<>();
    
    Set<String> attendees = new HashSet<>(request.getAttendees());

    if (!attendees.isEmpty()) {
        availableTimes = getAvailableTimes(events, durationMin, attendees);
    }

    if (!request.getOptionalAttendees().isEmpty()) {
        attendees.addAll(request.getOptionalAttendees());
        List<TimeRange> includingOptionalTimes = getAvailableTimes(events, durationMin, attendees);
        if (!includingOptionalTimes.isEmpty()){
            availableTimes = includingOptionalTimes;
        }
    }

    return ImmutableList.copyOf(availableTimes);
  }

  /**
   * This method returns the available times that the given attendees all have in common.
   */
  private List<TimeRange> getAvailableTimes(Collection<Event> events, int durationMin, Set<String> attendees) {

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    
    List<TimeRange> unavailableTimes = new LinkedList<>();
    List<TimeRange> availableTimes = new LinkedList<>();

    for (Event event : events) {
        Set<String> eventAttendees = event.getAttendees();
        if (!Collections.disjoint(eventAttendees, attendees)) {
            unavailableTimes.add(event.getWhen());
        }
    }

    if (unavailableTimes.isEmpty()) {
        availableTimes.add(TimeRange.fromStartEnd(startOfDay, endOfDay, /* inclusive */ true));
        return availableTimes;
    }

    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

    int marker = startOfDay;
    for (int i = 0; i < unavailableTimes.size(); i++) {
        TimeRange time = unavailableTimes.get(i);
        if (time.start() >= marker + durationMin) {
            availableTimes.add(TimeRange.fromStartEnd(marker, time.start(), /* inclusive */ false));
        }
        if (time.end() > marker) {
            marker = time.end();
        }
        if (i == unavailableTimes.size() - 1) {
            if (endOfDay >= marker + durationMin) {
                availableTimes.add(TimeRange.fromStartEnd(marker, endOfDay, /* inclusive */ true));
            }
        }
    }

    return availableTimes;
  }

}
