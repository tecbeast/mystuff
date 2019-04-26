define(['Stapes', 'jQuery'], function (Stapes, jQuery) {
	'use strict';
	
	var TILE_SIZE = 75;
	
    var UserInterface = Stapes.subclass({
    	
        constructor : function () {
        },

		drawTile : function (elements, x, y) {
			var canvas = $('#canvas'),
				ctx = canvas.getContext('2d'),
				srcY = 0,
				srcX, destX, destY, i, parts, img;
			for (i = 0; i < elements.length; i++) {
				parts = elements[i].split("-");
				if (parts && (parts.length > 1)) {
					img = $('#img_' + parts[0].toLowerCase());
					if (img) {
						if ('E' === parts[1]) {
							srcX = TILE_SZE;
						} else if ('S' === parts[1]) {
							srcX = 2 * TILE_SIZE;
						} else if ('W' === parts[1]) {
							srcX = 3 * TILE_SIZE;
						} else {
							srcX = 0;
						}
						destX = x * TILE_SIZE + 1;
						destY = y * TILE_SIZE + 1;
		                ctx.drawImage(img, srcX, srcY, TILE_SIZE, TILE_SIZE, destX, destY, TILE_SIZE, TILE_SIZE);
		            }
				}
			}
		},
		
		drawBoard : function (tiles) {
			var canvas = $('#canvas'),
				ctx = canvas.getContext('2d'),
				y, x;
			ctx.fillStyle = '#000000';
			ctx.fillRect(0, 0, 301, 301); 
			for (y = 0; y < tiles.length; y++) {
				for (x = 0; x < tiles[y].length; x++) {
					this.drawTile(tiles[y][x]);
				}
			}
		}

    });
    
    return UserInterface;
    
});