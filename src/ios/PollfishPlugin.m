#import "PollfishPlugin.h"
#import <Cordova/CDV.h>

@implementation PollfishPlugin

static NSString* onPollfishSurveyReceived = nil;
static NSString* onPollfishSurveyCompleted = nil;
static NSString* onPollfishSurveyNotAvailable = nil;
static NSString* onPollfishUserNotEligible = nil;
static NSString* onPollfishUserRejectedSurvey = nil;
static NSString* onPollfishOpened = nil;
static NSString* onPollfishClosed = nil;


- (void) init:(CDVInvokedUrlCommand*)command{
    
    NSLog(@"init called");
    
    NSLog(@"[command arguments] count: %d", [command arguments].count);
    
    BOOL releaseMode = [[[command arguments] objectAtIndex:0] boolValue];
    
    NSLog(@"releaseMode: %@",releaseMode ? @"Yes" : @"No");
    
    BOOL rewardMode = [[[command arguments] objectAtIndex:1] boolValue];
    
    NSLog(@"rewardMode: %@",rewardMode ? @"Yes" : @"No");
    
    NSString* appKey = [[command arguments] objectAtIndex:2];
    
    int pos = [[[command arguments] objectAtIndex:3] integerValue];
    int padding = [[[command arguments] objectAtIndex:4] integerValue];
    
    NSLog(@"appKey: %@",appKey);
    NSLog(@"pos: %d",pos);
    NSLog(@"padding: %d",padding);
    
    NSString* requestUUID = nil;
    
    if([command arguments].count>=5)
    {
        requestUUID=[[command arguments] objectAtIndex:5];
    }
    
    NSLog(@"requestUUID: %@",requestUUID);
    
	BOOL offerwallMode = false;
    
    
    if([command arguments].count>=6)
    {
        offerwallMode=[[command arguments] objectAtIndex:6];
    }
    
    NSLog(@"offerwallMode: %@",offerwallMode ? @"Yes" : @"No");
    
    
    NSMutableDictionary *userAttributesDict;
    
    if([command arguments].count>=7)
    {
        userAttributesDict=[[command arguments] objectAtIndex:7];
    }
    
    
    PollfishParams *pollfishParams =  [PollfishParams initWith:^(PollfishParams *pollfishParams) {
        
        pollfishParams.indicatorPosition=pos;
        pollfishParams.indicatorPadding=padding;
        pollfishParams.releaseMode= releaseMode;
        pollfishParams.offerwallMode= offerwallMode;
        pollfishParams.rewardMode=rewardMode;
        pollfishParams.requestUUID=requestUUID;
        pollfishParams.userAttributes=userAttributesDict;
    }];
    
    [Pollfish initWithAPIKey:appKey andParams:pollfishParams];
}

- (void) show:(CDVInvokedUrlCommand*)command
{
    NSLog(@"show manually called");
    
    [Pollfish show];
}

- (void) hide:(CDVInvokedUrlCommand*)command{
    
    NSLog(@"hide manually called");
    
    [Pollfish hide];
}

- (void)surveyOpened:(NSNotification *)notification
{
    NSLog(@"surveyOpened notification");
    
    if(onPollfishOpened != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishOpened];
        
    }else{
        
        NSLog(@"surveyOpened notification not set");
    }
}

- (void)surveyClosed:(NSNotification *)notification
{
    NSLog(@"surveyClosed notification");
    
    if(onPollfishClosed != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishClosed];
        
    }else{
        
        NSLog(@"surveyClosed notification not set");
    }
}

- (void)surveyReceived:(NSNotification *)notification
{
    NSLog(@"surveyReceived notification");
    
    if(onPollfishSurveyReceived != nil) {
        
        int surveyPrice = [[[notification userInfo] valueForKey:@"survey_cpa"] intValue];
    	int surveyIR = [[[notification userInfo] valueForKey:@"survey_ir"] intValue];
    	int surveyLOI = [[[notification userInfo] valueForKey:@"survey_loi"] intValue];
    
    	NSString *surveyClass =[[notification userInfo] valueForKey:@"survey_class"];
  
        NSString *rewardName = [[notification userInfo] valueForKey:@"reward_name"];
    
      	int rewardValue = [[[notification userInfo] valueForKey:@"reward_value"] intValue];
    
   		NSLog(@"Pollfish: Survey Received - SurveyPrice:%d andSurveyIR: %d andSurveyLOI:%d andSurveyClass:%@ andRewardName:%@ andRewardValue:%d", surveyPrice,surveyIR, surveyLOI, surveyClass, rewardName, rewardValue);
        
        NSArray *keys = [NSArray arrayWithObjects:@"survey_cpa", @"survey_ir", @"survey_loi", @"survey_class",@"reward_name", @"reward_value", nil];
        NSArray *objects = [NSArray arrayWithObjects:[NSString stringWithFormat: @"%d",surveyPrice],[NSString stringWithFormat: @"%d",surveyIR],[NSString stringWithFormat: @"%d",surveyLOI],[NSString stringWithFormat: @"%@",surveyClass],[NSString stringWithFormat: @"%@",rewardName],[NSString stringWithFormat: @"%d",rewardValue], nil];
       
        NSDictionary *dictionary = [NSDictionary dictionaryWithObjects:objects
                                                               forKeys:keys];
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyReceived];
        
    }else{
        
        NSLog(@"surveyReceived notification not set");
    }
    
}

