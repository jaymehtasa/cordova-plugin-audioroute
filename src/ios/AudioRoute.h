#import <AVFoundation/AVFoundation.h>
#import <Cordova/CDV.h>
#import <MediaPlayer/MediaPlayer.h>

@interface AudioRoute :CDVPlugin {
    NSString* callbackId;
    MPVolumeView *mpVolumeView;
}

- (void) currentOutputs:(CDVInvokedUrlCommand*)command;
- (void) overrideOutput:(CDVInvokedUrlCommand*)command;
- (void) setRouteChangeCallback:(CDVInvokedUrlCommand*)command;

@end
