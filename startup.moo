rem startup.moo
rem Startup routines.
rem $Source: /cvsroot/stellation/stellation/startup.moo,v $
rem $State: Exp $

.patch startup.moo 4 1
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
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem


