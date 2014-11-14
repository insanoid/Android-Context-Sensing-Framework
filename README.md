# Android Context Sensing Framework 

A simple context monitoring framework to handle context sensing and polling of contextual information.

To monitor a particular context:

			ContextManager mContextManager = new ContextManager(getApplicationContext());
			
			//Specify the context along with the the after which you need an update for.
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION, 60*60*1000L);
			
			//Alternatively, Specify the context along with the the after which you need an update for, polling interval and flag.
			mContextManager.monitorContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION, 10*60*1000L, 30*60*1000L, null);

To receive updates about context:

			mContextBroadCastReceiver = new ContextBroadCastReceiver();
			IntentFilter filter = new IntentFilter(com.uob.contextframework.support.Constants.CONTEXT_CHANGE_NOTIFY);
			filter.addCategory(Intent.CATEGORY_DEFAULT);
			registerReceiver(mContextBroadCastReceiver, filter);
			...
			public class ContextBroadCastReceiver extends BroadcastReceiver {

				@Override
				public void onReceive(Context context, Intent intent) {

				String contentType = intent.getStringExtra(com.uob.contextframework.support.Constants.INTENT_TYPE);
				if(contentType.equalsIgnoreCase(com.uob.contextframework.support.Constants.LOC_NOTIFY)){
					//do something with the information.
					...
					}
				}
			}

To stop monitoring a particular context:

		mContextManager.stopMonitoringContext(ContextManagerServices.CTX_FRAMEWORK_LOCATION);

### TODO
	- Monitor more contextual information (Application usage monitoring).
	- Adaptive sensing
	- Energy effective sensing (modify itself based on power levels)

### License
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, karthikeyaudupa@gmail.com

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
