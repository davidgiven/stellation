(function()
{
	"use strict";
	
	window.G = window;
	
	G.CGI = "//localhost/cgi-bin/stellationcgi.cgi";

	G.main = function()
	{
		Terminal.Init();
		GSM.Start();
	}
}
)();
