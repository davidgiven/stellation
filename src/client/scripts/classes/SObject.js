(function()
{
    "use strict";

    S.Classes.SObject =
    {
    	showName: function (o)
    	{
    		return "#"+o.Oid;
    	},
    	
    	createSummary: function (element)
    	{
    		$(element).text("summary pane for "+this.Oid);
    	}
    };
})();