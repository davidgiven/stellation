rem metaplayer.moo
rem Metaplayer. The metaplayer handles the non-user-specific bits of the web
rem interface.
.patch metaplayer.moo 5 1
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

