rem tug.moo
rem Space tug.
rem $Source: /cvsroot/stellation/stellation/tug.moo,v $
rem $State: Exp $

.patch tug.moo 6 1
notify(player, "tug.moo");

$god:prop(#0, "tug", create($ship, $god));
$tug.name = "tug";

$tug.description = "Tugs are small, powerful craft used to tow otherwise unpowered vessels and other artifacts.";

# --- Constructor/destructor --------------------------------------------------

.program $god $tug:destroy tnt
	result = this:unload();
	if (result[1] == "")
		result[2]:attack(result[2].maxdamage * 2.0 / 3.0);
	endif
	return pass();
.

# --- Resource consumption ----------------------------------------------------

.program $god $tug:starve tnt
	contents = this:contents();
	if (contents != {})
		contents[1]:starve();
	endif
	pass();
.

# --- Property fetchers -------------------------------------------------------

.program $god $tug:mass tnt
	contents = this:contents();
	if (contents != {})
		return this:restmass() + contents[1]:mass();
	endif
	return this:restmass();
.

# --- Pick up a new load ------------------------------------------------------

.program $god $tug:load tnt
	{unit} = args;
	starsystem = this.location.location;
	if (!starsystem:descendentof($star))
		return {"The tug is not in a star system."};
	endif
	if (this:contents() != {})
		return {"The tug is already towing something."};
	endif
	if (unit == #-2)
		"Metallic asteroid.";
		if (starsystem:asteroids()[1] == 0)
			return {"This system has no metallic asteroids available."};
		endif
		unit = $masteroid:create();
		starsystem:changeasteroids(-1, 0);
	elseif (unit == #-3)
		"Carbonaceous asteroid.";
		if (starsystem:asteroids()[2] == 0)
			return {"This system has no carbonaceous asteroids available."};
		endif
		unit = $casteroid:create();
		starsystem:changeasteroids(0, -1);
	elseif (!(unit in starsystem:contents()))
		return {"That unit is not in this system."};
	endif
	if (!(unit:descendentof($unit)))
		return {"You can't pick that up."};
	endif
	if (unit:descendentof($factory) && unit:deployed())
		return {"Factories have to be mothballed before they can be taken under tow."};
	endif
	starsystem:notify("<B>"+unit.name+"</B> taken under tow.");
	unit:make_dead();
	unit:moveto(this);
	return {""};
.

# --- Make the tug drop its load ----------------------------------------------

.program $god $tug:unload tnt
	contents = this:contents();
	starsystem = this.location.location;
	if (!starsystem:descendentof($star))
		return {"The tug is not in a star system."};
	endif
	if (contents == {})
		return {"The tug is not towing anything."};
	endif
	starsystem:notify("<B>"+contents[1].name+"</B> dropped by a tug.");
	if (contents[1]:descendentof($masteroid))
		contents[1]:destroy();
		starsystem:changeasteroids(1, 0);
	elseif (contents[1]:descendentof($casteroid))
		contents[1]:destroy();
		starsystem:changeasteroids(0, 1);
	else
		contents[1]:moveto(starsystem);
		contents[1]:make_alive();
	endif
	return {"", contents[1]};
.

# --- Make a tug drop its load ------------------------------------------------

.program $god $tug:http_unload tnt
	{c, method, param} = args;
	{objnum} = $http_server:parseparam(param, {"objnum"});
	if (this.owner != player)
		$http_server:formsyntax(c);
		return;
	endif
	result = this:unload();
	if (result[1] != "")
		$htell(c, "Failed!");
		$htell(c, result[1]);
	else
		player:redirect(c, this);
		/* $http_server:redirect(c, "/player/unit?objnum="+tostr(toint(this))); */
	endif
.

# --- ...or pick up a new load ------------------------------------------------

.program $god $tug:http_load tnt
	{c, method, param} = args;
	{objnum, ?unit="-1"} = $http_server:parseparam(param, {"objnum", "unit"});
	unit = toobj(unit);
	if (this.owner != player)
		$http_server:formsyntax(c);
		return;
	endif
	result = this:load(unit);
	if (result[1] != "")
		$htell(c, "Failed!");
		$htell(c, result[1]);
	else
		player:redirect(c, this);
		/* $http_server:redirect(c, "/player/unit?objnum="+tostr(toint(this))); */
	endif
.

# --- HTML operations ---------------------------------------------------------

.program $god $tug:http_info tnt
	{c} = args;
	contents = this:contents();
	if (contents == {})
		$htell(c, "(idle)");
	else
		$htell(c, "("+contents[1].name+")");
	endif
.

.program $god $tug:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "unload")
		return this:http_unload(c, method, param);
	elseif (cmd == "load")
		return this:http_load(c, method, param);
	elseif (cmd == "transfer")
		return this:http_transfer(c, method, param);
	endif
	$htell(c, "<B>Unladen mass:</B>");
	$htell(c, tostr($numutils:round(10, this.mass)));
	contents = this:contents();
	$htell(c, "<BR><B>Currently towing:</B>");
	if (contents == {})
		$htell(c, "nothing");
		$htell(c, "<BR><HR>");
		loadable = {};
		a = (this.location.location):asteroids();
		if (a[1] > 0)
			loadable = {@loadable, #-2};
		endif
		if (a[2] > 0)
			loadable = {@loadable, #-3};
		endif
		for i in ((this.location.location):contents())
			if (!i:descendentof($fleet))
				loadable = {@loadable, i};
			endif
		endfor
		if (loadable == {})
			return;
		endif
		$htell(c, "<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=objnum VALUE=\""+tostr(toint(this))+"\"><INPUT TYPE=hidden NAME=cmd VALUE=\"load\">");
		$htell(c, "Load: <SELECT NAME=\"unit\">");
		for i in (loadable)
			$htell(c, "<OPTION VALUE=\""+tostr(toint(i))+"\">");
			if (i == #-2)
				$htell(c, "metallic asteroid");
			elseif (i == #-3)
				$htell(c, "carbonaceous asteroid");
			else
				$htell(c, i.name);
			endif
			$htell(c, "</OPTION>");
		endfor
		$htell(c, "</SELECT><INPUT TYPE=submit VALUE=\"Load\"></FORM>");
	else
		$htell(c, contents[1].name);
		$htell(c, "<BR><HR>");
		$htell(c, "<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=objnum VALUE=\""+tostr(toint(this))+"\"><INPUT TYPE=hidden NAME=cmd VALUE=\"unload\"><INPUT TYPE=submit VALUE=\"Unload\"></FORM>");
	endif
.

.program $god $tug:http_menu_notowned tnt
	{c, method, param} = args;
	$htell(c, "<B>Unladen mass:</B>");
	$htell(c, tostr($numutils:round(10, this.mass)));
	contents = this:contents();
	$htell(c, "<BR><B>Currently towing:</B>");
	if (contents == {})
		$htell(c, "nothing");
	else
		$htell(c, contents[1].name);
	endif
.

# --- Accept a unit -----------------------------------------------------------

.program $god $tug:accept tnt
	{what} = args;
	return 1;
.

.quit

rem Revision History
rem $Log: tug.moo,v $
rem Revision 1.6  2000/08/07 20:13:30  dtrg
rem Changes so that when a tug is destroyed, its cargo is dealt 2/3 damage
rem and dropped.
rem
rem Revision 1.5  2000/08/03 18:59:53  dtrg
rem When carbonaceous asteroids are picked up, they don't mysteriously turn
rem into metallic ones any more.
rem
rem Revision 1.4  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
rem Revision 1.3  2000/08/01 22:06:04  dtrg
rem Owned stars are now showed in yellow again.
rem Fixed viewing other people's units; all the tracebacks should have gone.
rem Various minor bug fixes and formatting changes.
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


