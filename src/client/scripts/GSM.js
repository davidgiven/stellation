(function()
{
	"use strict";
	
	S.GSM =
	{
        Start: function ()
        {
        	console.log("state machine init");
			S.Database.Reset();
			S.IO.GetStatics(
				function()
				{
					S.HideLoading();
					S.GSM.Login();
				}
			);
        },

        Error: function (e)
        {
        	console.log("Error: "+e);
        },
        
        Login: function ()
        {
            S.LoginPage.Show();
        },
        
        Logout: function ()
        {
        	S.Database.Reset();
        	S.GSM.Login();
        },
        
        Register: function ()
        {
            S.RegisterPage.Show();
        },

		Game: function ()
		{
			S.GamePage.Show();
		},
	};
}
)();
