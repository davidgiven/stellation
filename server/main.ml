(* server/main.ml
 * Main program.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/main.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;
open Interface;;

Random.init (int_of_float (Unix.time ()));;

(* Populate the galaxy with stars. *)

let number_of_stars = 400 in
let galactic_radius = 400 in
let module StarSet = Set.Make(struct
	type t = baseObject
	let compare = compare
end) in
let stars = ref StarSet.empty in
while ((StarSet.cardinal !stars) < number_of_stars) do
	stars := StarSet.add (Star.make ()) !stars
done;
let addstar _star =
	galaxy#addstar (objectStore#add _star) in
StarSet.iter addstar !stars
;;

(* Initialise the font end. *)

let interface = new interfaceClass;;
interface#init;;

(* Main loop. *)

while true do
	(getevent interface)#show;
	flush stdout
done;;

(* Revision history
 * $Log: main.ml,v $
 * Revision 1.2  2004/05/28 23:27:26  dtrg
 * Rewrote entirely, now using objects and a much cleaner design. It works!
 *
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *)

