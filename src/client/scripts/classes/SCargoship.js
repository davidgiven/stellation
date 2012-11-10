(function()
{
    "use strict";

    S.Classes.SCargoship =
    {	
    	showSummaryDetail: function ()
    	{
    		var e = $("<span/>");
    		S.ExpandTemplate(this, e, "cargo_fragment_tmpl");
    		return e;
    	},
	
    	createDetails: function (element)
    	{
    		S.Classes.SShip.createDetails.call(this, element);
    		
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "cargoship_details_tmpl",
        		{
        			_changed: function (object, element)
        			{
        				var star = object.Location.Location;
        				var e = $(element).find(".available");
        				
        				S.TemplatedMonitor(star, e, "resources_fragment_tmpl");
        			}
        		}
        	);
    
    	}
    };
})();