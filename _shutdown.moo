rem $Source: /cvsroot/stellation/stellation/_shutdown.moo,v $
rem $State: Exp $

exec $player:create_player("Test", "test", "test@test.com", "Testers", "Test Player")
exec $abort = 0
exec shutdown()

rem Revision History
rem $Log: _shutdown.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

