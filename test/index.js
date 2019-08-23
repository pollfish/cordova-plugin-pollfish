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
var api_key;
var pos;
var request_uuid;
var padding; 
var offerwallMode; 
var userAttributes={};
 
 
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
       
        app.receivedEvent('deviceready'); 
  		
  		// initialise Pollfish
 		     
		releaseMode = false;
		rewardMode = true;
		api_key = "298f3f43-1a78-4da4-ab55-24c21c1c0ea8";
		pos=pollfishplugin.Position.TOP_LEFT;
		padding = 50; 
		offerwallMode = true;
		request_uuid="my_id";
		
        // register to listen to Pollfish events
        
        pollfishplugin.setEventCallback('onPollfishSurveyReceived',app.surveyReceivedEvent);
        pollfishplugin.setEventCallback('onPollfishSurveyCompleted',app.surveyCompletedEvent);
        pollfishplugin.setEventCallback('onPollfishSurveyNotAvailable',app.surveyNotAvailableEvent);
        pollfishplugin.setEventCallback('onPollfishUserNotEligible',app.userNotEligibleEvent);
        pollfishplugin.setEventCallback('onPollfishUserRejectedSurvey',app.userRejectedSurveyEvent);
        
        pollfishplugin.setEventCallback('onPollfishOpened',app.pollfishPanelOpenEvent);
        pollfishplugin.setEventCallback('onPollfishClosed',app.pollfishPanelClosedEvent);
    
        
         // register to listen when app comes to foreground
        
        document.addEventListener("resume", app.onResume, false);
        
        // register to listen when app changes orientation
 
        document.addEventListener("orientationchange", app.updateOrientation,false);

 		// sent user attributes 
 		
        
        userAttributes['FacebookID'] = 'My Facebook';
        userAttributes['LinkedInID'] = 'My LinkedIn';
        
               
        pollfishplugin.initWithUserAttributes(releaseMode,rewardMode,api_key,pos,padding,request_uuid,offerwallMode,userAttributes); 
 
        
		// manually call show or hide Pollfish
   
        document.getElementById('show').addEventListener('click', function() {
                                                        
                                                         pollfishplugin.showPollfish();
                                                        
                                                         }, false);
        
        document.getElementById('hide').addEventListener('click', function() {
                                                         
                                                         pollfishplugin.hidePollfish();
                                                    
                                                         }, false);
  
    },
    
	updateOrientation: function () {
    
    	console.log("PollfishPlugin updateOrientation");
    
 		pollfishplugin.init(releaseMode,rewardMode,api_key,pos,padding,request_uuid, offerwallMode); 
	},
	
	onResume: function () {
   
   		console.log("PollfishPlugin onResume");
    
 		pollfishplugin.init(releaseMode,rewardMode,api_key,pos,padding,offerwallMode); 
	},
    
    surveyReceivedEvent: function(id) {
    
    	try{   
    	
        	console.log("Pollfish Survey Received - CPA: " + id.survey_cpa + " IR: " + id.survey_ir + " LOI: " + id.survey_loi + " Survey Class: " + id.survey_class+ " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value);
        
       	 	document.getElementById('logger').innerHTML = "Survey Received - CPA: " + id.survey_cpa + " IR: " + id.survey_ir+ " LOI: " + id.survey_loi+ " Survey Class: " + id.survey_class + " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value;
        
    	}catch(e){
        
    	}
    
	},
    
	surveyCompletedEvent: function(id) {
    
    	try{
         	console.log("Pollfish Survey Completed - CPA: " + id.survey_cpa + " IR: " + id.survey_ir + " LOI: " + id.survey_loi + " Survey Class: " + id.survey_class + " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value);
        
        	document.getElementById('logger').innerHTML = "Survey Completed - CPA: " + id.survey_cpa + " IR: " + id.survey_ir+ " LOI: " + id.survey_loi+ " Survey Class: " + id.survey_class + " Reward Name: " + id.reward_name + " Reward Value: " + id.reward_value;
        
    	}catch(e){
        
    	}
	},
    
	surveyNotAvailableEvent: function(id) {
   
   		console.log("Pollfish Survey Not Available");
    
    	document.getElementById('logger').innerHTML = "Survey Not Available";  
	},
    
	pollfishPanelOpenEvent: function(id) {
    	
    	console.log("Pollfish Panel Open");
    	
    	document.getElementById('logger').innerHTML = "Panel Open";
	},
    
	userNotEligibleEvent: function(id) {
    
    	console.log("Pollfish User Not Eligible");
    
    	document.getElementById('logger').innerHTML = "User Not Eligible"; 
	},
    
	userRejectedSurveyEvent: function(id) {
    
    	console.log("Pollfish User Rejected Survey");
    
    	document.getElementById('logger').innerHTML = "User Rejected Survey"; 
	},
    
	pollfishPanelClosedEvent: function(id) {
    
    	console.log("Pollfish Panel Closed");
    
    	document.getElementById('logger').innerHTML = "Panel Closed"; 
    
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