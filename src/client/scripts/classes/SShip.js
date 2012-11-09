(function()
{
    "use strict";

    S.Classes.SShip =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "ship_summary_tmpl",
        		{
        		}
        	);
    	}
    };
})();