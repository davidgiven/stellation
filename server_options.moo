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
$god:prop($server_options, "fg_ticks", 600000);
$god:prop($server_options, "fg_seconds", 10);
$god:prop($server_options, "bg_ticks", 600000);
$god:prop($server_options, "bg_seconds", 10);
$god:prop($server_options, "gdrender_url", "http://zhaneel.local/cgi-bin/gdrender.cgi");
#$god:prop($server_options, "gdrender_url", "http://stellation.sourceforge.net/cgi-bin/gdrender.cgi");
$god:prop($server_options, "server_url", "http://pyanfar.local:7778/metaplayer/mapdata");
#$god:prop($server_options, "server_url", "http://plover.net:7778/metaplayer/mapdata");
$god:prop($server_options, "maxplayers", 75);

$god:prop($server_options, "motd", {});

$server_options.motd = {"Welcome to Stellation, the hot new on-line gaming service! Please note that this game is currently in ALPHA testing. It's not guaranteed to do anything useful. In fact, it is guaranteed not to work. However, you're welcome to poke around as much as you like. If you see any bugs, or traceback messages, please drop me a note and I'll look into it. Send me (Hjalfi) an FRM. <B>Big warning:</B> every so often I will need to rebuild the server from scratch as I make major changes. This will involve wiping the database, including all your players, units, etc. It is only alpha, remember."};
$server_options.motd = {@$server_options.motd, "Join the <A HREF=\"http://lists.sourceforge.net/mailman/listinfo/stellation-players\">mailing list</A>!"};

$server_options.motd = {@$server_options.motd, "269.443: Fixed a bug that was preventing RAM bombers from being transferred from one fleet to another. Also added a feature to only display some of the intelligence --- people were complaining of 1MB+ HTML pages. And upped the tick limits, again, allowing bigger fleets."};
$server_options.motd = {@$server_options.motd, "269.037+a bit: Rewrote the sorting algorithm. With luck, the top 20 list will work a bit better... incidentally, now I've added the cargoship minimum level setting, I've noticed that my intelligence listings have been spammed silly with cargo transfers. Should I hide these completely? It means you won't see when another player in the same system is stealing your resources. Or should I do something else? Suggestions, anyone? (To the mailing list, preferably; it's easier to manage there.)"};
$server_options.motd = {@$server_options.motd, "269.037+a bit: Rewrote the sorting algorithm. With luck, the top 20 list will work a bit better... incidentally, now I've added the cargoship minimum level setting, I've noticed that my intelligence listings have been spammed silly with cargo transfers. Should I hide these completely? It means you won't see when another player in the same system is stealing your resources. Or should I do something else? Suggestions, anyone? (To the mailing list, preferably; it's easier to manage there.)"};
$server_options.motd = {@$server_options.motd, "269.037: Woohoo! With your shiny new RAM bombers, you can now do damage to someone else's factories and refineries and capture them! You'll find the RAM bomber (that <b>r</b>elativistic <b>a</b>nti<b>m</b>atter, if you're interested) in your Basic Factories. I hope. There's a faint possibility that it'll only show up in newly-built factories. Its use is fairly self-explanatory; there are two things to note: firstly, you need to do 2/3 damage to a unit before it can be captured, and secondly, if you fire on one of your own units, the rather basic IFF system will classify yourself as an enemy and all your ships will turn on each other. Have fun."};
$server_options.motd = {@$server_options.motd, "268.942: Someone pointed out that if you jumped into a system and stole all of someone's resources, five minutes later all their units would starve to death. As a result, I've made a few minor changes: stationary units (factories and refineries) no longer die when starved. Now they just mothball themselves. (Note that mobile units still die.) Also, there's a new feature in cargo ships that will keep them automatically stocked up. So you can keep the bulk of your resources safely locked away in a cargo ship, and only keep the minimum necessary in the star system itself where anyone can pick it up. Oh, yes, and I've tripled the output of hydroponics plants. <B>Important!</B> You will have to mothball and redeploy your hydro plants to make this change go into effect!"};
$server_options.motd = {@$server_options.motd, "268.798: Stationary units with more than 2/3 damage can now be captured by other players. There's currently no way of doing damage to them, however (other than blasting their tugs when they're being towed)."};
$server_options.motd = {@$server_options.motd, "268.774: Adjusted the top players list to include stationary units as well. Should work better, but I have since discovered that my sorting algorithm doesn't appear to work. Heigh ho."};
$server_options.motd = {@$server_options.motd, "268.727: Added the top players table. Well, that was a nasty shock... my two test players make #16 and #17! Obviously people are losing lots of units somehow. Please, if you feel that the game's too hard, or that you've been hard done by, drop me an e-mail... preferably to the mailing list (it's easier for me to track that way). Also doubled the output from metal mines. Hopefully this should move things along a bit."};
$server_options.motd = {@$server_options.motd, "268.722+a bit: Well, that was easy. Now for some extra functionality."};
$server_options.motd = {@$server_options.motd, "268.722: I'm back! I had a lovely holiday shovelling concrete in Lochcarron and now I've discovered several bugs, that I'm now working on. In particular: the server runs out of ticks on very large fleets (like my seriously cannoned-up ones). These means certain operations, like FTL jumps, fail. Also, there seems to be a bug that limits all pages to about 60kB. This will cause Intelligence to fail if you've been doing a lot. I'll fix them ASAP."};
$server_options.motd = {@$server_options.motd, "268.281: Reset, as promised. All players, mail, stars, etc have been destroyed. This was necessary to remove a number of dead and broken units left by prior bugs that I have now fixed. Everything should now be just tickety-boo."};
$server_options.motd = {@$server_options.motd, "268.243: Yet more minor bug fixes, including some not-so-minor ones: newly-built refineries work, intelligence works after combat (previously it died completely), etc. Also I've doubled the amount of antimatter produced by a distillery and halved the amount of organics produced by a hydroponic plant, in an attempt to make the game a bit more balanced."};
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
rem Revision 1.15  2000/09/26 20:05:36  dtrg
rem MOTD update.
rem
rem Revision 1.14  2000/09/09 22:37:57  dtrg
rem MOTD update.
rem
rem Revision 1.13  2000/09/05 23:20:05  dtrg
rem MOTD update.
rem
rem Revision 1.12  2000/09/05 23:13:39  dtrg
rem MOTD update.
rem
rem Revision 1.11  2000/08/30 22:54:26  dtrg
rem MOTD update.
rem
rem Revision 1.10  2000/08/27 23:59:50  dtrg
rem Doubled the number of ticks for fg and bg processes, as operations on
rem big fleets were failing.
rem
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

