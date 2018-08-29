package com.pollfish.cordova;

/**
 * Created by Pollfish on 4/27/15.
 */

import android.util.Log;

import com.pollfish.constants.Position;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishSurveyCompletedListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishSurveyReceivedListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.main.PollFish;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PollfishPlugin extends CordovaPlugin {
    
    protected static String TAG = "PollfishPlugin";
    
    // Pollfish functions
    
    private static final String INIT_POLLFISH = "init";
    private static final String SHOW_POLLFISH = "show";
    private static final String HIDE_POLLFISH = "hide";
    private static final String SET_ATTRIBUTES_MAP_POLLFISH = "setAttributesMap";
    private static final String SET_EVENTS_POLLFISH = "setEventCallback";
    
    
    // Pollfish listeners
    
    private CallbackContext onPollfishSurveyReceived=null;
    private CallbackContext onPollfishSurveyCompleted =null;
    private CallbackContext onPollfishSurveyNotAvailable=null;
    private CallbackContext onPollfishUserNotEligible=null;
    private CallbackContext onPollfishUserRejectedSurvey=null;
    private CallbackContext onPollfishOpened=null;
    private CallbackContext onPollfishClosed=null;
    
    
    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }
    
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param pollfishParams              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback context used when calling back into JavaScript.
     * @return                  True when the action was valid, false otherwise.
     */
    @Override
    public boolean execute(String action, final JSONArray pollfishParams, CallbackContext callbackContext) throws JSONException {
        
        Log.d(TAG, "action: "+ action);
        Log.d(TAG, "Number of Pollfish params: "+ pollfishParams.length());
        
        if (action.equals(INIT_POLLFISH))  {
            
            final String curAction=action;
            
            final boolean debugMode = pollfishParams.getBoolean(0);
            final boolean customMode = pollfishParams.getBoolean(1);
            
            final String apiKey = pollfishParams.getString(2);
            final int positionInt =pollfishParams.getInt(3);
            final int indPadding=pollfishParams.getInt(4);
            
            String request_uuid_temp =null;
            
            if(pollfishParams.length()>5) {
                request_uuid_temp  =pollfishParams.getString(5);
            }
            
            final String request_uuid = request_uuid_temp;
            
            Log.d(TAG, "debugMode: "+ debugMode);
            Log.d(TAG, "customMode: "+ customMode);
            Log.d(TAG, "apiKey: "+ apiKey);
            Log.d(TAG, "position: "+ positionInt);
            Log.d(TAG, "indPadding: "+ indPadding);
            Log.d(TAG, "request_uuid: "+ request_uuid);
            
            final PollfishSurveyReceivedListener pollfishSurveyReceivedListener = new PollfishSurveyReceivedListener() {
                
                @Override
                public void onPollfishSurveyReceived(boolean playfulSurveys, int surveyPrice) {
                    
                    Log.d(TAG, "onPollfishSurveyReceived (" + playfulSurveys  + ","+ String.valueOf(surveyPrice) + ")");
                    
                    if(onPollfishSurveyReceived!=null) {
                        
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                            
                            json_object.put("playfulSurveys", playfulSurveys);
                            json_object.put("surveyPrice"    , surveyPrice);
                            
                        } catch (JSONException exception) {
                            
                            Log.e(TAG, "Exception onPollfishSurveyReceived: "+ exception);
                            
                            throw new RuntimeException(exception);
                        }
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK, json_object);
                        result.setKeepCallback(true);
                        onPollfishSurveyReceived.sendPluginResult(result);
                        
                    }
                }
            };
            
            final PollfishSurveyCompletedListener pollfishSurveyCompletedListener = new PollfishSurveyCompletedListener() {
                
                @Override
                public void onPollfishSurveyCompleted(boolean playfulSurveys, int surveyPrice) {
                    
                    Log.d(TAG, "onPollfishSurveyCompleted (" + playfulSurveys + ","+ String.valueOf(surveyPrice) + ")");
                    
                    if(onPollfishSurveyCompleted!=null) {
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                            
                            json_object.put("playfulSurveys", playfulSurveys);
                            json_object.put("surveyPrice"    , surveyPrice);
                            
                        } catch (JSONException exception) {
                            
                            Log.e(TAG, "Exception onPollfishSurveyCompleted: "+ exception);
                            
                            throw new RuntimeException(exception);
                        }
                        
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK,json_object);
                        result.setKeepCallback(true);
                        onPollfishSurveyCompleted.sendPluginResult(result);
                    }
                    
                }
            };
            
            final PollfishOpenedListener pollfishOpenedListener = new PollfishOpenedListener() {
                
                public void onPollfishOpened() {
                    
                    Log.d(TAG, "onPollfishOpened");
                    
                    if(onPollfishOpened!=null) {
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(false);
                        onPollfishOpened.sendPluginResult(result);
                    }
                    
                };
                
            };
            
            final PollfishClosedListener pollfishClosedListener = new PollfishClosedListener() {
                
                public void onPollfishClosed() {
                    
                    Log.d(TAG, "onPollfishClosed");
                    
                    if(onPollfishClosed!=null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishClosed.sendPluginResult(result);
                    }
                    
                }
            };
            
            final PollfishSurveyNotAvailableListener pollfishSurveyNotAvailableListener = new PollfishSurveyNotAvailableListener() {
                public void onPollfishSurveyNotAvailable() {
                    
                    Log.d(TAG, "onPollfishSurveyNotAvailable");
                    
                    if(onPollfishSurveyNotAvailable!=null) {
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishSurveyNotAvailable.sendPluginResult(result);
                    }
                    
                };
                
            };
            
            final PollfishUserNotEligibleListener pollfishUserNotEligibleListener = new PollfishUserNotEligibleListener() {
                public void onUserNotEligible() {
                    
                    Log.d(TAG, "onUserNotEeligible");
                    
                    if(onPollfishUserNotEligible!=null) {
                        
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishUserNotEligible.sendPluginResult(result);
                    }
                }
            };
            
            final PollfishUserRejectedSurveyListener pollfishUserRejectedSurveyListener = new PollfishUserRejectedSurveyListener() {
                public void onUserRejectedSurvey() {
                    
                    Log.d(TAG, "onUserRejectedSurvey");
                    
                    if(onPollfishUserRejectedSurvey!=null) {
                        
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishUserRejectedSurveyEligible.sendPluginResult(result);
                    }
                }
            };
            
            final Position position = Position.values()[positionInt];
            
            
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    if(!customMode) {
                        
                        
                        PollFish.init(cordova.getActivity(), apiKey, position,
                                      indPadding, pollfishSurveyReceivedListener,
                                      pollfishSurveyNotAvailableListener,
                                      pollfishSurveyCompletedListener,
                                      pollfishOpenedListener, pollfishClosedListener,
                                      pollfishUserNotEligibleListener,pollfishUserNotEligibleListener,null,request_uuid);
                        
                    }else{
                        
                        PollFish.customInit(cordova.getActivity(), apiKey, position,
                                            indPadding, pollfishSurveyReceivedListener,
                                            pollfishSurveyNotAvailableListener,
                                            pollfishSurveyCompletedListener,
                                            pollfishOpenedListener, pollfishClosedListener,
                                            pollfishUserNotEligibleListener,pollfishUserNotEligibleListener, null, request_uuid);
                    }
                }
            });
            
            return true;
            
        }else if(action.equals(SHOW_POLLFISH)){
            
            Log.d(TAG, "show Pollfish");
            
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    PollFish.show();
                }
            });
            
        }else if(action.equals(HIDE_POLLFISH)){
            
            Log.d(TAG, "hide Pollfish");
            
            
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    PollFish.hide();
                }
            });
            
        }else if(action.equals(SET_ATTRIBUTES_MAP_POLLFISH)){
            
            Log.d(TAG, "set Attributes Map");
            
            
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    Map<String, String> map = new HashMap<String,String>();
                    
                    JSONObject mapObject = pollfishParams.optJSONObject(0);
                    
                    Iterator<?> keys = mapObject.keys();
                    
                    while (keys.hasNext()) {
                        
                        try {
                            String key = (String) keys.next();
                            String value = mapObject.getString(key);
                            
                            Log.d(TAG, "Attribute with key: " + key + " and value: " + value);
                            
                            
                            map.put(key, value);
                            
                        }catch (JSONException e){
                            
                            Log.e(TAG, "Exception while iterating in map dictionary: " + e);
                            
                        }
                        
                        PollFish.setAttributesMap(map);
                    }
                }});
            
            
            
        }else if(action.equals(SET_EVENTS_POLLFISH)) {
            
            Log.d(TAG, "set event listeners");
            
            String eventName = pollfishParams.getString(0);
            
            setEventCallback(eventName,callbackContext);
        }
        
        
        return true;
    }
    
    
    private void setEventCallback (String eventName,CallbackContext callbackFunction) {
        
        Log.d ("Pollfish", "setEventCallback name: " + eventName);
        
        if("onPollfishSurveyReceived".equals(eventName)) {
            
            Log.d ("Pollfish", "onPollfishSurveyReceived set");
            
            onPollfishSurveyReceived= callbackFunction;
            
        }else if ("onPollfishSurveyNotAvailable".equals(eventName)) {
            
            onPollfishSurveyNotAvailable= callbackFunction;
            
        }else if ("onPollfishSurveyCompleted".equals(eventName)) {
            
            onPollfishSurveyCompleted = callbackFunction;
            
        }else if ("onPollfishUserNotEligible".equals(eventName)) {
            
            onPollfishUserNotEligible= callbackFunction;
            
        }else if ("onPollfishUserRejectedSurvey".equals(eventName)) {
            
            onPollfishUserRejectedSurvey= callbackFunction;
            
        }else if ("onPollfishOpened".equals(eventName)) {
            
            onPollfishOpened = callbackFunction;
            
        }else if ("onPollfishClosed".equals(eventName)) {
            
            onPollfishClosed= callbackFunction;
        }
    }
    
    public Position getPollfishIndicatorPosition(String positionStr) {
        
        Position indPosition;
        
        if(positionStr.equals("TOP_LEFT")){
            indPosition = Position.TOP_LEFT;
        }else if(positionStr.equals("MIDDLE_LEFT")){
            indPosition=Position.MIDDLE_LEFT;
        }else if(positionStr.equals("BOTTOM_LEFT")){
            indPosition=Position.BOTTOM_LEFT;
        }else if(positionStr.equals("TOP_RIGHT")){
            indPosition=Position.TOP_RIGHT;
        }else if(positionStr.equals("MIDDLE_RIGHT")){
            indPosition=Position.MIDDLE_RIGHT;
        }else if(positionStr.equals("BOTTOM_RIGHT")) {
            indPosition = Position.BOTTOM_RIGHT;
        }else{
            indPosition=Position.BOTTOM_RIGHT;
        }
        
        return indPosition;
    }
}