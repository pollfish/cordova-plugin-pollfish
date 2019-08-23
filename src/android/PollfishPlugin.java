package com.pollfish.cordova;

/**
 * Created by Pollfish on 4/27/15.
 */

import android.util.Log;

import com.pollfish.constants.Position;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishCompletedSurveyListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishReceivedSurveyListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.interfaces.PollfishUserRejectedSurveyListener;
import com.pollfish.constants.UserProperties;
import com.pollfish.main.PollFish;
import com.pollfish.classes.SurveyInfo;
import com.pollfish.main.PollFish.ParamsBuilder;

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
            
            final boolean releaseMode = pollfishParams.getBoolean(0);
            final boolean rewardMode = pollfishParams.getBoolean(1);
            
            final String apiKey = pollfishParams.getString(2);
            final int positionInt =pollfishParams.getInt(3);
            final int indPadding=pollfishParams.getInt(4);
            
            String request_uuid_temp =null;
            
            if(pollfishParams.length()>=5) {
                request_uuid_temp  =pollfishParams.getString(5);
            }
                     
            boolean offerwallMode_temp =false;
        
            if(pollfishParams.length()>=6) {
                offerwallMode_temp  = pollfishParams.getBoolean(6);
            }
       
             JSONObject mapObject_temp = null;
             UserProperties userProperties_temp=null;
             
             if(pollfishParams.length()>=7) {
               
               mapObject_temp  = pollfishParams.optJSONObject(7);
            
     		   userProperties_temp = new UserProperties();
                    
               
                Iterator<?> keys = mapObject_temp.keys();
                    
                while (keys.hasNext()) {
                        
                        try {
                            String key = (String) keys.next();
                            String value = mapObject_temp.getString(key);
                            
                            Log.d(TAG, "Attribute with key: " + key + " and value: " + value);
                            
                            
                            userProperties_temp.setCustomAttributes(key, value);
                            
                        }catch (JSONException e){
                            
                            Log.e(TAG, "Exception while iterating in map dictionary: " + e);
                            
                        }
                        
                }
            }

            
            final String request_uuid = request_uuid_temp;
            final boolean offerwallMode = offerwallMode_temp;
            final JSONObject mapObject = mapObject_temp;
            final UserProperties userProperties =  userProperties_temp;
            
            Log.d(TAG, "releaseMode: "+ releaseMode);
            Log.d(TAG, "rewardMode: "+ rewardMode);
            Log.d(TAG, "apiKey: "+ apiKey);
            Log.d(TAG, "position: "+ positionInt);
            Log.d(TAG, "indPadding: "+ indPadding);
            Log.d(TAG, "request_uuid: "+ request_uuid);
            Log.d(TAG, "offerwallMode: "+ offerwallMode);
            
            final PollfishReceivedSurveyListener pollfishReceivedSurveyListener = new PollfishReceivedSurveyListener() {
                
                @Override
                public void onPollfishSurveyReceived(SurveyInfo surveyInfo) {
        
        
        		
                    if(onPollfishSurveyReceived!=null) {
                        
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                        
                        if(surveyInfo!=null){
                            
                        	Log.d(TAG, "Pollfish onPollfishSurveyReceived :: CPA: " + surveyInfo.getSurveyCPA()+ " SurveyClass: " + surveyInfo.getSurveyClass() + " LOI: " + surveyInfo.getSurveyLOI() + " IR: " + surveyInfo.getSurveyIR());
   
                            json_object.put("survey_cpa", surveyInfo.getSurveyCPA());
               				json_object.put("survey_ir", surveyInfo.getSurveyIR());
               				json_object.put("survey_loi", surveyInfo.getSurveyLOI());
               				json_object.put("survey_class", surveyInfo.getSurveyClass());
               				json_object.put("reward_name", surveyInfo.getRewardName());
               				json_object.put("reward_value", surveyInfo.getRewardValue());
               				
               				}
                            
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
            
            final PollfishCompletedSurveyListener pollfishCompletedSurveyListener = new PollfishCompletedSurveyListener() {
                
                @Override
                public void onPollfishSurveyCompleted(SurveyInfo surveyInfo) {
                    
                  
                    if(onPollfishSurveyCompleted!=null) {
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                            
                           if(surveyInfo!=null){
                                       
                                       
                            Log.d(TAG, "Pollfish onPollfishSurveyCompleted :: CPA: " + surveyInfo.getSurveyCPA()+ " SurveyClass: " + surveyInfo.getSurveyClass() + " LOI: " + surveyInfo.getSurveyLOI() + " IR: " + surveyInfo.getSurveyIR());
   
   
                       		json_object.put("survey_cpa", surveyInfo.getSurveyCPA());
               				json_object.put("survey_ir", surveyInfo.getSurveyIR());
               				json_object.put("survey_loi", surveyInfo.getSurveyLOI());
               				json_object.put("survey_class", surveyInfo.getSurveyClass());
               				json_object.put("reward_name", surveyInfo.getRewardName());
               				json_object.put("reward_value", surveyInfo.getRewardValue());
               				}
                            
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
                        onPollfishUserRejectedSurvey.sendPluginResult(result);
                    }
                }
            };
            
            final Position position = Position.values()[positionInt];
            
            
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
    
                        ParamsBuilder paramsBuilder = new ParamsBuilder(apiKey)
                        .indicatorPadding(indPadding)
                        .indicatorPosition(position)
                        .rewardMode(rewardMode)
                        .releaseMode(releaseMode)
                        .pollfishOpenedListener(pollfishOpenedListener)
                        .pollfishUserRejectedSurveyListener(pollfishUserRejectedSurveyListener)
                        .pollfishClosedListener(pollfishClosedListener)
                        .pollfishCompletedSurveyListener(pollfishCompletedSurveyListener)
                        .pollfishSurveyNotAvailableListener(pollfishSurveyNotAvailableListener)
                        .pollfishReceivedSurveyListener(pollfishReceivedSurveyListener)
                        .pollfishUserNotEligibleListener(pollfishUserNotEligibleListener)
                        .requestUUID(request_uuid)
                        .offerWallMode(offerwallMode)
                        .userProperties(userProperties)
                        .build();


                	PollFish.initWith(cordova.getActivity(), paramsBuilder);
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
            
            onPollfishSurveyReceived = callbackFunction;
            
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