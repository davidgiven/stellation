rem basicfactory.moo
rem Represents a basic (entry-level) factory.
rem $Source: /cvsroot/stellation/stellation/basicfactory.moo,v $
rem $State: Exp $

.patch basicfactory.moo 6 1
notify(player, "basicfactory.moo");

$god:prop(#0, "basicfactory", create($factory, $god));
$basicfactory.name = "basic factory";

$basicfactory.description = "The basic factory is a semi-automated device that, given the appropriate quantities of basic resources, can produce (one at a time) any of a considerable number of units.";

$basicfactory.buildable = {$jumpship, $cargoship, $tug, $basicfactory, $metalmine, $antimatterdistillery, $hydroponicsplant, $messagebuoy, $novacannon, $bomber};

.quit

rem Revision History
rem $Log: basicfactory.moo,v $
rem Revision 1.3  2000/09/09 22:37:16  dtrg
rem Added entries for the RAM bomber.
rem
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

