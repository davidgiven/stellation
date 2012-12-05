(function()
{
    "use strict";

    var _super = S.Classes.SUnit;
    
    S.Classes.SFixed =
    {	
    	createDetails: function (element)
    	{
    		_super.createDetails.call(this, element);
    	},
    	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "fixed.summary",
        		{
        			details: function (object, element)
        			{
    					S.GamePage.ChangeDetail(object);
        			}
        		}
        	);
    	},
    };
})();