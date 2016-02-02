package com.whcyit.cordova.plugin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;

import org.json.JSONException;

import org.xmlpull.v1.XmlPullParser;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.Exception;

public class ImmersePlugin extends CordovaPlugin {
  private Long backgroundColor = 0L;

  private SystemBarTintManager tintManager;

  public ImmersePlugin() {
  }

  @Override
  public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    if (backgroundColor == 0) {
      try {
        new CustomConfigXmlParser().parse(webView.getContext());
      } catch (Exception ex) {
        Log.e(ImmersePlugin.class.getSimpleName(), "unexpected error", ex);
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      final Activity activity = cordova.getActivity();
      final Window window = activity.getWindow();
      setTranslucentStatus(window, true);

      tintManager = new SystemBarTintManager(activity);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setTintColor(backgroundColor.intValue());

      View root = window.getDecorView().findViewById(android.R.id.content);
      if (root != null) {
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        root.setPadding(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());
      }
    }
  }

  @Override
  public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
    final Activity activity = this.cordova.getActivity();

    if ("setColor".equals(action)) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            tintManager.setTintColor(Long.decode(args.getString(0)).intValue());
          } catch (JSONException ex) {
            Log.e(ImmersePlugin.class.getSimpleName(), "unexpected error", ex);
          }
        }
      });
      return true;
    }

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

    return false;
  }

  private void setTranslucentStatus(Window win, boolean on) {
    WindowManager.LayoutParams winParams = win.getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }

  private class CustomConfigXmlParser extends ConfigXmlParser {
    @Override
    public void handleStartTag(XmlPullParser xml) {
      String strNode = xml.getName();
      if (strNode.equals("immerse")) {
        String color = xml.getAttributeValue(null, "color");
        backgroundColor = Long.decode(color);
      }
    }

    @Override
    public void handleEndTag(XmlPullParser xml) {
    }
  }
}

