(* server/Interface.ml
 * Interface to the outside world.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Interface.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;

(* Represents the interface to the outside world. *)

class interfaceClass = object (self)
	val socketname = "/tmp/stellation-socket"
	val mutable mastersocket = Unix.stdin
	
	method init =
		mastersocket <- Unix.socket Unix.PF_UNIX Unix.SOCK_STREAM 0;
		Unix.unlink socketname;
		Unix.bind mastersocket (Unix.ADDR_UNIX socketname);
		Unix.listen mastersocket 10

	method wait _timeout =
		printf "Wait %f\n" _timeout;
		flush stdout;
		match (Unix.select [mastersocket] [] [] _timeout) with
		  ([], [], []) -> ()
		| _ -> self#executecommand

	method executecommand =
		match (Unix.accept mastersocket) with
		(_fd, Unix.ADDR_UNIX _source) ->
		(
			printf "connection from <%s>\n" _source;
			flush stdout;
			let _in = Unix.in_channel_of_descr _fd in
			let _out = Unix.out_channel_of_descr _fd in
			set_binary_mode_in _in false;
			set_binary_mode_out _out false;
			(
				match (input_line _in) with
				"stars" -> self#cmd_stars _in _out
				| _ -> self#cmd_invalid _in _out
			);
			flush _out;
			Unix.close _fd;
			printf "connection closed\n"
		)
		| _ ->
			() (* unreached *)
	
	method cmd_invalid _in _out =
		output_string _out "EUnrecognised command\n"

	method cmd_stars _in _out =
		let showstar _starid =
			let _star = objectStore#deref _starid in
			let _name = (_star#get "name")#getstring in
			let _x = (_star#get "x")#getint in
			let _x = string_of_int _x in
			let _y = (_star#get "y")#getint in
			let _y = string_of_int _y in
			output_string _out ("D" ^
				_name ^ " " ^ _x ^ " " ^ _y ^ "\n")
		in
		List.iter showstar galaxy#visibleStars;
		output_string _out "X\n"
end
;;

(* Revision history
 * $Log: Interface.ml,v $
 * Revision 1.2  2004/05/28 23:27:26  dtrg
 * Rewrote entirely, now using objects and a much cleaner design. It works!
 *
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)

