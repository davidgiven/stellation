(function()
{
	"use strict";
	
	window.G = window;
	
	G.CGI = "/cgi-bin/stellationcgi.cgi";

	G.main = function()
	{
		Terminal.Init();
		GSM.Start();
	}
}
)();
