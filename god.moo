rem god.moo
rem Special functions for God.
rem $Source: /cvsroot/stellation/stellation/god.moo,v $
rem $State: Exp $

.patch god.moo 6 1
notify(player, "god.moo");

# --- Administrative telnet login -------------------------------------------

.program $god #0:do_login_command tnt
	{?password=""} = args;
	if (password == "")
		return;
	endif
	if (crypt(password, $god.password) != $god.password)
		this:failedauth(player, "Invalid password.");
		return;
	endif
	return $god;
.

# --- Main HTML index -------------------------------------------------------

.program $god $god:http_index tnt
	{c, method, parameters} = args;
	$http_server:htmlheader(c, 200, "Stellation Administration");
	$http_server:menu(c, "Server functions",
		{"Object Browser",	"/player/objectbrowser"},
		{"Checkpoint now",	"/player/checkpoint"},
		{"Shutdown server",	"/player/shutdown"});
	$htell(c, "<P>");
	$http_server:menu(c, "Game observation",
		{"Star map",		"/player/map"});
	$http_server:htmlfooter(c);
.

# --- Checkpoint ------------------------------------------------------------

.program $god $god:http_checkpoint tnt
	{c, method, parameters} = args;
	$http_server:htmlheader(c, 200, "Stellation Administration");
	dump_database();
	$htell(c, "Checkpoint initiated.<P>");
	$http_server:anchor(c, "Back.",
		"/player");
	$http_server:htmlfooter(c);
.

# --- Shutdown --------------------------------------------------------------

.program $god $god:http_shutdown tnt
	{c, method, parameters} = args;
	$http_server:htmlheader(c, 200, "Stellation Administration");
	shutdown();
	$htell(c, "Shutdown initiated.<P>");
	$http_server:anchor(c, "Back.",
		"/player");
	$http_server:htmlfooter(c);
.

# --- Emit an object reference ----------------------------------------------

