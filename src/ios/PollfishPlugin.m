#import "PollfishPlugin.h"
#import <Cordova/CDV.h>

@implementation PollfishPlugin
{
    BOOL initialized;
}

static NSString* onPollfishSurveyReceived = nil;
static NSString* onPollfishSurveyCompleted = nil;
static NSString* onPollfishSurveyNotAvailable = nil;
static NSString* onPollfishUserNotEligible = nil;
static NSString* onPollfishUserRejectedSurvey = nil;
static NSString* onPollfishOpened = nil;
static NSString* onPollfishClosed = nil;

- (void) init:(CDVInvokedUrlCommand*)command{
    NSString* apiKey = [[command arguments] objectAtIndex:1];
    int position = [[[command arguments] objectAtIndex:2] integerValue];
    int padding = [[[command arguments] objectAtIndex:3] integerValue];
    BOOL offerwallMode = [[[command arguments] objectAtIndex:4] boolValue];
    BOOL releaseMode = [[[command arguments] objectAtIndex:5] boolValue];
    BOOL rewardMode = [[[command arguments] objectAtIndex:6] boolValue];
    NSString* requestUUID = [[command arguments] objectAtIndex:7];
    NSMutableDictionary *userAttributesDict = [[command arguments] objectAtIndex:8];
    NSString* clickId = [[command arguments] objectAtIndex:9];
    NSString* userId = [[command arguments] objectAtIndex:10];
    NSString* signature = [[command arguments] objectAtIndex:11];
    NSMutableDictionary *rewardInfoDict = [[command arguments] objectAtIndex:12];

    if (apiKey == [NSNull null]) {
        initialized = NO;
        return;
    }

    PollfishParams * pollfishParams = [[PollfishParams alloc] init:apiKey];
    [pollfishParams indicatorPadding:padding];
    [pollfishParams indicatorPosition:position];   
    [pollfishParams releaseMode:releaseMode];
    [pollfishParams offerwallMode:offerwallMode];
    [pollfishParams rewardMode:rewardMode];
    [pollfishParams platform:PlatformCordova];
    
    if (requestUUID != [NSNull null]) {
        [pollfishParams requestUUID:requestUUID];
    }

    if (clickId != [NSNull null]) {
        [pollfishParams clickId:clickId];
    }

    if (userId != [NSNull null]) {
        [pollfishParams userId:userId];
    }

    if (signature != [NSNull null]) {
        [pollfishParams signature:signature];
    }
    
    if (rewardInfoDict != [NSNull null]) {
        RewardInfo *rewardInfo = [[RewardInfo alloc] initWithRewardName: [rewardInfoDict valueForKey:@"rewardName"]
                                                       rewardConversion: [[rewardInfoDict valueForKey:@"rewardConversion"] doubleValue]];
        [pollfishParams rewardInfo:rewardInfo];
    }

    if (userAttributesDict != [NSNull null]) {
        UserProperties *userProperties = [[UserProperties alloc] init];
        
        [userAttributesDict enumerateKeysAndObjectsUsingBlock:^(id key, id object, BOOL *stop) {
            [userProperties customAttribute:object forKey:key];
        }];

        [pollfishParams userProperties:userProperties];
    }
    
    [Pollfish initWith:pollfishParams delegate:self];

    initialized = YES;
}

- (void) show:(CDVInvokedUrlCommand*)command
{
    if (initialized == YES) {
        [Pollfish show];
    }
}

- (void) hide:(CDVInvokedUrlCommand*)command
{
    if (initialized == YES) {
        [Pollfish hide];
    }
}

- (void) isPollfishPresent:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:initialized ? [Pollfish isPollfishPresent] : NO];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) isPollfishPanelOpen:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool: initialized ? [Pollfish isPollfishPanelOpen] : NO];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) pollfishOpened
{
    if (onPollfishOpened != nil) {
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishOpened];
        
    }
}

- (void) pollfishClosed
{
    if (onPollfishClosed != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishClosed];   
    }
}

- (void) pollfishSurveyReceivedWithSurveyInfo:(SurveyInfo *)surveyInfo
{
    if (onPollfishSurveyReceived != nil) {

        CDVPluginResult* pluginResult;

        if (surveyInfo == nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                          messageAsDictionary:nil];
        } else {
             pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[self dictionaryFrom:surveyInfo]];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyReceived];
    }
}

- (NSDictionary *) dictionaryFrom:(SurveyInfo*) surveyInfo
{
    int surveyPrice = [[surveyInfo cpa] intValue];
    int surveyIR = [[surveyInfo ir] intValue];
    int surveyLOI = [[surveyInfo loi] intValue];
    
    NSString *surveyClass = [surveyInfo surveyClass];
    
    NSString *rewardName = [surveyInfo rewardName];
    int rewardValue = [[surveyInfo rewardValue] intValue];
    int remainingCompletes = [[surveyInfo remainingCompletes] intValue];

    
    NSArray *keys = [NSArray arrayWithObjects:@"survey_cpa", @"survey_ir", @"survey_loi", @"survey_class",@"reward_name", @"reward_value", @"remaining_completes", nil];
    NSArray *objects = [NSArray arrayWithObjects:[NSString stringWithFormat: @"%d", surveyPrice],
                        [NSString stringWithFormat: @"%d", surveyIR],
                        [NSString stringWithFormat: @"%d", surveyLOI],
                        [NSString stringWithFormat: @"%@", surveyClass],
                        [NSString stringWithFormat: @"%@", rewardName],
                        [NSString stringWithFormat: @"%d", rewardValue],
                        [NSString stringWithFormat: @"%d", remainingCompletes],
                        nil];
    
    NSDictionary *dictionary = [NSDictionary dictionaryWithObjects:objects
                                                            forKeys:keys];
    return dictionary;
}

- (void) pollfishSurveyCompletedWithSurveyInfo:(SurveyInfo *)surveyInfo
{
    if (onPollfishSurveyCompleted != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[self dictionaryFrom:surveyInfo]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyCompleted];
    }
}

- (void) pollfishUsernotEligible
{
    if (onPollfishUserNotEligible != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];   
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserNotEligible];
    }
}

- (void) pollfishUserRejectedSurvey
{
    if (onPollfishUserRejectedSurvey != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];       
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishUserRejectedSurvey];
    }
}

- (void) pollfishNotAvailable
{
    if (onPollfishSurveyNotAvailable != nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:onPollfishSurveyNotAvailable];
    }
}

- (void) setEventCallback:(CDVInvokedUrlCommand*)command
{    
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* eventName =[NSString stringWithFormat: @"%@", name];
    
    if ([@"onPollfishSurveyReceived" isEqualToString:eventName]) {    
        onPollfishSurveyReceived = command.callbackId;
    } else if ([@"onPollfishSurveyNotAvailable" isEqualToString:eventName]) {
        onPollfishSurveyNotAvailable = command.callbackId;
    } else if ([@"onPollfishSurveyCompleted" isEqualToString:eventName]) {
        onPollfishSurveyCompleted = command.callbackId;
    } else if ([@"onPollfishUserNotEligible" isEqualToString:eventName]) {
        onPollfishUserNotEligible = command.callbackId;
    } else if ([@"onPollfishUserRejectedSurvey" isEqualToString:eventName]) {
        onPollfishUserRejectedSurvey = command.callbackId;
    } else if ([@"onPollfishOpened" isEqualToString:eventName]) {
        onPollfishOpened = command.callbackId;
    } else if ([@"onPollfishClosed" isEqualToString:eventName]) {
        onPollfishClosed = command.callbackId;
    }
}

@end
