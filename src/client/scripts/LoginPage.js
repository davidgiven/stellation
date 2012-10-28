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
        
        Commands.Authenticate(
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
            		IO.SetCookie(msg.cookie);
            		Commands.Ping({},
            			function (msg)
            			{
            				console.log("sync done");
            				GSM.Game();
            			}
            		);
            	}
            }
        );
    };
    
    G.LoginPage =
    {
        Show: function ()
        {
            $("#page").load("login.html",
            	function ()
            	{
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
                            	GSM.Register();
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
