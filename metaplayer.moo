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

.program $god $metaplayer:http_index tnt
	{c, method, param} = args;
	this:toplevel(c, {"notloggedin", ""}, {"index", ""});
.

.program $metaplayer $metaplayer:http_notloggedin_single tnt
	{c, method, path} = args;
	this:htmlheader(c, method, "Not Logged In");
	$http_server:anchor(c, "Create new player",
		"/metaplayer/createplayer");
	$htell(c, "<BR>");
	$http_server:anchor(c, "Login",
		"/player");
	this:htmlfooter(c, method);
.

# --- Create a new player ---------------------------------------------------

.program $god $metaplayer:http_createplayer tnt
	{c, method, param} = args;
	this:toplevel(c, {"createplayer", ""}, {"index", ""});
.

.program $metaplayer $metaplayer:http_createplayer_single tnt
	{c, method, parameters} = args;
	this:htmlheader(c, method, "Create new player");
	if (length(players()) >= $server_options.maxplayers)
		$htell(c, "Sorry! The game is full. It's currently configured to accept");
		$htell(c, tostr($server_options.maxplayers));
		$htell(c, "players only. I'm afraid you'll have to wait until one of the players commits suicide, gets killed, or the server limit is raised. If you <I>really</I> want in, and can give justification, contact <A HREF=\"mailto:dg@tao-group.com\">the author</A> and he'll think about it.");
	endif
	if (length(parameters[1]) == 0)
		$htell(c, "<FORM ACTION=\"/metaplayer/createplayer\">");
		$htell(c, "Name of player:<BR>");
		$htell(c, "<INPUT NAME=\"name\" SIZE=20><BR>");
		$htell(c, "Password:<BR>");
		$htell(c, "<INPUT NAME=\"password\" SIZE=8>");
		$htell(c, "<BR><INPUT TYPE=submit></FORM>");
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
			$http_server:redirect(c, "/player");
		else
			$htell(c, result[1]);
			$htell(c, "<P>");
			$http_server:anchor(c, "Try again.",
				"/metaplayer/createplayer");
		endif
	endif
	this:htmlfooter(c, method);
.

.quit

rem Revision History
rem $Log: metaplayer.moo,v $
rem Revision 1.4  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
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

