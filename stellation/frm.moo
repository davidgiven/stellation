rem frm.moo
rem Flaw Resonance Mail.
.patch frm.moo 3 1
notify(player, "frm.moo");

$god:prop($player, "newmail", 0);
$god:prop($player, "mail", {});

# --- Deliver mail ------------------------------------------------------------

.program $god $player:delivermail tnt
	{from, subject, msg} = args;
	this.mail = {{from, subject, time(), msg}, @this.mail};
	this.newmail = this.newmail + 1;
	this:notify("Incoming FRM from <B>"+from:name()+"</B>: subject <B>"+subject+"</B>.");
	return {""};
.

# --- Send mail ---------------------------------------------------------------

.program $god $player:sendmail tnt
	{to, subject, msg} = args;
	if (typeof(to) == LIST)
		for i in (to)
			r = this:sendmail(i, subject, msg);
			if (r[1] != "")
				return r;
			endif
		endfor
		return {""};
	endif
	try
		if (!to:descendentof($player))
			return {"FRM address not recognised."};
		endif
	except (E_INVIND)
		return {"FRM address not recognised."};
	endtry
	if (typeof(msg) == STR)
		msg = {msg};
	endif
	return to:delivermail(this, subject, msg);
.

# --- Delete a message --------------------------------------------------------

.program $god $player:deletemail tnt
	{msgno} = args;
	try
		this.mail = listdelete(this.mail, msgno);
		return {""};
	except (E_RANGE)
		return {"FRM message ID not recognised."};
	endtry
.

# --- Retrieve mail -----------------------------------------------------------

.program $god $player:mail tnt
	return this.mail;
.

# === HTML interface ==========================================================

# --- Compose a new message ---------------------------------------------------

.program $god $player:http_frm_compose tnt
	{c, title, to, subject, body} = args;
	$htell(c, "<H3>"+title+"</H3>");
	$htell(c, "<P>");
	$htell(c, "<FORM ACTION=\"/player/frm\">");
	$htell(c, "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"send\">");
	$htell(c, "<TABLE WIDTH=100% COLS=2>");
	$htell(c, "<TR><TD WIDTH=10%>");
	$htell(c, "<TABLE WIDTH=10% BORDER=0>");
	$htell(c, "<TR><TD ALIGN=right>To:</TD>");
	$htell(c, "<TD ALIGN=right><INPUT NAME=\"to\" VALUE=\""+to+"\" SIZE=50></TD>");
	$htell(c, "</TR>");
	$htell(c, "<TR><TD ALIGN=right>Subject:</TD>");
	$htell(c, "<TD ALIGN=right><INPUT NAME=\"subject\" VALUE=\""+subject+"\" SIZE=50></TD>");
	$htell(c, "</TR>");
	$htell(c, "<TR><TD COLSPAN=2>");
	$htell(c, "<TEXTAREA NAME=\"body\" COLS=60 ROWS=20>");
	for i in (body)
		$htell(c, "> "+i);
	endfor
	$htell(c, "</TEXTAREA>");
	$htell(c, "</TD></TR></TABLE>");
	$htell(c, "</TD><TD ALIGN=left VALIGN=top>");
	$htell(c, "<CENTER>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Send Message\">");
	$htell(c, "</CENTER>");
	$htell(c, "You can send a message to multiple recipients by specifying a list of players seperated by white space. There is no way to send messages to all players; don't even ask.");
	$htell(c, "<B>Not implemented yet</B>");
	$htell(c, "</TD></TR></TABLE></FORM>");
	$htell(c, "<B>Known players:</B>");
	for i in (player.knownplayers)
		$htell(c, i:name());
	endfor
.

# --- Read mail ---------------------------------------------------------------

