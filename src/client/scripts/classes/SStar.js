(function()
{
    "use strict";

    S.Classes.SStar =
    {
    	showName: function (o)
    	{
    		var e = document.createElement("span");
    		S.ExpandTemplate(o, e, "star_name_tmpl",
    			{
    				map: function (object, element)
					{
						S.GamePage.ChangeMapFocus(object.X, object.Y);
					},
					
					summary: function (object, element)
					{
						S.GamePage.ChangeSummary(object);
					}
    			}
    		);
    		return e;
    	},
    	
    	createSummary: function (element)
    	{
        	S.TemplatedMonitor(this, element, "star_summary_tmpl",
        		{
        			_changed:
        				function (object, element)
        				{
        					var b = parseInt(object.Brightness);
        					var img = $(element).find(".starimage");
        					img.attr("src", "res/star"+b+".png");
        					
        					var t = $(element).find(".star_summary_content");
        					$.each(object.Contents,
        						function (o)
        						{
            						var e = $("<tbody/>");
            						t.append(e);
            						o.createSummary(e);
            						
            						e = $("<tbody/>");
            						t.append(e)
            						o.createContentSummary(e);
        						}
        					);
        				}
        		}
        	);
    	}
    };
})();