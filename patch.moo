rem patch.moo
rem On-the-fly patch system.
rem $Source: /cvsroot/stellation/stellation/patch.moo,v $
rem $State: Exp $

exec add_property(#0, "abort", 1, {$god, "r"})

rem --- Patch shell -----------------------------------------------------------

verb $god $god .patch aaa
.program $god:.patch
	{?filename = "(none)", ?linenumber = "0", ?abort = ""} = args;
	linenumber = toint(linenumber);
	abort = toint(abort);
	if ($abort == 0)
		abort == 0;
	endif
	set_connection_option(player, "hold-input", 1);
	while (1)
		line = read();
		linenumber = linenumber + 1;
		if ((length(line) > 1) && (line[1] == "."))
			line = line[2..$];
			{cmd, params} = $stringutils:firstword(line, " ");
			if (cmd == "quit")
				break;
			elseif (cmd == "program")
				{owner, object} = $stringutils:firstword(params, " ");
				{object, verb} = $stringutils:firstword(object, ":");
				{verb, perms} = $stringutils:firstword(verb, " ");
				owner = eval("return "+owner+";")[2];
				object = eval("return "+object+";")[2];
				if (perms == "aaa")
					perms = {"any", "any", "any"};
				elseif (perms == "tnt")
					perms = {"this", "none", "this"};
				else
					notify(player, "Invalid arguments for verb "+tostr(object)+":"+verb+" in "+filename+" at line "+tostr(linenumber));
					return this:patch_exit(abort);
				endif
					
				program = {};
				while (1)
					line = read();
					linenumber = linenumber + 1;
					if (line == ".")
						break;
					endif
					program = {@program, line};
				endwhile

				if (!(verb in verbs(object)))
					add_verb(object, {owner, "rxd", verb}, perms);
				endif
				
				result = set_verb_code(object, verb, program);
				if (result != {})
					notify(player, "Compilation of "+tostr(object)+":"+verb+" failed, in "+filename+" at "+tostr(linenumber));
					for i in (result)
						notify(player, i);
					endfor
					return this:patch_exit(abort);
				endif
			else
				notify(player, "Unknown command "+cmd+" in "+filename+" at line "+tostr(linenumber));
				return this:patch_exit(abort);
			endif
		elseif (line == "")
		elseif ((length(line) > 0) && (line[1] == "#"))
		else
			try
				result = eval(line);
				if (result[1] == 0)
					notify(player, "Command failed in "+filename+" at line "+tostr(linenumber));
					for i in (result[2])
						notify(player, i);
					endfor
					return this:patch_exit(abort);
				endif
			except v (ANY)
				notify(player, "MOO command failed in "+filename+" at line "+tostr(linenumber));
				notify(player, "Command was: ["+line+"]");
				$traceback(v);
				this:patch_exit(abort);
			endtry
		endif
	endwhile
	set_connection_option(player, "hold-input", 0);
.

verb $god $god patch_exit tnt
.program $god:patch_exit
	{abort} = args;
	set_connection_option(player, "hold-input", 0);
	while (1)
		line = read();
		if (line == ".quit")
			break;
		endif
	endwhile
	if (abort)
		shutdown("**** ABORTED BY PATCH ****");
	endif
.


rem Revision History
rem $Log: patch.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

