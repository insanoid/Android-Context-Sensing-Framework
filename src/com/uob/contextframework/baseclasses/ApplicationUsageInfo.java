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

package com.uob.contextframework.baseclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.media.AudioManager;
import android.provider.Browser;

/**
 * App monitoring utilities.
 * @author Karthikeya Udupa K M
 *
 */

public class ApplicationUsageInfo {

	/**
	 * Possible browser strings.
	 */
	static String[] browserPackagenames = {
			"com.android.chrome",
			"me.android.browser",

	};

	/**
	 * Call checker
	 * @param context
	 * @return true if a call is going on.
	 */
	public static boolean isACallActive(Context context) {
		AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if(manager.getMode()==AudioManager.MODE_IN_CALL){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Checks if the current application is a browser.
	 * @param packageName
	 * @return
	 */
	public static boolean isBrowser(String packageName) {

		for(String s: browserPackagenames){
			if(packageName.equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	 * Gets the name and package-name of the application on top of the stack.
	 * @param ctx
	 * @return
	 */
	public static ApplicationModal getForegroundAppInformation(Context ctx) {

		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(android.content.Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1); 
		PackageManager manager = ctx.getPackageManager();
		CharSequence appNameSequence = null;
		ApplicationModal appModel = new ApplicationModal();

		try {

			appNameSequence = manager.getApplicationLabel(manager.getApplicationInfo(taskInfo.get(0).baseActivity.getPackageName() , PackageManager.GET_META_DATA));
			appModel.setApplicationName(appNameSequence.toString()!=null?appNameSequence.toString():"");
			appModel.setApplicationPackageName(taskInfo.get(0).baseActivity.getPackageName()!=null?taskInfo.get(0).baseActivity.getPackageName():"");
			if(taskInfo.get(0).baseActivity.getPackageName()!=null){
				Boolean isBrowser =  isBrowser(taskInfo.get(0).baseActivity.getPackageName());
				appModel.setIsBrowser(isBrowser);
				if(isBrowser==true){
					String content = getLastAccessedBrowserPage(ctx);
					URL urlContent;
					try {
						urlContent = new URL(content);
						appModel.setAssociatedURIResource(urlContent);
					} catch (MalformedURLException e) {
					}

				}
			}else{
				appModel.setIsBrowser(false);
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appModel;
	}

	/**
	 * Fetches the page being accessed by Google Chrome.
	 * @param ctx
	 * @return
	 */
	public static String getLastAccessedBrowserPage(Context ctx) {

		Cursor webLinksCursor = ctx.getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, Browser.BookmarkColumns.DATE + " DESC");
		int row_count = webLinksCursor.getCount();

		int title_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE);
		int url_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.URL);

		if ((title_column_index > -1) && (url_column_index > -1) && (row_count > 0))
		{
			webLinksCursor.moveToFirst();
			while (webLinksCursor.isAfterLast() == false)
			{
				if (webLinksCursor.getInt(Browser.HISTORY_PROJECTION_BOOKMARK_INDEX) != 1)
				{
					if (!webLinksCursor.isNull(url_column_index))
					{
						return webLinksCursor.getString(url_column_index);
					}
				}
				webLinksCursor.moveToNext();
			}            
		}
		webLinksCursor.close();
		return null;
	}
}
