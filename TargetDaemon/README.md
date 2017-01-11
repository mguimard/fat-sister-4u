# FAT SISTER 4U DAEMON

This component is meant to be started on the waken computer. It connects a websocket to the server and listen for commands.

## Server connection

At start, the daemon will try to connect the central server over websocket.

The server IP can be configured in config.json

## Commands over web socket

The web socket client listens to the following event names 

| Event name | data           |
|------------|----------------|
| command    | command object |

**The command object** 

A JSON object containing a command key, and the corresponding value

    { "command" : "IDE" }
    
Available commands are defined in config.json

You may also pass shortcuts
 
    { "command" : "SLACK" }

## Run 

To run the client on the waken computer : 

    npm install
    node index.js
