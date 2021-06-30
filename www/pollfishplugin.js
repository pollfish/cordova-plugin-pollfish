/*global cordova, module*/

module.exports = {
    
	/**
     * Function to init Pollfish
     *
     * @param releaseMode
     * @param rewardMode
     * @param apiKey
     * @param position
     * @param requestUUID
     * @param offerwallMode
     */
    init: function(releaseMode, rewardMode, apiKey, position, indPadding, requestUUID, offerwallMode) {
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [releaseMode, rewardMode, apiKey, position, indPadding, requestUUID, offerwallMode]);
    },
    
    /**
     * Function to init Pollfish with user attributes
     *
     * @param releaseMode
     * @param rewardMode
     * @param apiKey
     * @param position
     * @param requestUUID
     * @param offerwallMode
     * @param userAttributes
     */
    initWithUserAttributes: function(releaseMode, rewardMode, apiKey, position, indPadding, requestUUID, offerwallMode, userAttributes) {
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [releaseMode, rewardMode, apiKey, position, indPadding, requestUUID, offerwallMode, userAttributes]);   
    },
    
    /**
     * Function to manually show Pollfish
     */
    
    showPollfish: function() {
        cordova.exec (undefined, undefined, "PollfishPlugin", "show", []);
    },
    
    /**
     * Function to manually hide Pollfish
     */
    
    hidePollfish: function() {
        cordova.exec (undefined, undefined, "PollfishPlugin", "hide", []);
    },
    
    /**
     * Function to set event callbacks 
     * @param eventName
     * @param callbackFunction
     */
    
    setEventCallback: function (eventName, callbackFunction) {
        cordova.exec (callbackFunction, undefined, "PollfishPlugin", "setEventCallback", [eventName])
    },
    
    /**
     * Function to sent extra user attributes in a key/pair dictionary
     * @param map
     */
    
    setAttributesMap: function (map) { 
        cordova.exec (undefined, undefined, "PollfishPlugin", "setAttributesMap", [map])
    },

    Position: {
        TOP_LEFT: 0,
        TOP_RIGHT: 1,
        MIDDLE_LEFT: 2,
        MIDDLE_RIGHT: 3,
        BOTTOM_LEFT:4,
        BOTTOM_RIGHT: 5
    },

    EventListener: {
        OnPollfishClosed: 'onPollfishClosed',
        OnPollfishOpened: 'onPollfishOpened',
        OnPollfishSurveyReceived: 'onPollfishSurveyReceived',
        OnPollfishSurveyCompleted: 'onPollfishSurveyCompleted',
        OnPollfishUserNotEligible: 'onPollfishUserNotEligible',
        OnPollfishUserRejectedSurvey: 'onPollfishUserRejectedSurvey',
        OnPollfishSurveyNotAvailable: 'onPollfishSurveyNotAvailable'
    }
    
};
