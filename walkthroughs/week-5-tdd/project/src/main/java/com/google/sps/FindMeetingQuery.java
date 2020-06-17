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
    long requestDuration = request.getDuration();
    int requestSlots = (int) requestDuration / 30;
    int numEvents = events.size();

    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
    ArrayList<Integer> availableSlots = new ArrayList<Integer>();

    int[] timeSlots = new int[48];
    Arrays.fill(timeSlots, 1);

    // if the duration is longer than a day means there should be no options.
    if (requestDuration >= TimeRange.WHOLE_DAY.duration() + 1)
      return Arrays.asList();
    // if there are no mandatory attendees, the time range is the whole day
    if (requestAttendees.size() == 0 && optionalAttendees.size() == 0) 
      return Arrays.asList(TimeRange.WHOLE_DAY);
    

    // sets up timeSlots array; anywhere left true has all the requestAttendees available
    for (Event event : events) {
      Set<String> mandatoryEventAttendees = event.getAttendees();
      TimeRange when = event.getWhen();
      int start = (when.start()) / 30;
      int end = (when.end()) / 30;
      int eventSlots = 0;
      while (end > start) {
        end--;
        eventSlots++;
      }    

      int count = numEvents;
      Iterator<String> eventAi = mandatoryEventAttendees.iterator(); 
      while(eventAi.hasNext()) {
        count--;
        String eventAttendee = eventAi.next();
        Iterator<String> optionalAi = optionalAttendees.iterator(); 
        while(optionalAi.hasNext()) {
          String optionalAttendee = optionalAi.next();
          if (eventAttendee.equals(optionalAttendee)) {
            for (int i = 0; i < eventSlots; i++) {
              if(timeSlots[start + i] != -1)
                timeSlots[start + i] = 0;
            }
            break;
          }
        }
        start = (when.start()) / 30;
        Iterator<String> requestAi = requestAttendees.iterator(); 
        count = numEvents;
        while(requestAi.hasNext()) {
          String requestAttendee = requestAi.next();
          if (eventAttendee.equals(requestAttendee)) {
            for (int i = 0; i < eventSlots; i++) {
              timeSlots[start + i] = -1;
            }
            break;
          }
        }
        if(count == 0) {
            break;
        }
      }
    }

    //checks the duration and how many time slots are necessary
    if (Arrays.asList(timeSlots).contains(1)) {
      boolean add;
      for (int i = 0; i < 48; i++) {
        add = false;
        if (timeSlots[i] == 1) {
          add = true;
          // checks if the duration is longer than a single time slot
          for (int j = 1; j < requestSlots; j++) {
            if((i+j)<timeSlots.length && (timeSlots[i+j] == -1 || timeSlots[i+j] == 0)) {
              add = false;
            }
          }
        }
        
        if(add) {
          for (int j = 0; j < requestSlots; j++) {
            availableSlots.add(i+j);
          }
        }
      }
    }
    if (availableSlots.size() < requestSlots) {
      boolean add;
      for (int i = 0; i < 48; i++) {
        add = false;
        if (timeSlots[i] == 0) {
          add = true;
          // checks if the duration is longer than a single time slot
          for (int j = 1; j < requestSlots; j++) {
            if((i+j)<timeSlots.length && timeSlots[i+j] == -1) {
              add = false;
            }
          }
        }
        
        if(add) {
          for (int j = 0; j < requestSlots; j++) {
            availableSlots.add(i+j);
          }
        }
      }
    }
    int start = TimeRange.START_OF_DAY;
    boolean done = false;
    // if there's just enough room
    if (requestSlots == availableSlots.size()){
      start = availableSlots.get(0)*30;
      availableTimes.add(TimeRange.fromStartEnd(start, start + (int) requestDuration, false));
    }
    // everything else
    for (int i = 0; i < availableSlots.size() - requestSlots; i++) {
      // if the next value at the next index is not +1 from the last
      if (availableSlots.get(i+1) - 1 != availableSlots.get(i)) {
        availableTimes.add(TimeRange.fromStartEnd(start, availableSlots.get(i)*30+30, false));
        start = availableSlots.get(i+1)*30;
        numEvents--;
      }
      if(i == availableSlots.size() - requestSlots - 1) {
        done = true;
      }
      if(numEvents == 0){
        done = true;
        break;
      }
    }
    if(done) {
      availableTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
    }

    return availableTimes;
  }
}
