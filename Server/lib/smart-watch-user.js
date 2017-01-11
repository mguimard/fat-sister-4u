/**
 * Created by kelto on 11/01/17.
 */
let wol = require('node-wol');
const MAC_FOR_USER = require('./user.json');

let client_for_mac = {};
let slave_for_client = {};

function check_auth(id, password) {
    return MAC_FOR_USER.id === id && password === MAC_FOR_USER.password;
}

function get_mac_for_id(id) {
    return MAC_FOR_USER.mac;
}

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

function disconnect(socket, error) {
    socket.emit('auth', error);
    socket.disconnect();
}

const authUser = function(socket, auth) {
    
    let id = auth.id;
    let password = auth.password;

    if(check_auth(id, password)) {
        let mac = get_mac_for_id(id);
        wake(mac).then(() => {
            socket.emit('auth', 'SUCCESS');
        }).catch(disconnect);
    } else {
        disconnect(socket, 'Bad Credential');
    }
};

const mapSlave = function(socket) {
    socket.on('auth', function (data) {
        let mac = data.mac;
        console.log("Slave connected with mac: " + mac);
        let client = client_for_mac[mac];
        slave_for_client[client] = socket;
        client.emit('boot', {'status': 'Ok'});
    });

    socket.on('disconnect', function() {
        console.log('Slave disconnected');
        let client = client_for_mac[socket];
        client.emit('shutdown', 'Slave disconnected');
        client.disconnect();
    });
};

const sendCommand = function(socket, command) {
    let slave = slave_for_client[socket];
    if(!slave) {
        socket.emit('error', 'Could not find slave socket');
    } else {
        slave.emit('command', command);
    }
};

exports.authUser = authUser;
exports.mapSlave= mapSlave;
exports.sendCommand = sendCommand;
    

