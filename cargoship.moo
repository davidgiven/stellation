rem cargoship.moo
rem Cargo ship.
rem $Source: /cvsroot/stellation/stellation/cargoship.moo,v $
rem $State: Exp $

.patch cargoship.moo 6 1
notify(player, "cargoship.moo");

$god:prop(#0, "cargoship", create($ship, $god));
$cargoship.name = "cargoship";

$god:prop($cargoship, "cargo", {0.0, 0.0, 0.0});
$god:prop($cargoship, "maxcargo", {0.0, 0.0, 0.0});
$god:prop($cargoship, "mincargo", {0.0, 0.0, 0.0});
$god:prop($cargoship, "scale", 300.0);

$cargoship.description = "Cargo ships can carry large quantities of the three main commodities. They can be used to transfer resources from one star system to another, and are also used to supply vessels in operation.";

# --- Property fetchers -------------------------------------------------------

.program $god $cargoship:restmass tnt
	scale = this.scale;
	return this.mass + this.cargo[1]/scale + this.cargo[2]/scale +
		this.cargo[3]/scale;
.

# --- Load or unload cargo ship -----------------------------------------------

.program $god $cargoship:load tnt
	{m, a, o} = args;
	starsystem = this.location.location;
	if (!starsystem:descendentof($star))
		return {"The fleet that ship's in is not in a star system!"};
	endif
	{mm, aa, oo} = starsystem:resources();
	if (-m > this.cargo[1])
		return {"The ship doesn't have that much metal on board."};
	endif
	if (m > mm)
		return {starsystem.name+" doesn't have that much metal available."};
	endif
	if (-a > this.cargo[2])
		return {"The ship doesn't have that much antimatter on board."};
	endif
	if (a > aa)
		return {starsystem.name+" doesn't have that much antimatter available."};
	endif
	if (-o > this.cargo[3])
		return {"The ship doesn't have that much organics on board."};
	endif
	if (o > oo)
		return {starsystem.name+" doesn't have that much organics available."};
	endif
	result = starsystem:changeresources(-m, -a, -o);
	if (result[1] != "")
		return result;
	endif
	starsystem:notify("Cargo transfer: "+$http_server:reslist(m, a, o));
	this.cargo[1] = this.cargo[1] + m;
	this.cargo[2] = this.cargo[2] + a;
	this.cargo[3] = this.cargo[3] + o;
	return {""};
.

# --- Consume (& destroy) resources -------------------------------------------

.program $god $cargoship:destroyresources tnt
	{m, a, o} = args;
	if ((m > this.cargo[1]) || (a > this.cargo[2]) || (o > this.cargo[3]))
		return 1;
	endif
	this.cargo[1] = this.cargo[1] - m;
	this.cargo[2] = this.cargo[2] - a;
	this.cargo[3] = this.cargo[3] - o;
	return 0;
.

# --- Periodic operation ------------------------------------------------------

.program $god $cargoship:tick tnt
	m = this.mincargo[1] - this.cargo[1];
	a = this.mincargo[2] - this.cargo[2];
	o = this.mincargo[3] - this.cargo[3];
	if (m < 0.0)
		m = 0.0;
	endif
	if (a < 0.0)
		a = 0.0;
	endif
	if (o < 0.0)
		o = 0.0;
	endif
	if ((m > 0.0) || (a > 0.0) || (o > 0.0))
		this:load(m, a, o);
	endif
	return pass(@args);
.

# --- Set the low watermark level ---------------------------------------------

.program $god $cargoship:setmincargo tnt
	{m, a, o} = args;
	if ((m < 0.0) || (a < 0.0) || (o < 0.0))
		return {"Negative values make no sense here."};
	endif
	this.mincargo = {m, a, o};
	return {""};
.

.program $god $cargoship:http_setmincargo tnt
	{c, method, param} = args;
	{objnum, cmd, ?m="0.0", ?a="0.0", ?o="0.0"} = $http_server:parseparam(param, {"objnum", "cmd", "m", "a", "o"});
	m = tofloat(m);
	a = tofloat(a);
	o = tofloat(o);
	if (this.owner != player)
		$http_server:formsyntax(c);
		return;
	endif
	result = this:setmincargo(m, a, o);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- HTML Load or unload -----------------------------------------------------

.program $god $cargoship:http_load tnt
	{c, method, param} = args;
	{objnum, cmd, ?m="0.0", ?a="0.0", ?o="0.0"} = $http_server:parseparam(param, {"objnum", "cmd", "m", "a", "o"});
	m = tofloat(m);
	a = tofloat(a);
	o = tofloat(o);
	if (this.owner != player)
		$http_server:formsyntax(c);
		return;
	endif
	result = this:load(m, a, o);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- HTML operations ---------------------------------------------------------

.program $god $cargoship:http_info tnt
	{c} = args;
	$htell(c, $http_server:reslist(@this.cargo));
.

.program $god $cargoship:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "load")
		return this:http_load(c, method, param);
	elseif (cmd == "transfer")
		return this:http_transfer(c, method, param);
	elseif (cmd == "setmincargo")
		return this:http_setmincargo(c, method, param);
	endif
	contents = this:contents();
	$htell(c, "<B>Unladen mass:</B>");
	$htell(c, tostr($numutils:round(10, this.mass)));
	$htell(c, "<BR><B>Cargo:</B>");
	$htell(c, $http_server:reslist(@this.cargo));
	$htell(c, "<BR><B>Available to pick up:</B>");
	$htell(c, $http_server:reslist(@this.location.location:resources()));
	$htell(c, "<BR><HR>");

	$http_server:startform(c, "/player/unit", objnum, "load");
	$htell(c, "M: <INPUT NAME=\"m\" VALUE=\"0.0\" SIZE=6>");
	$htell(c, "A: <INPUT NAME=\"a\" VALUE=\"0.0\" SIZE=6>");
	$htell(c, "O: <INPUT NAME=\"o\" VALUE=\"0.0\" SIZE=6>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Load\">");
	$http_server:endform(c);

	$http_server:startform(c, "/player/unit", objnum, "load");
	m = this.cargo[1] - 1.0;
	if (m<0.0)
		m = 0.0;
	endif
	$htell(c, "<INPUT NAME=\"m\" TYPE=hidden VALUE=\""+tostr(-m)+"\" SIZE=7>");
	m = this.cargo[2] - 1.0;
	if (m<0.0)
		m = 0.0;
	endif
	$htell(c, "<INPUT NAME=\"a\" TYPE=hidden VALUE=\""+tostr(-m)+"\" SIZE=5>");
	m = this.cargo[3] - 1.0;
	if (m<0.0)
		m = 0.0;
	endif
	$htell(c, "<INPUT NAME=\"o\" TYPE=hidden VALUE=\""+tostr(-m)+"\" SIZE=5>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Unload All\">");
	$http_server:endform(c);

	$http_server:startform(c, "/player/unit", objnum, "setmincargo");
	$htell(c, "Set minimum cargo level to");
	$htell(c, "M: <INPUT NAME=\"m\" VALUE=\""+floatstr(this.mincargo[1], 1)+"\" SIZE=6>");
	$htell(c, "A: <INPUT NAME=\"a\" VALUE=\""+floatstr(this.mincargo[2], 1)+"\" SIZE=6>");
	$htell(c, "O: <INPUT NAME=\"o\" VALUE=\""+floatstr(this.mincargo[3], 1)+"\" SIZE=6>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Change\"><BR>");
	$htell(c, "(The ship will automatically load cargo to keep above this value)");
	$http_server:endform(c);
.

.quit

rem Revision History
rem $Log: cargoship.moo,v $
rem Revision 1.5  2000/09/05 23:09:22  dtrg
rem Added minimum cargo level feature.
rem Now displays the amount of cargo available to load, because it's a pain
rem switching from the star system page to the cargoship page and
rem remembering all the numbers.
rem
rem Revision 1.4  2000/08/03 10:32:08  dtrg
rem Fixed a persistent but annoying bug preventing `Unload All' from working
rem properly.
rem
rem Revision 1.3  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.

