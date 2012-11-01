(function()
{
	"use strict";

	var galaxy;
	var map;
	var graticule_layer;
	var star_layer_group;
	
	var any_star_changed_pending = false;
	var star_changed_cb = function(o)
	{
		if (!any_star_changed_pending)
		{
			any_star_changed_pending = true;
			setTimeout(any_star_changed_cb, 0);
		}
	}
	
	var any_star_changed_cb = function()
	{
		any_star_changed_pending = false;

		star_layer_group.clearLayers();
		$.each(galaxy.VisibleStars,
			function (_, star)
			{
    			var m = L.marker([star.X, star.Y]);
    			m.bindLabel(star.Name);
    			star_layer_group.addLayer(m);
    		}
		);
	}
	
	var on_mousemove_cb = function(event)
	{
		var p = event.latlng;
		$("#currentcoordinates").text("["+p.lng+", "+p.lat+"]");
	};
	
	var sensible_spacing = function(scale)
	{
		/* scale is pixels per map unit. */
		
		var target = 1 / scale; /* map unit per pixel */
		if (target < 0.1)
			target = 0.1;
		
		var spacing = 0.1;		
		while (spacing < target)
			spacing *= 10;
		return spacing;
	};
	
	var calculate_graticule_thickness = function(x)
	{
		var g = Math.round(Math.abs(x*10));
		
		var i;
		if ((g % 500) === 0)
			i = 5;
		else if ((g % 100) === 0)
			i = 4;
		else if ((g % 50) === 0)
			i = 3;
		else if ((g % 10) === 0)
			i = 2;
		else if ((g % 5) === 0)
			i = 1;
		else
			i = 0;
		
		return (i+1)/2 - (6 - map.getZoom()) / 4;
	};
	
	var drawtile_cb = function(canvas, tile)
	{
		var ctx = canvas.getContext("2d");
		var ts = this.options.tileSize;

		/* Calculate map position of the top-left and bottom-right corners
		 * of the tile. */

		var tl_px = tile.multiplyBy(ts);
		var br_px = tl_px.add([ts, ts]);
		
		/* Convert to map coordinates. */
		
		var tl = map.unproject(tl_px);
		var br = map.unproject(br_px);
		
		/* Calculate the scale. */
		
		var scalex = ts / (br.lng - tl.lng);
		var scaley = ts / (tl.lat - br.lat);
		
		/* Calculate the grid spacing. */
		
		var gridx = sensible_spacing(scalex);
		var gridy = sensible_spacing(scaley);
		
		/* Calculate the area of grid we're going to draw. */
		
		var x1 = Math.floor(tl.lng);
		var x2 = Math.ceil(br.lng);
		var y1 = Math.floor(tl.lat);
		var y2 = Math.ceil(br.lat);
		
		/* Draw graticules. */
		
	    ctx.lineWidth = 0.5;
        ctx.strokeStyle = "#0000ff";
        
        for (var x=x1; x<=x2; x+=gridx)
        {
        	var t = calculate_graticule_thickness(x);
        	if (t <= 0)
        		continue;
        	ctx.lineWidth = t;
        	
        	var p = map.project({lng: x, lat: 0}).subtract(tl_px);
        	
        	ctx.beginPath();
        	ctx.moveTo(p.x, 0);
        	ctx.lineTo(p.x, ts);
        	ctx.stroke();
        	ctx.closePath();
        }
        
        for (var y=y1; y<=y2; y+=gridy)
        {
        	var t = calculate_graticule_thickness(y);
        	if (t <= 0)
        		continue;
        	ctx.lineWidth = t;
        	
        	var p = map.project({lng: 0, lat: y}).subtract(tl_px);
        	
        	ctx.beginPath();
        	ctx.moveTo(0, p.y);
        	ctx.lineTo(ts, p.y);
        	ctx.stroke();
        	ctx.closePath();
        }
	};
	
    G.GamePage =
    {
        Show: function ()
        {
            $("#page").load("game.html",
            	function ()
            	{
            		galaxy = Universe.Galaxy;

            		/* We apply a pretty nasty fudge factor here, because the
            		 * background image is rather smaller than the galactic
            		 * radius. */
            		
            		var scalefactor = 1 / (galaxy.GalacticRadius*3.5);
                	var cartesianCRS = L.Util.extend({}, L.CRS,
            			{
            	    		projection: L.Projection.LonLat,
            	    		transformation: new L.Transformation(
            	    			scalefactor, 0.5,
            	    			scalefactor, 0.5)
            			}
            		);
                		
					map = L.map('map',
						{
							center: [0, 0],
							zoom: 1,
							maxZoom: 6,
							crs: cartesianCRS,
							attributionControl: false,
							unloadInvisibleTiles: false
						}
					);

					L.tileLayer("map/{z}/{x}_{y}.jpg",
						{
							attribution: "",
							minZoom: 1,
							tileSize: 250,
							noWrap: true
						}
					).addTo(map);
					
					graticule_layer = L.tileLayer.canvas();
					graticule_layer.drawTile = drawtile_cb;
					graticule_layer.addTo(map);
					
					map.on("mousemove", on_mousemove_cb);
					
					star_layer_group = L.layerGroup();
					star_layer_group.addTo(map);
					
					Database.Watch(galaxy, star_changed_cb);
					
					for (var i=0; i<galaxy.VisibleStars.length; i++)
					{
						var star = galaxy.VisibleStars[i];
						Database.Watch(star, star_changed_cb);
					}
            	}
            );
        }
    };
}
)();
