(function()
{
	"use strict";

    var attempt_login_cb = function()
    {
        $("input").each(
            function (a, b)
            {
                b.disabled = true;
            }
        );
        
        var email = $("#email").prop("value");
        var password = $("#password").prop("value");
        
        IO.RPC(
            {
                cmd: "Authenticate",
                email: email,
                password: password
            },
            function (msg)
            {
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
