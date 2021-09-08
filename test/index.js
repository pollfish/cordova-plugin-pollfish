/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var pollfishParams;
 
var app = {

    initialize: function() {
        this.bindEvents();
    },
	
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

	onDeviceReady: function() {
       
        app.receivedEvent('deviceready'); 

		document.addEventListener("resume", app.onResume, false);
        		
        document.getElementById('show').addEventListener('click', function() {                                              
			pollfish.show();
		}, false);
        
        document.getElementById('hide').addEventListener('click', function() {               
        	pollfish.hide();                 
		}, false);
  	
        // Subscribe to Pollfish events
		
        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyReceived, (result) => {
			document.getElementById('logger').innerHTML = 'Survey received: ' + JSON.stringify(result);
			console.log("Survey Received: " + JSON.stringify(result));
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyCompleted, (result) => {
			document.getElementById('logger').innerHTML = 'Survey completed: ' + JSON.stringify(result);
			console.log("Survey Completed: " + JSON.stringify(result));
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyNotAvailable, (_) => {
			document.getElementById('logger').innerHTML = 'Survey not available';
			console.log("Pollfish Survey not available");
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserNotEligible, (_) => {
			document.getElementById('logger').innerHTML = 'User not eligible' + JSON.stringify(result);
			console.log("Pollfish User Not Eligible");
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserRejectedSurvey, (_) => {
			document.getElementById('logger').innerHTML = 'User rejected survey';
			console.log("Pollfish User Rejected Survey");
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishOpened, (_) => {
			document.getElementById('logger').innerHTML = 'Panel opened';
			console.log("Pollfish Survey panel is open");
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishClosed, (_) => {
			document.getElementById('logger').innerHTML = 'Panel closed';
			console.log("Pollfish Survey panel is closed");
		});
        
		var releaseMode = false;
		var rewardMode = false;
		var androidApiKey = 'ANDROID_API_KEY';
		var iOSApiKey = 'IOS_API_KEY'
		var position = pollfish.Position.TOP_LEFT;
		var requestUUID = 'REQUEST_UUID';
		var padding = 50; 
		var offerwallMode = true; 
		var userProperties = {
			'gender': '1',
			'education': '1'
		};
		var clickId = "CLICK_ID";
		var signature = "SIG";
		var rewardInfo = {
			rewardName: "Points",
			rewardConversion: 1.3
		};

		// Create the Params configuration object
		
		var builder = new pollfish.Builder(androidApiKey, iOSApiKey);

		params.rewardMode(rewardMode);
		params.offerwallMode(offerwallMode);
		params.indicatorPadding(padding);
		params.indicatorPosition(position);
		params.requestUUID(requestUUID);
		params.releaseMode(releaseMode);
		params.userProperties(userProperties);
		params.rewardInfo(rewardInfo);
		params.clickId(clickId);
		params.signature(signature);

		pollfishParams = builder.build()

		pollfish.init(pollfishParams);
    },
    
	onResume: function () {
		pollfish.init(pollfishParams);
	},
	
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();