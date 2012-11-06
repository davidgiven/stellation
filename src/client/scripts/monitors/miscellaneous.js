(function()
{
    "use strict";

    S.TitlePaneMonitor =
    	function (e)
    	{
        	S.TemplatedMonitor(S.Player, e, "title_tmpl",
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