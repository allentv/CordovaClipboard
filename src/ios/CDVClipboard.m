#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "CDVClipboard.h"
#import "NSDataBase64.h"

@implementation CDVClipboard

- (void)copy:(CDVInvokedUrlCommand*)command {
	[self.commandDelegate runInBackground:^{
		UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
		NSString     *text       = [command.arguments objectAtIndex:0];

		[pasteboard setValue:text forPasteboardType:@"public.text"];

		CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:text];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void)paste:(CDVInvokedUrlCommand*)command {
	[self.commandDelegate runInBackground:^{
		UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
		NSString     *text       = [pasteboard valueForPasteboardType:@"public.text"];
	    
	    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:text];
	    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

-(NSString *)UIImageToBaseSixtyFour:(UIImage *)sourceImage
{
        NSData *imageData = UIImageJPEGRepresentation(sourceImage, 1.0); 
        NSString *base64 = [NSString stringWithFormat:@"%@",[UIImageJPEGRepresentation(sourceImage, 0.95) base64EncodedString]];
        return base64;
}

- (void)pasteImage:(CDVInvokedUrlCommand*)command {
	[self.commandDelegate runInBackground:^{
		
        UIImage *img = [UIPasteboard generalPasteboard].image;		        
         NSString *text =[self UIImageToBaseSixtyFour:img];
	    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:text];
	    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	    
	}];
}

@end
