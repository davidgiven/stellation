rem fleet.moo
rem Represents a fleet of ships.
rem $Source: /cvsroot/stellation/stellation/fleet.moo,v $
rem $State: Exp $

.patch fleet.moo 6 1
notify(player, "fleet.moo");

$god:prop(#0, "fleet", create($object, $god));
$fleet.name = "Generic Fleet";

# --- Constructor, destructor -------------------------------------------------

.program $god $fleet:destroy tnt
	this.owner.fleets = setremove(this.owner.fleets, this);
	return pass();
.
	
# --- Create an instance of a unit in this fleet ------------------------------

.program $god $fleet:create_unit tnt
	{unitclass} = args;
	if (unitclass:descendentof($ship))
		unit = unitclass:create();
		unit.name = unitclass.name;
		unit:moveto(this);
		unit:make_alive();
	else
		"Need to find an empty tug to put this unit into tow.";
		tugs = this:empty_tugs();
		if (tugs == {})
			return {"No empty tugs to pick up unit."};
		endif
		unit = unitclass:create();
		unit.name = unitclass.name;
		unit:moveto(tugs[1]);
	endif
	return {"", unit};
.

# --- Find ships of a given class ---------------------------------------------

.program $god $fleet:find_ships tnt
	{unitclass} = args;
	l = {};
	for i in (this:contents())
		if (i:descendentof(unitclass))
			l = {@l, i};
		endif
	endfor
	return l;
.

# --- Find list of empty tugs -------------------------------------------------

.program $god $fleet:empty_tugs tnt
	l = {};
	for i in (this:find_ships($tug))
		if (i:contents() == {})
			l = {@l, i};
		endif
	endfor
	return l;
.

# --- Accept a unit -----------------------------------------------------------

.program $god $fleet:accept tnt
	{what} = args;
	return 1;
.

# --- Something has consumed resources ----------------------------------------

.program $god $fleet:consume tnt
	{m, a, o} = args;
	cargoships = this:find_ships($cargoship);
	for i in (cargoships)
		if (!i:destroyresources(m, a, o))
			return 0;
		endif
	endfor
	return this.location:consume(m, a, o);
.

# --- Calculate the mass of the fleet -----------------------------------------

.program $god $fleet:mass tnt
	mass = 0.0;
	for i in (this:contents())
		mass = mass + i:mass();
	endfor
	return mass;
.

# --- Change the name of the fleet --------------------------------------------

.program $god $fleet:changename tnt
	{name} = args;
	if (name == "")
		return {"Fleet names can't be empty."};
	endif
	this.location:notify("fleet <B>"+this.name+"</B> has had its name changed to "+name+".");
	this.name = name;
	return {""};
.

# --- Create a new fleet ------------------------------------------------------

.program $god $fleet:createnewfleet tnt
	{?name=""} = args;
	if (name == "")
		return {"Fleet names can't be empty."};
	endif
	this.location:notify("new fleet <B>"+name+"</B> has just been created.");
	fleet = player:create_fleet();
	fleet:moveto(this.location);
	return {""};
.

# --- Disband an (empty) fleet ------------------------------------------------

.program $god $fleet:disband tnt
	if (this:contents() != {})
		return {"You can not disband a fleet that contains units."};
	endif
	this.location:notify("<B>"+this.name+"</B> has been disbanded.");
	this:destroy();
	return {""};
.

# --- Initiate FTL jump -------------------------------------------------------

.program $god $fleet:ftljump tnt
	{x, y, star} = args;
	dx = this.location.position[1] - x;
	dy = this.location.position[2] - y;
	distance = sqrt(dx*dx + dy*dy);
	mass = this:mass();
	cost = distance * mass;
	eta = toint(tofloat(time()) + distance*$tick);
	if (this:consume(0.0, cost, 0.0))
		return {"There isn't enough antimatter available to initiate the jump."};
	endif
	this.location:notify("outgoing transit bubble to <B>["+floatstr(x, 1)+", "+floatstr(y, 1)+"]</B> of mass <B>"+floatstr(mass, 1)+"</B>; ETA "+$numutils:timetostr(eta)+".");
	$transit:create(this.location.position, {x, y}, distance, this);
	if (star != #-1)
		star:notify("incoming transit bubble from <B>["+floatstr(x, 1)+", "+floatstr(y, 1)+"]</B> of mass <B>"+floatstr(mass, 1)+"</B>; ETA "+$numutils:timetostr(eta)+".");
	endif
	return {""};
.

# --- Change the name of the fleet --------------------------------------------

.program $god $fleet:http_changename tnt
	{c, method, param} = args;
	{objnum, cmd, ?name=""} = $http_server:parseparam(param, {"objnum", "cmd", "name"});
	result = this:changename(name);
	if (result[1] != "")
		$htell(c, "Failed!");
		$htell(c, result[1]);
	else
		player:redirect(c, this);
	endif
.

# --- Create a new fleet ------------------------------------------------------

.program $god $fleet:http_newfleet tnt
	{c, method, param} = args;
	{objnum, cmd, ?name=""} = $http_server:parseparam(param, {"objnum", "cmd", "name"});
	result = this:createnewfleet(name);
	if (result[1] != "")
		$htell(c, "Failed!");
		$htell(c, result[1]);
	else
		player:redirect(c, this);
	endif
.

# --- Disband the fleet -------------------------------------------------------

.program $god $fleet:http_disband tnt
	{c, method, param} = args;
	l = this.location;
	result = this:disband();
	if (result[1] != "")
		$htell(c, "Failed!");
		$htell(c, result[1]);
	else
		player:redirect(c, l);
	endif
.

# --- Start FTL jump to named star --------------------------------------------

.program $god $fleet:http_ftlstar tnt
	{c, method, param} = args;
	{objnum, cmd, ?name=""} = $http_server:parseparam(param, {"objnum", "cmd", "star"});
	"Search for the named star.";
	star = #-1;
	for i in ($galaxy.stars)
		if (i.name == name)
			star = i;
			break;
		endif
	endfor
	if (star == #-1)
		return $http_server:error(c, "There is no star with that name!", "/player/fleet?objnum="+tostr(toint(this)));
	endif
	suspend(0);
	return this:http_ftlxy(c, method, {{"objnum", "cmd", "x", "y"}, {objnum, cmd, tostr(star.position[1]), tostr(star.position[2])}});
.

# --- Start FTL jump to [x, y] location ---------------------------------------

.program $god $fleet:http_ftlxy tnt
	{c, method, param} = args;
	{objnum, cmd, ?x="", ?y="", ?confirm=""} = $http_server:parseparam(param, {"objnum", "cmd", "x", "y", "confirm"});
	if ((x == "") || (y == ""))
		return $http_server:formsyntax(c);
	endif
	x = tofloat(x);
	y = tofloat(y);
	"Ensure there's a jumpship in this fleet.";
	if (this:find_ships($jumpship) == {})
		return $http_server:error(c, "A fleet can't make FTL jumps without a jumpship.", "/player/fleet?objnum="+tostr(toint(this)));
	endif
	"Search for a star at this location.";
	star = #-1;
	for i in ($galaxy.stars)
		thisx = i.position[1];
		thisy = i.position[2];
		if (((x-thisx)*(x-thisx) + (y-thisy)*(y-thisy)) < 0.01)
			star = i;
			break;
		endif
	endfor
	dx = this.location.position[1] - x;
	dy = this.location.position[2] - y;
	distance = sqrt(dx*dx + dy*dy);
	cost = distance * this:mass();
	if (confirm != "y")
		$htell(c, "<B>Confirm FTL jump</B><P>");
		$htell(c, "Please confirm that you wish "+this.name+" to make an FTL jump to");
		if (star == #-1)
			$htell(c, "<B>interstellar space</B>");
		else
			$htell(c, "the star <B>"+star.name+"</B>");
		endif
		$htell(c, "at <B>["+tostr($numutils:round(10, x))+", "+tostr($numutils:round(10, y))+"]</B>,");
		$htell(c, "which is <B>"+tostr($numutils:round(10, distance))+"</B> parsecs away");
		$htell(c, "at a cost of <B>"+tostr($numutils:round(10, cost))+"</B> units of antimatter.");
		$htell(c, "Once the fleet has been launched, you will not be able to recall or communicate with it. Are you really sure you want to do this?");
		$http_server:startform(c, "/player/fleet", this, "ftlxy");
		$htell(c, "<INPUT TYPE=\"hidden\" NAME=\"x\" VALUE=\""+tostr(x)+"\">");
		$htell(c, "<INPUT TYPE=\"hidden\" NAME=\"y\" VALUE=\""+tostr(y)+"\">");
		$htell(c, "<INPUT TYPE=\"hidden\" NAME=\"confirm\" VALUE=\"y\">");
		$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Initiate Jump\">");
		$http_server:endform(c);
	else
		result = this:ftljump(x, y, star);
		if (result[1] != "")
			return $http_server:error(c, result[1], "/player/fleet?objnum="+tostr(toint(this)));
		endif
		player:redirect(c, this.location);
	endif
.

# --- HTML operations ---------------------------------------------------------

.program $god $fleet:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "changename")
		return this:http_changename(c, method, param);
	elseif (cmd == "newfleet")
		return this:http_newfleet(c, method, param);
	elseif (cmd == "ftlxy")
		return this:http_ftlxy(c, method, param);
	elseif (cmd == "ftlstar")
		return this:http_ftlstar(c, method, param);
	elseif (cmd == "ftlconfirm")
		return this:http_ftlconfirm(c, method, param);
	elseif (cmd == "disband")
		return this:http_disband(c, method, param);
	endif
	$http_server:startform(c, "/player/fleet", this, "changename");
	$htell(c, "<INPUT NAME=\"name\" SIZE=25 VALUE=\""+this.name+"\">");
	$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Change Name\">");
	$http_server:endform(c);
	$http_server:startform(c, "/player/fleet", this, "newfleet");
	$htell(c, "<INPUT NAME=\"name\" SIZE=25 VALUE=\""+player.name+"'s fleet no. "+tostr(player.fleetcount+1)+"\">");
	$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Create New Fleet\">");
	$http_server:endform(c);
	if (this:find_ships($jumpship) != {})
		"Jump to [x,y]";
		$http_server:startform(c, "/player/fleet", this, "ftlxy");
		$htell(c, "X:<INPUT NAME=\"x\" SIZE=5 VALUE=\""+tostr($numutils:round(10, this.location.position[1]))+"\">");
		$htell(c, "Y:<INPUT NAME=\"y\" SIZE=5 VALUE=\""+tostr($numutils:round(10, this.location.position[2]))+"\">");
		$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Jump to [x,y]\">");
		$http_server:endform(c);
		"Jump to star system";
		$http_server:startform(c, "/player/fleet", this, "ftlstar");
		$htell(c, "<INPUT NAME=\"star\" SIZE=25 VALUE=\""+this.location:name()+"\">");
		$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Jump to star\">");
		$http_server:endform(c);
	endif
	if (this:contents() == {})
		"Fleet is empty.";
		$http_server:startform(c, "/player/fleet", this, "disband");
		$htell(c, "<INPUT TYPE=\"submit\" VALUE=\"Disband fleet\">");
		$http_server:endform(c);
	endif
.

.quit

rem Revision History
rem $Log: fleet.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

