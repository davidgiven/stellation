(function()
{
    "use strict";

    var _super = S.Classes.SUnit;
    
    S.Classes.SShip =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "ship.summary",
        		{
        			details: function (object, element)
        			{
    					S.GamePage.ChangeDetail(object);
        			}
        		}
        	);
    	},
    	
    	createDetails: function (element)
    	{
    		_super.createDetails.call(this, element);
    	}
    };
})();