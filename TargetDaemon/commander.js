'use strict';

var config = require('./config.json');
var exec = require('child_process').exec;

var getCommandLine = function (key) {
    return config.commands[key];
};


var runCommandLine = function (commandLine) {
    return exec(commandLine, function (error, stdout, stderr) {
        console.log(stdout);
        console.log(stderr);
    });
};

var runShortcut = function (shortcut) {
    Object.keys(shortcut).forEach(function(key){
        runCommandLine(getCommandLine(key) + ' ' + shortcut[key]);
    });
};

module.exports = {

    run: function (commandData) {

        var commandLine = getCommandLine(commandData.command);

        if (commandLine) {
            return runCommandLine(commandLine);
        }

        var shortcut = config.shortcuts[commandData.command];

        if (shortcut && typeof shortcut === 'object') {
            return runShortcut(shortcut);
        }

        console.log('Unrecognized command key : ' + commandKey);
        return -1;
    }
};
