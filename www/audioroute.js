
var exec = require('cordova/exec');
function AudioRoute() {
    cordova.exec(routeChangeCallback, null, 'AudioRoute', 'setRouteChangeCallback', []);
}

AudioRoute.prototype.currentOutputs = function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'currentOutputs', []);
};

AudioRoute.prototype.overrideOutput = function (output, successCallback, errorCallback) {
    // if (output !== 'default' && output !== 'speaker') {
    //     throw new Error('output must be one of "default" or "speaker"');
    // }
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'overrideOutput', [output]);
};
AudioRoute.prototype.setRouteChangeCallback = function (onChanged) {
    exec(onChanged, function (err) {
        console.log(err);
    }, 'AudioRoute', 'setRouteChangeCallback', []);
}

function routeChangeCallback(reason) {
    cordova.fireDocumentEvent('audioroute-changed', { reason: reason });
}




if (!window.plugins)
    window.plugins = {};

if (!window.plugins.AudioRoute)
    window.plugins.AudioRoute = new AudioRoute();

if (typeof module != 'undefined' && module.exports)
    module.exports = AudioRoute;