package com.uob.contextframework.support;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.uob.contextframework.baseclasses.Event;


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