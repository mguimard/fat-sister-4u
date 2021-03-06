/**
 * Created by kelto on 11/01/17.
 */
let wol = require('node-wol');
let repo = require('./socket-repository');

function wake(mac) {
    return new Promise((resolve, reject) => {
        wol.wake(mac, function(error) {
            if(error) {
                console.log(error);
                return reject(error);
            }
            resolve();
        });
    });
}

function auth_failure(socket, error) {
    console.log('auth failure');
    socket.emit('auth', error);
    socket.disconnect();
}

const authUser = function(socket, auth) {

    let id = auth.id;
    let password = auth.password;

    if(repo.check_auth(id, password)) {
        let mac = repo.get_mac_for_id(id);
        repo.map_client(mac, socket);
        wake(mac).then(() => {
            socket.emit('auth', 'SUCCESS');
        }).catch(auth_failure);
    } else {
        auth_failure(socket, 'Bad Credential');
    }
};

const mapSlave = function(socket) {

    socket.on('auth', (data) => {

        let mac = data.mac;
        console.log("Slave connected with mac: " + mac);
        let client = repo.map_slave(mac, socket);
        if(client === 'undefined' || client === undefined) {
            console.info('Client not connected ... nothing to do');
        } else {
            client.emit('boot', {'status': 'Ok'});
        }
    });

    socket.on('disconnect', () => {
        console.log('Slave disconnected');
        let client = repo.get_client_of(socket);
        if(client !== undefined && client !== 'undefined' ) {
            try {
              client.emit('shutdown', 'Slave disconnected');
            client.disconnect();
            } catch(e) {
                console.log("could not notify client");
            }
        }
    });
};

const sendCommand = (socket, command) => {
    let slave = repo.get_slave(socket);

    if(slave === undefined || slave === 'undefined') {
        socket.emit('not_found', 'Could not find slave socket');
    } else {
        console.log('sending command: ');
        console.log(command);
        slave.emit('command', JSON.parse(command));
    }
};

module.exports = {
    authUser,
    mapSlave,
    sendCommand
};


