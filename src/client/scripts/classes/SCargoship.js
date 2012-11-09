(function()
{
    "use strict";

    S.Classes.SCargoship =
    {	
    	showSummaryDetail: function ()
    	{
    		var e = $("<span/>");
    		S.ExpandTemplate(this, e, "cargo_fragment_tmpl");
    		return e;
    	}
    };
})();