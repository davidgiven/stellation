rem stats.moo
rem Unit stats.
rem $Source: /cvsroot/stellation/stellation/stats.moo,v $
rem $State: Exp $

.patch stats.moo 6 1
notify(player, "stats.moo");

#                     Mass    Time-cost        Build-cost              Time-to-build       Max damage
$jumpship:setstats(   5000.0, {0.0, 5.0, 2.0}, {10000.0, 20000.0, 1000.0}, 5.0,              1000);
$cargoship:setstats(  1000.0, {0.0, 2.0, 1.0}, { 5000.0, 10000.0, 1000.0}, 3.0,               300);
$tug:setstats(        1000.0, {0.0, 4.0, 1.0}, { 3000.0,  8000.0, 1000.0}, 2.0,               100);
$novacannon:setstats( 1500.0, {0.0, 5.0, 1.0}, { 4000.0,  8000.0, 1000.0}, 4.0,                50);

$basicfactory:setstats(        10000.0, {0.0, 5.0, 1.0}, {20000.0, 30000.0, 2000.0}, 10.0,   5000);
$metalmine:setstats(           10000.0, {0.0, 5.0, 1.0}, {10000.0, 30000.0, 2000.0}, 5.0,    3000);
$antimatterdistillery:setstats( 8000.0, {0.0, 5.0, 1.0}, {10000.0, 30000.0, 2000.0}, 5.0,    3000);
$hydroponicsplant:setstats(    10000.0, {0.0, 5.0, 1.0}, {10000.0, 30000.0, 2000.0}, 5.0,    3000);
$messagebuoy:setstats(           100.0, {0.0, 0.0, 0.0}, {  100.0,    10.0,    0.0}, 0.2,       1);

$metalmine.asteroids                       = {1, 0};
$metalmine.rate                            = 1.0/24.0;
$metalmine.production                      = {5000.0/240.0, 0.0, 0.0};

$antimatterdistillery.asteroids            = {0, 0};
$antimatterdistillery.rate                 = 2.0/24.0;
$antimatterdistillery.production           = {0.0, 20000.0/240.0, 0.0};
$antimatterdistillery.brightnessmultiplier = 1.0;

$hydroponicsplant.asteroids                = {0, 1};
$hydroponicsplant.rate                     = 1.0/24.0;
$hydroponicsplant.production               = {0.0, 0.0, 1500.0/240.0};
$hydroponicsplant.brightnessmultiplier     = 1.0;

$tick = 3600.0;
$god.password = crypt("defaultadminpassword");


.quit

rem Revision History
rem $Log: stats.moo,v $
rem Revision 1.3  2000/07/31 23:50:16  dtrg
rem First interim checkin of the new indirected map code.
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


