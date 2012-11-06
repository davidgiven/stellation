(function()
{
	"use strict";

	var galaxy;
	var map;
	var graticule_layer;
	var star_layer_group;
	
	var star_images = [];
	var star_images_filenames =
		[
		 	"res/star1.png",
		 	"res/star2.png",
		 	"res/star3.png",
		 	"res/star4.png",
		 	"res/star5.png",
		 	"res/star6.png",
		 	"res/star7.png",
		 	"res/star8.png",
		 	"res/star9.png",
		];
	
	var any_star_changed_pending = false;
	var star_changed_cb = function(o)
	{
		if (!any_star_changed_pending)
		{
			any_star_changed_pending = true;
			setTimeout(any_star_changed_cb, 0);
		}
	}
	
	var icon_for_star = function(star)
	{
		var b = Math.floor(star.Brightness) - 1;
		var size = 8 * (1 + map.getZoom()*3);
		
		return L.icon(
				{
					iconUrl: star_images[b],
    				iconSize: [16, 16],
    				iconAnchor: [size/2, size/2]
				}
			);
	};
	
	var any_star_changed_cb = function()
	{
		any_star_changed_pending = false;
		graticule_layer.redraw();

		var icon = L.divIcon(
			{
				className: "invisible-map-marker", 
				iconSize: [16, 16],
				iconAnchor: [8, 8]
			}
		);
		
		star_layer_group.clearLayers();
		$.each(galaxy.VisibleStars,
			function (star)
			{
    			var m = L.marker({lng: star.X, lat: star.Y},
    				{
    					icon: icon
    				}
    			);
    			
    			m.bindLabel(star.Name);
    			star_layer_group.addLayer(m);
    		}
		);
	}
	
	var on_mousemove_cb = function(event)
	{
		var p = event.latlng;
		$("#mapinfo .content").text("["+p.lng.toFixed(1)+", "+p.lat.toFixed(1)+"]");
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
	
	var smallest_visible_graticule = function()
	{
		if (calculate_graticule_thickness(0.1) > 0)
			return 0.1;
		if (calculate_graticule_thickness(0.5) > 0)
			return 0.5;
		if (calculate_graticule_thickness(1.0) > 0)
			return 1.0;
		if (calculate_graticule_thickness(5.0) > 0)
			return 5.0;
		return 10.0;
	};
	
	var floorto = function(n, quantum)
	{
		return Math.floor(n / quantum) * quantum;
	};
	
	var roundto = function(n, quantum)
	{
		return Math.round(n / quantum) * quantum;
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
		
		var svg = smallest_visible_graticule();
		
		/* Calculate the area of grid we're going to draw. */
		
		var x1 = floorto(tl.lng, svg);
		var x2 = Math.ceil(br.lng);
		var y1 = floorto(tl.lat, svg);
		var y2 = Math.ceil(br.lat);
		
		/* Draw graticules. */
		
	    ctx.lineWidth = 0.5;
        ctx.strokeStyle = "rgba(0,0,0,0.1)";
        
        for (var x=x1; x<=x2; x+=svg)
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
        
        for (var y=y1; y<=y2; y+=svg)
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
        
        /* Draw the stars. */
        
        $.each(galaxy.VisibleStars,
        	function (star)
        	{
        		var b = Math.floor(star.Brightness) - 1;
        		var size = 8 * (1 + map.getZoom()*3);

            	var p = map.project({lng: star.X, lat: star.Y}).subtract(tl_px);
            	var img = star_images[b];
            	ctx.drawImage(img, p.x-size/2, p.y-size/2, size, size);
        	}
        );
	};
	
    S.GamePage =
    {
    	Preload: function (cb)
    	{
    		S.PreloadImages(star_images_filenames, star_images, cb);
    	},
    	
        Show: function ()
        {
            $("#page").load("game.html",
            	function ()
            	{
            		galaxy = S.Galaxy;

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
							zoomControl: false,
							unloadInvisibleTiles: false,
						}
					);

					new L.Control.Zoom(
						{
							position: "bottomright"
						}
					).addTo(map);
					
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
					map.on("viewreset", star_changed_cb);
					
					star_layer_group = L.layerGroup();
					star_layer_group.addTo(map);
					
					S.Database.Watch(galaxy, star_changed_cb);
					
					for (var i=0; i<galaxy.VisibleStars.length; i++)
					{
						var star = galaxy.VisibleStars[i];
						S.Database.Watch(star, star_changed_cb);
					}
					
					/* Set up data panes. */
					
					$("#clockpane")
						.addClass("ui-dialog ui-widget-content ui-corner-all");
					
					$("#titlepane")
						.addClass("ui-dialog ui-widget-content ui-corner-all");
					
					$("#indexpane")
						.addClass("ui-dialog ui-widget-content ui-corner-all");
					
					$("#detailpane")
						.addClass("ui-dialog ui-widget-content ui-corner-all");
					
					$("#contentpane")
						.addClass("ui-dialog ui-widget-content ui-corner-all");
					
					$("#contentpane .minimise-button")
						.click(
							function()
							{
								$("#contentpane").toggleClass("minimised-vertically");
							}
						);
					
					/* Make any hoverable buttons hoverable (used for control
					 * buttons in pane title bars). */
					
					$("#page .needs-ui-state-hover").each(
						function ()
						{
							$(this).hover(
						        function ()
						        {
						            $(this).addClass('ui-state-hover');
						        },
						        function () {
						            $(this).removeClass('ui-state-hover');
						        }
						    );
						}
					);
					
					S.TitlePaneMonitor($("#titlepane"));
					S.IndexPaneMonitor($("#indexpane .content"));
            	}
            );
        }
    };
}
)();
