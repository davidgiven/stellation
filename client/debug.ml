(* client/debug.ml
 * Very simple program that connects to the server, performs a single
 * transaction, and exits.
 * $Source: /cvsroot/stellation/stellation2/client/Attic/debug.ml,v $
 * $State: Exp $
 *)

open Printf;;

let socketname = "/tmp/stellation-socket";;
let socket = Unix.socket Unix.PF_UNIX Unix.SOCK_STREAM 0;;

Unix.connect socket (Unix.ADDR_UNIX socketname);;

let pending _socket =
	match (Unix.select [_socket] [] [] 0.0) with
		  ([], [], []) -> false
		| _ -> true
;;

let copybyte _from _to =
	let buf = " " in
	if ((Unix.read _from buf 0 1) != 1) then
	(
		raise End_of_file;
		()
	)
	else
		ignore (Unix.write _to buf 0 1)
;;

while true do
	match (Unix.select [socket; Unix.stdin] [] [] 3600.0) with
	  ([], [], []) -> ()
	| _ ->
		while (pending Unix.stdin) do
			copybyte Unix.stdin socket
		done;
		while (pending socket) do
			copybyte socket Unix.stdout
		done
done
;;

(* Revision history
 * $Log: debug.ml,v $
 * Revision 1.1  2004/05/28 23:29:13  dtrg
 * Added minimal front end for test purposes.
 *
 *)

