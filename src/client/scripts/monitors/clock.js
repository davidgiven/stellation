(function()
{
    "use strict";

    S.TimeDelta = 0;
    
    var gettime = function()
    {
    	return (new Date().getTime()) / 1000;
    };
    
    var update = function(element)
    {
		var now = S.TimeDelta + gettime(); // seconds
		now = now / 3600; // to hours
		
		var thou = parseInt(now / 1000);
		var hours = parseInt(now) % 1000;
		var milli = parseInt(now * 1000) % 1000;
		
		var s = "";
		
		if (thou < 100)
			s += "0";
		if (thou < 10)
			s += "0";
		s += thou;
		s += ".";
		
		if (hours < 100)
			s += "0";
		if (hours < 10)
			s += "0";
		s += hours;
		s += ".";
		
		if (milli < 100)
			s += "0";
		if (milli < 10)
			s += "0";
		s += milli;
		
		element.find(".time").text(s);
    }
    
    S.SetServerTime = function(t)
    {
    	S.TimeDelta = t - gettime();
    }
    
    S.ClockMonitor = function(element)
    {
    	update(element);
    	window.setInterval(
    		function()
    		{
    			update(element);
    		},
    		3600 // thousandth of an hour
    	);
    };
})();
