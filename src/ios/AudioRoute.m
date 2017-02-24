#import "AudioRoute.h"


@implementation AudioRoute


- (void)pluginInitialize {
    NSLog(@"Initializing AudioRoute plugin");
}



- (void) currentOutputs:(CDVInvokedUrlCommand*)command {
    
    if(!mpVolumeView){
        mpVolumeView = [[MPVolumeView alloc] initWithFrame:CGRectZero];
        mpVolumeView.showsVolumeSlider = FALSE;
        [self.webView.superview addSubview:mpVolumeView];
        [mpVolumeView setAlpha:0.01];
    }
    
    UIButton* btn = nil;
    for (UIView *view in [mpVolumeView subviews]){
        if ([view.class.description isEqualToString:@"MPButton"]){
            btn = (UIButton*)view;
            break;
        }
    }
    
    if(btn){
        [btn sendActionsForControlEvents:UIControlEventTouchUpInside];
    }
}


- (void) overrideOutput:(CDVInvokedUrlCommand*)command {
     NSLog(@"overrideOutput AudioRoute plugin - Not in use for iOS");
}

- (void) setRouteChangeCallback:(CDVInvokedUrlCommand*)command{
    NSLog(@"setRouteChangeCallback AudioRoute plugin - Not in use for iOS");
}
@end
