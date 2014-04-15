package com.uob.contextframework.support;

import java.util.Date;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationsHelper {

	
	
	public static boolean isAnUpdateRequired(Location location) {
		
		long timeDelta = new Date().getTime()- location.getTime();
		boolean required = timeDelta > Constants.MAXIMUM_ACCEPTABLE_TIME;
		return required;
	}
	
	//Google function for better location verification.
	public static boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > Constants.MAXIMUM_ACCEPTABLE_TIME;
		boolean isSignificantlyOlder = timeDelta < -Constants.MAXIMUM_ACCEPTABLE_TIME;
		;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > Constants.MAXIMUM_ACCEPTABLE_DISTANCE;
		boolean isMoreAccurate = accuracyDelta < Constants.MAXIMUM_ACCEPTABLE_DISTANCE;
		boolean isSignificantlyLessAccurate = accuracyDelta > 400;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	public static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static Location getLatestLocation(Context context) {
		
		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = manager.getBestProvider(criteria, true);
		Location bestLocation;
		if (provider != null)
			bestLocation = manager.getLastKnownLocation(provider);
		else
			bestLocation = null;
		Location latestLocation = getLatest(bestLocation,
				manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		latestLocation = getLatest(latestLocation,
				manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		latestLocation = getLatest(latestLocation,
				manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
		return latestLocation;
	}

	private static Location getLatest(final Location location1,
			final Location location2) {
		if (location1 == null)
			return location2;

		if (location2 == null)
			return location1;

		if (location2.getTime() > location1.getTime())
			return location2;
		else
			return location1;
	}

	
}
