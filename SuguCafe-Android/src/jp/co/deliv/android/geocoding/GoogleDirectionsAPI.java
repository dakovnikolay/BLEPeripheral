package jp.co.deliv.android.geocoding;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jp.co.deliv.android.http.HttpClient;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;


/**
 * Google Maps API WEBサービスの Directions API を使用して、
 * 緯度経度で指定される２点間のルート検索を行い、出発地から目的地までの
 * 経路情報を取得する。このAPIを使用するときはGoogleMapと併用すること。
 * @author Atsushi
 */
public class GoogleDirectionsAPI {  

	/*
	 * DirectionsAPIで取得するタグ情報
	 */
	private static final String DIRECTIONS_RESPONSE_TAG = "DirectionsResponse";
	private static final String STEP_TAG = "step";
	private static final String START_LOCATION_TAG = "start_location";
	private static final String END_LOCATION_TAG = "end_location";		
	private static final String LATITUDE_TAG = "latitude";
	private static final String LONGITUDE_TAG = "longitude";

	/*
	 * 経路検索結果
	 */
	private List<RouteStep> routeSteps = null;
	
	/*
	 * DirectionsAPIのアクセスURL
	 */
    private String urlStr;  
  
    private static final String API_URL = "http://maps.google.com/maps/api/directions/xml?mode=walking&sensor=true";
        
    public GoogleDirectionsAPI(double orgLat, double orgLng, double destLat, double destLng) {
    	String apiOption = "origin=" + orgLat + "," + orgLng + "&destination=" + destLat + "," + destLng;
        this.urlStr = API_URL + "&" + apiOption;  
    }  
	  
	public void parse() {
		
        XmlPullParser parser = Xml.newPullParser();  
        
        try {
        	byte[] data = HttpClient.getByteArrayFromURL(urlStr);
        	parser.setInput(new StringReader(new String(data, "UTF-8")));
        	int eventType = parser.getEventType();
        	boolean isFinished = false;

        	RouteStep currentStep = null;
        	GeoLocation currentLocation = null;
        	
        	while (eventType != XmlPullParser.END_DOCUMENT && !isFinished) {
        		
	            String name = null;
	            
	            switch (eventType) {
	            
	            case XmlPullParser.START_DOCUMENT:  
	            	routeSteps = new ArrayList<RouteStep>();  
	                break;  
	            
	            case XmlPullParser.START_TAG:

	            	// 開始タグを取得
	                name = parser.getName();
	                	                
	                /*
	                 * タグ名に応じた処理
	                 */
	                if (name.equalsIgnoreCase(STEP_TAG)) {
	                	// stepタグが開始されたらRouteStepクラスのインスタンスを作る
	                    currentStep = new RouteStep();  
	                } else if (currentStep != null) {
	                    if (name.equalsIgnoreCase(START_LOCATION_TAG)) {  
	                    	currentLocation = currentStep.getStartLocation();
	                    } else if (name.equalsIgnoreCase(END_LOCATION_TAG)) {  
	                    	currentLocation = currentStep.getEndLocation();
	                    } else if (name.equals(LATITUDE_TAG)) {
	                    	if(currentLocation != null) {
	                    		currentLocation.setLatitude(Double.parseDouble(parser.nextText()));
	                    	}
	                    } else if (name.equals(LONGITUDE_TAG)) {
	                    	if(currentLocation != null) {
	                    		currentLocation.setLongitude(Double.parseDouble(parser.nextText()));
	                    	}
	                    }
	                }  
	                break;  
	            
	            case XmlPullParser.END_TAG:
	            	
	            	// 終了タグ名を取得
	                name = parser.getName();

	                if(currentStep != null && currentLocation != null) {
		                if(name.equalsIgnoreCase(START_LOCATION_TAG)) {
		                	currentStep.setStartLocation(currentLocation);
		                	currentLocation = null;
		                } else if(name.equalsIgnoreCase(END_LOCATION_TAG)) {
		                	currentStep.setEndLocation(currentLocation);
		                	currentLocation = null;		                	
		                }
	                }
	                if(name.equalsIgnoreCase(STEP_TAG) && currentStep != null) {
	                	routeSteps.add(currentStep);
	                	currentStep = null;
	                } else if(name.equalsIgnoreCase(DIRECTIONS_RESPONSE_TAG)) {
	                	isFinished = true;
	                }
	                break;  
	            }  
	            eventType = parser.next();  
        	}  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	}  
}
