(* server/Star.ml
 * Represents a star.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Star.ml,v $
 * $State: Exp $
 *)

open Printf;;
open Engine;;

module Super = Object

let namegen _seed =
	let syllables1 = [|
		"an"; "ca"; "jo"; "ka"; "kri"; "da"; "re"; "de"; "ed"; "ma";
		"ni"; "qua"; "qa"; "li"; "la"; "in"; "on"; "an"; "un"; "ci";
		"cu"; "ce"; "co"; "xa"; "xef"; "xii"; "xo'o"; "xu"; "ram";
		"noq"; "mome"; "pawa"; "limi"; "ney" |] in
	let syllables2 = [|
		"the"; "ru"; "shu"; "be"; "po"; "fol"; "boo"; "qwa"; "xi";
		"lo"; "fi" |] in
	let syllables3 = [|
		"drew"; "rine"; "vid"; "a"; "na"; "sten"; "niel"; "cca";
		"vin"; "ven"; "cor"; "rion"; "rath"; "tong"; "lar"; "bol";
		"ting"; "narg"; "aq"; "blan"; "sim"; "pil"; "rib"; "org";
		"lig"; "zim"; "frob"; "cha"; "poo"; "tang" |] in
	let generate _s0 _s1 _s2 _s3 =
		syllables1.(_s1) ^ if _s0 then
			syllables2.(_s2)
		else
			""
		^ syllables3.(_s3) in
	if (_seed = 0) then
		generate (Random.bool ())
			 (Random.int (Array.length syllables1))
			 (Random.int (Array.length syllables2))
			 (Random.int (Array.length syllables3))
	else
		let _s0 = _seed mod 2 in
		let _seed = _seed / 2 in
		let _s1 = _seed mod (Array.length syllables1) in
		let _seed = _seed / (Array.length syllables1) in
		let _s2 = _seed mod (Array.length syllables2) in
		let _seed = _seed / (Array.length syllables2) in
		let _s3 = _seed mod (Array.length syllables3) in
		generate (_s0 = 1) _s1 _s2 _s3

let eventHandler _object _event =
	Super.eventHandler _object _event

let scopeHandler _object _player =
	Super.scopeHandler _object _player

let galactic_radius = 400.0
let pi = 3.14159

let make () =
	let _o = Object.make eventHandler scopeHandler in
	let _r = Random.float galactic_radius in
	let _theta = Random.float (2.0 *. pi) in
	let _x = _r *. (sin _theta) in
	let _y = _r *. (cos _theta) in
	let _x = int_of_float (_x *. 10.0) in
	let _y = int_of_float (_y *. 10.0) in
	Object.setprop _o "name" (Object.StringProp (namegen 0));
	Object.setprop _o "x" (Object.IntProp _x);
	Object.setprop _o "y" (Object.IntProp _y);
	_o

(* Revision history
 * $Log: Star.ml,v $
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)

