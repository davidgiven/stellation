rem http_server.moo
rem HTTP server front end.
.patch http_server.moo 4 1
notify(player, "http_server.moo");

$god:prop(#0, "http_server", create(#-1, $god));
$http_server.name = "HTTP Server";

# --- HTTP server properties --------------------------------------------------

$god:prop($http_server, "canon", {});
$god:prop($http_server, "dtd", "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">");

# --- Start and stop HTTP server ----------------------------------------------

.program $god $http_server:start tnt
	if (player == #-1)
		this.canon = {};
	endif
	if (this.canon != {})
		return E_QUOTA;
	endif
	this.canon = listen(this, $server_options.http_port, 0);
.

.program $god $http_server:stop tnt
	if (this.canon == {})
		return E_QUOTA;
	endif

	unlisten(this.canon);
	this.canon = {};
.

# --- Accept a connection -----------------------------------------------------

# This verb is called when a new client wants to talk to us. It will get
# passed parameters such as:
#
# GET / HTML/1.0
# or
# GET /
#
# (We may get a blank line as the very first thing, in which case we ignore
# it.)
#
# In the second instance, that is all the command. In the first, a list of
# options will follow, terminated by a blank line. See
# $http_server:read_options() for more information.

.program $god $http_server:do_login_command tnt
	c = player;
	try
		notify($god, toliteral(args));
		{?command="", ?path="", ?options={}} = args;
		if (command == "")
			return;
		endif
		if (options != {})
			options = this:read_options(options);
		endif
		"First thing is to check the command.";
		if (command != "GET")
			this:htmlheader(c, 405, "Method not allowed");
			notify(c, "Only GET methods are currently supported.");
			this:htmlfooter(c);
			boot_player(c);
			return;
		endif
		"Next, check the path.";
		if (path[1] == "/")
			path = path[2..$];
		endif
		if (path == "")
			path = "metaplayer";
		endif
		{base, path} = $stringutils:firstword(path, "/");
		if (base == "player")
			playerobj = this:authenticate(options);
		else
			playerobj = $metaplayer;
		endif
		if (playerobj == 0)
			boot_player(c);
			return;
		endif
		player = playerobj;
		player:httprequest(c, command, path);
		boot_player(c);
	except v (ANY)
		$traceback(v, c);
		boot_player(c);
	endtry
.
		
# --- Authenticate a player -------------------------------------------------

# Get the remote browser to ask for the player's username and password, and
# try to authenticate them.

.program $god $http_server:authenticate tnt
	{options} = args;
	"We need authentication.";
	if ((length(options) != 2) || !("Authorization" in options[1]))
		this:failedauth(player, "Your browser's not sending me any authentication information!");
		return;
	endif
	"...and check it.";
	hash = options[2]["Authorization" in options[1]];
	{authtype, hash} = $stringutils:firstword(hash, " ");
	if (authtype != "Basic")
		this:failedauth(player, "You tried to use the "+authtype+"authentication mechanism, but I only understand Basic. Your browser needs fixing.");
		return;
	endif
	{username, password} = $stringutils:firstword($stringutils:frombase64(hash), ":");
	userobj = $player:find_player(username);
	if (userobj == $failed_match)
		this:failedauth(player, "The player \""+username+"\" does not exist.");
		return;
	endif
	"Did the user enter the right password?";
	if (crypt(password, userobj.password) != userobj.password)
		this:failedauth(player, "You entered an incorrect password.");
		return;
	endif
	return userobj;
.

# --- Read in options list --------------------------------------------------

# Read in a set of HTTP options. These will be in the mail-header form.

.program $god $http_server:read_options tnt
	{htmltype} = args;
	options1 = {"HTML"};
	options2 = {htmltype};
	while ((line = read()) != "")
		{o1, o2} = $stringutils:firstword(line, ":");
		options1 = {@options1, o1};
		options2 = {@options2, $stringutils:strip(o2)};
	endwhile
	return {options1, options2};
.

# --- Display a page header and footer --------------------------------------

# Displays all the cruft to go at the top of an HTML document.

.program $god $http_server:htmlheader tnt
	{c, response, title, ?frameset = 0} = args;
	notify(c, "HTTP/1.1 "+tostr(response));
	notify(c, "Server: "+$server_options.http_servername+"/"+$server_options.http_serverversion);
	if (c != $metaplayer)
		notify(c, "WWW-Authenticate: Basic realm=\""+$server_options.http_authenticationrealm+"\"");
	endif
	notify(c, "Content-Type: text/html");
	notify(c, "Connection: close");
	notify(c, "");
	notify(c, this.dtd);
	notify(c, "<HTML><HEAD>");
	notify(c, "<TITLE>");
	if (response != 200)
		notify(c, tostr(response));
	endif
	notify(c, title);
	notify(c, "</TITLE></HEAD>");
	if (!frameset)
		notify(c, "<BODY><H1>");
		if (response != 200)
			notify(c, tostr(response));
		endif
		notify(c, title);
		notify(c, "</H2>");
	endif
.

# ...and at the bottom.

.program $god $http_server:htmlfooter tnt
	{c, ?frameset = 0} = args;
	if (!frameset)
		if (0)
			notify(c, "<P><HR><I>");
			notify(c, tostr($server_options.fg_ticks - ticks_left()));
			notify(c, "ticks used;");
			notify(c, tostr($server_options.fg_seconds - seconds_left()));
			notify(c, "seconds");
		endif
		notify(c, "</BODY>");
	endif
	notify(c, "</HTML>");
.

# --- Failed authorization --------------------------------------------------

# Displays a generic failed-authorization page.

.program $god $http_server:failedauth tnt
	{c, reason} = args;
	this:htmlheader(c, 401, "Authorization Failed");
	notify(c, "I couldn't let you in!");
	notify(c, reason);
	this:htmlfooter(c);
.

# --- Syntax error ----------------------------------------------------------

.program $god $http_server:formsyntax tnt
	raise(E_INVARG, "Invalid arguments to form");
.

# --- Emit an anchor --------------------------------------------------------

# Utility method that emits an anchor.

.program $god $http_server:anchor tnt
	{c, name, target} = args;
	notify(c, "<A HREF=\""+target+"\">"+name+"</A>");
.

# --- Emit any text ---------------------------------------------------------

# Players can't notify their HTTP channel, so we have to wrap the accesses
# up here.

.program $god $http_server:tell tnt
	{c, @args} = args;
	line = "";
	for i in (args)
		line = line + tostr(i);
	endfor
	notify(c, line);
.

# ...and a shortcut to it.

.program $god #0:htell tnt
	$http_server:tell(@args);
.

# --- Parse a set of form parameters ----------------------------------------

# Forms return their arguments as a string like:
#
#   name=foo&password=bar
#
# This routine converts the above into:
#
#   {{"name", "password"}, {"foo", "bar"}}

.program $god $http_server:s2p tnt
	{string} = args;
	if (typeof(string) == LIST)
		return string;
	endif
	o1 = {};
	o2 = {};
	while (string != "")
		{item, string} = $stringutils:firstword(string, "&");
		{key, value} = $stringutils:firstword(item, "=");
		value = strsub(value, "+", " ");
		value1 = decode_binary(strsub(value, "%", "~"));
		value = {};
		for i in (value1)
			if (typeof(i) == STR)
				value = {@value, i};
			endif
		endfor
		if (length(value) == 1)
			value = value[1];
		elseif (length(value) == 0)
			value = "";
		endif
		o1 = {@o1, key};
		o2 = {@o2, value};
	endwhile
	return {o1, o2};
.

# ...and convert backwards.

.program $god $http_server:p2s tnt
	{param} = args;
	if (typeof(param) == STR)
		return param;
	endif
	{key, value} = param;
	string = "";
	for i in [1..(length(key))]
		string = string + "&" + key[i] + "=" + value[i];
	endfor
	return string[2..$];
.

# --- Emit a menu -----------------------------------------------------------

.program $god $http_server:menu tnt
	{c, title, @parameters} = args;
	if (title != "")
		notify(c, title+":");
	endif
	for i in (parameters)
		notify(c, "[<A HREF=\""+i[2]+"\">"+i[1]+"</A>]");
	endfor
.

# --- Emit a redirection ----------------------------------------------------

.program $god $http_server:redirect tnt
	{c, url, ?target=""} = args;
	if (target == "")
		notify(c, "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL="+url+"\">");
	else
		notify(c, "<LINK REL=refresh HREF=\""+url+"\" TARGET=\""+target+"\">");
		/* notify(c, "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL="+url+" TARGET="+target+"\">"); */
	endif
.

# --- Emit an error ---------------------------------------------------------

.program $god $http_server:error tnt
	{c, msg, url} = args;
	notify(c, msg);
	notify(c, "<P>");
	this:anchor(c, "Continue", url);
.

# --- Extract values from a parameter list ----------------------------------

.program $god $http_server:parseparam tnt
	{parameters, spec} = args;
	output = {};
	for i in (spec)
		j = (i in parameters[1]);
		if (j == 0)
			return output;
		endif
		output = {@output, parameters[2][j]};
	endfor
	return output;
.

# --- Emit resource list ------------------------------------------------------

.program $god $http_server:reslist tnt
	{m, a, o, ?br=0} = args;
	s = "<B>M</B> "+floatstr(m, 1)+" ";
	if (br)
		s = s + "<BR>";
	endif
	s = s + "<B>A</B> "+floatstr(a, 1)+" ";
	if (br)
		s = s + "<BR>";
	endif
	s = s + "<B>O</B> "+floatstr(o, 1);
	return s;
.

# --- Form utilities ----------------------------------------------------------

# Emits the top end of a form with two hidden values: objnum, which contains
# the object number; and cmd, which contains the passed-in string.

.program $god $http_server:startform tnt
	{c, url, obj, cmd} = args;
	$htell(c, "<FORM ACTION=\""+url+"\"><INPUT TYPE=\"hidden\" NAME=\"objnum\" VALUE=\""+tostr(toint(obj))+"\"><INPUT TYPE=\"hidden\" NAME=\"cmd\" VALUE=\""+cmd+"\">");
.

.program $god $http_server:endform tnt
	{c} = args;
	$htell(c, "</FORM>");
.

# --- Start HTTP server on bootup ---------------------------------------------

# Ensure the HTTP server is started whenever the system is booted up.

$server_started_list = {@$server_started_list, $http_server};

.quit

