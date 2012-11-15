(function()
{
    "use strict";

    S.Classes.SPlayer =
    {
    	cansee: function(o)
    	{
    		return this.VisibleObjects.contains(o);
    	}
    };
})();