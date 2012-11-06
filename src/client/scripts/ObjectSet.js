(function()
{
    "use strict";

    S.ObjectSet = function()
    {
    	this.data = {};
    	for (var i=0; i<arguments.length; i++)
    	{
    		var o = arguments[i];
    		this.add(o);
    	}
    };
    
    S.ObjectSet.prototype.each = function(f)
    {
    	for (var oid in this.data)
    	{
    		var o = this.data[oid];
    		var r = f(o);
    		if (r === true)
    			break;
    	}
    };
    
    S.ObjectSet.prototype.add = function(o)
    {
    	this.data[o.Oid] = o;
    };
    
    S.ObjectSet.prototype.remove = function(o)
    {
    	delete this.data[o.Oid];
    };
    
    S.ObjectSet.prototype.clear = function()
    {
    	this.data = {};
    };
    
    S.ObjectSet.prototype.contains = function(o)
    {
    	return !!this.data[o.Oid];
    };
    
    var originalEach = $.each;
    $.each = function(o, f)
    {
    	if (o instanceof S.ObjectSet)
    		return o.each(f);
    	return originalEach(o, f);
    };
    
})();
