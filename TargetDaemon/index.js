var config = require('./config.json');
var url = config.server_ws_endpoint;
var commander = require('./commander');

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
        handleMessage(data);
    });

    socket.on('disconnect', function () {
        console.log('disconnected');
    });

};

var handleMessage = function (data) {
    // assume message is formatted as described in README
    if (data.command) {

        var args = null;

        if (data.args) {
            try {
                args = JSON.parse(data.args);
            } catch (e) {
                console.log('Cannot parse arguments payload, wont use any arguments');
            }
        }

        commander.run(data.command, args);
    }
};

// Uncomment for testing
//handleMessage({command: 'IDE', args: ''});
//handleMessage({command: 'HALT', args: ''});