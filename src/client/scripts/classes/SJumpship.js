(function()
{
    "use strict";

    S.Classes.SJumpship =
    {	
    	createDetails: function (element)
    	{
    		S.Classes.SShip.createDetails.call(this, element);
    		
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "jumpship_details_tmpl",
        		{
        			fleet: function (object, element)
        			{
        				S.GamePage.ChangeDetail(object.Location);
        			}
        		}
        	);
    
    	}
    };
})();