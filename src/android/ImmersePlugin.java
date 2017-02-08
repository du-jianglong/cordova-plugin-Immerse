package org.apache.cordova.immerse;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.apache.cordova.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.view.ViewCompat;

public class ImmersePlugin extends CordovaPlugin {
  private SystemBarTintManager tintManager;

  public ImmersePlugin() {
  }

  @Override
  public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    if (SystemBarTintManager.isEmui()) {
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      final Activity activity = cordova.getActivity();
      final Window window = activity.getWindow();

      setTranslucentStatus(window);

      tintManager = new SystemBarTintManager(activity);
    }
  }

  @Override
  public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
    if (tintManager == null) {
      return false;
    }

    final Activity activity = this.cordova.getActivity();

    if ("setDarkMode".equals(action)) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            tintManager.setStatusBarDarkMode(args.getBoolean(0), activity);
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
            int height = tintManager.getStatusbarHeight();
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

  private void setTranslucentStatus(Window win) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      win.setStatusBarColor(Color.TRANSPARENT);
      win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    } else {
      win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    
    ViewGroup mContentView = (ViewGroup) cordova.getActivity().findViewById(Window.ID_ANDROID_CONTENT);
    View mContentChild = mContentView.getChildAt(0);
    if (mContentChild != null) {
      ViewCompat.setFitsSystemWindows(mContentChild, false);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        ViewCompat.requestApplyInsets(mContentChild);
      }
    }
  }
}
