/*global cordova, module*/

module.exports = {

 	
 	/**
     * Function to init Pollfish
     *
     * @param debugMode
     * @param customMode
     * @param apiKey
     * @param position
     * @param indPadding
     */
    
    init: function(debugMode, customMode, apiKey,position,indPadding) {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [debugMode, customMode, apiKey, position, indPadding]);
        
    },

    
    /**
     * Function to init Pollfish with request uuid for s2s callback
     *
     * @param debugMode
     * @param customMode
     * @param apiKey
     * @param position
     * @param request_uuid
     */
    initWithRequestUUID: function(debugMode, customMode, apiKey,position,indPadding,request_uuid) {
        
        cordova.exec (undefined, undefined, "PollfishPlugin", "init", [debugMode, customMode, apiKey, position, indPadding,request_uuid]);
        
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
