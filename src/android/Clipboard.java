package com.verso.cordova.clipboard;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.ClipDescription;

public class Clipboard extends CordovaPlugin {

    private static final String actionCopy = "copy";
    private static final String actionPaste = "paste";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        ClipboardManager clipboard = (ClipboardManager) cordova.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if (action.equals(actionCopy)) {
            try {
                String text = args.getString(0);
                ClipData clip = ClipData.newPlainText("Text", text);

                clipboard.setPrimaryClip(clip);

                callbackContext.success(text);

                return true;
            } catch (JSONException e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
            }
        } else if (action.equals(actionPaste)) {
            try {
                ClipDescription itemClipDescription = clipboard.getPrimaryClipDescription();
                if (!(
                    itemClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                    itemClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)
                )) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.NO_RESULT));
                }
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String textData = null;
                if(itemClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    textData = item.getText().toString();
                } else {
                    Context cordovaContext = this.cordova.getActivity().getApplicationContext();
                    // Coerce into text to support HTML text
                    textData = item.coerceToText(cordovaContext).toString();
                }
                if (textData == null) textData = "";

                callbackContext.success(textData);

                return true;
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
            }
        }

        return false;
    }
}
