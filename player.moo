rem player.moo
rem Player superclass.
rem $Source: /cvsroot/stellation/stellation/player.moo,v $
rem $State: Exp $

.patch player.moo 3 1
notify(player, "player.moo");

$god:prop(#0, "player", create($object, $god));
$player.name = "Generic player";

# --- Generic player properties -----------------------------------------------

$god:prop($player, "password", crypt(""));
$god:prop($player, "email", "");
$god:prop($player, "empire", "");
$god:prop($player, "description", "");
$god:prop($player, "news", {});
$god:prop($player, "newnews", 0);
$god:prop($player, "fleetcount", 0);
$god:prop($player, "fleets", {});
$god:prop($player, "mapmode", "text");
$god:prop($player, "mapwidth", 75);
$god:prop($player, "mapheight", 20);
$god:prop($player, "mapaspect", 0.5);
$god:prop($player, "mapdefaultscale", 15.0);
$god:prop($player, "displaymode", 1);

# =============================================================================
#                              PLAYER OPERATIONS
# =============================================================================

# These methods actually do the work of manipulating the game state.

# --- Create a new player -----------------------------------------------------

.program $god $player:create_player tnt
	{name, password, email, empire, description} = args;
	if (length(name) < 4)
		return {"Your name must be at least four letters long."};
	endif
	if (length(password) < 4)
		return {"Your password must be at least four characters long."};
	endif
	if (email == "")
		return {"You must supply a valid email address."};
	endif
	if ($player:find_player(name) != $failed_match)
		return {"That name is already taken."};
	endif
	n = $player:create();
	n.owner = n;
	n.name = name;
	n.password = crypt(password);
	n.email = email;
	n.empire = empire;
	n.description = description;
	set_player_flag(n, 1);
	player = n;
	return n:init();
.

.program $god $player:init tnt
	fleet = this:create_fleet();
	star = $galaxy.stars[random(length($galaxy.stars))];
	move(fleet, star);
	fleet:create_unit($jumpship);
	fleet:create_unit($tug);
	fleet:create_unit($basicfactory);
	fleet:create_unit($tug);
	fleet:create_unit($metalmine);
	fleet:create_unit($tug);
	fleet:create_unit($antimatterdistillery);
	fleet:create_unit($tug);
	fleet:create_unit($hydroponicsplant);
	{x, n} = fleet:create_unit($cargoship);
	n.cargo = {100000.0, 100000.0, 100000.0};
	return {"", this};
.

# --- Set the player's properties ---------------------------------------------

.program $god $player:set_name tnt
	{name} = args;
	if (this.name == name)
		return {""};
	endif
	for i in (players())
		if ((i != this) && (i.name == name))
			return {"That name is already in use."};
		endif
	endfor
	this.name = name;
	return {""};
.

.program $god $player:set_empire tnt
	{empire} = args;
	if (this.empire == empire)
		return {""};
	endif
	for i in (players())
		if ((i != this) && (i.empire == empire))
			return {"That name is already in use."};
		endif
	endfor
	this.empire = empire;
	return {""};
.

.program $god $player:set_email tnt
	{email} = args;
	this.email = email;
	return {""};
.

.program $god $player:set_mapwidth tnt
	{mapwidth} = args;
	mapwidth = toint(mapwidth);
	if (mapwidth <= 0)
		return {"You can't make the map narrower than 0 characters."};
	endif
	this.mapwidth = mapwidth;
	return {""};
.

.program $god $player:set_mapheight tnt
	{mapheight} = args;
	mapheight = toint(mapheight);
	if (mapheight <= 0)
		return {"You can't make the map shorter than 0 characters."};
	endif
	this.mapheight = mapheight;
	return {""};
.

.program $god $player:set_mapdefaultscale tnt
	{mapdefaultscale} = args;
	mapdefaultscale = tofloat(mapdefaultscale);
	if (mapdefaultscale <= 0.1)
		return {"You can't make the map smaller than that."};
	endif
	this.mapdefaultscale = mapdefaultscale;
	return {""};
.

.program $god $player:set_mapaspect tnt
	{mapaspect} = args;
	mapaspect = tofloat(mapaspect);
	if (mapaspect <= 0.1)
		return {"Aspect ratios have to be bigger than that."};
	endif
	this.mapaspect = mapaspect;
	return {""};
.

.program $god $player:set_mapmode tnt
	{mapmode} = args;
	if ((mapmode != "text") && (mapmode != "gif"))
		return {"That is not a valid map mode."};
	endif
	this.mapmode = mapmode;
	return {""};
.

# --- Create a new fleet belonging to the player ------------------------------

.program $god $player:create_fleet tnt
	{?name=""} = args;
	fleet = create($fleet, this);
	this.fleetcount = this.fleetcount+1;
	if (name == "")
		name = this:name()+"'s fleet no. "+tostr(this.fleetcount);
	endif
	fleet.name = name;
	this.fleets = {@this.fleets, fleet};
	return fleet;
.

# --- Return a list of all fleets belonging to the player ---------------------

.program $god $player:fleets tnt
	{?star=0} = args;
	if (star == 0)
		return this.fleets;
	endif
	l = {};
	for i in (this.fleets)
		if (i.location == star)
			l = {@l, i};
		endif
	endfor
	return l;
.

# --- Return a list of all fleets visible to one particular fleet -------------

.program $god $player:visible_fleets tnt
	{fleet} = args;
	star = fleet.location;
	l = {};
	notify($god, "Star contents: "+toliteral(star:contents()));
	for i in (star:contents())
		if (i:descendentof($fleet) && (i.owner == player) && (i != fleet))
			l = setadd(l, i);
		endif
	endfor
	notify($god, "Visible fleets: "+toliteral(l));
	return l;
.

# --- Return a list of all star systems visible to the player -----------------

.program $god $player:starsystems tnt
	s = {};
	for i in (this:fleets())
		if (i:find_ships($jumpship) != {})
			s = setadd(s, i.location);
		endif
	endfor
	return s;
.

# --- Find a player by name ---------------------------------------------------

.program $god $player:find_player tnt
	{name} = args;
	for i in (players())
		if (i:name() == name)
			return i;
		endif
	endfor
	return $failed_match;
.

# --- Notify the player of some news ------------------------------------------

.program $god $player:notify tnt
	{msg, ?star=#0} = args;
	if (star == #0)
		p = #0;
	else
		p = star.position;
	endif
	this.news = {{time(), p, player, msg}, @this.news};
	this.newnews = 1;
.

# =============================================================================
#                             HTML USER INTERFACE
# =============================================================================

# These methods comprise the bulk of the player-specific user interface. Most
# unit- or fleet-specific parts are implemented in the appropriate objects, but
# this does most of the work.

# --- Top-level pages ---------------------------------------------------------

# We can display in frames or tables. This entails some rather complex code.

.program $god $player:toplevel tnt
	{c, left, middle, ?right={}} = args;
	{left, leftp} = left;
	{middle, middlep} = middle;
	if (right != {})
		{right, rightp} = right;
	endif
	if (player.displaymode == 0)
		"Frames.";
		$http_server:htmlheader(c, 200, player:name()+" of the "+player.empire, 1);
		leftp = $http_server:p2s(leftp);
		middlep = $http_server:p2s(middlep);
		if (right == {})
			$htell(c, "<frameset cols=\"25%,1*\" topmargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" framespacing=\"0\">");
		else
			$htell(c, "<frameset cols=\"25%,30%,1*\" topmargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" framespacing=\"0\">");
			rightp = $http_server:p2s(rightp);
		endif
		$htell(c, "<frame src=\"/player/"+left+"_single?"+leftp+"\">");
		$htell(c, "<frame src=\"/player/"+middle+"_single?"+middlep+"\">");
		if (right == {})
			$htell(c, "<frame src=\"/player/"+right+"_single?"+rightp+"\">");
		endif
		$htell(c, "</frameset>");
		$http_server:htmlfooter(c);
	elseif (player.displaymode == 1)
		"Tables.";
		$http_server:htmlheader(c, 200, player:name()+" of the "+player.empire, 1);
		leftp = $http_server:s2p(leftp);
		middlep = $http_server:s2p(middlep);
		if (right == {})
			$htell(c, "<TABLE WIDTH=100% COLS=2><TR><TD WIDTH=25% ALIGN=left VALIGN=top>");
		else
			$htell(c, "<TABLE WIDTH=100% COLS=3><TR><TD WIDTH=25% ALIGN=left VALIGN=top>");
			rightp = $http_server:s2p(rightp);
		endif
		player:("http_"+left+"_single")(c, "", leftp);
		if (right == {})
			$htell(c, "<TD WIDTH=75% ALIGN=left VALIGN=top>");
			player:("http_"+middle+"_single")(c, "", middlep);
			$htell(c, "</TD>");
		else
			$htell(c, "<TD WIDTH=30% ALIGN=left VALIGN=top>");
			player:("http_"+middle+"_single")(c, "", middlep);
			$htell(c, "</TD>");
			$htell(c, "<TD WIDTH=45% ALIGN=left VALIGN=top>");
			player:("http_"+right+"_single")(c, "", rightp);
			$htell(c, "</TD>");
		endif
		$htell(c, "</TR></TABLE>");
		$http_server:htmlfooter(c);
	else
		raise(E_INVARG, "Invalid display mode");
	endif
.

.program $god $player:http_star tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"star", param}, {"blank", ""});
.

.program $god $player:http_map tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"map", param});
.

