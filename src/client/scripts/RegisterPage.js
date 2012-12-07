(function()
{
	"use strict";

	var fail = function (message)
	{
		console.log(message);
	};
	
    var attempt_registration_cb = function()
    {
    	var playername = $("#playername").prop("value");
    	var empirename = $("#empirename").prop("value");
        var email = $("#email").prop("value");
        var password = $("#password").prop("value");
        var password2 = $("#password2").prop("value");
        
        if (!playername)
        	return fail("You must specify a valid player name.");
        if (!empirename)
        	return fail("You must specify a valid empire name.");
        if (!email)
        	return fail("You must specify a valid email address.");
        if (!password)
        	return fail("An empty password —-- really?");
        if (password !== password2)
        	return fail("Your passwords don't match.");
        
        S.Commands.CreatePlayer(
            {
                email: email,
                password: password,
                name: playername,
                empire: empirename
            },
            function (msg)
            {
            	if (msg.result == "OK")
            	{
       				S.GSM.Login();
            	}
            }
        );
    };
    
    S.RegisterPage =
    {
        Show: function ()
        {
            $("#page").load("register.html",
            	function ()
            	{
            		S.Markup($("#page"));
            		
                    $("#playername").keydown(
                        function (event)
                        {
                            if (event.keyCode === 13)
        	                    $("#empirename").focus();
                        }
                    );

                    $("#empirename").keydown(
                        function (event)
                        {
                            if (event.keyCode === 13)
        	                    $("#email").focus();
                        }
                    );

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
                                $("#password2").focus();
                        }
                    );
                            
                    $("#password2").keydown(
                        function (event)
                        {
                            if (event.keyCode === 13)
                                attempt_registration_cb();
                        }
                    );
                            
                    $("#cancel").button()
                        .click(
                            function (event)
                            {
                            	S.GSM.Login();
                            }
                        );
                        
                    $("#register").button()
                        .click(attempt_registration_cb);
            	}
            );
        }
    };
}
)();
