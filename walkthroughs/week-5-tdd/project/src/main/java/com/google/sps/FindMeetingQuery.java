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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator; 
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> requestAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    Collection<String> allAttendees = new ArrayList<>();
    allAttendees.addAll(requestAttendees);
    allAttendees.addAll(optionalAttendees);
    long requestDuration = request.getDuration();
    int numRequestSlots = (int) requestDuration / 30;
    int numEvents = events.size();

    boolean[] mandatoryTimeSlots = new boolean[48];
    boolean[] allTimeSlots = new boolean[48];

    // if the duration is longer than a day there are no options
    if (requestDuration >= TimeRange.WHOLE_DAY.duration() + 1)
      return Arrays.asList();
    // if there are no attendees, the time range is the whole day
    if (events.size() == 0 || allAttendees.size() == 0) 
      return Arrays.asList(TimeRange.WHOLE_DAY);
    
    // sets up timeSlots array; anywhere left true has all the requestAttendees available
    for (Event event : events) { 
      mandatoryTimeSlots = convertToTimeSlots(requestAttendees, events);
      allTimeSlots = convertToTimeSlots(allAttendees, events);    
    }

    ArrayList<Integer> possibleSlots = convertToSlots(mandatoryTimeSlots, numRequestSlots);
    ArrayList<Integer> allSlots = convertToSlots(allTimeSlots, numRequestSlots);
    
    ArrayList<TimeRange> possibleTimes = convertToTimes(possibleSlots, numRequestSlots, numEvents);
    ArrayList<TimeRange> allTimes = convertToTimes(allSlots, numRequestSlots, numEvents);

    if (allTimes.isEmpty() && !possibleTimes.contains(TimeRange.WHOLE_DAY))
        return possibleTimes;
    return allTimes;
  }
  
  public static boolean[] convertToTimeSlots (Collection<String> requestAttendees, Collection<Event> events) {
    boolean[] timeSlots = new boolean[48];
    Arrays.fill(timeSlots, Boolean.TRUE);
    int numEvents = events.size();

    for (Event event : events) {
      Set<String> eventAttendees = event.getAttendees();
      TimeRange when = event.getWhen();
      int start = (when.start()) / 30;
      int eventSlots = when.duration() / 30;

      Iterator<String> eventAi = eventAttendees.iterator(); 
      while(eventAi.hasNext()) {
        numEvents--;
        String eventAttendee = eventAi.next();

        Iterator<String> requestAi = requestAttendees.iterator(); 
        while (requestAi.hasNext()) {
          String requestAttendee = requestAi.next();
          if (eventAttendee.equals(requestAttendee)) {
            for (int i = 0; i < eventSlots; i++) 
              timeSlots[start + i] = false;
            break;
          }
        }
        if(numEvents == 0)
            break;
      }
    }
    return timeSlots;
  }

  // converts boolean availability to list of available slots
  public static ArrayList<Integer> convertToSlots (boolean timeSlots[], int numRequestSlots) {
    ArrayList<Integer> slots = new ArrayList<Integer>();
    boolean add;
    for (int i = 0; i < 48; i++) {
      add = false;
      if (timeSlots[i]) {
        add = true;
        // checks if the duration is longer than a single time slot
        for (int j = 1; j < numRequestSlots; j++)
          if ((i+j) < timeSlots.length && !timeSlots[i+j])
            add = false;
      }
      if(add)
        for (int j = 0; j < numRequestSlots; j++)
          slots.add(i+j);
    }
    return slots;
  }

  // converts list of available slots to a list of TimeRanges 
  public static ArrayList<TimeRange> convertToTimes (ArrayList<Integer> slots, int numRequestSlots, int numEvents) {
    ArrayList<TimeRange> possibleTimes = new ArrayList<TimeRange>();
    int start;
    boolean done = false;

    // if slots size is zero
    if(slots.size() == 0) 
      return possibleTimes;

    start = slots.get(0)*30;

    // if there's just enough room
    if (numRequestSlots == slots.size()){
      possibleTimes.add(TimeRange.fromStartEnd(start, start + numRequestSlots*30, false));
      return possibleTimes;
    }

    // if the whole day is available
    if (slots.size() == 48){
      possibleTimes.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, TimeRange.END_OF_DAY, true));
      return possibleTimes;
    }

    // everything else
    for (int i = 0; i < slots.size() - numRequestSlots; i++) {
      // if the next value at the next index is not +1 from the last
      int current = slots.get(i);
      int next = slots.get(i+1);
      if (current != next - 1) {
        possibleTimes.add(TimeRange.fromStartEnd(start, current*30+30, false));
        start = next*30;
        numEvents--;
      }
      if (i == slots.size() - numRequestSlots - 1)
        done = true;
      if (numEvents == 0){
        done = true;
        break;
      }
    }
    if(done) {
      int end =  slots.get(slots.size()-1)+1;
      if (end + 1 == 1440) 
        possibleTimes.add(TimeRange.fromStartEnd(start, end*30, true));
      else
        possibleTimes.add(TimeRange.fromStartEnd(start, end*30, false));
    }
    return possibleTimes;
  }
}