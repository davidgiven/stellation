rem novacannon.moo
rem Basic nova cannon.
rem $Source: /cvsroot/stellation/stellation/novacannon.moo,v $
rem $State: Exp $

.patch novacannon.moo 6 1
notify(player, "novacannon.moo");

$god:prop(#0, "novacannon", create($warship, $god));
$novacannon.name = "nova cannon";

$god:prop($novacannon, "firepower", 100);
$novacannon.description = "Nova cannons use spatial disturbances similar to those used to create a flaw to cause spontaneous energy discharges over wide regions of space. They are commonly used to attack large numbers of units at once.";

# --- Calculate total mass of a set of fleets ---------------------------------

.program $god $novacannon:fleetmass tnt
	{fleetlist} = args;
	mass = 0.0;
	for i in (fleetlist)
		mass = mass + i:mass();
	endfor
	return mass;
.

# --- Actually attack someone ------------------------------------------------

.program $god $novacannon:tick tnt
	player = this.owner;
	h = this:hostilefleets();
	notify($god, "player="+player:name()+" h="+toliteral(h));
	if (length(h) > 0)
		r = tofloat(this.firepower)/this:fleetmass(h);
		notify($god, "hitting "+toliteral(h)+" for "+floatstr(r, 1));
		for i in (h)
			for j in (i:contents())
				j:attack(r*tofloat(j:restmass()));
			endfor
		endfor
	endif
	return pass();
.

# --- HTML operations ---------------------------------------------------------

.program $god $novacannon:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "")
		$htell(c, "<B>Firepower:</B>");
		$htell(c, toint(this.firepower));
		$htell(c, "per 1000.0 mass");
	endif
	h = this:hostilefleets();
	if (length(h) == 0)
		$htell(c, "<BR>No hostile units currently engaged.");
	else
		$htell(c, "<BR><FONT COLOR=#FF0000>"+tostr(length(h)));
		$htell(c, "hostile fleet(s) currently engaged.</FONT>");
		$htell(c, "<BR><B>Total mass of hostile fleets:</B>");
		m = this:fleetmass(h);
		$htell(c, floatstr(m, 1));
		$htell(c, "<BR><B>Relative firepower:</B>");
		$htell(c, floatstr(tofloat(this.firepower*1000)/m, 1));
		$htell(c, "per 1000.0 mass");
	endif
.

.quit

rem Revision History
rem $Log: novacannon.moo,v $
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

