(function()
{
	"use strict";
	
	G.GSM =
	{
        Start: function ()
        {
        	console.log("state machine init");
			Database.Reset();
			IO.GetStatics(
				function()
				{
					GamePage.Preload(
						function()
						{
							GSM.Login();
						}
					);
				}
			);
        },
        
        Login: function ()
        {
            LoginPage.Show();
        },
        
        Logout: function ()
        {
        	Database.Reset();
        	GSM.Login();
        },
        
        Register: function ()
        {
            console.log("register");
        },

		Game: function ()
		{
			GamePage.Show();
		},
	};
}
)();
