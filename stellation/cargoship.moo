rem cargoship.moo
rem Cargo ship.
.patch cargoship.moo 3 1
notify(player, "cargoship.moo");

$god:prop(#0, "cargoship", create($ship, $god));
$cargoship.name = "cargoship";

$god:prop($cargoship, "cargo", {0.0, 0.0, 0.0});
$god:prop($cargoship, "maxcargo", {0.0, 0.0, 0.0});
$god:prop($cargoship, "scale", 300.0);

$cargoship.description = "Cargo ships can carry large quantities of the three main commodities. They can be used to transfer resources from one star system to another, and are also used to supply vessels in operation.";

# --- Property fetchers -------------------------------------------------------

.program $god $cargoship:mass tnt
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
	endif
	contents = this:contents();
	$htell(c, "<B>Unladen mass:</B>");
	$htell(c, tostr($numutils:round(10, this.mass)));
	$htell(c, "<BR><B>Cargo:</B>");
	$htell(c, $http_server:reslist(@this.cargo));
	$htell(c, "<BR><HR>");
	$htell(c, "<FORM ACTION=\"/player/unit\"><INPUT NAME=\"objnum\" TYPE=hidden VALUE=\""+tostr(toint(this))+"\"><INPUT NAME=\"cmd\" TYPE=hidden VALUE=\"load\">");
	$htell(c, "M: <INPUT NAME=\"m\" VALUE=\"0.0\" SIZE=5>");
	$htell(c, "A: <INPUT NAME=\"a\" VALUE=\"0.0\" SIZE=5>");
	$htell(c, "O: <INPUT NAME=\"o\" VALUE=\"0.0\" SIZE=5>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Load\"></FORM>");

	$htell(c, "<FORM ACTION=\"/player/unit\"><INPUT NAME=\"objnum\" TYPE=hidden VALUE=\""+tostr(toint(this))+"\"><INPUT NAME=\"cmd\" TYPE=hidden VALUE=\"load\">");
	$htell(c, "<INPUT NAME=\"m\" TYPE=hidden VALUE=\""+tostr(-this.cargo[1])+"\" SIZE=5>");
	$htell(c, "<INPUT NAME=\"a\" TYPE=hidden VALUE=\""+tostr(-this.cargo[2])+"\" SIZE=5>");
	$htell(c, "<INPUT NAME=\"o\" TYPE=hidden VALUE=\""+tostr(-this.cargo[3])+"\" SIZE=5>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Unload All\"></FORM>");
.

.quit

