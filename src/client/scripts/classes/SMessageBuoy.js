(function()
{
    "use strict";

    var _super = S.Classes.SFixed;
    
    S.Classes.SMessageBuoy =
    {	
    	showSummaryDetail: function ()
    	{
    		var e = $("<span/>");
    		S.ExpandTemplate(this, e, "messagebuoy.summary");
    		return e;
    	},
	
    	showMessageCount: function ()
    	{
    		var count = this.Contents.length();
    		if (count === 1)
    			count += " message";
    		else
    			count += " message";
    		return count;
    	}	
    };
})();