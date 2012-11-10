(function()
{
    "use strict";

    S.Classes.SFleet =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "fleet_summary_tmpl",
        		{
        			details: function (object)
        			{
    					S.GamePage.ChangeDetail(object);
        			}
        		}
        	);
    	},
    	
    	createContentSummary: function (element)
    	{
    		var object = this;
    		var object_change_cb =
    			function (o)
    			{
    				$(element).empty();
    	    		
					$.each(object.Contents,
						function (o)
						{
    						var e = $("<tr/>");
    						$(element).append(e);
    						o.createSummary(e);
						}
					);
    			}
    		
    		S.Monitor(object, element, object_change_cb);
    	}
    };
})();