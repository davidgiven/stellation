(* server/main.ml
 * Main program.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/main.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;

Random.init (int_of_float (Unix.time ()));;

(* Populate the galaxy with stars. *)

let number_of_stars = 400 in
let galactic_radius = 400 in
let module StarSet = Set.Make(struct
	type t = Object.t
	let compare _o1 _o2 =
		let _r = compare (Object.getstringprop _o1 "name")
			(Object.getstringprop _o2 "name") in
		if (_r != 0) then
			_r
		else
		let _r = compare (Object.getintprop _o1 "x")
			(Object.getintprop _o2 "x") in
		if (_r != 0) then
			_r
		else compare (Object.getintprop _o1 "y")
			(Object.getintprop _o2 "y")
	end) in
let stars = ref StarSet.empty in
while ((StarSet.cardinal !stars) < number_of_stars) do
	stars := StarSet.add (Star.make ()) !stars
done;
let addstar _star =
	Galaxy.addstar (ObjectStore.add _star) in
StarSet.iter addstar !stars
;;

(* Initialise the font end. *)

Interface.init ()
;;

(* And some test code. *) 
	 
(* let id = ObjectStore.add (Object.make Object.eventHandler);;
let event = Event.make id id 3.0 `NoopMsg;;
Event.sendevent event;;
let event = Event.make id id 2.0 `NoopMsg;;
Event.sendevent event;;
let event = Event.make id id 1.0 `NoopMsg;;
Event.sendevent event;;
let event = Event.make id id 0.0 `NoopMsg;;
Event.sendevent event;;*)

while true do
	Event.showevent (Event.getevent ());
	flush stdout
done;;

(* Revision history
 * $Log: main.ml,v $
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)

