rem startup.moo
rem Startup routines.
rem $Source: /cvsroot/stellation/stellation/startup.moo,v $
rem $State: Exp $

.patch startup.moo 6 1
notify(player, "startup.moo");

$god:prop(#0, "server_started_list", {});

# --- Called on server startup ------------------------------------------------

.program $god #0:server_started tnt
	for i in (#0.server_started_list)
		(i):start();
	endfor
.

.quit

rem Revision History
rem $Log: startup.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem


