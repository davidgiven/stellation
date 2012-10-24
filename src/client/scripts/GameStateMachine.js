"use strict";

define(
	["jquery", "G", "Database", "IO", "LoginPage"],
	function ($, G, Database, IO, LoginPage)
	{
	    var GSM =
	    {
	        Start: function ()
	        {
				Database.Reset();
				IO.GetStatics(GSM.Login);
	        },
	        
	        Login: function ()
	        {
	            LoginPage.Show();
	        }
	    };
	    
	    return GSM;
	}
);

