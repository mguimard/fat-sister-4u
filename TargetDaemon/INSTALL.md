# Install the daemon under systemd

## Service creation

Create a file `/etc/systemd/system/fat-sister-4u.service`
    
    [Unit]
    Description=Fat Sister 4u daemon service
    
    [Service]
    ExecStart=/usr/bin/node /path/to/TargetDaemon/index.js
    Restart=always
    RestartSec=10                      
    StandardOutput=syslog               
    StandardError=syslog               
    SyslogIdentifier=nodejs-fat-sister-4u
    #User=<alternate user>
    #Group=<alternate group>
    Environment=NODE_ENV=production PORT=1337
    
    [Install]
    WantedBy=multi-user.target
    
    
## Edit the file to match your current environment

Node may be not installed in `/usr/bin/node`, change it if the command below outputs something else

    which node
    /usr/bin/node
  
Change `/path/to/TargetDaemon/index.js` to the right location
    
## Control the service

Enable the service

    systemctl enable fat-sister-4u.service
    
Start the service

    systemctl start fat-sister-4u.service

Verify it is running

    systemctl status fat-sister-4u.service
    
Reboot and verify it's running
