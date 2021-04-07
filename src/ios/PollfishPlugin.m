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
    
    NSLog(@"releaseMode: %@", releaseMode ? @"Yes" : @"No");
    
    BOOL rewardMode = [[[command arguments] objectAtIndex:1] boolValue];
    
    NSLog(@"rewardMode: %@", rewardMode ? @"Yes" : @"No");
    
    NSString* appKey = [[command arguments] objectAtIndex:2];

    NSLog(@"appKey: %@", appKey);
    
    int pos = [[[command arguments] objectAtIndex:3] integerValue];

    NSLog(@"pos: %d", pos);

    int padding = [[[command arguments] objectAtIndex:4] integerValue];
    
    NSLog(@"padding: %d", padding);
    
    NSString* requestUUID = nil;
    
    if([command arguments].count >= 6)
    {
        requestUUID=[[command arguments] objectAtIndex:5];
    }
    
    NSLog(@"requestUUID: %@", requestUUID);
    
	BOOL offerwallMode = false;
    
    if([command arguments].count >= 7)
    {
        offerwallMode = [[[command arguments] objectAtIndex:6] boolValue];
    }
    
    NSLog(@"offerwallMode: %d", offerwallMode);
    
    NSMutableDictionary *userAttributesDict;
    
    if([command arguments].count>=8)
    {
        userAttributesDict=[[command arguments] objectAtIndex:7];
    }

    PollfishParams * pollfishParams = [[PollfishParams alloc] init:appKey];
    [pollfishParams indicatorPadding:padding];
    [pollfishParams indicatorPosition:pos];   
    [pollfishParams releaseMode:releaseMode];
    [pollfishParams offerwallMode:offerwallMode];
    [pollfishParams rewardMode:rewardMode];
    [pollfishParams requestUUID:requestUUID];
    [pollfishParams platform:PlatformCordova];

    UserProperties *userProperties = [[UserProperties alloc] init];
    
    [userAttributesDict enumerateKeysAndObjectsUsingBlock:^(id key, id object, BOOL *stop) {
        [userProperties customAttribute:object forKey:key];
    }];

    [pollfishParams userProperties:userProperties];
    
    [Pollfish initWith:pollfishParams delegate:self];
}

- (void) show:(CDVInvokedUrlCommand*)command
{
    NSLog(@"show manually called");
    
    [Pollfish show];
}

- (void) hide:(CDVInvokedUrlCommand*)command
{
    NSLog(@"hide manually called");
    
    [Pollfish hide];
}

- (void) pollfishOpened
{
    NSLog(@"surveyOpened notification");
    
    if (onPollfishOpened != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishOpened];
        
    } else {
        
        NSLog(@"surveyOpened notification not set");
    }
}

- (void) pollfishClosed
{
    NSLog(@"surveyClosed notification");
    
    if (onPollfishClosed != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishClosed];
        
    } else {
        
        NSLog(@"surveyClosed notification not set");
    }
}

- (void) pollfishSurveyReceivedWithSurveyInfo:(SurveyInfo *)surveyInfo
{
    NSLog(@"surveyReceived notification");
    
    if (onPollfishSurveyReceived != nil) {
        
        int surveyPrice = [[surveyInfo cpa] intValue];
        int surveyIR = [[surveyInfo ir] intValue];
        int surveyLOI = [[surveyInfo loi] intValue];
        
        NSString *surveyClass = [surveyInfo surveyClass];
        
        NSString *rewardName = [surveyInfo rewardName];
        int rewardValue = [[surveyInfo rewardValue] intValue];
    
   		NSLog(@"Pollfish: Survey Received - SurveyPrice:%d andSurveyIR: %d andSurveyLOI:%d andSurveyClass:%@ andRewardName:%@ andRewardValue:%d", surveyPrice,surveyIR, surveyLOI, surveyClass, rewardName, rewardValue);
        
        NSArray *keys = [NSArray arrayWithObjects:@"survey_cpa", @"survey_ir", @"survey_loi", @"survey_class",@"reward_name", @"reward_value", nil];
        NSArray *objects = [NSArray arrayWithObjects:[NSString stringWithFormat: @"%d",surveyPrice],[NSString stringWithFormat: @"%d",surveyIR],[NSString stringWithFormat: @"%d",surveyLOI],[NSString stringWithFormat: @"%@",surveyClass],[NSString stringWithFormat: @"%@",rewardName],[NSString stringWithFormat: @"%d",rewardValue], nil];
       
        NSDictionary *dictionary = [NSDictionary dictionaryWithObjects:objects
                                                               forKeys:keys];
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyReceived];
        
    } else {
        
        NSLog(@"surveyReceived notification not set");
    }
}

