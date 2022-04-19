var pollfishParams;

var app = {

    initialize: function() {
        this.bindEvents();
    },
	
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

	onDeviceReady: function() {
        document.getElementById('show').addEventListener('click', function() {                                              
			pollfish.show();
		}, false);
        
        // Subscribe to Pollfish events
		
        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyReceived, (result) => {
			document.getElementById('logger').innerHTML = 'Survey received: ' + JSON.stringify(result);
			console.log("Survey Received: " + JSON.stringify(result));
			document.getElementById('show').style.display = 'block';
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyCompleted, (result) => {
			document.getElementById('logger').innerHTML = 'Survey completed: ' + JSON.stringify(result);
			console.log("Survey Completed: " + JSON.stringify(result));
			document.getElementById('show').style.display = 'none';
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishSurveyNotAvailable, (_) => {
			document.getElementById('logger').innerHTML = 'Survey not available';
			console.log("Pollfish Survey not available");
			document.getElementById('show').style.display = 'none';
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserNotEligible, (_) => {
			document.getElementById('logger').innerHTML = 'User not eligible' + JSON.stringify(result);
			console.log("Pollfish User Not Eligible");
			document.getElementById('show').style.display = 'none';
		});

        pollfish.setEventCallback(pollfish.EventListener.OnPollfishUserRejectedSurvey, (_) => {
			document.getElementById('logger').innerHTML = 'User rejected survey';
			console.log("Pollfish User Rejected Survey");
			document.getElementById('show').style.display = 'none';
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
			.rewardMode(true)
			.build();

		// Initialize Pollfish
		pollfish.init(pollfishParams);

		document.getElementById('logger').innerHTML = 'Pollfish initialized';
		console.log("Pollfish initialized");
    },

};

app.initialize();