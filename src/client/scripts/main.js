(function()
{
	"use strict";
	
	window.S = {};
	
	S.CGI = "/cgi-bin/stellationcgi.cgi";

	S.Classes = {};
	
	S.main = function()
	{
		S.Terminal.Init();
		S.GSM.Start();
	}
}
)();
