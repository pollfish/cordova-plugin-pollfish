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

var releaseMode;
var rewardMode;
var apiKey;
var position;
var requestUUID;
var padding; 
var offerwallMode; 
var userAttributes;
 
var app = {

    initialize: function() {
        this.bindEvents();
    },
	
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

	onDeviceReady: function() {
       
        app.receivedEvent('deviceready'); 
  	
        // register to listen to Pollfish events
		
        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishSurveyReceived, (result) => {
			document.getElementById('logger').innerHTML = 'Survey received: ' + JSON.stringify(result);
			console.log("Survey Received: " + JSON.stringify(result));
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishSurveyCompleted, (result) => {
			document.getElementById('logger').innerHTML = 'Survey completed: ' + JSON.stringify(result);
			console.log("Survey Completed: " + JSON.stringify(result));
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishSurveyNotAvailable, (_) => {
			document.getElementById('logger').innerHTML = 'Survey not available';
			console.log("Pollfish Survey not available");
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishUserNotEligible, (_) => {
			document.getElementById('logger').innerHTML = 'User not eligible' + JSON.stringify(result);
			console.log("Pollfish User Not Eligible");
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishUserRejectedSurvey, (_) => {
			document.getElementById('logger').innerHTML = 'User rejected survey';
			console.log("Pollfish User Rejected Survey");
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishOpened, (_) => {
			document.getElementById('logger').innerHTML = 'Panel opened';
			console.log("Pollfish Survey panel is open");
		});

        pollfishplugin.setEventCallback(pollfishplugin.EventListener.OnPollfishClosed, (_) => {
			document.getElementById('logger').innerHTML = 'Panel closed';
			console.log("Pollfish Survey panel is closed");
		});
    
        // register to listen when app comes to foreground
        
        document.addEventListener("resume", app.onResume, false);
        		
        document.getElementById('show').addEventListener('click', function() {                                              
			pollfishplugin.showPollfish();
		}, false);
        
        document.getElementById('hide').addEventListener('click', function() {               
        	pollfishplugin.hidePollfish();                                        
		}, false);

		releaseMode = false;
		rewardMode = false;
		apiKey = 'YOUR_API_KEY';
		position = pollfishplugin.Position.TOP_LEFT;
		requestUUID = 'my_id';
		padding = 50; 
		offerwallMode = true; 
		userAttributes = {
			'gender': '1',
			'education': '1'
		};
		
		pollfishplugin.initWithUserAttributes(
			releaseMode, 
			rewardMode,
			apiKey, 
			position, 
			padding, 
			requestUUID, 
			offerwallMode, 
			userAttributes);
    },
    
	onResume: function () {
   		console.log("PollfishPlugin onResume");
    
		pollfishplugin.initWithUserAttributes(
			releaseMode, 
			rewardMode,
			apiKey, 
			position, 
			padding, 
			requestUUID, 
			offerwallMode, 
			userAttributes);
	},
	
    // Update DOM on a Received Event
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