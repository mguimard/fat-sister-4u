/**
 * Created by kelto on 11/01/17.
 */
let smartWatchServer = require('./server/waker-server');
let slaveServer = require('./server/slave-server');

slaveServer.listen();
smartWatchServer.listen();