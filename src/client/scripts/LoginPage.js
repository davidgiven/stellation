(function()
{
	"use strict";

    var attempt_login_cb = function()
    {
        $("#page input").each(
            function (a, b)
            {
                b.disabled = true;
            }
        );
        
        var email = $("#email").prop("value");
        var password = $("#password").prop("value");
        
        S.Commands.Authenticate(
            {
                email: email,
                password: password
            },
            function (msg)
            {
                $("#page input").each(
                        function (a, b)
                        {
                            b.enabled = true;
                        }
                    );
                    
            	if (msg.result == "OK")
            	{
            		S.IO.SetCookie(msg.cookie);
            		S.Commands.Ping({},
            			function (msg)
            			{
            				console.log("sync done");
            				S.GSM.Game();
            			}
            		);
            	}
            }
        );
    };
    
    S.LoginPage =
    {
        Show: function ()
        {
            $("#page").load("login.html",
            	function ()
            	{
            		S.Markup($("#page"));
            		
                    $("#email").keydown(
                        function (event)
                        {
                            if (event.keyCode === 13)
        	                    $("#password").focus();
                        }
                    );
                    
                    $("#password").keydown(
                        function (event)
                        {
                            if (event.keyCode === 13)
                                attempt_login_cb();
                        }
                    );
                            
                    $("#register").button()
                        .click(
                            function (event)
                            {
                            	S.GSM.Register();
                            }
                        );
                        
                    $("#login").button()
                        .click(attempt_login_cb);
            	}
            );
        }
    };
}
)();
