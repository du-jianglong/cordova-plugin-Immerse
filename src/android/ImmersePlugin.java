package org.apache.cordova.immerse;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import com.gyf.barlibrary.ImmersionBar;
import org.apache.cordova.*;
import org.json.JSONException;
import org.json.JSONObject;

public class ImmersePlugin extends CordovaPlugin {
  private ImmersionBar immersionBar;

  public ImmersePlugin() {
  }

  @Override
  public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    immersionBar = ImmersionBar.with(cordova.getActivity());
    immersionBar.keyboardEnable(true).init();
  }

  @Override
  public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
    if (immersionBar == null) {
      return false;
    }

    final Activity activity = this.cordova.getActivity();

    if ("setDarkMode".equals(action)) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            immersionBar.statusBarDarkFont(args.getBoolean(0)).init();
          } catch (JSONException ex) {
            Log.e(ImmersePlugin.class.getSimpleName(), "unexpected error", ex);
          }
        }
      });
      return true;
    }

    if ("getStatusbarHeight".equals(action)) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            int height = ImmersionBar.getStatusBarHeight(activity);
            JSONObject r = new JSONObject();
            r.put("statusbarHeight", height);
            callbackContext.success(r);
          } catch (JSONException ex) {
            Log.e(ImmersePlugin.class.getSimpleName(), "unexpected error", ex);
          }
        }
      });
      return true;
    }

    return false;
  }
}
