/*global cordova, module*/

module.exports = {
    
	/**
     * Function to init Pollfish
     *
     * @param releaseMode
     * @param rewardMode
     * @param apiKey
     * @param position
     * @param request_uuid
     * @param offerwallMode
     */
    init: function(releaseMode, rewardMode, apiKey,position,indPadding,request_uuid, offerwallMode) {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [releaseMode, rewardMode, apiKey, position, indPadding,request_uuid, offerwallMode]);
        
    },
    
    /**
     * Function to init Pollfish
     *
     * @param releaseMode
     * @param rewardMode
     * @param apiKey
     * @param position
     * @param request_uuid
     * @param offerwallMode
     * @param userAttributes
     */
    initWithUserAttributes: function(releaseMode, rewardMode, apiKey,position,indPadding,request_uuid, offerwallMode, userAttributes) {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [releaseMode, rewardMode, apiKey, position, indPadding,request_uuid, offerwallMode, userAttributes]);
        
    },
    
    /**
     * Function to manually call show Pollfish
     */
    
    showPollfish: function() {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "show",[]);
        
    },
    
    /**
     * Function to manually call hide Pollfish
     */
    
    hidePollfish: function() {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "hide",[]);
        
    },
    
    /**
     * Function to set event callbacks 
     * @param eventName
     * @param callbackFunction
     */
    
    setEventCallback: function (eventName,callbackFunction) {
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
        TOP_LEFT : 0,
        BOTTOM_LEFT : 1,
        TOP_RIGHT : 2,
        BOTTOM_RIGHT : 3,
        MIDDLE_LEFT :4,
        MIDDLE_RIGHT : 5
    }

    
};
