var config = require('./config.json');
var exec = require('child_process').exec;

module.exports = {
    run: function (commandKey, args) {

        var commandLine = config.commands[commandKey];

        if (!commandLine) {
            console.log('Unrecognized command key : ' + commandKey);
            return -1;
        }

        exec(commandLine);
    }
};
