(function()
{
    "use strict";

    S.Classes.SObject =
    {
    	showName: function (o)
    	{
    		return "#"+o.Oid;
    	},
    	
    	showOwner: function (o)
    	{
    		var owner = o.Owner;
    		if (S.Player.cansee(owner))
    			return owner.Name;
    		return "unknown";
    	},
    	
    	createSummary: function (element)
    	{
    		$(element).text("summary pane for "+this.Oid);
    	}
    };
})();