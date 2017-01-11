/**
 * Created by kelto on 11/01/17.
 */
const MAC_FOR_USER = require('./user.json');

let client_for_mac = {};
let slave_for_mac = {};
let slave_for_client = {};

const check_auth = function(id, password) {
    return MAC_FOR_USER.id === id && password === MAC_FOR_USER.password;
};

const get_mac_for_id = function(id) {
    return MAC_FOR_USER.mac;
};

const get_slave = function(client) {
    return slave_for_client[client];
};

const get_client_of = function(socket) {
    let client = Object.keys(slave_for_client).find(client => slave_for_client[client] === socket);
    if(!client) {
        let slave_mac = Object.keys(slave_for_mac).find(mac => slave_for_mac[mac] === socket);
        client = client_for_mac[slave_mac];
    }
    return client;
};

const map_client = function (mac, client) {
    client_for_mac[mac] = client;
    map_slave(mac, slave_for_mac[mac]);
};

const map_slave = function(mac, slave) {
    let client = client_for_mac[mac];
    slave_for_mac[mac] = slave;
    slave_for_client[client] = slave;
    return client;
};

module.exports = {
    check_auth,
    get_mac_for_id,
    get_slave,
    get_client_of,
    map_slave,
    map_client
};