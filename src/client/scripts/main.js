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
	
	S.Dialogue = function(message)
	{
		var d = $("#dialogue").clone();
		d.insertAfter($("#dialogue"));
		d.find(".dialoguetext").text(message);
		
		d.dialog(
			{
				modal: true,
				buttons: {
					Ok: function()
    				{
    					$(this).dialog("close");
    					d.remove();
    				}
				}
			}
		);
	};
	
	S.main = function()
	{
		S.Terminal.Init();
		S.GSM.Start();
	};
}
)();
