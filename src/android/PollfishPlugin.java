package com.pollfish.cordova;

import android.util.Log;

import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.builder.Platform;
import com.pollfish.builder.Position;
import com.pollfish.builder.RewardInfo;
import com.pollfish.builder.UserProperties;
import com.pollfish.callback.PollfishClosedListener;
import com.pollfish.callback.PollfishOpenedListener;
import com.pollfish.callback.PollfishSurveyCompletedListener;
import com.pollfish.callback.PollfishSurveyNotAvailableListener;
import com.pollfish.callback.PollfishSurveyReceivedListener;
import com.pollfish.callback.PollfishUserNotEligibleListener;
import com.pollfish.callback.PollfishUserRejectedSurveyListener;
import com.pollfish.callback.SurveyInfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PollfishPlugin extends CordovaPlugin {

    protected static String TAG = "PollfishPlugin";

    // Pollfish functions

    private static final String INIT_POLLFISH = "init";
    private static final String SHOW_POLLFISH = "show";
    private static final String HIDE_POLLFISH = "hide";
    private static final String IS_POLLFISH_PRESENT = "isPollfishPresent";
    private static final String IS_POLLFISH_PANEL_OPEN = "isPollfishPanelOpen";
    private static final String SET_EVENTS_POLLFISH = "setEventCallback";

    // Pollfish listeners

    private CallbackContext onPollfishSurveyReceived = null;
    private CallbackContext onPollfishSurveyCompleted = null;
    private CallbackContext onPollfishSurveyNotAvailable = null;
    private CallbackContext onPollfishUserNotEligible = null;
    private CallbackContext onPollfishUserRejectedSurvey = null;
    private CallbackContext onPollfishOpened = null;
    private CallbackContext onPollfishClosed = null;

    private Boolean initialized = false;

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param pollfishParams  JSONArray of arguments for the plugin.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return True when the action was valid, false otherwise.
     */
    @Override
    public boolean execute(String action, final JSONArray pollfishParams, CallbackContext callbackContext) throws JSONException {

        if (action.equals(INIT_POLLFISH)) {
            final String apiKey = pollfishParams.getString(0);
            final Position position = getPollfishIndicatorPosition(pollfishParams.getInt(2));
            final int indicatorPadding = pollfishParams.getInt(3);
            final boolean offerwallMode = pollfishParams.getBoolean(4);
            final boolean releaseMode = pollfishParams.getBoolean(5);
            final boolean rewardMode = pollfishParams.getBoolean(6);
            final String requestUUID = pollfishParams.getString(7);
            final Object userPropertiesObject = pollfishParams.get(8);
            final String clickId = pollfishParams.getString(9);
            final String signature = pollfishParams.getString(10);
            final Object rewardInfoObject = pollfishParams.get(11);

            if (apiKey == null || apiKey.equals("null")) {
                initialized = false;
                return false;
            }

            Params.Builder paramsBuilder = new Params.Builder(apiKey)
                    .indicatorPadding(indicatorPadding)
                    .indicatorPosition(position)
                    .rewardMode(rewardMode)
                    .releaseMode(releaseMode)
                    .offerwallMode(offerwallMode)
                    .platform(Platform.CORDOVA);

            if (requestUUID != null && !requestUUID.equals("null")) {
                paramsBuilder.requestUUID(requestUUID);
            }

            if (clickId != null && !clickId.equals("null")) {
                paramsBuilder.clickId(clickId);
            }

            if (signature != null && !signature.equals("null")) {
                paramsBuilder.signature(signature);
            }

            if (!userPropertiesObject.equals(null)) {
                UserProperties.Builder userPropertiesBuiler = new UserProperties.Builder();
                Iterator<?> keys = ((JSONObject) userPropertiesObject).keys();

                while (keys.hasNext()) {
                    try {
                        String key = (String) keys.next();
                        String value = ((JSONObject) userPropertiesObject).getString(key);

                        userPropertiesBuiler.customAttribute(key, value);
                    } catch (JSONException exception) {
                        Log.e(TAG, "Exception while iterating in map dictionary: " + exception);
                    }
                }

                paramsBuilder.userProperties(userPropertiesBuiler.build());
            }

            if (!rewardInfoObject.equals(null)) {
                try {
                    RewardInfo rewardInfo = new RewardInfo(
                        ((JSONObject) rewardInfoObject).getString("rewardName"),
                        ((JSONObject) rewardInfoObject).getDouble("rewardConversion"));

                    paramsBuilder.rewardInfo(rewardInfo);
                } catch (Exception exception) {
                    Log.e(TAG, "Exception while accessing reward info: " + exception);
                }
                
            }

            final PollfishSurveyReceivedListener pollfishSurveyReceivedListener = new PollfishSurveyReceivedListener() {

                @Override
                public void onPollfishSurveyReceived(SurveyInfo surveyInfo) {
                    if (onPollfishSurveyReceived != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK, getJsonObjectFromSurveyInfo(surveyInfo));
                        result.setKeepCallback(true);
                        onPollfishSurveyReceived.sendPluginResult(result);
                    }
                }
            };

            final PollfishSurveyCompletedListener pollfishSurveyCompletedListener = new PollfishSurveyCompletedListener() {
                @Override
                public void onPollfishSurveyCompleted(SurveyInfo surveyInfo) {
                    if (onPollfishSurveyCompleted != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK, getJsonObjectFromSurveyInfo(surveyInfo));
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

            paramsBuilder
                    .pollfishOpenedListener(pollfishOpenedListener)
                    .pollfishUserRejectedSurveyListener(pollfishUserRejectedSurveyListener)
                    .pollfishClosedListener(pollfishClosedListener)
                    .pollfishSurveyCompletedListener(pollfishSurveyCompletedListener)
                    .pollfishSurveyNotAvailableListener(pollfishSurveyNotAvailableListener)
                    .pollfishSurveyReceivedListener(pollfishSurveyReceivedListener)
                    .pollfishUserNotEligibleListener(pollfishUserNotEligibleListener);

            Params params = paramsBuilder.build();

            Pollfish.initWith(cordova.getActivity(), params);

            initialized = true;

            return true;

        } else if (action.equals(SHOW_POLLFISH) && initialized) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Pollfish.show();
                }
            });
        } else if (action.equals(HIDE_POLLFISH) && initialized) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Pollfish.hide();
                }
            });
        } else if (action.equals(SET_EVENTS_POLLFISH)) {
            String eventName = pollfishParams.getString(0);
            setEventCallback(eventName, callbackContext);
        } else if (action.equals(IS_POLLFISH_PANEL_OPEN)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, initialized ? Pollfish.isPollfishPanelOpen() : false);
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
        } else if (action.equals(IS_POLLFISH_PRESENT)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, initialized ? Pollfish.isPollfishPresent() : initialized);
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });
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

    private Position getPollfishIndicatorPosition(int positionInt) {
        Position indPosition;

        if (positionInt == 1) {
            indPosition = Position.TOP_RIGHT;
        } else if (positionInt == 2) {
            indPosition = Position.MIDDLE_LEFT;
        } else if (positionInt == 3) {
            indPosition = Position.MIDDLE_RIGHT;
        } else if (positionInt == 4) {
            indPosition = Position.BOTTOM_LEFT;
        } else if (positionInt == 5) {
            indPosition = Position.BOTTOM_RIGHT;
        } else {
            indPosition = Position.TOP_LEFT;
        }

        return indPosition;
    }

    private JSONObject getJsonObjectFromSurveyInfo(SurveyInfo surveyInfo) {
        JSONObject jsonObject = new JSONObject();

        try {
            if (surveyInfo != null) {
                jsonObject.put("survey_cpa", surveyInfo.getSurveyCPA());
                jsonObject.put("survey_ir", surveyInfo.getSurveyIR());
                jsonObject.put("survey_loi", surveyInfo.getSurveyLOI());
                jsonObject.put("survey_class", surveyInfo.getSurveyClass());
                jsonObject.put("reward_name", surveyInfo.getRewardName());
                jsonObject.put("reward_value", surveyInfo.getRewardValue());
                jsonObject.put("remaining_completes", surveyInfo.getRemainingCompletes());
            }
        } catch (JSONException exception) {
            Log.e(TAG, "Exception while accessing survey info: " + exception);
        }

        return jsonObject;
    }
}
