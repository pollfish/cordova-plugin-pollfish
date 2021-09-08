#import <Cordova/CDV.h>
#import <Pollfish/Pollfish-Swift.h>

@interface PollfishPlugin : CDVPlugin<PollfishDelegate>

- (void) init:(CDVInvokedUrlCommand*)command;
- (void) show:(CDVInvokedUrlCommand*)command;
- (void) hide:(CDVInvokedUrlCommand*)command;
- (void) setEventCallback:(CDVInvokedUrlCommand*)command;
- (void) isPollfishPresent:(CDVInvokedUrlCommand*)command;
- (void) isPollfishPanelOpen:(CDVInvokedUrlCommand*)command;

@end