- (void) pollfishSurveyCompletedWithSurveyInfo:(SurveyInfo *)surveyInfo
{
    NSLog(@"surveyCompleted notification");
    
    if (onPollfishSurveyCompleted != nil) {
       
        int surveyPrice = [[surveyInfo cpa] intValue];
        int surveyIR = [[surveyInfo ir] intValue];
        int surveyLOI = [[surveyInfo loi] intValue];
        
        NSString *surveyClass = [surveyInfo surveyClass];
        
        NSString *rewardName = [surveyInfo rewardName];
        int rewardValue = [[surveyInfo rewardValue] intValue];
        
   		NSLog(@"Pollfish: Survey Completed - SurveyPrice:%d andSurveyIR: %d andSurveyLOI:%d andSurveyClass:%@ andRewardName:%@ andRewardValue:%d", surveyPrice,surveyIR, surveyLOI, surveyClass, rewardName, rewardValue);
        
        NSArray *keys = [NSArray arrayWithObjects:@"survey_cpa", @"survey_ir", @"survey_loi", @"survey_class",@"reward_name", @"reward_value", nil];
        NSArray *objects = [NSArray arrayWithObjects:[NSString stringWithFormat: @"%d",surveyPrice],[NSString stringWithFormat: @"%d",surveyIR],[NSString stringWithFormat: @"%d",surveyLOI],[NSString stringWithFormat: @"%@",surveyClass],[NSString stringWithFormat: @"%@",rewardName],[NSString stringWithFormat: @"%d",rewardValue], nil];
       
        NSDictionary *dictionary = [NSDictionary dictionaryWithObjects:objects
                                                               forKeys:keys];
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
        
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyCompleted];
        
    } else {
        
        NSLog(@"onPollfishSurveyCompleted notification not set");
    }
}

- (void) pollfishUsernotEligible
{
    NSLog(@"userNotEligible notification");
    
    if (onPollfishUserNotEligible != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserNotEligible];
    } else { 

        NSLog(@"userNotEligible notification not set");

    }
}

- (void) pollfishUserRejectedSurvey
{
    NSLog(@"userRejectedSurvey notification");
    
    if (onPollfishUserRejectedSurvey != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserRejectedSurvey];
    } else {

        NSLog(@"userRejectedSurvey notification not set");
    
    }
}

- (void) pollfishNotAvailable
{
    NSLog(@"surveyNotAvailable notification");
    
    if (onPollfishSurveyNotAvailable != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyNotAvailable];
        
    } else {

        NSLog(@"onPollfishSurveyNotAvailable notification not set");

    }
}

- (void) setEventCallback:(CDVInvokedUrlCommand*)command{
    
    NSLog(@"setEventCallbackWithName");
    
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* eventName =[NSString stringWithFormat: @"%@", name];
    
    NSLog(@"eventName: %@",eventName);
    
    if ([@"onPollfishSurveyReceived" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyReceived set");
        
        onPollfishSurveyReceived = command.callbackId;
        
    } else if ([@"onPollfishSurveyNotAvailable" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyNotAvailable set");
        
        onPollfishSurveyNotAvailable = command.callbackId;
        
    } else if ([@"onPollfishSurveyCompleted" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishSurveyCompleted set");
        
        onPollfishSurveyCompleted = command.callbackId;
        
    } else if ([@"onPollfishUserNotEligible" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishUs1erNotEligible set");
        
        onPollfishUserNotEligible = command.callbackId;
        
    } else if ([@"onPollfishUserRejectedSurvey" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishUserRejectedSurvey set");
        
        onPollfishUserRejectedSurvey = command.callbackId;
        
    } else if ([@"onPollfishOpened" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishOpened set");
       
        onPollfishOpened = command.callbackId;
        
    } else if ([@"onPollfishClosed" isEqualToString:eventName]) {
        
        NSLog(@"Pollfish onPollfishClosed set");
        
        onPollfishClosed = command.callbackId;
    }
}


@end