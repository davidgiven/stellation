rem messagebuoy.moo
rem Message buoy.
.patch messagebuoy.moo 3 1
notify(player, "messagebuoy.moo");

$god:prop(#0, "messagebuoy", create($unit, $god));
$messagebuoy.name = "message buoy";

$god:prop($messagebuoy, "messages", {});
$god:prop($messagebuoy, "newmessages", 0);

$messagebuoy.description = "Message buoys are inert metal boxes containing static data storage. You can use them to leave messages in a system for other people to read.";

# --- Add a message -----------------------------------------------------------

.program $god $messagebuoy:add_message tnt
	{msg} = args;
	this.messages = {{player, "", time(), msg}, @this.messages};
	this.newmessages = this.newmessages + 1;
	this.location:notify("message added to message buoy.");
	return {""};
.

# --- Delete a message --------------------------------------------------------

.program $god $messagebuoy:delete_message tnt
	{msgno} = args;
	msgno = msgno + this.newmessages;
	try
		if (this.messages[msgno][1] != player)
			return {"You don't have permission to delete that message."};
		endif
		this.messages = listdelete(this.messages, msgno);
		this.location:notify("message removed from message buoy.");
		return {""};
	except (E_RANGE)
		return {"Message ID not recognised."};
	endtry
.

# --- HTML operations ---------------------------------------------------------

.program $god $messagebuoy:http_info tnt
	{c} = args;
	$htell(c, tostr(length(this.messages))+" messages");
.

.program $god $messagebuoy:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "addmsg")
		{body} = $http_server:parseparam(param, {"body"});
		if (typeof(body) == STR)
			body = {body};
		endif
		result = this:add_message(body);
		if (result[1] != "")
			$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
		else
			player:redirect(c, this);
		endif
	elseif (cmd == "delmsg")
		{msg} = $http_server:parseparam(param, {"msg"});
		msg = toint(msg);
		result = this:delete_message(msg);
		if (result[1] != "")
			$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
		else
			player:redirect(c, this);
		endif
	else
		$http_server:startform(c, "/player/unit", objnum, "addmsg");
		$htell(c, "<TEXTAREA NAME=\"body\" COLS=50 ROWS=10>");
		$htell(c, "</TEXTAREA>");
		$htell(c, "<INPUT TYPE=submit VALUE=\"Add Message\">");
		$http_server:endform(c);
		$htell(c, "<CENTER>");
		$htell(c, "<TABLE WIDTH=100% BORDER=2 COLS=1>");
		$htell(c, "<TR><TD ALIGN=center><B>START</B></TD></TR></TABLE>");
		i = 1;
		for m in (this.messages)
			$http_server:startform(c, "/player/unit", objnum, "delmsg");
			$htell(c, "<INPUT TYPE=hidden VALUE=\""+tostr(i)+"\" NAME=\"msg\">");
			$htell(c, "<TABLE WIDTH=100% BORDER=2 COLS=1>");
			$htell(c, "<TR><TD>From");
			$htell(c, "<B>"+m[1]:name()+"</B>");
			$htell(c, "at");
			$htell(c, "<B>"+$numutils:timetostr(m[3])+"</B>");
			$htell(c, "</TD></TR>");
			$htell(c, "<TR><TD><PRE>");
			for j in (m[4])
				$htell(c, j);
			endfor
			$htell(c, "</PRE></TD></TR>");
			if (m[1] == player)
				$htell(c, "<TR><TD ALIGN=center>");
				$htell(c, "<INPUT TYPE=submit NAME=\"cmd\" VALUE=\"Delete\">");
				$htell(c, "</TD></TR>");
			endif
			$htell(c, "</TABLE>");
			$http_server:endform(c);
			i = i + 1;
		endfor
		$htell(c, "<TABLE WIDTH=100% BORDER=2 COLS=1>");
		$htell(c, "<TR><TD ALIGN=center><B>END</B></TD></TR></TABLE>");
		$htell(c, "</CENTER>");
		this.newmessages = 0;
	endif
.

.quit

