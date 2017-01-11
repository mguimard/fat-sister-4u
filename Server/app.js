/**
 * Created by kelto on 11/01/17.
 */
let http = require('http');
let socketio = require('socket.io');
let user = require('./lib/user.js');

let wakerServer = http.createServer();
wakerServer.listen(3000);

let io = socketio.listen(wakerServer);

io.sockets.on('connection', function(socket) {
    console.log("New connection received.");

    socket.on('auth', function (auth) {
        user.authUser(auth);
    });

    socket.on('event', function(data) {
        user.sendCommand(socket, data);
    });
});

let slaveServer = http.createServer();
slaveServer.listen(9000);
let slaveIo = socketio.listen(slaveServer);

slaveIo.on('connection', function (socket) {
    user.mapSlave(socket);
});