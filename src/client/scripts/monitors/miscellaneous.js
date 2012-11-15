(function()
{
    "use strict";

    S.TitlePaneMonitor =
    	function (e)
    	{
        	S.TemplatedMonitor(S.Player, e, "fragments.title",
        		{
        			logout:
        				function()
        				{
        					S.GSM.Logout();
        				}
        		}
        	);
    	};
})();