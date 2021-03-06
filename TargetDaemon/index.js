'use strict';

const config = require('./config.json');
const url = config.server_ws_endpoint;
let commander = require('./commander');


require('getmac').getMac((err, macAddress) => {
    if (err) {
        throw err;
    }
    connect(macAddress);
});

const connect = function (macAddress) {

    console.log('Connecting to ' + url);
    console.log('Our mac address : ' + macAddress);
    console.log('Available commands');
    console.log(Object.keys(config.commands));

    commander.notify('SmartWatch', 'Connexion en cours');

    var socket = require('socket.io-client')(url);

    socket.on('connect', function () {
        console.log('Connected to server');
        commander.notify('SmartWatch', 'Connecté au serveur');
        socket.emit('auth', {mac: macAddress});
    });

    socket.on('command', handleMessage);

    socket.on('disconnect', function () {
        commander.notify('SmartWatch', 'Déconnecté du serveur');
        console.log('Disconnected from server');
    });

};

const handleMessage = function (data) {
    console.log('Handle message');
    console.log(data);
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