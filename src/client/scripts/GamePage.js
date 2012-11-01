(function()
{
	"use strict";

	var galaxy;
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
    			star_layer_group.addLayer(m);
    		}
		);
	}
	
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
                		
					var map = L.map('map',
						{
							center: [0, 0],
							zoom: 1,
							crs: cartesianCRS,
							attributionControl: false,
							unloadInvisibleTiles: false
						}
					);

					L.tileLayer("map/{z}/{x}_{y}.jpg",
						{
							attribution: "",
							minZoom: 1,
							maxZoom: 6,
							tileSize: 250,
							noWrap: true
						}
					).addTo(map);
					
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
