package com.example.webview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
	Context mContext;

	/** Instantiate the interface and set the context */
	WebAppInterface(Context c) {
		mContext = c;
	}

	/** Show a toast from the web page */
	@JavascriptInterface
	public void showToast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}
	@JavascriptInterface
	public String login(){
		try{
			JSONObject obj = new JSONObject();
			obj.put("status", "SUCCESS");
			obj.put("user_id","TRh5gdf/sNG4NWZh5DdbE2m6qB8HgTi5uBMiyJW0n6PWe0urpu/lyXAnOrgX\n5unxUgUdLSPURImibsvmSEgOMQ==\n");
			obj.put("name", "Deepak");
			obj.put("phone", 123456);
			obj.put("status", "SUCCESS");

			return obj.toString();	
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	@JavascriptInterface
	public String getCabAvailability(){

		JSONObject JsonObj = new JSONObject();
		try{
		JsonObj.put("status", "SUCCESS");

		JSONArray jsonArray = new JSONArray();

		JSONObject obj = new JSONObject();
		obj.put("lat", 13.204492);
		obj.put("lng", 77.707687);
		obj.put("eta", "3 mins");
		obj.put("id", 4385763);
		jsonArray.put("obj");

		JSONObject obj1 = new JSONObject();
		obj1.put("lat", 13.204492);
		obj1.put("lng", 77.707687);
		obj1.put("eta", "3 mins");
		obj1.put("id", 4385763);
		jsonArray.put("obj1");

		JSONObject obj2 = new JSONObject();
		obj2.put("lat", 13.204492);
		obj2.put("lng", 77.707687);
		obj2.put("eta", "3 mins");
		obj2.put("id", 4385763);
		jsonArray.put("obj2");
		
		JsonObj.put("cabs_available",jsonArray);

		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return JsonObj.toString();
		
	}
	@JavascriptInterface
	public String doAbooking() {
		JSONObject jsonObj = new JSONObject();
		try{
		jsonObj.put("status", "SUCCESS");
		JSONObject objBooking = new JSONObject();

		objBooking.put("booking_id", "a1f0f7bf-8124-6f4a-b17b-54cb69dc478c");

		JSONObject allotedCabObj = new JSONObject();
		allotedCabObj.put("id", "4385763");
		allotedCabObj.put("lat", "13.204492");
		allotedCabObj.put("lng", "77.707687");
		allotedCabObj.put("license_number", "KA 06 HJ 6666");
		allotedCabObj.put("car_model", "Maruti Swift Dzire");
		allotedCabObj.put("color", "Blue");
		allotedCabObj.put("driver_name", "Mohan.K");
		allotedCabObj.put("driver_mobile", "9412567863");
		allotedCabObj.put("duration", "5 mins");

		objBooking.put("alloted_cab_info",allotedCabObj);
		jsonObj.put("booking",objBooking);

		return jsonObj.toString();
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return null;
		}


}