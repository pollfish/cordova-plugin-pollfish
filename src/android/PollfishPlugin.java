package com.pollfish.cordova;

import android.util.Log;

import com.pollfish.builder.*;
import com.pollfish.callback.*;
import com.pollfish.Pollfish;

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
    
    private CallbackContext onPollfishSurveyReceived = null;
    private CallbackContext onPollfishSurveyCompleted = null;
    private CallbackContext onPollfishSurveyNotAvailable = null;
    private CallbackContext onPollfishUserNotEligible = null;
    private CallbackContext onPollfishUserRejectedSurvey = null;
    private CallbackContext onPollfishOpened = null;
    private CallbackContext onPollfishClosed = null;
    
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

        if (action.equals(INIT_POLLFISH))  {

            final String curAction = action;
            
            final boolean releaseMode = pollfishParams.getBoolean(0);
            final boolean rewardMode = pollfishParams.getBoolean(1);
            final String apiKey = pollfishParams.getString(2);
            final Position position = getPollfishIndicatorPosition(pollfishParams.getInt(3));
            final int indPadding = pollfishParams.getInt(4);
            
            String request_uuid_temp = null;
            
            if(pollfishParams.length() >= 6) {
                request_uuid_temp = pollfishParams.getString(5);
            }
                     
            boolean offerwallMode_temp = false;
        
            if(pollfishParams.length() >= 7) {
                offerwallMode_temp  = pollfishParams.getBoolean(6);
            }
       
            JSONObject mapObject_temp = null;
            UserProperties.Builder userProperties_temp = null;
             
            if(pollfishParams.length() >= 8) {
               
                mapObject_temp = pollfishParams.optJSONObject(7);
            
     		    userProperties_temp = new UserProperties.Builder();
                    
                Iterator<?> keys = mapObject_temp.keys();
                    
                while (keys.hasNext()) { 
                    try {
                        String key = (String) keys.next();
                        String value = mapObject_temp.getString(key);

                        userProperties_temp.customAttribute(key, value); 
                    } catch (JSONException e) {

                        Log.e(TAG, "Exception while iterating in map dictionary: " + e);
                        
                    }
                }
            }
            
            final String request_uuid = request_uuid_temp;
            final boolean offerwallMode = offerwallMode_temp;
            final JSONObject mapObject = mapObject_temp;

            UserProperties userProperties = null;

            if (userProperties_temp != null) {
                userProperties = userProperties_temp.build();
            }

            final PollfishSurveyReceivedListener pollfishSurveyReceivedListener = new PollfishSurveyReceivedListener() {
                
                @Override
                public void onPollfishSurveyReceived(SurveyInfo surveyInfo) {
        
                    if(onPollfishSurveyReceived != null) {
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                        
                            if(surveyInfo != null){
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
            
            final PollfishSurveyCompletedListener pollfishSurveyCompletedListener = new PollfishSurveyCompletedListener() {
                
                @Override
                public void onPollfishSurveyCompleted(SurveyInfo surveyInfo) {

                    if(onPollfishSurveyCompleted != null) {
                        
                        JSONObject json_object = new JSONObject();
                        
                        try {
                            
                           if (surveyInfo != null){
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

                @Override
                public void onPollfishOpened() {

                    if (onPollfishOpened != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(false);
                        onPollfishOpened.sendPluginResult(result);
                    }
                    
                }
                
            };
            
            final PollfishClosedListener pollfishClosedListener = new PollfishClosedListener() {

                @Override
                public void onPollfishClosed() {
                    if (onPollfishClosed != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishClosed.sendPluginResult(result);
                    }
                }

            };
            
            final PollfishSurveyNotAvailableListener pollfishSurveyNotAvailableListener = new PollfishSurveyNotAvailableListener() {

                @Override
                public void onPollfishSurveyNotAvailable() {

                    if (onPollfishSurveyNotAvailable != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishSurveyNotAvailable.sendPluginResult(result);
                    }
                    
                }

            };
            
            final PollfishUserNotEligibleListener pollfishUserNotEligibleListener = new PollfishUserNotEligibleListener() {

                @Override
                public void onUserNotEligible() {
                    if (onPollfishUserNotEligible != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishUserNotEligible.sendPluginResult(result);
                    }
                }

            };
            
            final PollfishUserRejectedSurveyListener pollfishUserRejectedSurveyListener = new PollfishUserRejectedSurveyListener() {

                @Override
                public void onUserRejectedSurvey() {
                    if (onPollfishUserRejectedSurvey != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK);
                        result.setKeepCallback(true);
                        onPollfishUserRejectedSurvey.sendPluginResult(result);
                    }
                }

            };            
            
            Params.Builder paramsBuilder = new Params.Builder(apiKey)
                .indicatorPadding(indPadding)
                .indicatorPosition(position)
                .rewardMode(rewardMode)
                .releaseMode(releaseMode)
                .platform(Platform.CORDOVA)
                .pollfishOpenedListener(pollfishOpenedListener)
                .pollfishUserRejectedSurveyListener(pollfishUserRejectedSurveyListener)
                .pollfishClosedListener(pollfishClosedListener)
                .pollfishSurveyCompletedListener(pollfishSurveyCompletedListener)
                .pollfishSurveyNotAvailableListener(pollfishSurveyNotAvailableListener)
                .pollfishSurveyReceivedListener(pollfishSurveyReceivedListener)
                .pollfishUserNotEligibleListener(pollfishUserNotEligibleListener)
                .requestUUID(request_uuid)
                .offerwallMode(offerwallMode);

            if (userProperties != null) {
                paramsBuilder.userProperties(userProperties);
            }
                
            Params params = paramsBuilder.build();

            Pollfish.initWith(cordova.getActivity(), params);
            
            return true;
            
        } else if (action.equals(SHOW_POLLFISH)) {

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    
                    Pollfish.show();
                }
            });
            
        } else if (action.equals(HIDE_POLLFISH)) {

            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    Pollfish.hide();
                }
            }); 
            
        } else if (action.equals(SET_EVENTS_POLLFISH)) {

            String eventName = pollfishParams.getString(0);
            
            setEventCallback(eventName,callbackContext);
        }
        
        return true;
    }
    
    
    private void setEventCallback(String eventName, CallbackContext callbackFunction) {

        if ("onPollfishSurveyReceived".equals(eventName)) {

            onPollfishSurveyReceived = callbackFunction;
            
        } else if ("onPollfishSurveyNotAvailable".equals(eventName)) {
            
            onPollfishSurveyNotAvailable = callbackFunction;
            
        } else if ("onPollfishSurveyCompleted".equals(eventName)) {
            
            onPollfishSurveyCompleted = callbackFunction;
            
        } else if ("onPollfishUserNotEligible".equals(eventName)) {
            
            onPollfishUserNotEligible = callbackFunction;
            
        } else if ("onPollfishUserRejectedSurvey".equals(eventName)) {
            
            onPollfishUserRejectedSurvey = callbackFunction;
            
        } else if ("onPollfishOpened".equals(eventName)) {
            
            onPollfishOpened = callbackFunction;
            
        } else if ("onPollfishClosed".equals(eventName)) {
            
            onPollfishClosed = callbackFunction;
        }
    }
    
    public Position getPollfishIndicatorPosition(int positionInt) {
        
        Position indPosition;

        if (positionInt == 1) {
            indPosition=Position.TOP_RIGHT;
        } else if (positionInt == 2) {
            indPosition=Position.MIDDLE_LEFT;
        } else if (positionInt == 3) {
            indPosition=Position.MIDDLE_RIGHT;
        }else if (positionInt == 4) {
            indPosition=Position.BOTTOM_LEFT;
        } else if (positionInt == 5) {
            indPosition = Position.BOTTOM_RIGHT;
        } else {
            indPosition=Position.TOP_LEFT;
        }
        
        return indPosition;
    }
}
