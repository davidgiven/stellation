(function()
{
	"use strict";

    G.GamePage =
    {
        Show: function ()
        {
            $("#page").load("game.html",
            	function ()
            	{
					var map = L.map('map',
						{
							center: [0, 0],
							zoom: 0
						}
					);

					L.tileLayer("map/{z}/{x}_{y}.jpg",
						{
							attribution: "Stellation 4 map",
							maxZoom: 6,
							tileSize: 250,
							noWrap: true
						}
					).addTo(map);
            	}
            );
        }
    };
}
)();
