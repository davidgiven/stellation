rem antimatterdistillery.moo
rem Represents a metal mine.
rem $Source: /cvsroot/stellation/stellation/antimatterdistillery.moo,v $
rem $State: Exp $

.patch antimatterdistillery.moo 3 1
notify(player, "antimatterdistillery.moo");

$god:prop(#0, "antimatterdistillery", create($solarrefinery, $god));
$antimatterdistillery.name = "antimatter distillery";

$antimatterdistillery.asteroids = {0, 0};
$antimatterdistillery.rate = (1.0/24.0);
$antimatterdistillery.production = {0.0, 0.0, 20.0};

$antimatterdistillery.description = "Antimatter distilleries orbit the local star at low altitude and use a continuous stream of scoop projectiles to collect plasma from the surface. The energy so collected is used to synthesise stabilised antimatter, which can be safely stored and used to power other units.";

.quit

rem Revision History
rem $Log: antimatterdistillery.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

