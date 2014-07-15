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

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.uob.contextframework.support.Constants;

/**
 * Model to store application usage records.
 * @author karthikeyaudupa
 *
 */
public class ApplicationModal {

	String applicationPackageName, applicationName;
	URL associatedURIResource;
	Boolean isBrowser;


	/**
	 * @return the applicationPackageName
	 */
	public String getApplicationPackageName() {
		return applicationPackageName;
	}
	/**
	 * @param applicationPackageName the applicationPackageName to set
	 */
	public void setApplicationPackageName(String applicationPackageName) {
		this.applicationPackageName = applicationPackageName;
	}
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the associatedURIResource
	 */
	public URL getAssociatedURIResource() {
		return associatedURIResource;
	}
	/**
	 * @param associatedURIResource the associatedURIResource to set
	 */
	public void setAssociatedURIResource(URL associatedURIResource) {
		this.associatedURIResource = associatedURIResource;
	}
	/**
	 * @return the isBrowser
	 */
	public Boolean getIsBrowser() {
		return isBrowser;
	}
	/**
	 * @param isBrowser the isBrowser to set
	 */
	public void setIsBrowser(Boolean isBrowser) {
		this.isBrowser = isBrowser;
	}

	public Boolean isApplicationSimilar(ApplicationModal model){
		
		if(model==null){
			return false;
		}
		
		if(!(getIsBrowser()==true && model.getIsBrowser()==true)){
			if(model.getApplicationPackageName()!=null){
				Boolean isSameModel = model.getApplicationPackageName().equalsIgnoreCase(getApplicationPackageName()!=null?getApplicationPackageName():"");
				return isSameModel;
			}else{
				return false;
			}
		}else{
			if(model.getAssociatedURIResource().equals(getAssociatedURIResource())){
				return true;
			}else{
				return false;
			}
		}
	}

	public String toString() {
		
		return toJSON().toString();
	}
	
	public JSONObject toJSON() {
		JSONObject jObject = new JSONObject();
		
		try {
			jObject.put(Constants.APP_NAME, (applicationName!=null)?applicationName:"");
			jObject.put(Constants.APP_PACKAGE, (applicationPackageName!=null)?applicationPackageName:"");
			jObject.put(Constants.APP_IS_BROWSER, isBrowser);
			jObject.put(Constants.APP_CONTENT, (associatedURIResource!=null)?associatedURIResource:"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObject;
		
	}
	
}
