.program #2:eval
notify(player, toliteral(eval("return " + argstr + ";")[2]));
.

;add_verb(#3, {#3, "rxd", "eval"}, {"any", "any", "any"})
.program #3:eval
	notify(player, toliteral(eval("return " + argstr + ";")[2]));
.
;#3.location = #-1
;recycle(#2)

;add_verb(#3, {#3, "rxd", "rem"}, {"any", "any", "any"})
.program #3:rem
.

;add_verb(#0, {#3, "rxd", "traceback"}, {"this", "none", "this"})
.program #0:traceback
	{v, ?c = 0} = args;
	html = (c != 0);
	if (c == 0)
		c = player;
	endif
	tb = v[4];
	if (html)
		notify(c, "");
		notify(c, "</TABLE></TABLE></TABLE></TABLE><P><HR><H2>System traceback</H2>");
	endif
	if (length(tb) == 0)
		notify(c, "** Illegal command: "+tostr(v[2]));
		if (html)
			notify(c, "<BR>");
		endif
	else
		top = tb[1];
		tb[1..1] = {};
		notify(c, tostr(top[1])+":"+tostr(top[2])+", line "+
			tostr(top[6])+":"+tostr(v[2]));
		if (html)
			notify(c, "<BR>");
		endif
		for fr in (tb)
			notify(c, "... called from "+
				tostr(fr[1])+":"+tostr(fr[2])+", line "+
				tostr(fr[6]));
			if (html)
				notify(c, "<BR>");
			endif
		endfor
		notify(c, "(End of traceback)");
		if (html)
			notify(c, "<P>");
		endif
	endif
.

;add_verb(#3, {#3, "rxd", "tell"}, {"this", "none", "this"})
.program #3:tell
	line = "";
	i = 1;
	for i in (args)
		line = line + tostr(i);
	endfor
	notify(this, line);
.

;add_verb(#3, {#3, "rxd", "exec"}, {"any", "any", "any"})
.program #3:exec
	try
		eval(argstr + ";");
	except v (ANY)
		$traceback(v);
		shutdown();
	endtry
.

rem System variables

exec add_property(#0, "god",			#3, {#3, "r"})
exec $god.name = "God"
rem exec add_property(#0, "server_options",		#-1, {$god, "r"})
exec add_property(#0, "nothing",		#-1, {$god, "r"})
exec add_property(#0, "ambiguous_match",	#-2, {$god, "r"})
exec add_property(#0, "failed_match",		#-3, {$god, "r"})
exec add_property(#0, "object", create(#-1, $god), {$god, "r"})

rem Revision History
rem $Log: _init.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

