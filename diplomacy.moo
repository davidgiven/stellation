rem diplomacy.moo
rem External affairs.
rem $Source: /cvsroot/stellation/stellation/diplomacy.moo,v $
rem $State: Exp $

.patch diplomacy.moo 6 1
notify(player, "diplomacy.moo");

$god:prop($player, "neutralplayers", {});
$god:prop($player, "friendlyplayers", {});
$god:prop($player, "enemyplayers", {});

.program $god $player:neutralplayers tnt
	return this.neutralplayers;
.

.program $god $player:friendlyplayers tnt
	return this.friendlyplayers;
.

.program $god $player:enemyplayers tnt
	return this.enemyplayers;
.

.program $god $player:knownplayers tnt
	return {@this.friendlyplayers, @this.neutralplayers, @this.enemyplayers};
.

# --- See a player ------------------------------------------------------------

.program $god $player:seeplayer tnt
	{p} = args;
	if (p in this.friendlyplayers)
		return;
	endif
	if (p in this.enemyplayers)
		return;
	endif
	this.neutralplayers = setadd(this.neutralplayers, p);
	return {""};
.

# --- Make hostile/friendly ---------------------------------------------------

.program $god $player:makehostile tnt
	{p} = args;
	this:seeplayer(p);
	this.enemyplayers = setadd(this.enemyplayers, p);
	this.neutralplayers = setremove(this.neutralplayers, p);
	this.friendlyplayers = setremove(this.friendlyplayers, p);
	return {""};
.

.program $god $player:makefriendly tnt
	{p} = args;
	this:seeplayer(p);
	this.enemyplayers = setremove(this.enemyplayers, p);
	this.neutralplayers = setremove(this.neutralplayers, p);
	this.friendlyplayers = setadd(this.friendlyplayers, p);
	return {""};
.

.program $god $player:makeneutral tnt
	{p} = args;
	this:seeplayer(p);
	this.enemyplayers = setremove(this.enemyplayers, p);
	this.neutralplayers = setadd(this.neutralplayers, p);
	this.friendlyplayers = setremove(this.friendlyplayers, p);
	return {""};
.

# === HTML interface ==========================================================

# --- Compose a new message ---------------------------------------------------

.program $god $player:http_diplomacy_single tnt
	{c, method, param} = args;
	{?cmd=""} = $http_server:parseparam(param, {"cmd"});
	this:htmlheader(c, method, "External Affairs");
	if (cmd == "")
		$htell(c, "<TABLE COLS=3 WIDTH=100%>");
		$htell(c, "<TR><TD VALIGN=top ALIGN=left WIDTH=33%>");
		$htell(c, "<H3>Hostile Players</H3>");
		l = this:enemyplayers();
		if (l == {})
			$htell(c, "(none yet)");
		else
			for i in (l)
				$htell(c, "<A HREF=\"/player/frm?cmd=compose+new&to="+i:name()+"\">"+i:name()+"</A>");
				$htell(c, "<A HREF=\"/player/diplomacy?cmd=neutral&player="+tostr(toint(i))+"\">--></A><BR>");
			endfor
		endif
		$htell(c, "</TD><TD VALIGN=top WIDTH=33%>");
		$htell(c, "<H3>Neutral Players</H3>");
		l = this:neutralplayers();
		if (l == {})
			$htell(c, "(none yet)");
		else
			for i in (l)
				$htell(c, "<A HREF=\"/player/diplomacy?cmd=hostile&player="+tostr(toint(i))+"\">&lt;--</A>");
				$htell(c, "<A HREF=\"/player/frm?cmd=compose+new&to="+i:name()+"\">"+i:name()+"</A>");
				$htell(c, "<A HREF=\"/player/diplomacy?cmd=friendly&player="+tostr(toint(i))+"\">--></A><BR>");
			endfor
		endif
		$htell(c, "<FORM ACTION=\"/player/diplomacy\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"makeknown\">");
		$htell(c, "<INPUT NAME=\"name\" SIZE=20><BR>");
		$htell(c, "<INPUT TYPE=submit VALUE=\"Add Player\">");
		$htell(c, "</FORM>");
		$htell(c, "</TD><TD VALIGN=top WIDTH=33%>");
		$htell(c, "<H3>Friendly Players</H3>");
		l = this:friendlyplayers();
		if (l == {})
			$htell(c, "(none yet)");
		else
			for i in (l)
				$htell(c, "<A HREF=\"/player/diplomacy?cmd=neutral&player="+tostr(toint(i))+"\">&lt;--</A>");
				$htell(c, "<A HREF=\"/player/frm?cmd=compose+new&to="+i:name()+"\">"+i:name()+"</A>");
			endfor
		endif
		$htell(c, "</TD></TR></TABLE>");
	elseif (cmd == "makeknown")
		{?name} = $http_server:parseparam(param, {"name"});
		name = $player:find_player(name);
		if (name == $failed_match)
			$http_server:error(c, "Failed! That name does not belong to any player.", "/player/diplomacy");
		else
			this:seeplayer(name);
			$http_server:redirect(c, "/player/diplomacy");
		endif
	elseif (cmd == "hostile")
		{?p} = $http_server:parseparam(param, {"player"});
		p = toobj(p);
		result = {"Stop trying to cheat."};
		try
			if (p:descendentof($player))
				result = this:makehostile(p);
			endif
		except (ANY)
		endtry
		if (result[1] != "")
			$http_server:error(c, "Failed! "+result[1], "/player/diplomacy");
		else
			$http_server:redirect(c, "/player/diplomacy");
		endif
	elseif (cmd == "friendly")
		{?p} = $http_server:parseparam(param, {"player"});
		p = toobj(p);
		result = {"Stop trying to cheat."};
		try
			if (p:descendentof($player))
				result = this:makefriendly(p);
			endif
		except (ANY)
		endtry
		if (result[1] != "")
			$http_server:error(c, "Failed! "+result[1], "/player/diplomacy");
		else
			$http_server:redirect(c, "/player/diplomacy");
		endif
	elseif (cmd == "neutral")
		{?p} = $http_server:parseparam(param, {"player"});
		p = toobj(p);
		result = {"Stop trying to cheat."};
		try
			if (p:descendentof($player))
				result = this:makeneutral(p);
			endif
		except (ANY)
		endtry
		if (result[1] != "")
			$http_server:error(c, "Failed! "+result[1], "/player/diplomacy");
		else
			$http_server:redirect(c, "/player/diplomacy");
		endif
	else
		$htell(c, "cmd=["+cmd+"]");
	endif
.

.quit

rem Revision History
rem $Log: diplomacy.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

