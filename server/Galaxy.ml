(* server/Galaxy.ml
 * Galaxy and star management.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Galaxy.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;

module StarSet = Set.Make(
	struct
		type t = ObjectStore.id
		let compare = compare
	end)

let stars = ref StarSet.empty

let addstar _starid =
	stars := StarSet.add _starid !stars

type starDescriptionType = {
	name: string;
	x: int;
	y: int;
}

let stars () =
	let makelist _element _tail =
		let star = ObjectStore.deref _element in
		{
			name = (Object.getstringprop star "name");
			x = (Object.getintprop star "x");
			y = (Object.getintprop star "y")
		} :: _tail
	in StarSet.fold makelist !stars []

(* Revision history
 * $Log: Galaxy.ml,v $
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)

