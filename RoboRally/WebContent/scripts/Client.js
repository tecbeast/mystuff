define(['Stapes', 'UserInterface'], function (Stapes, UserInterface) {
	'use strict';
	
    var Client = Stapes.subclass({

        constructor : function () {
        	this.userInterface = new UserInterface();
        	this.communication = new Communication();
        },

		init : function () {
			this.userInterface.init();
			this.communication.init();
		}

    });
    
    return Client;
    
});