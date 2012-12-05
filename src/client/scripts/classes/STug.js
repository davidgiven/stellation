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
    	
    	showCargoMass: function ()
    	{
    		var cargo = this.Contents.first();
    		if (cargo && S.Player.cansee(cargo))
    			return cargo.Mass;
   			return "n/a";
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
    		
    		var events =
    		{
    			_changed: function (object, element)
				{
					var controls = $(element).find(".tugcontrols");
					var cargo = object.Contents.first();
					if (cargo)
					{
						S.TemplatedMonitor(object, controls,
							"tug.controls_loaded",
							{
							}
						);
					}
					else
					{
						S.TemplatedMonitor(object, controls,
							"tug.controls_unloaded",
							{
								load: events.load
							}
						);
					}
				},
    				
    			load: function (object, e, o)
    			{
    				console.log("oid="+o.Oid);
    			}
    		};

        	S.TemplatedMonitor(object, e, "tug.details", events);
    	}
    };
})();