rem server_options.moo
rem Global server options.
.patch server_options.moo 4 1
notify(player, "server_options.moo");

$god:prop(#0, "server_options", create($object, $god));
$server_options.name = "Server Options";

$god:prop(#0, "tick", 3600.0);

$god:prop($server_options, "http_port", 7778);
$god:prop($server_options, "http_servername", "LambdaMOO-Stellation");
$god:prop($server_options, "http_serverversion", "1.0");
$god:prop($server_options, "http_authenticationrealm", "Stellation");
$god:prop($server_options, "fg_ticks", 30000);
$god:prop($server_options, "fg_seconds", 5);

$god:prop($server_options, "motd", {});

$server_options.motd = {"Welcome to Stellation, the hot new on-line gaming service! Please note that this game is currently in ALPHA testing. It's not guaranteed to do anything useful. In fact, it is guaranteed not to work. However, you're welcome to poke around as much as you like. If you see any bugs, or traceback messages, please drop me a note and I'll look into it. Send me (Hjalfi) an FRM. <B>Big warning:</B> every so often I will need to rebuild the server from scratch as I make major changes. This will involve wiping the database, including all your players, units, etc. It is only alpha, remember."};

$server_options.motd = {@$server_options.motd, "267.885: Server reset. New features: message buoys, hopefully sensible unit stats, better unit display in factories, assorted bug and spelling mistake fixes. Redid the star name generator. Star names ought to be shorter and, now, unique. The game should now, hopefully, be playable!"};
$server_options.motd = {@$server_options.motd, "267.670: Server reset. New features: FRMs, external relations. Several bug fixes and spelling mistake fixes. This MOTD feature. Reduced the number of stars to 100 to reduce startup time."};

.quit



