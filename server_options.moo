rem server_options.moo
rem Global server options.
rem $Source: /cvsroot/stellation/stellation/server_options.moo,v $
rem $State: Exp $

.patch server_options.moo 6 1
notify(player, "server_options.moo");

$god:prop(#0, "server_options", create($object, $god));
$server_options.name = "Server Options";

$god:prop(#0, "tick", 3600.0);

$god:prop($server_options, "http_port", 7778);
$god:prop($server_options, "http_servername", "LambdaMOO-Stellation");
$god:prop($server_options, "http_serverversion", "1.0");
$god:prop($server_options, "http_authenticationrealm", "Stellation");
$god:prop($server_options, "maintainer", "dg@tao-group.com");
$god:prop($server_options, "fg_ticks", 30000);
$god:prop($server_options, "fg_seconds", 5);
$god:prop($server_options, "gdrender_url", "http://zhaneel.local/cgi-bin/gdrender.cgi");
#$god:prop($server_options, "gdrender_url", "http://stellation.sourceforge.net/cgi-bin/gdrender.cgi");
$god:prop($server_options, "server_url", "http://pyanfar.local:7778/metaplayer/mapdata");
#$god:prop($server_options, "server_url", "http://plover.net:7778/metaplayer/mapdata");
$god:prop($server_options, "maxplayers", 75);

$god:prop($server_options, "motd", {});

$server_options.motd = {"Welcome to Stellation, the hot new on-line gaming service! Please note that this game is currently in ALPHA testing. It's not guaranteed to do anything useful. In fact, it is guaranteed not to work. However, you're welcome to poke around as much as you like. If you see any bugs, or traceback messages, please drop me a note and I'll look into it. Send me (Hjalfi) an FRM. <B>Big warning:</B> every so often I will need to rebuild the server from scratch as I make major changes. This will involve wiping the database, including all your players, units, etc. It is only alpha, remember."};
$server_options.motd = {@$server_options.motd, "Join the <A HREF=\"http://lists.sourceforge.net/mailman/listinfo/stellation-players\">mailing list</A>!"};

$server_options.motd = {@$server_options.motd, "268.198: Lots of minor bug fixes in response to reports. Keep sending 'em in!"};
$server_options.motd = {@$server_options.motd, "268.127: Server reset. Lots of major and minor changes; <B>combat system!</B>, upped the number of stars to 400 and the number of players to 300, lots of formatting fixes, minor bug fixes, etc."};
$server_options.motd = {@$server_options.motd, "268.102: Fixed a minor map problem, and made it so that you can look at other people's units and fleets without hideous tracebacks. Also changed the server administration password, as it was embarassingly in CVS..."};
$server_options.motd = {@$server_options.motd, "268.89: Added a URL redirection option to the map renderer. Now Internet Explorer users should be able to see maps properly. It's a hack, but it ought to work..."};
$server_options.motd = {@$server_options.motd, "268.74: Some frantic bug fixes. The map no longer shows `Interstellar Space' objects; this also has made it start working with Netscape again. Also added maximum number of players feature and fixed a few minor things."};
$server_options.motd = {@$server_options.motd, "268.55: Server reset. New features: cosmetic makeover, bug fixes. The nova cannon now looks as if it actually works (but doesn't, yet)."};
$server_options.motd = {@$server_options.motd, "268.31: Server reset. New features: threw away the nasty text-mode map and replaced it with the nice, smooth GIF one; rewrote the name generator --- again --- and now it produces decent names; set up a SourceForge project to put everything in after I had a very scary disk crash; fiddled with the animatter and organics refinery stats (the hydroponics plant was producing far too much, the antimatter refinery far too little). Various other minor changes, including some base structure for the combat system."};
$server_options.motd = {@$server_options.motd, "267.885: Server reset. New features: message buoys, hopefully sensible unit stats, better unit display in factories, assorted bug and spelling mistake fixes. Redid the star name generator. Star names ought to be shorter and, now, unique. The game should now, hopefully, be playable!"};
$server_options.motd = {@$server_options.motd, "267.670: Server reset. New features: FRMs, external relations. Several bug fixes and spelling mistake fixes. This MOTD feature. Reduced the number of stars to 100 to reduce startup time."};

.quit

rem Revision History
rem $Log: server_options.moo,v $
rem Revision 1.9  2000/08/05 22:44:08  dtrg
rem Many minor bug fixes.
rem Better object visibility testing --- less scope for cheating.
rem
rem Revision 1.8  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
rem Revision 1.7  2000/08/01 22:06:04  dtrg
rem Owned stars are now showed in yellow again.
rem Fixed viewing other people's units; all the tracebacks should have gone.
rem Various minor bug fixes and formatting changes.
rem
rem Revision 1.6  2000/08/01 09:54:21  dtrg
rem Finally fixed the map so it works on Internet Explorer systems.
rem (Now, instead of feeding all the map data in in the URL, it gives gdrender
rem a URL where it can fetch the data itself. It's a bit nasty, but works.)
rem
rem Revision 1.5  2000/07/31 23:50:16  dtrg
rem First interim checkin of the new indirected map code.
rem
rem Revision 1.4  2000/07/31 18:07:42  dtrg
rem Map no longer displays deep space objects (which means it works with
rem Netscape again).
rem Added maximum number of players feature.
rem A few formatting fixes.
rem
rem Revision 1.3  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.2  2000/07/30 00:00:40  dtrg
rem Took out the nasty text-mode map and replaced it with a gdrender GIF
rem based one.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.

