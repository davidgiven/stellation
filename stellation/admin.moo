rem admin.moo
rem Administration mode commands.

exec add_verb($god, {$god, "rxd", "prop"}, {"any", "any", "any"})
.program $god:prop
	{object, propname, value, ?perms = "r"} = args;
	if (propname in properties(object))
		return;
	endif
	if (typeof(object) == STR)
		object = eval("return "+object+";")[2];
	endif
	
	try
		add_property(object, propname, value, {player, perms});
	except (E_INVARG)
		object.(propname) = value;
	endtry
.

exec add_verb($god, {$god, "rxd", "verb"}, {"any", "any", "any"})
.program $god:verb
	if (length(args) != 4)
		notify(player, "Syntax: verb <owner> <object> <verbname> <perms>");
		return E_ARGS;
	endif

	owner = eval("return "+args[1]+";")[2];
	object = eval("return "+args[2]+";")[2];
	verbname = args[3];
	if (args[4] == "tnt")
		perms = {"this", "none", "this"};
	elseif (args[4] == "aaa")
		perms = {"any", "any", "any"};
	else
		notify(player, "Don't understand those permissions!");
		return E_ARGS;
	endif

	add_verb(object, {owner, "rxd", verbname}, perms);
.

