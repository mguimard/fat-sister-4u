let { createServer } = require('http');
let socketio = require('socket.io');
let user = require('./lib/smart-watch-user.js');

function listen() {

    let smartWatchServer = createServer();
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

        socket.on('command', function(data) {
            user.sendCommand(socket, data);
        });
    });
}

module.exports = {
    listen
};