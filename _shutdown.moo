rem $Source: /cvsroot/stellation/stellation/_shutdown.moo,v $
rem $State: Exp $

exec $player:create_player("Test", "test", "test@test.com", "Testers", "Test Player")
exec $player:create_player("Test1", "test1", "test@test.com", "Testers", "Test Player 2")
exec $player:find_player("Test1"):fleets()[1]:moveto($player:find_player("Test"):fleets()[1].location)
exec $abort = 0
exec shutdown()

rem Revision History
rem $Log: _shutdown.moo,v $
rem Revision 1.2  2000/08/01 22:06:04  dtrg
rem Owned stars are now showed in yellow again.
rem Fixed viewing other people's units; all the tracebacks should have gone.
rem Various minor bug fixes and formatting changes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

