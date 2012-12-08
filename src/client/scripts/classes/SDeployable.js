(function()
{
    "use strict";

    var _super = S.Classes.SFixed;
    
    S.Classes.SDeployable =
    {
    	showDeployed: function (o)
    	{
    		return o.Deployed ? "deployed" : "mothballed";
    	},
    	
    	createDetails: function (element)
    	{
    		_super.createDetails.call(this, element);
    		
    		var object = this;
    		
    		var e = $("<tbody/>");
    		$(element).append(e);
        	S.TemplatedMonitor(object, e, "deployable.details",
        		{
        			_changed: function (object, element)
        			{
        				var e = $(element).find(".deployableControls");
        				if (object.Deployed)
        				{
        					S.ExpandTemplate(object, e, "deployable.whendeployed",
        						{
            	        			mothball: function (object, element)
            	        			{
            	        				S.Commands.DeployableMothball(
            	        					{
            	        						oid: object.Oid
            	        					}
            	        				);
            	        			},
        						}
        					);
        				}
        				else
        				{
        					S.ExpandTemplate(object, e, "deployable.whenmothballed",
        						{
            	        			deploy: function (object, element)
            	        			{
            	        				S.Commands.DeployableDeploy(
            	        					{
            	        						oid: object.Oid
            	        					}
            	        				);
            	        			},
        						}
        					);
        				}
        			}
        		}
        	);
    	}
    	
    };
})();