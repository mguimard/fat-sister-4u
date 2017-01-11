# FAT SISTER TARGET DAEMON

This component is meant to be started on the waken computer

## Commands over web socket

Only this kind of messages will be parsed

    {
        "command":"command",
        "args":"arguments as json payload"
    }


**command** can takes the following values :

- IDE
- HALT
- REBOOT
- UNREAD_MAILS


## Server connection

At start, the daemon will try to connect the central server over websocket.

The server IP can be configured in config.json

## Run 

To run the client on the waken computer : 

    npm install
    node index.js
