var config = require('./config.json');
var url = config.server_ws_endpoint;

require('getmac').getMac(function (err, macAddress) {
    if (err) {
        throw err;
    }
    connect(macAddress);
});

var connect = function (macAddress) {

    console.log('Connecting to ' + url);
    console.log('Our mac address : ' + macAddress);

    var socket = require('socket.io-client')(url);

    socket.on('connect', function () {
        socket.emit('auth', {mac: macAddress});
        console.log('connected');
    });

    socket.on('event', function (data) {
        console.log('event');
        console.log(data);
    });

    socket.on('disconnect', function () {
        console.log('disconnected');
    });

};
