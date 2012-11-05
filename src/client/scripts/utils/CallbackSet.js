(function()
{
	"use strict";
	
	var c = function()
	{
		this._set = [];
	};

	c.prototype.add = function(f)
	{
		this._set.push(f) 
	};
	
	c.prototype.remove = function(f)
	{
		for (var i=0; i<this._set.length; i++)
		{
			if (this._set[i] === f)
			{
				this._set.remove(i);
				break;
			}
		}
	};
	
	c.prototype.call = function(arg)
	{
		/* Take a copy of the set of functions so that if the callbacks
		 * change it, bad things don't happen. */
		var copyset = this._set.slice();
		
		for (var i=0; i<copyset.length; i++)
			copyset[i](arg);
	};
	
	S.CallbackSet = c;
}
)();
