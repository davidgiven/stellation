rem factory.moo
rem Represents a generic factory.
rem $Source: /cvsroot/stellation/stellation/factory.moo,v $
rem $State: Exp $

.patch factory.moo 3 1
notify(player, "factory.moo");

$god:prop(#0, "factory", create($unit, $god));
$factory.name = "Generic factory";

$god:prop($factory, "buildable", {});
$god:prop($factory, "building", {});
$god:prop($factory, "starttime", 0);
$god:prop($factory, "endtime", 0);
$god:prop($factory, "deployed", 0);
$god:prop($factory, "warehouse", {});

# --- Resource consumption ----------------------------------------------------

# Only deployed units consume resources.

.program $god $factory:consume tnt
	{m, a, o} = args;
	notify(#3, $http_server:reslist(@args));
	if (this.deployed)
		notify(#3, "factory consuming");
		return pass(@args);
	endif
	return 0;
.

# --- Deploy/mothball factory -------------------------------------------------

.program $god $factory:deployed tnt
	return this.deployed;
.

.program $god $factory:deploy tnt
	if (!this.location:descendentof($star))
		return {("The " + this.name) + " is not in a star system."};
	endif
	if (this.deployed)
		return {("The " + this.name) + " is already deployed."};
	endif
	this.location:notify("<B>"+this.name+"</B> deployed.");
	this.deployed = 1;
	return {""};
.

.program $god $factory:mothball tnt
	if (!this.location:descendentof($star))
		return {"The " + this.name + " is not in a star system."};
	endif
	if (!this.deployed)
		return {"The " + this.name + " is already mothballed."};
	endif
	if (this.building != {})
		return {"The " + this.name + " cannot be mothballed if it is in operation."};
	endif
	this.location:notify("<B>"+this.name+"</B> mothballed.");
	this.deployed = 0;
	return {""};
.

# --- Start/stop building -----------------------------------------------------

.program $god $factory:stopbuilding tnt
	if (!this.location:descendentof($star))
		return {("The " + this.name) + " is not in a star system."};
	endif
	if (!this.deployed)
		return {("The " + this.name) + " is not deployed."};
	endif
	if (this.building == {})
		return {("The " + this.name) + " is building anything."};
	endif
	kill_task(this.building[2]);
	this.building = {};
	this.starttime = 0;
	this.endtime = 0;
	return {""};
.

.program $god $factory:startbuilding tnt
	{unit} = args;
	if (!this.location:descendentof($star))
		return {("The " + this.name) + " is not in a star system."};
	endif
	if (!this.deployed)
		return {("The " + this.name) + " is not deployed."};
	endif
	if (this.building != {})
		return {("The " + this.name) + " is already building something."};
	endif
	if (!(unit in this.buildable))
		return {("The " + this.name) + " can't build that."};
	endif
	result = this.location:changeresources(-unit.build_cost[1], -unit.build_cost[2], -unit.build_cost[3]);
	if (result[1] != "")
		return result;
	endif
	fork pid (toint(unit.build_time * $tick))
		if (unit:descendentof($ship))
			this.warehouse = {@this.warehouse, unit};
		else
			u = unit:create();
			move(u, this.location);
		endif
		this.location:notify("<B>" + this.name + "</B> has completed building " + unit.name + ".");
		this.building = {};
	endfork
	this.location:notify("<B>"+this.name+"</B> has started building <B>"+unit.name+"</B>.");
	this.building = {unit, pid};
	this.starttime = time();
	this.endtime = time() + toint(unit.build_time * $tick);
	return {""};
.

# --- Move item from warehouse ------------------------------------------------

.program $god $factory:fromwarehouse tnt
	{class, target} = args;
	if (!(class in this.warehouse))
		return {("The " + this.name) + "'s warehouse doesn't contain any of those."};
	endif
	if (target.location != this.location)
		return {"You can only move units into a fleet in the same star system as the factory."};
	endif
	this.warehouse = setremove(this.warehouse, class);
	target:create_unit(class);
	return {""};
.

# =============================================================================
#                                 HTML INTERFACE
# =============================================================================

# --- Deploy a factory --------------------------------------------------------

.program $god $factory:http_deploy tnt
	{c, method, param} = args;
	{objnum} = $http_server:parseparam(param, {"objnum"});
	result = this:deploy();
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- ...and mothball it ------------------------------------------------------

.program $god $factory:http_mothball tnt
	{c, method, param} = args;
	{objnum} = $http_server:parseparam(param, {"objnum"});
	result = this:mothball();
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- Start/stop building -----------------------------------------------------

.program $god $factory:http_start tnt
	{c, method, param} = args;
	{objnum, ?unit = "-1"} = $http_server:parseparam(param, {"objnum", "unit"});
	unit = toobj(unit);
	if (this.owner != player)
		return $http_server:formsyntax(c);
	endif
	result = this:startbuilding(unit);
	if (result[1] != "")
		$http_server:error(c, "Failed! " + result[1], "/player/unit?objnum=" + tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

.program $god $factory:http_stop tnt
	{c, method, param} = args;
	if (this.owner != player)
		return $http_server:formsyntax(c);
	endif
	result = this:stopbuilding();
	if (result[1] != "")
		$http_server:error(c, "Failed! " + result[1], "/player/unit?objnum=" + tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- Move item from warehouse ------------------------------------------------

.program $god $factory:http_fromwarehouse tnt
	{c, method, param} = args;
	{class, target} = $http_server:parseparam(param, {"class", "target"});
	class = toobj(class);
	target = toobj(target);
	result = this:fromwarehouse(class, target);
	if (result[1] != "")
		$http_server:error(c, "Failed! " + result[1], "/player/unit?objnum=" + tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- HTTP operations ---------------------------------------------------------

.program $god $factory:http_info tnt
	{c} = args;
	if (!this.deployed)
		return;
	elseif (this.building == {})
		$htell(c, "(idle)");
	else
		$htell(c, "(building");
		$htell(c, this.building[1].name);
		$htell(c, "ETA");
		$htell(c, $numutils:timetostr(this.endtime) + ":");
		$htell(c, tostr(((time() - this.starttime) * 100) / (this.endtime - this.starttime)) + "% complete");
	endif
.

.program $god $factory:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd = ""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "deploy")
		return this:http_deploy(c, method, param);
	elseif (cmd == "mothball")
		return this:http_mothball(c, method, param);
	elseif (cmd == "start")
		return this:http_start(c, method, param);
	elseif (cmd == "stop")
		return this:http_stop(c, method, param);
	elseif (cmd == "fromwarehouse")
		return this:http_fromwarehouse(c, method, param);
	endif
	if (this.deployed == 0)
		$htell(c, "This unit must be deployed before it can be used.");
		$htell(c, "<BR><HR>");
		$htell(c, ("<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=\"objnum\" VALUE=\"" + tostr(toint(this))) + "\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"deploy\"><INPUT TYPE=submit VALUE=\"Deploy\"></FORM>");
		return;
	endif
	if (this.building == {})
		$htell(c, "<B>Currently building:</B>");
		$htell(c, "Nothing<BR><HR>");
		$htell(c, ("<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=\"objnum\" VALUE=\"" + tostr(toint(this))) + "\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"start\">");
		if (0)
			$htell(c, "Build new:<SELECT NAME=\"unit\">");
			for i in (this.buildable)
				$htell(c, ((("<OPTION VALUE=\"" + tostr(toint(i))) + "\">") + i.name) + "</OPTION>");
			endfor
			$htell(c, "</SELECT><INPUT TYPE=submit VALUE=\"Start\">");
		else
			$htell(c, "<TABLE WIDTH=100% COLS=3>");
			$htell(c, "<TR><TD WIDTH=1></TD>");
			$htell(c, "<TH ALIGN=left>Item</TH><TH ALIGN=left>Cost</TH><TH ALIGN=left>Time</TH>");
			$htell(c, "</TR>");
			for i in (this.buildable)
				$htell(c, "<TR><TD WIDTH=1><INPUT TYPE=radio NAME=\"unit\" VALUE=\""+tostr(toint(i))+"\"></TD>");
				$htell(c, "<TD>"+i.name+"</TD>");
				$htell(c, "<TD><FONT SIZE=-2>"+$http_server:reslist(@i.build_cost, 1)+"</FONT></TD>");
				$htell(c, "<TD>"+floatstr(i.build_time, 1)+"</TD>");
				$htell(c, "</TR>");
			endfor
			$htell(c, "</TABLE>");
			$htell(c, "<INPUT TYPE=submit VALUE=\"Start Building\">");
		endif
		$htell(c, "</FORM>");
		$htell(c, ("<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=\"objnum\" VALUE=\"" + tostr(toint(this))) + "\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"mothball\"><INPUT TYPE=submit VALUE=\"Mothball\"></FORM>");
	else
		$htell(c, "<B>Currently building:</B>");
		this:http_info(c);
		$htell(c, "<BR><HR>");
		$htell(c, ("<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=\"objnum\" VALUE=\"" + tostr(toint(this))) + "\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"stop\"><INPUT TYPE=submit VALUE=\"Stop Building\">(warning: you will lose all your current progress on this unit)</FORM>");
	endif
	if (this.warehouse != {})
		$http_server:startform(c, "/player/unit", objnum, "fromwarehouse");
		$htell(c, "Transfer <SELECT NAME=\"class\">");
		for i in (this.warehouse)
			$htell(c, ("<OPTION VALUE=\"" + tostr(toint(i))) + "\">");
			$htell(c, i.name);
			$htell(c, "</OPTION>");
		endfor
		$htell(c, "</SELECT> from warehouse to <SELECT NAME=\"target\">");
		i = this.location:contents();
		for i in (i)
			if (i:descendentof($fleet))
				$htell(c, ("<OPTION VALUE=\"" + tostr(toint(i))) + "\">");
				$htell(c, i.name);
				$htell(c, "</OPTION>");
			endif
		endfor
		$htell(c, "</SELECT><INPUT TYPE=submit VALUE=\"Transfer Unit\"></FORM>");
		$http_server:endform(c);
	endif
.

.quit

rem Revision History
rem $Log: factory.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

