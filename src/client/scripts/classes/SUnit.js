(function()
{
    "use strict";

    S.Classes.SUnit =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "unit.summary",
        		{
        			details: function (object, element)
        			{
    					S.GamePage.ChangeDetail(object);
        			}
        		}
        	);
    	},
    	
    	showSummaryDetail: function ()
    	{
    		return "";
    	},
    	
    	createDetails: function (element)
    	{
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "unit.details",
        		{
        		}
        	);

    	}
    };
})();