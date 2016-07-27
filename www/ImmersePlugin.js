var exec = require('cordova/exec');

var ImmersePlugin = {
  setDarkMode: function (ok) {
    exec(null, null, "ImmersePlugin", "setDarkMode", [ok]);
  },
  getStatusbarHeight: function (successCallback) {
    exec(successCallback, null, "ImmersePlugin", "getStatusbarHeight", []);
  }
};

module.exports = ImmersePlugin;