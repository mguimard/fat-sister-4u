/**
 * Created by kelto on 11/01/17.
 */
let http = require('http');
let socketio = require('socket.io');
let user = require('./lib/smart-watch-user.js');

let smartWatchServer = http.createServer();
smartWatchServer.listen(3000);

let io = socketio.listen(smartWatchServer);

io.sockets.on('connection', (socket) => {

    console.log('The smart watch is now connected t the socket.');

    socket.on('auth', function (auth) {

        console.log('Smart watch trying to auth');
        let authObj;

        try {
            authObj = JSON.parse(auth);
            user.authUser(socket, authObj);
        } catch (e){
            console.error(e);
            console.error("Could not parse auth data");
            console.error(auth);
        }
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