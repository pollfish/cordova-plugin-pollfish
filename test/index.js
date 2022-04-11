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

		// Create the Params configuration object
		
		var builder = new pollfish.Builder('ANDROID_API_KEY', 'IOS_API_KEY');

		pollfishParams = builder
			.indicatorPosition(pollfish.Position.TOP_LEFT)
			.build();

		// Initialize Pollfish
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