rem star.moo
rem Represents a single star.
rem $Source: /cvsroot/stellation/stellation/star.moo,v $
rem $State: Exp $

.patch star.moo 6 1
notify(player, "star.moo");

$god:prop(#0, "star", create($object, $god));
$star.name = "Generic star";

# --- Generic star properties -------------------------------------------------

$god:prop($star, "position", {0.0, 0.0});
$god:prop($star, "resources", {0.0, 0.0, 0.0});
$god:prop($star, "asteroids", {0, 0});
$god:prop($star, "brightness", 0);

# --- Event notification ------------------------------------------------------

.program $god $star:notify tnt
	{msg} = args;
	p = {};
	for i in (this.contents)
		if (i:descendentof($fleet))
			if (i:find_ships($jumpship) != {})
				p = setadd(p, i.owner);
			endif
		endif
	endfor
	for i in (p)
		i:notify(msg, this);
	endfor
.

# --- Get and set a star's resources ------------------------------------------

.program $god $star:resources tnt
	return this.resources;
.

.program $god $star:changeresources tnt
	{m, a, o} = args;
	m = this.resources[1] + m;
	a = this.resources[2] + a;
	o = this.resources[3] + o;
	if ((m<0.0) || (a<0.0) || (o<0.0))
		return {this.name+" doesn't have those resources available."};
	endif
	this.resources = {m, a, o};
	return {""};
.

# --- Consume resources -------------------------------------------------------

.program $god $star:consume tnt
	{m, a, o} = args;
	if ((m > this.resources[1]) || (a > this.resources[2]) || (o > this.resources[3]))
		return 1;
	endif
	this.resources = {this.resources[1]-m, this.resources[2]-a, this.resources[3]-o};
	return 0;
.

# --- Asteroids management ----------------------------------------------------

.program $god $star:asteroids tnt
	return this.asteroids;
.

.program $god $star:changeasteroids tnt
	{m, c} = args;
	m = this.asteroids[1] + m;
	c = this.asteroids[2] + c;
	if ((m<0) || (c<0))
		return {this.name+" doesn't have any of those asteroids available."};
	endif
	this.asteroids = {m, c};
	return {""};
.

# --- Return the star's name --------------------------------------------------

# This is a method so it can be programmatically overridden by, say, $transit.

.program $god $star:name tnt
	return this.name;
.

# --- Is this star visible to the player? -------------------------------------

.program $god $star:visible tnt
	{?p=player} = args;
	return (this in (p:starsystems()));
	return 0;
.

# --- Accept an object --------------------------------------------------------

.program $god $star:accept tnt
	{what} = args;
	if (what:descendentof($fleet))
		return 1;
	endif
	return 0;
.

# --- Convert a stellar brightness number to name -----------------------------

.program $god $star:brightness tnt
	{value} = args;
	if (value == 0)
		return "no star present";
	endif
	return tostr(value);
.
	
# --- HTML interface ----------------------------------------------------------

.program $god $star:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	objnum = toobj(objnum);
	$htell(c, "<B>Location:</B> <A HREF=\"/player/map?x="+tostr(objnum.position[1])+"&y="+tostr(objnum.position[2])+"&scale="+tostr(player.mapdefaultscale)+"\" TARGET=\"_top\">("+tostr(objnum.position[1])+", "+tostr(objnum.position[2])+")</A>");
	$htell(c, "<BR><B>Resources:</B>");
	$htell(c, $http_server:reslist(@objnum.resources));
	$htell(c, "<BR><B>Metallic asteroids:</B>");
	$htell(c, tostr(objnum.asteroids[1]));
	$htell(c, "<BR><B>Carbonaceous asteroids:</B>");
	$htell(c, tostr(objnum.asteroids[2]));
	$htell(c, "<BR><B>Brightness:</B>");
	$htell(c, this:brightness(this.brightness));
	fleets = {};
	units = {};
	for i in (objnum.contents)
		if (i:descendentof($fleet))
			fleets = {@fleets, i};
		else
			units = {@units, i};
		endif
	endfor
	$htell(c, "<P>Fleets:");
	$htell(c, "<UL>");
	if (fleets == {})
		$htell(c, "<LI>(None)");
	else
		for i in (fleets)
			$htell(c, "<LI><A HREF=\"/player/fleet?objnum="+tostr(toint(i))+"\" TARGET=\"_top\"><B>"+i:name()+"</B></A>");
		endfor
	endif
	$htell(c, "</UL><P>Units:<UL>");
	if (units == {})
		$htell(c, "<LI>(None)");
	else
		for i in (units)
			$htell(c, "<LI><A HREF=\"/player/unit?objnum="+tostr(toint(i))+"\" TARGET=\"_top\">"+i:name()+"</A>");
			i:http_info(c);
		endfor
	endif
	$htell(c, "</UL>");
.

.quit

rem Revision History
rem $Log: star.moo,v $
rem Revision 1.4  2000/08/07 20:18:21  dtrg
rem Ensured that only fleets containing jumpships make a star system
rem visible.
rem
rem Revision 1.3  2000/08/05 22:44:08  dtrg
rem Many minor bug fixes.
rem Better object visibility testing --- less scope for cheating.
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


