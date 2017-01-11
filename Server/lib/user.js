/**
 * Created by kelto on 11/01/17.
 */

const MAC_FOR_USER = require('./user.json');
let socket_for_id = {};

function check_auth(id, password) {
    return MAC_FOR_USER.id === id && password === MAC_FOR_USER.password;
}

function get_mac_for_id(id) {
    return MAC_FOR_USER.mac;
}

function wake(mac) {
    return new Promise((resolve, reject) => {
        wol.wake("ec:a8:6b:fe:9c:ab", function(error) {
            if(error) {
                console.log(error);
                return reject(error);
            }
            resolve();
        });
    });
}

let authUser = function(socket, auth) {
    let id = auth.id;
    socket_for_id[id] = socket;
    let password = auth.password;
    if(check_auth(id, password)) {
        let mac = get_mac_for_id(id);
        wake(mac).then(() => {
            socket.emit('auth', 'SUCCESS');
        }).catch(error => socket.emit('auth', error));
    } else {
        socket.emit('auth', 'Bad credential');
        socket.disconnect();
    }
};

exports.authUser = authUser;
    
