(function()
{
    "use strict";

    S.Classes.SObject =
    {
    	isPlayerOwned: function ()
    	{
    		return (this.Owner === S.Player);
    	},
    	
    	useOtherTemplates: function ()
    	{
    		return false;
    	},
    
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