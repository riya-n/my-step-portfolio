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

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    int duration = (int) request.getDuration();
    
    if (startOfDay + duration > endOfDay) {
        return Arrays.asList();
    }

    if (events.isEmpty() || (request.getAttendees().isEmpty() && request.getOptionalAttendees().isEmpty())) {
        return Arrays.asList(TimeRange.fromStartEnd(startOfDay, endOfDay, true));
    }

    List<TimeRange> availableTimes = new LinkedList<>();
    
    Set<String> attendees = new HashSet<>(request.getAttendees());

    if (!attendees.isEmpty()) {
        availableTimes = getAvailableTimes(events, duration, attendees);
    }

    if (!request.getOptionalAttendees().isEmpty()) {
        attendees.addAll(request.getOptionalAttendees());
        List<TimeRange> includingOptionalTimes = getAvailableTimes(events, duration, attendees);
        if (!includingOptionalTimes.isEmpty()){
            availableTimes = includingOptionalTimes;
        }
    }

    return availableTimes;
  }

  public List<TimeRange> getAvailableTimes(Collection<Event> events, int duration, Set<String> attendees) {

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    
    List<TimeRange> unavailableTimes = new LinkedList<>();
    List<TimeRange> availableTimes = new LinkedList<>();

    for (Event event : events) {
        Set<String> eventAttendees = event.getAttendees();
        Set<String> intersection = new HashSet<>(eventAttendees);
        intersection.retainAll(attendees);
        if (!intersection.isEmpty()) {
            unavailableTimes.add(event.getWhen());
        }
    }

    if (unavailableTimes.isEmpty()) {
        availableTimes.add(TimeRange.fromStartEnd(startOfDay, endOfDay, true));
        return availableTimes;
    }

    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

    int marker = startOfDay;
    for (int i = 0; i < unavailableTimes.size(); i++) {
        TimeRange time = unavailableTimes.get(i);
        if (time.start() >= marker + duration) {
            availableTimes.add(TimeRange.fromStartEnd(marker, time.start(), false));
        }
        if (time.end() > marker) {
            marker = time.end();
        }
        if (i == unavailableTimes.size() - 1) {
            if (endOfDay >= marker + duration) {
                availableTimes.add(TimeRange.fromStartEnd(marker, endOfDay, true));
            }
        }
    }

    return availableTimes;
  }

}
