rem metaplayer.moo
rem Metaplayer. The metaplayer handles the non-user-specific bits of the web
rem interface.
rem $Source: /cvsroot/stellation/stellation/metaplayer.moo,v $
rem $State: Exp $

.patch metaplayer.moo 7 1
notify(player, "metaplayer.moo");

$god:prop(#0, "metaplayer", create($player, $god));
$metaplayer.name = "Metaplayer";

# --- Generic player properties -----------------------------------------------

$god:prop($metaplayer, "password", crypt(""));

# --- Main HTML index ---------------------------------------------------------

.program $metaplayer $metaplayer:http_index tnt
	{c, method, path} = args;
	$http_server:htmlheader(c, 200, "Stellation");
	$http_server:anchor(c, "Create new player",
		"/metaplayer/createplayer");
	$htell(c, "<P>");
	$http_server:anchor(c, "Login",
		"/player");
	$http_server:htmlfooter(c);
.

# --- Create a new player ---------------------------------------------------

.program $metaplayer $metaplayer:http_createplayer tnt
	{c, method, parameters} = args;
	$http_server:htmlheader(c, 200, "Create new player");
	if (length(players()) >= $server_options.maxplayers)
		$htell(c, "Sorry! The game is full. It's currently configured to accept");
		$htell(c, tostr($server_options.maxplayers));
		$htell(c, "players only. I'm afraid you'll have to wait until one of the players commits suicide, gets killed, or the server limit is raised. If you <I>really</I> want in, and can give justification, contact <A HREF=\"mailto:dg@tao-group.com\">the author</A> and he'll think about it.");
		$http_server:htmlfooter(c);
		return;
	endif
	if (length(parameters[1]) == 0)
		$htell(c, "<FORM ACTION=\"/metaplayer/createplayer\">");
		$htell(c, "Name of player:");
		$htell(c, "<INPUT NAME=\"name\" SIZE=20><BR>");
		$htell(c, "Password:");
		$htell(c, "<INPUT NAME=\"password\" SIZE=8><BR>");
		$htell(c, "<INPUT TYPE=submit></FORM>");
	elseif (length(parameters[1]) == 2)
		name = "name" in parameters[1];
		if (!name)
			$http_server:syntax();
			return;
		endif
		name = parameters[2][name];
		password = "password" in parameters[1];
		if (!password)
			$http_server:syntax();
			return;
		endif
		password = parameters[2][password];
		result = $player:create_player(name, password, "fnord", "fnord", "fnord");
		if (result[1] == "")
			$htell(c, "Success!<P>");
			$http_server:anchor(c, "Now log in.",
				"/player");
		else
			$htell(c, result[1]);
			$htell(c, "<P>");
			$http_server:anchor(c, "Try again.",
				"/metaplayer/createplayer");
		endif
	endif
	$http_server:htmlfooter(c);
.

.quit

rem Revision History
rem $Log: metaplayer.moo,v $
rem Revision 1.3  2000/07/31 18:07:42  dtrg
rem Map no longer displays deep space objects (which means it works with
rem Netscape again).
rem Added maximum number of players feature.
rem A few formatting fixes.
rem
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

