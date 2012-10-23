"use strict";

define(
	["jquery", "IO", "Database"],
	function ($, IO, Database)
	{
		return {
			main: function()
			{
				Database.Reset();
				IO.GetStatics(
					function ()
					{
        				IO.RPC(
        					{
        						cmd: "Authenticate",
        						email: "test@invalid.com",
        						password: "password"
        					},
        					function (msg)
        					{
        						if (msg.result === "OK")
        						{
        							IO.SetCookie(msg.cookie);
        							IO.GameCommand({});
        						}
        					}
        				);
					}
				);
			}
		};
	}
);

