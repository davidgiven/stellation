(function()
{
    "use strict";

    S.Classes.SUnit =
    {	
    	useOtherTemplates: function ()
    	{
    		return true;
    	},
    
    	showSummaryDetail: function ()
    	{
    		return "";
    	},
    	    	
    	createContentSummary: function (element)
    	{
    	},

    	fleetName: function ()
    	{
    		var e = $("<span/>");
    		S.TemplatedMonitor(this.Location, e, "fleet.index");
    		return e;
    	},
    	
    	findStar: function ()
    	{
    		var loc = this;
    		while (loc && (loc.Class !== "SStar"))
    			loc = loc.Location;
    		return loc;
    	},
    	
    	starName: function ()
    	{
    		var e = $("<span/>");
    		S.TemplatedMonitor(this.findStar(), e, "star.name");
    		return e;
    	},
    	
    	createDetails: function (element)
    	{
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "unit.details",
        		{
        			unitscrap: function()
        			{
        				S.Commands.UnitScrap(
        					{
        						oid: object.Oid
        					}
        				);
        			}
        		}
        	);
    	}
    };
})();