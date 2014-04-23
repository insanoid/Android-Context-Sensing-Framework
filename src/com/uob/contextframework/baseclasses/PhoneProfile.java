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

import org.json.JSONException;
import org.json.JSONObject;

import com.uob.contextframework.support.Constants;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author karthikeyaudupa
 * Provides information about the devices setting.
 */
public class PhoneProfile {

	private Integer ringVolumeLevel;
	private Integer normalVolumeLevel;
	private Integer screenBrightnessLevel;
	private Integer ringerMode;
	private Context ctx;

	/**
	 * Constructor.
	 * @param _ctx
	 */
	public PhoneProfile(Context _ctx){
		ctx = _ctx;
		AudioManager audio = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		ringVolumeLevel = audio.getStreamVolume(AudioManager.STREAM_RING);
		normalVolumeLevel = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
		ringerMode = audio.getRingerMode();

		screenBrightnessLevel = android.provider.Settings.System.getInt(
				ctx.getContentResolver(), 
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				-1);

	}

	/**
	 * Confirms if the phone profile has changed from previously provided information.
	 * @param phoneProfile
	 * @return
	 */
	public boolean hasProfileChanged(PhoneProfile phoneProfile) {

		if(ringVolumeLevel == phoneProfile.getRingVolumeLevel() &&
				normalVolumeLevel == phoneProfile.getNormalVolumeLevel() &&
				screenBrightnessLevel == phoneProfile.getScreenBrightnessLevel() &&
				ringerMode == phoneProfile.getRingerMode() 
				){

			return false;

		}

		return true;

	}

	/**
	 * @return the normalVolumeLevel
	 */
	public Integer getNormalVolumeLevel() {
		return normalVolumeLevel;
	}

	/**
	 * @return the ringerMode
	 */
	public Integer getRingerMode() {
		return ringerMode;
	}

	public Integer getRingVolumeLevel() {
		return ringVolumeLevel;
	}

	/**
	 * @return the screenBrightnessLevel
	 */
	public Integer getScreenBrightnessLevel() {
		return screenBrightnessLevel;
	}

	//Data conversion methods.
	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {

			jObject.put(Constants.RING_VOL, String.valueOf(getRingVolumeLevel()));
			jObject.put(Constants.NORM_VOL, String.valueOf(getNormalVolumeLevel()));
			jObject.put(Constants.SCREEN_BRIGHTNESS, String.valueOf(getScreenBrightnessLevel()));
			jObject.put(Constants.RINGER_MODE, String.valueOf(getRingerMode()));

		} catch (JSONException e) {

		}



		return jObject;
	}

}