.program $god $god:objref tnt
	{c, obj} = args;
	if (obj != #-1)
		if (valid(obj))
			$htell(c, "<A HREF=\"/player/objinfo?objnum=",
				toint(obj), "\">", obj.name, " (",
				toliteral(obj), ")</A>");
		else
			$htell(c, "<A HREF=\"/player/objinfo?objnum=",
				toint(obj), "\">", toliteral(obj), "</A>");
		endif
	endif
.

# --- Object browser --------------------------------------------------------

.program $god $god:http_objectbrowser tnt
	{c, method, parameters} = args;
	$http_server:htmlheader(c, 200, "Stellation Object Browser");
	$htell(c, "<TABLE>");
	$htell(c, "<TR>");
	$htell(c, "<TD><TD ALIGN=left><B>Name</B>");
	$htell(c, "<TD ALIGN=left><B>Parent</B>");
	$htell(c, "<TD ALIGN=left><B>Owner</B>");
	$htell(c, "<TD ALIGN=left><B>Flags</B>");
	$htell(c, "</TR>");
	for i in [#0..max_object()]
		$htell(c, "<TR>");
		$htell(c, "<TD ALIGN=right>");
		$htell(c, "<A HREF=\"/player/objinfo?objnum=",
			toint(i), "\">", toliteral(i), "</A>");
		if (!valid(i))
			$htell(c, "<TD ALIGN=left>");
			$htell(c, "Recycled");
			continue;
		endif
		$htell(c, "<TD ALIGN=left>");
		$htell(c, "<A HREF=\"/player/objinfo?objnum=",
			toint(i), "\">", i.name, "</A>");
		$htell(c, "<TD ALIGN=left>");
		this:objref(c, parent(i));
		$htell(c, "<TD ALIGN=left>");
		this:objref(c, i.owner);
		$htell(c, "<TD ALIGN=left>");
		if (is_player(i))
			$htell(c, "player");
		endif
		if (i.wizard)
			$htell(c, "wizard");
		endif
		$htell(c, "</TR>");
		suspend(0);
	endfor
	$htell(c, "</TABLE>");
	$http_server:htmlfooter(c);
.

# --- Get information on an object ------------------------------------------

.program $god $god:http_objinfo tnt
	{c, method, param} = args;
	{?objnum = {}} = $http_server:parseparam(param, {"objnum"});
	if (objnum == {})
		$http_server:formsyntax(c);
		return;
	endif
	objnum = toobj(objnum);
	$http_server:htmlheader(c, 200, "Stellation Object Browser");
	if (!valid(objnum))
		$htell(c, "<H2>", objnum, ": Invalid object number</H2>");
		$http_server:htmlfooter(c);
		return;
	endif
	$htell(c, "<H2>", objnum, ": ", objnum.name, "</H2>");
	$htell(c, "Class hierarchy:");
	i = objnum;
	while (i != #-1)
		this:objref(c, i);
		i = parent(i);
	endwhile
	$htell(c, "<BR>");
	$htell(c, "Owned by:");
	this:objref(c, objnum.owner);
	$htell(c, "<BR>");
	$htell(c, "Flags:");
	if (is_player(objnum))
		$htell(c, "player");
	endif
	if (objnum.wizard)
		$htell(c, "wizard");
	endif
	$htell(c, "<P><HR>");
	suspend(0);
	$htell(c, "<H3>Properties</H3>");
	$htell(c, "<TABLE><TR>");
	$htell(c, "<TD><B>Name</B>");
	$htell(c, "<TD><B>Value</B>");
	$htell(c, "</TR>");
	for i in (objnum:properties())
		$htell(c, "<TR>");
		$htell(c, "<TD>", i);
		$htell(c, "<TD>", toliteral(objnum.(i)));
		$htell(c, "</TR>");
	endfor
	$htell(c, "</TABLE>");
	$htell(c, "<P><HR>");
	suspend(0);
	$htell(c, "<H3>Verbs</H3>");
	$htell(c, "<TABLE><TR>");
	$htell(c, "<TD><B>Name</B>");
	$htell(c, "<TD><B>Commands</B>");
	$htell(c, "</TR>");
	for i in (objnum:verbs())
		$htell(c, "<TR>");
		$htell(c, "<TD><A TARGET=\""+i+"\">", i);
		$htell(c, "<TD>");
		if (i in verbs(objnum))
			$http_server:menu(c, "",
				{"View",	"/player/viewverb?objnum="+tostr(toint(objnum))+"&verb="+i+"&dis=0"},
				{"Disassemble",	"/player/viewverb?objnum="+tostr(toint(objnum))+"&verb="+i+"&dis=1"});
		else
			o = parent(objnum);
			while (o != #-1)
				if (i in verbs(o))
					break;
				endif
				o = parent(o);
			endwhile
			$http_server:menu(c, "",
				{"Go to progenitor", "/player/objinfo?objnum="+tostr(toint(o))+"#"+i});
		endif
		$htell(c, "</TR>");
	endfor
	$htell(c, "</TABLE>");
	$http_server:htmlfooter(c);
.

# --- View a single verb ----------------------------------------------------

.program $god $god:http_viewverb tnt
	{c, method, param} = args;
	{?objnum = {}, ?verb = {}, ?dis = 0} = $http_server:parseparam(param, {"objnum", "verb", "dis"});
	if ((objnum == {}) || (verb == {}))
		$http_server:formsyntax(c);
		return;
	endif
	objnum = toobj(objnum);
	$http_server:htmlheader(c, 200, "Stellation Object Browser");
	if (!valid(objnum))
		$htell(c, "<H2>", objnum, ": Invalid object number</H2>");
		$http_server:htmlfooter(c);
		return;
	endif
	if (!(verb in verbs(objnum)))
		$htell(c, "<H2>", objnum, ":", verb, " does not exit</H2>");
		$http_server:htmlfooter(c);
		return;
	endif
	$htell(c, "<H2>");
	if (dis)
		$htell(c, "Disassembly of");
	else
		$htell(c, "Listing of");
	endif
	$htell(c, "<A HREF=\"/player/objinfo?objnum=", toint(objnum),
		"\">", toliteral(objnum), "</A>:", verb, "</H2>");
	$htell(c, "<P><PRE>");
	if (dis)
		data = disassemble(objnum, verb);
	else
		data = verb_code(objnum, verb, 0, 1);
	endif
	for i in (data)
		$htell(c, i);
	endfor
	$htell(c, "</PRE>");
	$http_server:htmlfooter(c);
.
.quit

rem Revision History
rem $Log: god.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

