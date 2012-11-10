(function()
{
    "use strict";

    var tristate = function (a, b)
    {
		if (a < b)
			return -1;
		if (a > b)
			return 1;
		return 0;
    };
    
    var sort_by_names =	function (a, b)
	{
    	return tristate(a.Name, b.Name);
	};
		
    var sort_by_oids = function (a, b)
	{
    	return tristate(a.Oid, b.Oid);
	};
		
    S.IndexPaneFleetMonitor = function (fleet, element)
	{
		S.TemplatedMonitor(fleet, element, "index_fleet_tmpl",
			{
				clicked: function (object, element)
				{
					S.GamePage.ChangeDetail(object);
				}
			}
		);
	};
	
    S.IndexPaneStarMonitor = function (star, element)
	{
		S.TemplatedMonitor(star, element, "index_star_tmpl",
			{
				_changed:
					function (object, element)
					{
        				var fleets = new S.ObjectSet();
        				$.each(star.Contents,
        					function (o)
        					{
        						if (o.Class === "SFleet")
        							fleets.add(o);
        					}
        				);
        				
        				fleets.orderedEach(sort_by_oids,
        					function (_, fleet)
        					{
        						var e = $("<ul/>");
        						element.append(e);
        						S.IndexPaneFleetMonitor(fleet, e);
        					}
        				);
					}
			}
		);
	};
    	
    S.IndexPaneMonitor = function (e)
	{
    	S.TemplatedMonitor(S.Player, e, "index_overall_tmpl",
    		{
    			_changed:
    				function (object, element)
    				{
    					/* Collect set of visible stars. */
    				
    					var starset = new S.ObjectSet();
    					$.each(object.Fleets,
    						function (f)
    						{
    							if (f.JumpshipCount > 0)
    								starset.add(f.Location);
    						}
    					);
    				
    					/* Create widgetry. */
    					
    					starset.orderedEach(sort_by_names,
    						function (_, star)
    						{
    							var e = $("<ul/>");
    							element.append(e);
    							S.IndexPaneStarMonitor(star, e);
    						}
    					);
    				}
    		}
    	);
	};
})();