rem novacannon.moo
rem Basic nova cannon.
rem $Source: /cvsroot/stellation/stellation/novacannon.moo,v $
rem $State: Exp $

.patch novacannon.moo 3 1
notify(player, "novacannon.moo");

$god:prop(#0, "novacannon", create($ship, $god));
$novacannon.name = "nova cannon";

$god:prop($novacannon, "firepower", 10);
$novacannon.description = "Nova cannons use spatial disturbances similar to those used to create a flaw to cause spontaneous energy discharges over wide regions of space. They are commonly used to attack large numbers of units at once.";

# --- HTML operations ---------------------------------------------------------

.program $god $cargoship:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "")
		$htell(c, "<B>Firepower:</B>");
		$htell(c, toint(this.firepower));
		$htell(c, "per 1000.0 mass");
	endif
.

.quit

rem Revision History
rem $Log: novacannon.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

