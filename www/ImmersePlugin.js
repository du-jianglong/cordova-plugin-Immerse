var exec = require('cordova/exec');

var ImmersePlugin = {
  setColor: function (color) {
    exec(null, null, "ImmersePlugin", "setColor", [color]);
  },
  setDarkMode: function (ok) {
    exec(null, null, "ImmersePlugin", "setDarkMode", [ok]);
  }
};

module.exports = ImmersePlugin;