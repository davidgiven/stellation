(function()
{
    "use strict";

    S.Classes.SFleet =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "fleet_summary_tmpl",
        		{
        			_changed: function (object, element)
        			{
    					var t = $(element).find(".fleet_content");
    					$.each(object.Contents,
    						function (o)
    						{
        						var e = $("<tr/>");
        						t.append(e);
        						o.createSummary(e);
    						}
    					);
        			}
        		}
        	);
    	}
    };
})();