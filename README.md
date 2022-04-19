# Pollfish Cordova Plugin 

Pollfish Cordova Plugin allows integration of Pollfish surveys into Android and iOS apps. 

Pollfish is a mobile monetization platform delivering surveys instead of ads through mobile apps. Developers get paid per completed surveys through their apps.

Pollfish Cordova Plugin can be found on [npm Registry](https://www.npmjs.com/package/cordova-plugin-pollfish).

<br/>

# Prerequisites

* Android SDK 21 or higher using Google Play Services
* iOS 9.0 or higher
* Apache Cordova v3.0.0 or higher
* CocoaPods v1.10.0 or higher

<br/>

# Quick Guide

* Create Pollfish Developer account, create a new app and grap it's API key
* Install Pollfish plugin and call init function
* Set to Release mode and release in AppStore and Google Play
* Update your app's privacy policy
* Request your account to get verified from the Pollfish Dashboard

<br/>

> **Note:** Apps designed for [Children and Families program](https://play.google.com/about/families/ads-monetization/) should not be using Pollfish SDK, since Pollfish does not collect responses from users less than 16 years old   

> **Note:** Pollfish surveys can work with or without the IDFA permission on iOS 14+. If no permission is granted in the ATT popup, the SDK will serve non personalized surveys to the user. In that scenario the conversion is expected to be lower. Offerwall integrations perform better compared to single survey integrations when no IDFA permission is given

<br/>

# Installation

To add Pollfish plugin just type:

```bash
cordova plugin add cordova-plugin-pollfish
```

To remove Pollfish plugin type:

```bash
cordova plugin remove cordova-plugin-pollfish
```

*In iOS you may need to add manually pollfish.framework to your XCode project. In Xcode, select the target that you want to use and in the Build Phases tab expand the Link Binary With Libraries section. Press the + button, and press Add other… In the dialog box that appears, go to the Pollfish framework’s location (located in /src/ios/framework) and select it. The project will appear at the top of the Link Binary With Libraries section and will also be added to your project files (left-hand pane). The framework is a folder and you should add the whole folder into your project.*

<br/>

# Initialization

## 1. Create `pollfish.Builder` instance

The Pollfish plugin must be initialized with one or two api keys depending on which platforms are you targeting. You can retrieve an API key from Pollfish Dashboard when you [sign up](https://www.pollfish.com/signup/publisher) and create a new app.


```js
var builder = new pollfish.Builder('ANDROID_API_KEY', 'IOS_API_KEY')
	.rewardMode(true); // Android & iOS
```

```js
var builder = new pollfish.Builder('ANDROID_API_KEY', null)
	.rewardMode(true); // Android only
```

```js
var builder = new pollfish.Builder(null, 'IOS_API_KEY')
	.rewardMode(true); // iOS only
```

### 1.1 Configure Pollfish behaviour (Optional)

You can set several params to control the behaviour of Pollfish survey panel within your app with the use of the `pollfish.Builder` instance. Below you can see all the available options. Apart from the constructor all the other methods are optional.

Param               | Description
--------------------|:---------
**`constructor(String, String)`**           		| Sets Your Android and iOS API Keys (from step 2)
**`.indicatorPosition(pollfish.Position)`**	| Sets the Position where you wish to place the Pollfish indicator. There are six different options RNPollfish.Position.{topLeft, topRight, middleLeft, middleRight, bottomLeft, bottom Right}: 
**`.indicatorPadding(Int)`**                		| Sets the padding from top or bottom depending on the position of the indicator specified before (if used in middle position, padding is calculated from the top).
**`.offerwallMode(Boolean)`**	                	| Sets Pollfish to offerwall mode
**`.releaseMode(Boolean)`**      	            	| Choose Debug or Release Mode
**`.rewardMode(Boolean)`**           		        | Init in reward mode (skip Pollfish indicator to show a custom prompt)
**`.requestUUID(String)`**               		    | Sets a unique id to identify a user and be passed through server-to-server callbacks
**`.userProperties(Json)`**                  		| Send attributes that you receive from your app regarding a user, in order to receive a better fill rate and higher priced surveys. You can see a detailed list of the user attributes you can pass with their keys at the following [link](https://www.pollfish.com/docs/demographic-surveys)
**`.rewardInfo(Json)`**                     	 	| An object holding information regarding the survey completion reward. If set, `signature` must be calculated in order to receive surveys. See [here](https://www.pollfish.com/docs/api-documentation) in section **`Notes for sig query parameter`**
**`.clickId`**         		                      	| A pass throught param that will be passed back through server-to-server callback
**`.signature`**            	                 	| An optional parameter used to secure the `rewardConversion` and `rewardName` parameters passed on `rewardInfo` `Json` object. 

<br/>

Example of Pollfish configuration using the available options


```js
var builder = builder
	.offerwallMode(false)
	.releaseMode(false)
	.indicatorPadding(50)
	.indicatorPosition(pollfish.Position.BOTTOM_RIGHT)
	.requestUUID('REQUEST_UUID')
	.userProperties({
		gender: '1',
		education: '1',
		...
	})
	.clickId('CLICK_ID')
	.signature('SIGNATURE')
	.rewardInfo({
		rewardName: 'Points',
		rewardConversion: 1.3
	})
	.build();

pollfish.init(params); 
```

<br/>

> ### Debug vs Release Mode
>
> You can use Pollfish either in Debug or in Release mode. 
>  
> * **Debug mode** is used to show to the developer how Pollfish will be shown through an app (useful during development and testing).
> * **Release mode** is the mode to be used for a released app (start receiving paid surveys).
> 
> **Note:** Be careful to set the `releaseMode` parameter to `true` when you release your app in a relevant app store!!

<br/>

> ### Reward Mode 
> 
> Setting the `rewardMode` to `false` during initialization enables controlling the behavior of Pollfish in an app from the Pollfish panel. Enabling reward mode ignores Pollfish behavior from Pollfish panel. It always skips showing Pollfish indicator (small button) and always force open Pollfish view to app users. This method is usually used when app developers want to somehow incentivize their users before completing surveys to increase completion rates.

<br/>

## 3. Initialize Pollfish

Last but not least. Build the `Params` object by invoking `.build()` on the `pollfish.Builder` instance that you've configured earlier and call `RNPollfish.init(params)` passing the `Params` object as an argument.

```js
var params = builder.build();
pollfish.init(params);

// OR

pollfish.init(builder.build());
```

<br/>

# Optional section

In this section we will list several options that can be used to control Pollfish surveys behaviour, how to listen to several notifications or how be eligible to more targeted (high-paid) surveys. All these steps are optional.

<br/>

## Get notified when a Pollfish survey is received

You can get notified when a Pollfish survey is received. With this notification, you can also get informed about the type of survey that was received, money to be earned if survey gets completed, shown in USD cents and other info around the survey such as LOI and IR.

<br/>

> **Note:** If Pollfish is initialized in offerwall mode then the event parameter will be `undefined`, otherwise it will include info around the received survey.

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyReceived, (result) => {
    console.log("Survey Received: " + JSON.stringify(result));
});
```

<br/>

## Get notified when a Pollfish survey is completed

You can get notified when a user completed a survey. With this notification, you can also get informed about the type of survey, money earned from that survey in USD cents and other info around the survey such as LOI and IR.

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyCompleted, (result) => {
    console.log("Survey Completed: " + JSON.stringify(result));
});
```

<br/>

## Get notified when a user is not eligible for a Pollfish survey

You can get notified when a user is not eligible for a Pollfish survey. In market research monetization, users can get screened out while completing a survey beucase they are not relevant with the audience that the market researcher was looking for. In that case the user not eligible notification will fire and the publisher will make no money from that survey. The user not eligible notification will fire after the surveyReceived event, when the user starts completing the survey.

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserNotEligible, (_) => {
    console.log("Pollfish User Not Eligible");
});
```

<br/>

## Get notified when a Pollfish survey is not available

You can get notified when a Pollfish survey is not available

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyNotAvailable, (_) => {
    console.log("Pollfish Survey not available");
});
```

<br/>

## Get notified when a Pollfish survey panel has opened

You can register and get notified when a Pollfish survey panel has opened. Publishers usually use this notification to pause a game until the pollfish panel is closed again.


```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishOpened, (_) => {
    console.log("Pollfish Survey panel is open");
});
```

<br/>

## Get notified when a Pollfish survey panel has closed

You can register and get notified when a Pollfish survey panel has closed. Publishers usually use this notification to resume a game that they have previously paused when the Pollfish panel was opened.

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishClosed, (_) => {
    console.log("Pollfish Survey panel is closed");
});
```

<br/>

## Get notified when a use has rejected a survey

You can be notified when use has rejected a Pollfish survey

```js
pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserRejectedSurvey, (_) => {
    console.log("Pollfish User Rejected Survey");
});
```

<br/>

## Manually show/hide Pollfish panel 

You can manually hide and show Pollfish by calling anywhere after initialization: 

For example:

```js
pollfish.show();
```

or

```js
pollfish.hide();
```

<br/>

## Check if Pollfish surveys are available on your device

After you initialize Pollfish and a survey is received you can check at any time if the survey is available at the device by calling the following function.

```js
pollfish.isPollfishPresent((result) => {
	console.log(result)
});
```

<br/>

## Check if Pollfish panel is open

You can check at any time if the survey panel is open by calling the following function.

```js
pollfish.isPollfishPanelOpen((result) => {
	console.log(result)
});
```

<br/>

# More info

You can read more info on how the Native Pollfish SDKs work on Android and iOS or how to set up properly a Cordova environment at the following links:

<br/>

[Pollfish Corvova Plugin Documentation](https://pollfish.com/docs/cordova)

[Pollfish Android SDK Integration Guide](https://pollfish.com/docs/android)

[Pollfish iOS SDK Integration Guide](https://pollfish.com/docs/ios)

[Cordova Starting Guide](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)
