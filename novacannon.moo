rem novacannon.moo
rem Basic nova cannon.
rem $Source: /cvsroot/stellation/stellation/novacannon.moo,v $
rem $State: Exp $

.patch novacannon.moo 6 1
notify(player, "novacannon.moo");

$god:prop(#0, "novacannon", create($warship, $god));
$novacannon.name = "nova cannon";

$god:prop($novacannon, "firepower", 10);
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
		$htell(c, "hostile fleet(s) current engaged.</FONT>");
		$htell(c, "Total mass of hostile fleets:");
		$htell(c, floatstr(this:fleetmass(h), 1));
	endif
.

.quit

rem Revision History
rem $Log: novacannon.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.

