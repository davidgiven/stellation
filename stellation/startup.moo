rem startup.moo
rem Startup routines.
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

