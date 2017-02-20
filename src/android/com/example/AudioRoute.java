package com.example;

import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Bundle;

 

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

 

/**
 * Created by altaf.h.shaikh on 2/7/2017.
 */


public class AudioRoute extends CordovaPlugin {

    AudioManager audioM = null;
 
    //    String str;
    public static final String SPEAKER = "Speaker";
    public static final String BLUTOOTH = "Bluetooth";
    public static final String AUXILARY = "Auxilary";
    public static final String Headphone3 = "Headphone3";
    
     public static final String AUDIO_OUTPUT_CHANGED = "audio_output";
     public static final String CURRENT = "current_output";
    private CallbackContext audioCallbackContext = null;

    HashMap<String, String> hashDevices;
    private String currenOutput = "";

    AudioRouter audioRouter;
     public AudioRoute() {
        hashDevices = new HashMap<String, String>();
      
  }

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
     cordova.getActivity().
    registerReceiver(receiver, new IntentFilter(AUDIO_OUTPUT_CHANGED));

    audioRouter = new AudioRouter(cordova.getActivity());
  
  }

  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle bundle = intent.getExtras();
      String str = bundle.getString(CURRENT);
      // setRouteChangeCallback(str);
      // audioCallbackContext.sendPluginResult(new PluginResult(status, str));
      currenOutput = str;

      setRouteChangeCallback(str);
      //  callbackContext.sendPluginResult(result);
    }
  };

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    @Override
    public boolean execute(String action, JSONArray args,final CallbackContext callbackContext) throws JSONException {
        CordovaResourceApi resourceApi = webView.getResourceApi();
        final PluginResult.Status status = PluginResult.Status.OK;
        String result = "";
        if (action.equals("currentOutputs")) {
            HashMap<String, String> devicesList = this.getDevicesList();

            //get json value from the hashmap
            String jsonHashmap = currentOutputs(devicesList);
            callbackContext.sendPluginResult(new PluginResult(status, jsonHashmap));
            return true;
        }
         if (action.equals("overrideOutput")) {
            String typeSpeaker = args.getString(0);
            setAudioRoute(typeSpeaker);
            callbackContext.sendPluginResult(new PluginResult(status, typeSpeaker));
            return true;
      }
      if (action.equals("setRouteChangeCallback")) {
                 this.audioCallbackContext = callbackContext;
                setRouteChangeCallback(currenOutput);
                return true;

        }
        callbackContext.sendPluginResult(new PluginResult(status, result));
        return true;
    }

     private void setAudioRoute(String typeSpeaker) {
            audioRouter = new AudioRouter(cordova.getActivity());
        //default speaker mode enable
        // audioRouter.setRouteMode(AudioRouter.AudioRouteMode.SPEAKER);
         if (typeSpeaker.equalsIgnoreCase(Headphone3)) {
             audioRouter.setRouteMode(AudioRouter.AudioRouteMode.WIRED_HEADPHONE);
        } else if (typeSpeaker.equalsIgnoreCase(SPEAKER)) {
            audioRouter.setRouteMode(AudioRouter.AudioRouteMode.SPEAKER);
         } else if (typeSpeaker.equalsIgnoreCase(BLUTOOTH)) {
            audioRouter.setRouteMode(AudioRouter.AudioRouteMode.BLUETOOTH_A2DP);
    }
  }

    private String currentOutputs(HashMap<String, String> devicesList) {
        Set mapSet = (Set) devicesList.entrySet();
        Iterator iterator = mapSet.iterator();
        JSONObject jObject;
        JSONArray jsonArray = new JSONArray();
        while (iterator.hasNext()) {
            //Add json object
            jObject = new JSONObject();

            Map.Entry mapEntry = (Map.Entry) iterator.next();
            // getKey Method of HashMap access a key of map
            String keyValue = (String) mapEntry.getKey();
            //getValue method returns corresponding key's value
            String value = (String) mapEntry.getValue();

            try {
                jObject.put("Name", keyValue);
                jObject.put("Value", value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jObject);
        }

        return jsonArray.toString();
    }

    private HashMap<String, String> getDevicesList() {
        audioM = (AudioManager) cordova.getActivity().getApplicationContext().
                getSystemService(cordova.getActivity().getApplicationContext().AUDIO_SERVICE);

        AudioDeviceInfo[] adi = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            adi = audioM.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo devices :
                    adi) {
                CharSequence productName = devices.getProductName();
                if (devices.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES) {
                   
                    hashDevices.put("Headphone1", Headphone3);
                }
                if (devices.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    
                    hashDevices.put("Headphone3", Headphone3);
                }
                if (devices.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {
                    
                    hashDevices.put(productName.toString(), BLUTOOTH);
                }

                if (devices.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                   
                    hashDevices.put("Phone", SPEAKER);
                }
            }

        }
        return hashDevices;

    }
   public boolean setRouteChangeCallback(String typeSpeaker) {
    // PluginResult result = new PluginResult(PluginResult.Status.OK, typeSpeaker);
    PluginResult result = new PluginResult(PluginResult.Status.OK, typeSpeaker);
    result.setKeepCallback(true);
    if (audioCallbackContext != null)
      audioCallbackContext.sendPluginResult(result);
    Toast.makeText(cordova.getActivity(), typeSpeaker, Toast.LENGTH_SHORT).show();
    return true;
  }

}
