rem metalmine.moo
rem Represents a metal mine.
rem $Source: /cvsroot/stellation/stellation/metalmine.moo,v $
rem $State: Exp $

.patch metalmine.moo 3 1
notify(player, "metalmine.moo");

$god:prop(#0, "metalmine", create($refinery, $god));
$metalmine.name = "metal mine";

$metalmine.asteroids = {1, 0};
$metalmine.rate = (1.0/24.0);
$metalmine.production = {100.0, 0.0, 0.0};

$metalmine.description = "Automated metal mines collect metallic asteroids and process them, producing a steady stream of refined metal. This model consumes one asteroid at a time. When the internal stocks are depleted, it automatically seeks out another asteroid.";

.quit

rem Revision History
rem $Log: metalmine.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

