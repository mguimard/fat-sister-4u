let { createServer } = require('http');
let socketio = require('socket.io');
let user = require('./lib/smart-watch-user.js');

function listen() {

    let slaveServer = createServer();
    slaveServer.listen(9000);
    let slaveIo = socketio.listen(slaveServer);

    slaveIo.on('connection', function (socket) {
        user.mapSlave(socket);
    });
}

module.exports = {
    listen
};