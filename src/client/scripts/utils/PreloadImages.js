(function()
{
	"use strict";
	
	G.PreloadImages = function(uris, cb)
	{
	    var count = uris.length;
	    $(
	    	uris.map(
	    		function(f)
	    		{
	    			return '<img src="'+f+'" />';
	    		}
	    	).join('')
	    ).load(
	    	function()
	    	{
	    		count--;
	    		if (!count)
	    			cb();
	        }
	    );
	}
}
)();