.program $god $player:http_frm_single tnt
	{c, method, param} = args;
	{?cmd=""} = $http_server:parseparam(param, {"cmd"});
	this:htmlheader(c, method, "Flaw Resonance Message Centre");
	if (cmd == "")
		$htell(c, "<CENTER>");
		$htell(c, "<TABLE WIDTH=75% BORDER=0 COLS=2>");
		$htell(c, "<TR><TD VALIGN=top ALIGN=left>");
		$htell(c, "<B>"+tostr(length(this.mail))+"</B> messages");
		$htell(c, "<BR><B>"+tostr(this.newmail)+"</B> new messages");
		$htell(c, "</TD><TD VALIGN=top ALIGN=right>");
		$htell(c, "<FORM ACTION=\"/player/frm\"><INPUT TYPE=submit NAME=\"cmd\" VALUE=\"Compose New\"></FORM>");
		$htell(c, "</TD></TR></TABLE>");
		i = 1;
		$htell(c, "<TABLE WIDTH=75% BORDER=2 COLS=1>");
		$htell(c, "<TR><TD ALIGN=center><B>START</B></TD></TR></TABLE>");
		for m in (this.mail)
			$htell(c, "<FORM ACTION=\"/player/frm\"><INPUT TYPE=hidden VALUE=\""+tostr(i)+"\" NAME=\"msg\">");
			$htell(c, "<TABLE WIDTH=75% BORDER=2 COLS=2>");
			$htell(c, "<TR><TD COLSPAN=2>From");
			$htell(c, "<B>"+m[1]:name()+"</B>");
			$htell(c, "at");
			$htell(c, "<B>"+$numutils:timetostr(m[3])+"</B>");
			$htell(c, "<BR>Subject:");
			$htell(c, "<B>"+m[2]+"</B>");
			$htell(c, "</TD></TR>");
			$htell(c, "<TR><TD COLSPAN=2><PRE>");
			for j in (m[4])
				$htell(c, j);
			endfor
			$htell(c, "</PRE></TD></TR>");
			$htell(c, "<TR><TD ALIGN=center>");
			$htell(c, "<INPUT TYPE=submit NAME=\"cmd\" VALUE=\"Delete\">");
			$htell(c, "</TD><TD ALIGN=center>");
			$htell(c, "<INPUT TYPE=submit NAME=\"cmd\" VALUE=\"Reply\">");
			$htell(c, "</TD></TR>");
			$htell(c, "</TABLE>");
			$htell(c, "</FORM>");
			i = i + 1;
			if (this.newmail > 0)
				this.newmail = this.newmail - 1;
				if (this.newmail == 0)
					$htell(c, "<TABLE WIDTH=75% BORDER=2 COLS=1>");
					$htell(c, "<TR><TD ALIGN=center><B>END OF NEW MESSAGES</B></TD></TR></TABLE>");
				endif
				suspend(0);
			endif
		endfor
		$htell(c, "<TABLE WIDTH=75% BORDER=2 COLS=1>");
		$htell(c, "<TR><TD ALIGN=center><B>END</B></TD></TR></TABLE>");
		$htell(c, "</CENTER>");
	elseif (cmd == "delete")
		{msg} = $http_server:parseparam(param, {"msg"});
		msg = toint(msg);
		msg = msg + this.newmail;
		result = this:deletemail(msg);
		if (result[1] != "")
			$http_server:error("Failed! "+result[1], "/player/frm");
		else
			$http_server:redirect(c, "/player/frm");
		endif
	elseif (cmd == "reply")
		{msg} = $http_server:parseparam(param, {"msg"});
		msg = toint(msg) + this.newmail;
		msg = this.mail[msg];
		this:http_frm_compose(c, "Reply To Old Message", msg[1]:name(),
			"Re: "+msg[2], msg[4]);
	elseif (cmd == "send");
		{to, subject, body} = $http_server:parseparam(param, {"to", "subject", "body"});
		to = $player:find_player(to);
		result = player:sendmail(to, subject, body);
		if (result[1] != "")
			$htell(c, "Failed to send message:");
			$htell(c, result[1]);
		else
			$http_server:redirect(c, "/player/frm");
		endif
	elseif (cmd == "compose new")
		{?to="", ?subject="", ?body={}} = $http_server:parseparam(param, {"to", "subject", "body"});
		this:http_frm_compose(c, "Compose New Message", to, subject, body);
	else
		$http_server:error(c, "Invalid FRM command!", "/player/frm");
	endif
	this:htmlfooter(c, method);
.

.quit

