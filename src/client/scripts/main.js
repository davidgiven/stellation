(function()
{
	"use strict";
	
	window.S = {};
	
	S.CGI = "/cgi-bin/stellationcgi.cgi";

	S.Classes = {};
	
	S.ShowLoading = function()
	{
		$("#loading").show();
	};
	
	S.HideLoading = function()
	{
		$("#loading").hide();
	};
	
	S.main = function()
	{
		S.Terminal.Init();
		S.GSM.Start();
	};
}
)();