.program $god $player:http_preferences tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"preferences", param});
.

.program $god $player:http_news tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"news", ""});
.

.program $god $player:http_frm tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"frm", param});
.

.program $god $player:http_diplomacy tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"diplomacy", param});
.

.program $god $player:http_fleet tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"fleet", param}, {"fleetdetails", param});
.

.program $god $player:http_unit tnt
	{c, method, param} = args;
	{?objnum="-1"} = $http_server:parseparam(param, {"objnum"});
	objnum = toobj(objnum);
	if ((objnum == #-1) || (!objnum:descendentof($unit)) || (objnum.owner != player))
		$http_server:formsyntax(c);
		return;
	endif
	if (objnum.location:descendentof($fleet))
		this:toplevel(c, {"left", ""}, {"fleet", "objnum="+tostr(toint(objnum.location))}, {"unit", param});
	else
		this:toplevel(c, {"left", ""}, {"star", "objnum="+tostr(toint(objnum.location))}, {"unit", param});
	endif
.

.program $god $player:htmlheader tnt
	{c, method, title} = args;
	if (method == "")
		$htell(c, "<H1>"+title+"</H1>");
	else
		$http_server:htmlheader(c, 200, title);
	endif
.

.program $god $player:htmlfooter tnt
	{c, method} = args;
	if (method != "")
		$http_server:htmlfooter(c);
	endif
.

# --- Update fleet & unit displays --------------------------------------------

.program $god $player:redirect tnt
	{c, unit} = args;
	if (unit:descendentof($fleet))
		$http_server:redirect(c, "/player/fleet?objnum="+tostr(toint(unit)));
	elseif (unit:descendentof($star))
		$http_server:redirect(c, "/player/star?objnum="+tostr(toint(unit)));
	else
		$http_server:redirect(c, "/player/unit?objnum="+tostr(toint(unit)));
	endif
.

# --- Process an HTTP request -------------------------------------------------

.program $god $player:httprequest tnt
	{c, method, path} = args;
	{cmd, path} = $stringutils:firstword(path, "?");
	if (cmd == "")
		cmd = "index";
	endif
	cmd = "http_"+cmd;
	parameters = $http_server:s2p(path);
	if (!(cmd in player:verbs()))
		$http_server:htmlheader(c, 404, "Not found");
		$htell(c, "That URL does not exist.<P>");
		$htell(c, "Magic follows:<P>");
		$htell(c, "No verb "+cmd+" on player object");
		$htell(c, player:name()+" ("+toliteral(player)+").");
		$htell(c, "Call arguments:<BR>");
		$htell(c, "<UL>");
		for i in [1..length(parameters[1])]
			$htell(c, "<LI>");
			$htell(c, parameters[1][i]);
			$htell(c, "=");
			$htell(c, parameters[2][i]);
		endfor
		$htell(c, "</UL>");
		http_server:htmlfooter(c);
		return;
	endif
	"Reset tick counters.";
	suspend(0);
	player:(cmd)(c, method, parameters);
.

# --- Blank page --------------------------------------------------------------

.program $god $player:http_blank_single tnt
	{c, method, param} = args;
	this:htmlheader(c, method, "");
	this:htmlfooter(c, method);
.

# --- Main index --------------------------------------------------------------

.program $god $player:http_index tnt
	{c, method, param} = args;
	this:toplevel(c, {"left", ""}, {"index", ""});
.

# --- Title page --------------------------------------------------------------

.program $god $player:http_index_single tnt
	{c, method, param} = args;
	this:htmlheader(c, method, "");
	$htell(c, "<TABLE WIDTH=100% COLS=10 BORDER=0><TR>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>S</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>T</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>E</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>L</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>L</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>A</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>T</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>I</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>O</TD>");
	$htell(c, "<TD ALIGN=center WIDTH=10%>N</TD>");
	$htell(c, "</TR></TABLE>");
	$htell(c, "<H3>Message of the day</H3>");
	for i in ($server_options.motd)
		$htell(c, i);
		$htell(c, "<HR>");
	endfor
	this:htmlfooter(c, method);
.

# --- Left frame --------------------------------------------------------------

# General index

.program $god $player:http_left_single tnt
	{c, method, param} = args;
	this:htmlheader(c, method, player:name()+" of the "+player.empire);
	$htell(c, "<HR>");
	$htell(c, "<B>Current time:</B>");
	$htell(c, $numutils:timetostr(time()));
	$htell(c, "<P><HR>");
	$htell(c, "<A HREF=\"/player\" TARGET=\"_top\">Game Status</A>");
	$htell(c, "<BR><A HREF=\"/player/preferences\" TARGET=\"_top\">Internal Affairs</A>");
	$htell(c, "<BR><A HREF=\"/player/diplomacy\" TARGET=\"_top\">External Relations</A>");
	if (this.newmail)
		$htell(c, "<BR><A HREF=\"/player/frm\" TARGET=\"_top\"><B>Flaw Resonance Messages</B></A>");
	else
		$htell(c, "<BR><A HREF=\"/player/frm\" TARGET=\"_top\">Flaw Resonance Messages</A>");
	endif
	if (this.newnews)
		$htell(c, "<BR><A HREF=\"/player/news\" TARGET=\"_top\"><B>Intelligence</B></A>");
	else
		$htell(c, "<BR><A HREF=\"/player/news\" TARGET=\"_top\">Intelligence</A>");
	endif
	$htell(c, "<BR><A HREF=\"/player/map\" TARGET=\"_top\">Stellar Cartography</A>");
	$htell(c, "<BR><HR>");
	$htell(c, "</CENTER>");
	for i in (this:starsystems())
		$htell(c, "<A HREF=\"/player/star?objnum="+tostr(toint(i))+"\" TARGET=\"_top\"><B>"+i:name()+"</B></A>");
		if (!i:descendentof($transit))
			$htell(c, "<A HREF=\"/player/map?x="+tostr(i.position[1])+"&y="+tostr(i.position[2])+"&scale="+tostr(this.mapdefaultscale)+"\" TARGET=\"_top\">("+tostr(i.position[1])+", "+tostr(i.position[2])+")</A>");
		endif
		$htell(c, "<BR>");
		if (i:contents() != {})
			for j in (i.contents)
				if (j:descendentof($fleet))
					if (i:descendentof($transit))
						$htell(c, "&nbsp;&nbsp;"+j:name()+"<BR>");
					else
						$htell(c, "&nbsp;&nbsp;<A HREF=\"/player/fleet?objnum="+tostr(toint(j))+"\" TARGET=\"_top\">"+j:name()+"</A>");
						if (j.owner == player)
							$htell(c, "<BR>");
						else
							$htell(c, "(belonging to");
							$htell(c, j.owner:name());
							$htell(c, "<BR>");
							this:seeplayer(j.owner);
						endif
					endif
				endif
			endfor
		endif
		$htell(c, "<BR>");
	endfor
	this:htmlfooter(c, method);
.

# --- Middle frame ------------------------------------------------------------

.program $god $player:http_middleframe tnt
	{c, method, param} = args;
	{?middle = "", ?right = ""} = $http_server:parseparam(param, {"middle", "right"});
	$http_server:htmlheader(c, 200, "Fnord!", 1);
	$htell(c, "<frameset cols=\"40%,1*\" topmargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" framespacing=\"0\">");
	$htell(c, "<frame src=\""+middle+"\" name=\"middle\">");
	$htell(c, "<frame src=\""+right+"\" name=\"right\">");
	$htell(c, "</frameset>");
	$http_server:htmlfooter(c);
.

# --- Intelligence ------------------------------------------------------------

.program $god $player:http_news_single tnt
	{c, method, param} = args;
	this:htmlheader(c, method, "Intelligence");
	$htell(c, "<TABLE BORDER=0 COLS=4 WIDTH=100%>");
	$htell(c, "<TR><TD WIDTH=5% ALIGN=center><B>Time</B></TD><TD WIDTH=5% ALIGN=center><B>Player</B></TD><TD WIDTH=5% ALIGN=center><B>Location</B></TD><TD><B>Message</B></TD></TR>");
	for i in (this.news)
		$htell(c, "<TR><TD VALIGN=top ALIGN=center>");
		$htell(c, $numutils:timetostr(i[1]));
		$htell(c, "</TD><TD VALIGN=top ALIGN=center>");
		$htell(c, i[3]:name());
		$htell(c, "</TD><TD VALIGN=top ALIGN=center>");
		if (i[2] != #0)
			$htell(c, "["+floatstr(i[2][1], 1)+", "+floatstr(i[2][2], 1)+"]");
		endif
		$htell(c, "</TD><TD VALIGN=top ALIGN=left>");
		$htell(c, i[4]);
		$htell(c, "</TD></TR>");
	endfor
	$htell(c, "</TABLE>");
	this.newnews = 0;
	this:htmlfooter(c, method);
.

# --- Star system information -------------------------------------------------

.program $god $player:http_star_single tnt
	{c, method, param} = args;
	{?objnum = "-1"} = $http_server:parseparam(param, {"objnum"});
	objnum = toobj(objnum);
	if ((objnum == #-1) || (!objnum:descendentof($star)) || (!objnum:isvisible()))
		$http_server:formsyntax(c);
		return;
	endif
	this:htmlheader(c, method, objnum:name());
	$htell(c, "<HR>");
	objnum:http_menu(c, method, param);
	this:htmlfooter(c, method);
.

# --- Fleet information -------------------------------------------------------

.program $god $player:http_fleet_single tnt
	{c, method, param} = args;
	{?objnum = "-1"} = $http_server:parseparam(param, {"objnum"});
	objnum = toobj(objnum);
	if ((objnum == #-1) || (!objnum:descendentof($fleet)) || (objnum.owner != player) || (objnum.location:descendentof($transit)))
		$http_server:formsyntax(c);
		return;
	endif
	this:htmlheader(c, method, objnum:name());
	$htell(c, "<HR><B>Location:</B>");
	i = objnum.location;
	$htell(c, "<A HREF=\"/player/star?objnum="+tostr(toint(i))+"\"><B>"+i:name()+"</B></A>");
	$htell(c, "<A HREF=\"/player/map?x="+tostr(i.position[1])+"&y="+tostr(i.position[2])+"&scale="+tostr(this.mapdefaultscale)+"\" TARGET=\"_top\">("+tostr(i.position[1])+", "+tostr(i.position[2])+")</A>");
	if (objnum.owner != player)
		$htell(c, "<BR><B>Owned by:</B>");
		$htell(c, objnum.owner:name());
	endif
	$htell(c, "<BR><B>Total mass:</B>");
	$htell(c, $numutils:round(10, objnum:mass()));
	$htell(c, "<BR><HR>");
	$htell(c, "<B>Fleet contents:</B>");
	$htell(c, "<UL>");
	contents = objnum:contents();
	if (contents != {})
		for i in (contents)
			if (objnum.owner == player)
				$htell(c, "<LI><A HREF=\"/player/unit?objnum="+tostr(toint(i))+"\" TARGET=\"_top\">"+i:name()+"</A>");
			else
				$htell(c, "<LI>"+i:name());
				this:see_player(objnum.owner);
			endif
			i:http_info(c);
		endfor
	else
		$htell(c, "<LI>(Empty)");
	endif
	$htell(c, "</UL>");
	this:htmlfooter(c, method);
.

# --- Operations on an individual unit ----------------------------------------

.program $god $player:http_unit_single tnt
	{c, method, param} = args;
	{?objnum = "-1", ?cmd=""} = $http_server:parseparam(param, {"objnum"});
	objnum = toobj(objnum);
	if ((objnum == #-1) || (!objnum:descendentof($unit)) || (objnum.owner != player))
		$http_server:formsyntax(c);
		return;
	endif
	this:htmlheader(c, method, objnum:name());
	if (cmd == "")
		$htell(c, "<HR>");
		$htell(c, objnum.description);
		$htell(c, "<HR>");
		$htell(c, "<B>Total Mass:</B>");
		$htell(c, tostr($numutils:round(10, objnum:mass())));
		$htell(c, "<BR><B>Running costs:</B>");
		$htell(c, $http_server:reslist(@objnum:time_cost()));
		$htell(c, "<BR><B>Damage:</B>");
		$htell(c, tostr(objnum.damage)+"/"+tostr(objnum.maxdamage));
		$htell(c, "<BR><HR>");
	endif
	objnum:http_menu(c, method, param);
	notify($god, "cmd="+cmd);
	if ((cmd == "") && objnum:descendentof($ship))
		f = this:visible_fleets(objnum.location);
		if (f != {})
			$http_server:startform(c, "/player/unit", objnum, "transfer");
			$htell(c, "Transfer unit to: <SELECT NAME=\"target\">");
			for i in (f)
				$htell(c, "<OPTION VALUE=\""+tostr(toint(i))+"\">");
				$htell(c, i.name);
				$htell(c, "</OPTION>");
			endfor
			$htell(c, "</SELECT><INPUT TYPE=submit VALUE=\"Transfer Unit\"></FORM>");
			$http_server:endform(c);
		endif
	endif
	this:htmlfooter(c, method);
.

# --- Operations on a complete fleet ------------------------------------------

.program $god $player:http_fleetdetails_single tnt
	{c, method, param} = args;
	{?objnum = "-1", ?cmd=""} = $http_server:parseparam(param, {"objnum"});
	objnum = toobj(objnum);
	if ((objnum == #-1) || (!objnum:descendentof($fleet)) || (objnum.owner != player))
		$http_server:formsyntax(c);
		return;
	endif
	this:htmlheader(c, method, objnum:name());
	$htell(c, "<HR>");
	objnum:http_menu(c, method, param);
	this:htmlfooter(c, method);
.

# --- Log the player out ------------------------------------------------------

.program $god $player:http_logout tnt
	{c, method, param} = args;
	$http_server:htmlheader(c, 401, "Logged out");
	$http_server:anchor(c, "Back to the main page",
		"/");
	$http_server:htmlfooter(c);
.

# --- Draw a map --------------------------------------------------------------

.program $god $player:http_map_single tnt
	{c, method, param} = args;
	{?x = "0.0", ?y = "0.0", ?scale = "1.0"} = $http_server:parseparam(param, {"x", "y", "scale"});
	x = tofloat(x);
	y = tofloat(y);
	scale = tofloat(scale);
	this:htmlheader(c, method, "Stellar Cartography");
	$htell(c, "<HR>");
	if (scale > 2.0)
		buf = {{$mapper.GRID, x-tofloat(toint(x)), y-tofloat(toint(y)), scale}};
	else
		buf = {};
	endif
	for i in ($galaxy.stars)
		sx = (i.position[1]-x)*scale;
		sy = (y-i.position[2])*scale;
		buf = {@buf, {$mapper.STAR, sx, sy, i:name()}};
	endfor
	$htell(c, "<CENTER>");
	$mapper:render(c, @buf);
	$htell(c, "<FORM ACTION=\"/player/map\">");
	$htell(c, "<TABLE BORDER=0 ROWS=3 COLS=2 WIDTH=50%>");
	$htell(c, "<TR><TH>Map Center:</TH>");
	$htell(c, "<TD><INPUT NAME=\"x\" VALUE=\""+tostr(x)+"\" SIZE=5></TD>");
	$htell(c, "<TD><INPUT NAME=\"y\" VALUE=\""+tostr(y)+"\" SIZE=5></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TH>Scale:</TH>");
	$htell(c, "<TD><INPUT NAME=\"scale\" VALUE=\""+tostr(scale)+"\" SIZE=5></TD>");
	$htell(c, "</TR>");
	$htell(c, "<TR><TD></TD><TD COLSPAN=2 ALIGN=center>");
	$htell(c, "<INPUT TYPE=submit></TD></TR>");
	$htell(c, "</TABLE>");
	$htell(c, "</FORM>");
	$htell(c, "</CENTER>");
	this:htmlfooter(c, method);
.

# --- User preferences --------------------------------------------------------

.program $god $player:http_preferences_single tnt
	{c, method, param} = args;
	{?name=player.name, ?empire=player.empire, ?email=player.email, ?mapwidth=tostr(player.mapwidth), ?mapheight=tostr(player.mapheight), ?mapdefaultscale=tostr(player.mapdefaultscale), ?mapaspect=tostr(player.mapaspect), ?mapmode=player.mapmode} = $http_server:parseparam(param, {"name", "empire", "email", "mapwidth", "mapheight", "mapdefaultscale", "mapaspect", "mapmode"});
	this:htmlheader(c, method, "Internal Affairs");
	$htell(c, "<HR>");
	notify($god, toliteral(param));
	notify($god, "name="+tostr(name));
	result = player:set_name(name);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_empire(empire);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_email(email);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_mapwidth(mapwidth);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_mapheight(mapheight);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_mapdefaultscale(mapdefaultscale);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_mapaspect(mapaspect);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	result = player:set_mapmode(mapmode);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/preferences");
	endif
	$htell(c, "<FORM ACTION=\"/player/preferences\">");
	$htell(c, "<TABLE BORDER=0 COLS=2 WIDTH=70% ALIGN=center>");
	$htell(c, "<TR><TD ALIGN=right>Name of player:<BR>(You will need to reauthenticate if you change this)</TD>");
	$htell(c, "<TD ALIGN=left WIDTH=10%><INPUT NAME=\"name\" VALUE=\""+player.name+"\" SIZE=32></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Name of empire:</TD>");
	$htell(c, "<TD ALIGN=left><INPUT NAME=\"empire\" VALUE=\""+player.empire+"\" SIZE=32></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Email address:</TD>");
	$htell(c, "<TD ALIGN=left><INPUT NAME=\"email\" VALUE=\""+player.email+"\" SIZE=32></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Map width and height:</TD>");
	$htell(c, "<TD ALIGN=left>");
	$htell(c, "<INPUT NAME=\"mapwidth\" VALUE=\""+tostr(player.mapwidth)+"\" SIZE=14>");
	$htell(c, "<INPUT NAME=\"mapheight\" VALUE=\""+tostr(player.mapheight)+"\" SIZE=14></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Default map scale:</TD>");
	$htell(c, "<TD ALIGN=left><INPUT NAME=\"mapdefaultscale\" VALUE=\""+tostr(player.mapdefaultscale)+"\" SIZE=14></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Map aspect ratio:</TD>");
	$htell(c, "<TD ALIGN=left><INPUT NAME=\"mapaspect\" VALUE=\""+tostr(player.mapaspect)+"\" SIZE=14></TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD ALIGN=right>Map type:</TD>");
	$htell(c, "<TD ALIGN=left><INPUT TYPE=radio NAME=\"mapmode\" VALUE=\"text\">Nasty ASCII art<BR>");
	$htell(c, "<INPUT TYPE=radio NAME=\"mapmode\" VALUE=\"gif\">Remotely rendered GIF</TD>");
	$htell(c, "</TR><TR>");
	$htell(c, "<TD></TD>");
	$htell(c, "<TD VALIGN=top ALIGN=left><INPUT TYPE=submit VALUE=\"Submit Changes\"></TD>");
	$htell(c, "</TR></TABLE>");
	$htell(c, "</FORM>");
.

# --- Make sure that God is a player ------------------------------------------

chparent($god, $player);

.quit

rem Revision History
rem $Log: player.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

