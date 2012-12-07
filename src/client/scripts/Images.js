(function()
{
    "use strict";
    
    S.Images = {};
    
    $("IMG").each(
    	function (_, e)
    	{
    		var id = e.id;
    		if (id)
    			S.Images[id] = e;
    	}
	);
})();
