define(['Stapes'], function (Stapes) {
	'use strict';
	
    var Communication = Stapes.subclass({

        constructor : function () {
        },

		init : function () {
            if (window.location.protocol == 'http:') {
                this.connect('ws://' + window.location.host + '/RoboRally/websocket');
            } else {
                this.connect('wss://' + window.location.host + '/RoboRally/websocket');
            }
		},
        
        connect : function (host) {
        	
            if ('WebSocket' in window) {
                this.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                this.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            this.socket.onopen = function () {
                Console.log('Info: WebSocket connection opened.');                
            };

            this.socket.onclose = function () {
                Console.log('Info: WebSocket closed.');
            };

            this.socket.onmessage = function (message) {
            	if (message) {
            		Console.log(message);
            	}
            };
            
        },

        send : function (command) {
        	if (command) {
        		this.socket.send(JSON.stringify(command));
        	}
        }

    });
    
    return Communication;
    
});