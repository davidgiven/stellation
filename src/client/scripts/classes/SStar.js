(function()
{
    "use strict";

    S.Classes.SStar =
    {
    	showName: function (o)
    	{
    		var e = document.createElement("span");
    		S.ExpandTemplate(o, e, "star_name_tmpl");
    		return e;
    	},
    };
})();