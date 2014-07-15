package com.uob.contextframework.baseclasses;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.uob.contextframework.support.Constants;

public class AccelerometerInfoMonitor implements SensorEventListener {

	Context mContext;
	public AccelerometerInfoMonitor(Context context){
		mContext = context;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// check sensor type
				if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
					
					AccelerometerInfoModel val = new AccelerometerInfoModel();
					val.x=event.values[0];
					val.y=event.values[1];
					val.z=event.values[2];
					Intent proxIntent = new Intent(Constants.INTERNAL_ACC_CHANGE_NOTIFY);
					proxIntent.putExtra(Constants.INTENT_TYPE, Constants.ACCL_NOTIFY);
					proxIntent.putExtra(Constants.ACCL_NOTIFY,val.toString());
					mContext.sendBroadcast(proxIntent);
				}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
