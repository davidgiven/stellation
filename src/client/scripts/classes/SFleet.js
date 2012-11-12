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
    			var tx = object.TargetX;
    			var ty = object.TargetY;
    			
    			$(element).find(".current").text(
    				cx + ", " + cy
    			);
    			
    			var dx = cx - tx;
    			var dy = cy - ty;
    			var distance = Math.sqrt(dx*dx + dy*dy);
    			var time = distance;
    			var antimatter = distance * object.Mass;
    			
    			$(element).find(".distance").text(distance.toFixed(1));
    			$(element).find(".time").text(time.toFixed(3));
    			$(element).find(".antimatter").text(antimatter.toFixed());
    		};
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "fleet.details",
        		{
        			_changed: function (object, element)
        			{
        				update();
        			},
        			
        			current_map: function (object, element)
        			{
						S.GamePage.ChangeMapFocus(object.Location.X, object.Location.Y);
        			},
        			
        			target_map: function (object, element)
        			{
						S.GamePage.ChangeMapFocus(object.TargetX, object.TargetY);
        			},
        			
        			copy_from_map: function (object, element)
        			{
        				var xy = S.GamePage.GetMapTarget();
        				S.Commands.ChangeFleetTarget(
        					{
        						oid: object.Oid,
        						x: xy.x,
        						y: xy.y
        					}
        				);
        			},
        			
        			jump: function (object, element)
        			{
        				S.Commands.Jump(
        					{
        						oid: object.Oid
        					}
        				);
        			}
        		}
        	);
    	},
    };
})();