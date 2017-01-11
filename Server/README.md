# Central Server

This part will manage the detection of the smartwatch and trigger the wake on lan.

## Listening server

Two listening port:
- 3000 for the smartwatch (or any component) to wake the corresponding computer
- 1337 for the computer to start

## Connection

The client to first connect, must send an auth message. On success the corresponding computer will be started.
On failure, the socket will be disconnected

## Bus Handling name:

'boot': message to inform the client if the slave was boot
'command': message to send a command to the slave
'shutdown': message send to the client to inform that the slave was disconnected ... immidiately followed by a disconnect

