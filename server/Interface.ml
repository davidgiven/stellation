(* server/Interface.ml
 * Interface to the outside world.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Interface.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;

let docommand _timeout =
	printf "wait %f\n" _timeout;
	flush stdout;
	ignore (Unix.select [] [] [] _timeout)

let init () =
	Event.setcommandhandler docommand

(* Revision history
 * $Log: Interface.ml,v $
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)

