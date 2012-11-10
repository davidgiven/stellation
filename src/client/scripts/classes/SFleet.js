(function()
{
    "use strict";

    S.Classes.SFleet =
    {	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "fleet.summary",
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
    	},
    	
    	createDetails: function (element)
    	{
    		var object = this;
    		
    		var update = function()
    		{
    			var cx = object.Location.X;
    			var cy = object.Location.Y;
    			
    			$(element).find(".current").text(
    				cx + ", " + cy
    			);
    			
    			var distance = 0;
    			var time = 0;
    			var antimatter = 0;
    			
    			$(element).find(".distance").text(distance);
    			$(element).find(".time").text(time);
    			$(element).find(".antimatter").text(antimatter);
    		};
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "fleet.details",
        		{
        			_changed: function (object, element)
        			{
        				update();
        			}
        		}
        	);
    	},
    };
})();