(function()
{
    "use strict";

    S.Classes.SUnit =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "unit_summary_tmpl",
        		{
        			details: function (object, element)
        			{
        			}
        		}
        	);
    	},
    	
    	showSummaryDetail: function ()
    	{
    		return "";
    	}
    };
})();