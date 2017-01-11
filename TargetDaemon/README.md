# FAT SISTER TARGET DAEMON

This component is meant to be started on the waken computer

## Commands over web socket

Only this kind 

    {
        "command":"commandKey",
        "args":"arguments as json payload"
    }


**command** can takes the following values :

- IDE
- HALT
- REBOOT
- UNREAD_MAILS


## Discovery

At start, the daemon will try to discover the central server, by trying to reach it.

The server IP can be configured in config.json
