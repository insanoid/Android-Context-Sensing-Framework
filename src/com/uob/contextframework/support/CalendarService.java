/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.contextframework.support;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.uob.contextframework.baseclasses.Event;


/**
 * @author karthikeyaudupa
 * The calendar service helper to read and access calendar.
 */
public class CalendarService {

	public static List<Event> readCalendarEvents(Context context) {

		    Cursor cursor = context.getContentResolver()
		            .query(
		                    Uri.parse("content://com.android.calendar/events"),
		                    new String[] { "calendar_id", "title", "description",
		                            "dtstart", "dtend", "eventLocation","allDay" }, null,
		                    null, null);
		    cursor.moveToFirst();
		    List<Event> eventList = new ArrayList<Event>();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {

		    	Event event = new Event();
		    	event.setEndDate(cursor.getLong(4));
		    	event.setStartDate(cursor.getLong(3));
		    	event.setAllDayEvent(cursor.getString(6).equals("0"));
		    	event.setLocation(cursor.getString(5));
		        eventList.add(event);
		    	cursor.moveToNext();
		        

		    }
		    cursor.close();
		    return eventList;
		}

}