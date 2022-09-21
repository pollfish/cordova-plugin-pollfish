/*global cordova, module*/

const Position = {
    TOP_LEFT: 0,
    TOP_RIGHT: 1,
    MIDDLE_LEFT: 2,
    MIDDLE_RIGHT: 3,
    BOTTOM_LEFT:4,
    BOTTOM_RIGHT: 5
}

const EventListener = {
    OnPollfishClosed: 'onPollfishClosed',
    OnPollfishOpened: 'onPollfishOpened',
    OnPollfishSurveyReceived: 'onPollfishSurveyReceived',
    OnPollfishSurveyCompleted: 'onPollfishSurveyCompleted',
    OnPollfishUserNotEligible: 'onPollfishUserNotEligible',
    OnPollfishUserRejectedSurvey: 'onPollfishUserRejectedSurvey',
    OnPollfishSurveyNotAvailable: 'onPollfishSurveyNotAvailable'
}

let Params = function(
    androidApiKey, 
    iOSApiKey, 
    indicatorPosition,
    indicatorPadding,
    offerwallMode,
    releaseMode,
    rewardMode,
    requestUUID,
    userProperties,
    clickId,
    userId,
    rewardInfo,
    signature) {
        this.androidApiKey = androidApiKey;
        this.iOSApiKey = iOSApiKey;
        this.indicatorPosition = indicatorPosition;
        this.indicatorPadding = indicatorPadding;
        this.offerwallMode = offerwallMode;
        this.releaseMode = releaseMode;
        this.rewardMode = rewardMode;
        this.requestUUID = requestUUID;
        this.userProperties = userProperties;
        this.clickId = clickId;
        this.userId = userId;
        this.rewardInfo = rewardInfo;
        this.signature = signature;
    };

let Builder = function(androidApiKey, iOSApiKey) {
    this._androidApiKey = androidApiKey;
    this._iOSApiKey = iOSApiKey;
    this._indicatorPosition = Position.TOP_LEFT;
    this._indicatorPadding = 8;
    this._offerwallMode = false;
    this._releaseMode = false;
    this._rewardMode = false;
    this._requestUUID = null;
    this._userProperties = {};
    this._clickId = null;
    this._userId = null;
    this._rewardInfo = null;
    this._signature = null;
};

/**
 * Sets the Position where you wish to place the Pollfish indicator
 * 
 * @param {number} indicatorPosition 
 * @returns {Builder} itself
 */
Builder.prototype.indicatorPosition = function(indicatorPosition) {
    this._indicatorPosition = indicatorPosition;
    return this;
};

/**
 * Sets the padding from the top or the bottom of the view, according to the Position of Pollfish indicator
 * 
 * @param {number} indicatorPadding 
 * @returns {Builder} itself
 */
Builder.prototype.indicatorPadding = function(indicatorPadding) {
    this._indicatorPadding = indicatorPadding;
    return this;
};

/**
 * Sets Pollfish to offerwall mode
 * 
 * @param {Boolean} offerwallMode 
 * @returns {Builder} itself
 */
Builder.prototype.offerwallMode = function(offerwallMode) {
    this._offerwallMode = offerwallMode;
    return this;
};

/**
 * Sets Pollfish SDK to Developer or Release mode
 * 
 * @param {Boolean} releaseMode 
 * @returns {Builder} itself
 */
Builder.prototype.releaseMode = function(releaseMode) {
    this._releaseMode = releaseMode;
    return this;
};

/**
 * Initializes Pollfish in reward mode
 * 
 * @param {Boolean} rewardMode 
 * @returns {Builder} itself
 */
Builder.prototype.rewardMode = function(rewardMode) {
    this._rewardMode = rewardMode;
    return this;
};

/**
 * Sets a unique id to identify a user and be passed through server-to-server callbacks
 * 
 * @param {String} requestUUID 
 * @returns {Builder} itself
 */
Builder.prototype.requestUUID = function(requestUUID) {
    this._requestUUID = requestUUID;
    return this;
};

/**
 * Provides user attributes upfront during initialization
 * 
 * @param {Object} userProperties 
 * @returns {Builder} itself
 */
Builder.prototype.userProperties = function(userProperties) {
    this._userProperties = userProperties;
    return this;
};

/**
 * A pass throught param that will be passed back through server-to-server callback
 * 
 * @param {String} clickId 
 * @returns {Builder} itself
 */
Builder.prototype.clickId = function(clickId) {
    this._clickId = clickId;
    return this;
};

/**
 * A unique id to identify a user
 * 
 * @param {String} userId 
 * @returns 
 */
Builder.prototype.userId = function(userId) {
    this._userId = userId;
    return this;
}

/**
 * An optional parameter used to secure the rewardConversion and rewardName parameters passed on RewardInfo object
 * 
 * @param {String} signature 
 * @returns {Builder} itself
 */
Builder.prototype.signature = function(signature) {
    this._signature = signature;
    return this;
};

/**
 * An object holding information regarding the survey completion reward
 * 
 * @param {Object} rewardInfo 
 * @returns {Builder} itself
 */
Builder.prototype.rewardInfo = function(rewardInfo) {
    this._rewardInfo = rewardInfo;
    return this;
};

/**
 * Creates the initialization params object passed in pollfishpugin.init function
 * 
 * @returns {Params} object which is used to initialize Pollfish
 */
Builder.prototype.build = function() {
    return new Params(
        this._androidApiKey,
        this._iOSApiKey,
        this._indicatorPosition,
        this._indicatorPadding,
        this._offerwallMode,
        this._releaseMode,
        this._rewardMode,
        this._requestUUID,
        this._userProperties,
        this._clickId,
        this._userId,
        this._rewardInfo,
        this._signature
    );
}

module.exports = {
    Builder,
    Params,
    EventListener,
    Position,
    
	/**
     * Function to initialize Pollfish. Accepts a JSON object built with Builder.build method.
     *
     * @param {Params} params
     */
    init: function(params) {
        cordova.exec(undefined, undefined, "PollfishPlugin", "init", [
            params.androidApiKey,
            params.iOSApiKey,
            params.indicatorPosition,
            params.indicatorPadding,
            params.offerwallMode,
            params.releaseMode,
            params.rewardMode,
            params.requestUUID,
            params.userProperties,
            params.clickId,
            params.userId,
            params.signature,
            params.rewardInfo
        ]);
    },
    
    /**
     * Function to manually show Pollfish
     */
    
    show: function() {
        cordova.exec(undefined, undefined, "PollfishPlugin", "show", []);
    },
    
    /**
     * Function to manually hide Pollfish
     */
    
    hide: function() {
        cordova.exec(undefined, undefined, "PollfishPlugin", "hide", []);
    },

    /**
     * Function to check if Pollfish surveys are available on your device
     * @param {function(Boolean)} callback 
     */

    isPollfishPresent: function(callback) {
        cordova.exec(callback, undefined, "PollfishPlugin", "isPollfishPresent", [callback]);
    },

    /**
     * Function to check if Pollfish panel is open
     * @param {function(Boolean)} callback 
     */
    isPollfishPanelOpen: function(callback) {
        cordova.exec(callback, undefined, "PollfishPlugin", "isPollfishPanelOpen", [callback]);
    },
    
    /**
     * Function to set event callbacks 
     * @param {EventListener} eventName
     * @param {function(JSON)} callback
     */
    
    setEventCallback: function (eventName, callback) {
        cordova.exec(callback, undefined, "PollfishPlugin", "setEventCallback", [eventName]);
    },
    
};
