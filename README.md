# Pollfish Cordova Plugin 

Pollfish Cordova Plugin allows integration of Pollfish surveys into Android and iOS apps. 

Pollfish is a mobile monetization platform delivering surveys instead of ads through mobile apps. Developers get paid per completed surveys through their apps.

## Prerequisites

*	Android 21+ using Google Play Services
*	iOS version 9.0+
*	Apache Cordova v3.0.4+

> **Note:** Pollfish iOS SDK utilizes Apple's Advertising ID (IDFA) to identify and retarget users with Pollfish surveys. As of iOS 14 you should initialize Pollfish Cordova/Ionic plugin in iOS only if the relevant IDFA permission was granted by the user

## Quick Guide

* Create Pollfish Puvlisher account, create new app and grap it's API key
* Install Pollfish plugin and call init function
* Update your app's privacy policy
* Set to Release mode and publish in Store
* Request your account to get verified


<br/>

![alt tag](https://www.pollfish.com/homeassets/images/rocketMobile.png)

## Steps Analytically

### 1. Obtain a Publisher Account

Register as a Publisher at [www.pollfish.com](http://www.pollfish.com)

### 2. Add new app on Pollfish panel and copy the given API Key

Login at [www.pollfish.com](http://www.pollfish.com) and add a new app at Pollfish panel in section My Apps and copy the given API key for this app to use later in your init function in your app.

### 3. Installing the plugin

To add Pollfish plugin just type:

```cordova plugin add https://github.com/pollfish/cordova-plugin-pollfish.git```

To remove Pollfish plugin type:

```cordova plugin remove com.pollfish.cordova```





*In iOS you may need to add manually pollfish.framework to your XCode project. In Xcode, select the target that you want to use and in the Build Phases tab expand the Link Binary With Libraries section. Press the + button, and press Add other… In the dialog box that appears, go to the Pollfish framework’s location (located in /src/ios/framework) and select it. The project will appear at the top of the Link Binary With Libraries section and will also be added to your project files (left-hand pane). The framework is a folder and you should add the whole folder into your project.*




### 4. Initialize Pollfish

Init function takes the following parameters:

1.	**releaseMode**: - Choose Debug or Release Mode
2.	**rewardMode**: - Init in reward mode (skip Pollfish indicator to show a custom prompt)
3.	**api_key**: - Your API Key (from step 2)
4.	**pos**: - The Position where you wish to place the Pollfish indicator. There are four different options {Position.TOP_LEFT, Position.BOTTOM_LEFT, Position.MIDDLE_LEFT, Position.TOP_RIGHT, Position.BOTTOM_RIGHT, Position.MIDDLE_RIGHT}
5.	**padding**: - The padding (in dp) from top or bottom according to Position of the indicator specified before (0 is the default value – |*if used in MIDDLE position, padding is calculating from top).
6.	**request_uuid**: - Sets a unique id to identify a user and be passed through server-to-server callbacks
7.	**offerwallMode**: - Sets Pollfish to offerwall mode.

For example:

```
var releaseMode = false;
var rewardMode = false;
var api_key = "YOUR_API_KEY";
var pos = pollfishplugin.Position.TOP_LEFT;
var padding = 50;
var request_uuid = "my_id";
var offerwallMode = true; 

pollfishplugin.init(releaseMode,rewardMode,api_key,pos,padding,request_uuid, offerwallMode); 
```

#### Debug Vs Release Mode

You can use Pollfish either in Debug or in Release mode. 
  
* **Debug mode** is used to show to the developer how Pollfish will be shown through an app (useful during development and testing).
* **Release mode** is the mode to be used for a released app (start receiving paid surveys).


**Note: In Android debugMode parameter is ignored. Your app turns into debug mode once it is signed with a debug key. If you sign your app with a release key it automatically turns into Release mode.**

**Note: Be careful to turn the releaseMode parameter to true when you release your app in a relevant app store!!**



#### Reward Mode 

Reward mode false during initialization enables controlling the behavior of Pollfish in an app from Pollfish panel. Enabling reward mode ignores Pollfish behavior from Pollfish panel. It always skips showing Pollfish indicator (small button) and always force open Pollfish view to app users. This method is usually used when app developers want to incentivize first somehow their users before completing surveys to increase completion rates.

#### 4.1 Other Init functions (optional)

##### Passing user attributes during initialization

You can send attributes that you receive from your app regarding a user, in order to receive a better fill rate and higher priced surveys. 


You can see a detailed list of the user attribues you can pass with their keys at the following [link](https://www.pollfish.com/docs/demographic-surveys)

For example:

```
var userAttributes = {};

userAttributes['FacebookID'] = 'My Facebook';
userAttributes['LinkedInID'] = 'My LinkedIn';

pollfishplugin.initWithUserAttributes(releaseMode,rewardMode,api_key,pos,padding,request_uuid,offerwallMode,userAttributes); 
 
```

### 5. Update your Privacy Policy

#### Add the following paragraph to your app's privacy policy


*Survey Serving Technology*

*This app uses Pollfish SDK. Pollfish is an on-line survey platform, through which, anyone may conduct surveys. Pollfish collaborates with Developers of applications for smartphones in order to have access to users of such applications and address survey questionnaires to them. When a user connects to this app, a specific set of user’s device data (including Advertising ID which will may be processed by Pollfish only in strict compliance with google play policies- and/or other device data) and response meta-data (including information about the apps which the user has installed in his mobile phone)  is automatically sent to Pollfish servers, in order for Pollfish to discern whether the user is eligible for a survey. For a full list of data received by Pollfish through this app, please read carefully Pollfish respondent terms located at https://www.pollfish.com/terms/respondent. These data will be associated with your answers to the questionnaires whenever Pollfish sents such questionnaires to eligible users. By downloading the application you accept this privacy policy document and you hereby give your consent for the processing by Pollfish of the aforementioned data. Furthermore, you are informed that you may disable Pollfish operation at any time by using the Pollfish “opt out section” available on Pollfish website . We once more invite you to check the respondent’s terms of use, if you wish to have more detailed view of the way Pollfish works.*


*APPLE, GOOGLE AND AMAZON ARE NOT A SPONSOR NOR ARE INVOLVED IN ANY WAY IN THE DRAWS. NO APPLE PRODUCTS ARE BEING USED AS PRIZES.*


### 6. Handling orientation changes (optional)

If your app supports both orientations you should listen for the orientation event and initialise Pollfish again.

For example:

```
document.addEventListener("orientationchange", app.updateOrientation,false);
```

```
updateOrientation: function () {
	pollfishplugin.init(releaseMode,rewardMode,api_key,pos,padding,request_uuid, offerwallMode); 
}
```

### 7. Handling application entering to foreground (optional)

You should handle the event when your app is entering to foreground in order to initialise Pollfish again by listening to the relevant event.

For example:

```
document.addEventListener("resume", app.onResume, false);
```

```
onResume: function () {
 	pollfishplugin.init(releaseMode,rewardMode,api_key,pos,padding,request_uuid, offerwallMode); 
}
```


### 8. Implement Pollfish event listeners

#### 8.1 Get notified when a Pollfish survey is received (optional)

You can be notified when a Pollfish survey is received.

For example:

```
pollfishplugin.setEventCallback('onPollfishSurveyReceived',app.surveyReceivedEvent);
```

```
surveyReceivedEvent: function(id) {

try{
	console.log("Pollfish Survey Received - CPA: " + id.survey_cpa + " IR: " + id.survey_ir + " LOI: " + id.survey_loi + " Survey Class: " + id.survey_class+ " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value);
                
 }catch(e){}
}
```

#### 8.2 Get notified when a Pollfish survey is completed (optional)

You can be notified when a Pollfish survey is completed.

For example:

```
pollfishplugin.setEventCallback('onPollfishSurveyCompleted',app.surveyCompletedEvent);
```

```
surveyCompletedEvent: function(id) {

try{
	console.log("Pollfish Survey Completed - CPA: " + id.survey_cpa + " IR: " + id.survey_ir + " LOI: " + id.survey_loi + " Survey Class: " + id.survey_class + " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value);
        
 }catch(e){}
}
```

#### 8.3 Get notified when a user is not eligible for a Pollfish survey (optional)

You can be notified when a user is not eligible for a Pollfish survey.

For example:

```
pollfishplugin.setEventCallback('onPollfishUserNotEligible',app.userNotEligibleEvent);
```

```
userNotEligibleEvent: function(id) {
 console.log("Pollfish User Not Eligible");
}
```

#### 8.4 Get notified when a Pollfish survey is not available (optional)

You can be notified when a Pollfish survey is not available

For example:

```
pollfishplugin.setEventCallback('onPollfishSurveyNotAvailable',app.surveyNotAvailableEvent);
```

```
surveyNotAvailableEvent: function(id) {
 console.log("Pollfish Survey not available");
}
```

#### 8.5 Get notified when a Pollfish survey panel is open (optional)

You can be notified when Pollfish survey panel is open

For example:

```
pollfishplugin.setEventCallback('onPollfishOpened',app.pollfishPanelOpenEvent);
```

```
pollfishPanelOpenEvent: function(id) {
 console.log("Pollfish Survey panel is open");
}
```

#### 8.6 Get notified when a Pollfish survey panel is closed (optional)

You can be notified when Pollfish survey panel is closed

For example:

```
pollfishplugin.setEventCallback('onPollfishOpened',app.pollfishPanelClosedEvent);
```

```
pollfishPanelClosedEvent: function(id) {
 console.log("Pollfish Survey panel is closed");
}
```

### 9. Other actions (optional)

#### 9.1 Manually show Pollfish panel

You can manually hide and show Pollfish from your various UIVIewControllers. by calling anywhere after initialization: 

For example:

```
pollfishplugin.showPollfish();
```

or

```
pollfishplugin.hidePollfish();
```



## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/4.0.0/guide_cli_index.md.html#The%20Command-Line%20Interface)

For more info about Pollfish please check [Pollfish Website](http://www.pollfish.com/publisher)
