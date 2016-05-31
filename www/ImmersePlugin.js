var exec = require('cordova/exec');

var ImmersePlugin = {
  setColor: function (color) {
    exec(null, null, "ImmersePlugin", "setColor", [color]);
  },
  setDarkMode: function (ok) {
    exec(null, null, "ImmersePlugin", "setDarkMode", [ok]);
  },
  isMiui: function (successCallback) {
    exec(successCallback, null, "ImmersePlugin", "isMiui", []);
  }
};

module.exports = ImmersePlugin;