- (void)surveyCompleted:(NSNotification *)notification
{
    NSLog(@"surveyCompleted notification");
    
    if(onPollfishSurveyCompleted != nil) {
        
       
        int surveyPrice = [[[notification userInfo] valueForKey:@"survey_cpa"] intValue];
    	int surveyIR = [[[notification userInfo] valueForKey:@"survey_ir"] intValue];
    	int surveyLOI = [[[notification userInfo] valueForKey:@"survey_loi"] intValue];
    
    	NSString *surveyClass =[[notification userInfo] valueForKey:@"survey_class"];
  		NSString *rewardName = [[notification userInfo] valueForKey:@"reward_name"];
    
      	int rewardValue = [[[notification userInfo] valueForKey:@"reward_value"] intValue];
    
   		
        
   		NSLog(@"Pollfish: Survey Completed - SurveyPrice:%d andSurveyIR: %d andSurveyLOI:%d andSurveyClass:%@ andRewardName:%@ andRewardValue:%d", surveyPrice,surveyIR, surveyLOI, surveyClass, rewardName, rewardValue);
        
        NSArray *keys = [NSArray arrayWithObjects:@"survey_cpa", @"survey_ir", @"survey_loi", @"survey_class",@"reward_name", @"reward_value", nil];
        NSArray *objects = [NSArray arrayWithObjects:[NSString stringWithFormat: @"%d",surveyPrice],[NSString stringWithFormat: @"%d",surveyIR],[NSString stringWithFormat: @"%d",surveyLOI],[NSString stringWithFormat: @"%@",surveyClass],[NSString stringWithFormat: @"%@",rewardName],[NSString stringWithFormat: @"%d",rewardValue], nil];
       
        NSDictionary *dictionary = [NSDictionary dictionaryWithObjects:objects
                                                               forKeys:keys];
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
        
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyCompleted];
        
    }else{
        
        NSLog(@"onPollfishSurveyCompleted notification not set");
    }
}

- (void)userNotEligible:(NSNotification *)notification
{
    NSLog(@"userNotEligible notification");
    
    if(onPollfishUserNotEligible != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserNotEligible];
    }else{
        
        NSLog(@"userNotEligible notification not set");
    }
}

- (void)userRejectedSurvey:(NSNotification *)notification
{
    NSLog(@"userRejectedSurvey notification");
    
    if(onPollfishUserRejectedSurvey != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserRejectedSurvey];
    }else{
        
        NSLog(@"userRejectedSurvey notification not set");
    }
}

- (void)surveyNotAvailable:(NSNotification *)notification
{
    NSLog(@"surveyNotAvailable notification");
    
    if(onPollfishSurveyNotAvailable != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyNotAvailable];
        
    }else{
        
        NSLog(@"onPollfishSurveyNotAvailable notification not set");
    }
}


- (void) setEventCallback:(CDVInvokedUrlCommand*)command{
    
    NSLog(@"setEventCallbackWithName");
    
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* eventName =[NSString stringWithFormat: @"%@", name];
    
    NSLog(@"eventName: %@",eventName);
    
    if([@"onPollfishSurveyReceived" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyReceived set");
        
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(surveyReceived:)
                                                     name:@"PollfishSurveyReceived" object:nil];
        
        onPollfishSurveyReceived = command.callbackId;
        
    }else if ([@"onPollfishSurveyNotAvailable" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyNotAvailable set");
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(surveyNotAvailable:)
                                                     name:@"PollfishSurveyNotAvailable" object:nil];
        
        onPollfishSurveyNotAvailable = command.callbackId;
        
    }else if ([@"onPollfishSurveyCompleted" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyCompleted set");
        
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(surveyCompleted:)
                                                     name:@"PollfishSurveyCompleted" object:nil];
        
        onPollfishSurveyCompleted = command.callbackId;
        
    }else if ([@"onPollfishUserNotEligible" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishUs1erNotEligible set");
        
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userNotEligible:)
                                                     name:@"PollfishUserNotEligible" object:nil];
        
        onPollfishUserNotEligible = command.callbackId;
        
    }else if ([@"onPollfishUserRejectedSurvey" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishUserRejectedSurvey set");
        
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userRejectedSurvey:)
                                                     name:@"PollfishUserRejectedSurvey" object:nil];
        
        onPollfishUserRejectedSurvey = command.callbackId;
        
    }else if ([@"onPollfishOpened" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishOpened set");
        
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(surveyOpened:)
                                                     name:@"PollfishOpened" object:nil];
        
        onPollfishOpened = command.callbackId;
        
    }else if ([@"onPollfishClosed" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishClosed set");
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(surveyClosed:)
                                                     name:@"PollfishClosed" object:nil];
        
        onPollfishClosed = command.callbackId;
    }
}


@end