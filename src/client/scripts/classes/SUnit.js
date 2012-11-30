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
    	
    	fleetName: function ()
    	{
    		var e = $("<span/>");
    		S.TemplatedMonitor(this.Location, e, "fleet.index");
    		return e;
    	},
    	
    	starName: function ()
    	{
    		var e = $("<span/>");
    		S.TemplatedMonitor(this.Location.Location, e, "star.name");
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