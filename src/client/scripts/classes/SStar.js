(function()
{
    "use strict";

    var tristate = function (x, y)
    {
    	if (x < y)
    		return -1;
    	if (x > y)
    		return 1;
    	return 0;
    }
    
    var unit_sort_cb = function (o1, o2)
    {
    	var c1 = o1.Class;
    	var c2 = o2.Class;
    	if ((c1 === "SFleet") && (c2 !== "SFleet"))
    		return 1;
    	if ((c1 !== "SFleet") && (c2 === "SFleet"))
    		return -1;

    	var i = tristate(o1.Class, o2.Class);
    	if (i)
    		return i;
    	
    	i = tristate(o1.Owner.Oid, o2.Owner.Oid);
    	if (i)
    		return i;
    	
    	i = tristate(o1.Name, o2.Name);
    	return i;
    };
    
    S.Classes.SStar =
    {
    	showName: function (o)
    	{
    		var e = document.createElement("span");
    		S.ExpandTemplate(o, e, "star.name",
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
        	S.TemplatedMonitor(this, element, "star.summary",
        		{
        			_changed:
        				function (object, element)
        				{
        					var b = parseInt(object.Brightness);
        					var img = $(element).find(".starimage");
        					img.attr("src", "res/star"+b+".png");
        					
        					var t = $(element).find(".star_summary_content");
        					object.Contents.orderedEach(
        						unit_sort_cb,
        						function (_, o)
        						{
            						var e = $("<tbody/>");
            						t.append(e);
            						o.createSummary(e);
            						
            						e = $("<tbody/>");
            						t.append(e)
            						o.createContentSummary(e);
        						}
        					);
        					
        					var e = $(element).find(".star_summary_debris");
        					if (object.Debris > 0)
        						S.ExpandTemplate(object, e, "star.debris");
        					else
        						e.empty();
        				}
        		}
        	);
    	}
    };
})();