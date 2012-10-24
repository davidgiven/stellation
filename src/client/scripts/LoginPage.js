"use strict";

define(
	["jquery", "G", "IO", "GameStateMachine", "text!login.html"],
	function ($, G, IO, GSM, login_fragment)
	{
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
	    
	    return {
	        Show: function ()
	        {
	            $("#page").replaceWith(login_fragment);
	            
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
	    };
	}
);

