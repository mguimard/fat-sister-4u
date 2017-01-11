'use strict';

var config = require('./config.json');
var url = config.server_ws_endpoint;
var commander = require('./commander');

console.log('Available commands');
console.log(Object.keys(config.commands));

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

    socket.on('command', function (data) {
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
    if (data.command && typeof data.command === 'string') {
        commander.run(data);
    }
};

// Uncomment for testing

// Direct commands
//handleMessage({command: 'IDE'});

// Shortcuts
//handleMessage({command: 'SLACK'});
//handleMessage({command: 'WORK'});