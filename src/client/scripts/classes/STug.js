(function()
{
    "use strict";

    var _super = S.Classes.SShip;
    
    S.Classes.STug =
    {	
    	showCargo: function ()
    	{
    		var cargo = this.Contents.first();
    		if (cargo)
    		{
    			if (S.Player.cansee(cargo))
    				return cargo.Name;
    			return "unknown";
    		}
    		return "nothing";
    	},
    	
    	showSummaryDetail: function ()
    	{
    		var e = $("<span/>");
    		S.ExpandTemplate(this, e, "tug.cargo");
    		return e;
    	},
	
    	createDetails: function (element)
    	{
    		_super.createDetails.call(this, element);
    		
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "tug.details",
        		{
        		}
        	);    
    	}
    };
})();