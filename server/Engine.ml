(* server/Engine.ml
 * The main game engine.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Engine.ml,v $
 * $State: Exp $
 *)

open Printf;;

type propertyType =
	  VoidProperty
	| BoolProperty
	| IntProperty
	| FloatProperty
	| StringProperty
	| IdProperty

type eventPayload = 
	  NoopMsg
	| MessageMsg of string

exception Invalid_type

(* Defines the global object store; this is the mapping between object ids and
 * actual objects. *)

class objectStoreClass = object (self)
	val store: ((int, baseObject) Hashtbl.t) = Hashtbl.create 1000
	val mutable maxid = 0

	method add _object =
		let i = maxid in
		maxid <- maxid + 1;
		Hashtbl.add store i _object;
		i

	method sub _id =
		Hashtbl.remove store _id

	method deref _id =
		Hashtbl.find store _id
end

(* Represents an event that is sent from one object to another. We haven't
 * defined baseObject yet, so we need to use a placeholder. The type will get
 * resolved later. *)

and event _source _dest _message = object (self)
	val _timestamp = Unix.time ()
	val _source = _source
	val _dest = _dest
	val _message = _message

	method timestamp =
		_timestamp

	method source =
		_source

	method destination =
		_dest

	method message =
		_message

	method asstring =
		"source=" ^ (string_of_int self#source) ^
		" dest=" ^ (string_of_int self#destination) ^
		" timestamp=" ^ (string_of_float self#timestamp)
		^ " " ^ match self#message with
		  NoopMsg -> "NoopMsg"
		| _ -> "UnknownMsg"

	method show =
		printf "%s\n" self#asstring
end


(* Properties. *)

and baseProperty = object
	method gettype =
		VoidProperty

	method getbool =
		raise Invalid_type;
		false

	method getint =
		raise Invalid_type;
		0

	method getstring =
		raise Invalid_type;
		""

	method getid =
		raise Invalid_type;
		([]: int list)

	method setbool (_p:bool) =
		raise Invalid_type;
		()

	method setint (_p:int) =
		raise Invalid_type;
		()

	method setstring (_p:string) =
		raise Invalid_type;
		()

	method addid (_p:int) =
		raise Invalid_type;
		()

	method subid (_p:int) =
		raise Invalid_type;
		()

	method clearids =
		raise Invalid_type;
		()
end

and boolProperty = object
	inherit baseProperty
	val mutable value = false

	method gettype =
		BoolProperty

	method getbool =
		value

	method setbool _v =
		value <- _v
end

and intProperty = object
	inherit baseProperty
	val mutable value = 0

	method gettype =
		FloatProperty

	method getint =
		value

	method setint _v =
		value <- _v
end

and stringProperty = object
	inherit baseProperty
	val mutable value = ""

	method gettype =
		StringProperty

	method getstring =
		value

	method setstring _v =
		value <- _v
end

(* An actual object. *)

and baseObject = object
	val properties: (string, baseProperty) Hashtbl.t = Hashtbl.create 10

	method event (_event:event) =
		printf "Unhandled event: %s\n" (_event#asstring)

	method scope (_player:int) =
		([]: int list)

	method check _name _type =
		try
			((Hashtbl.find properties _name)#gettype = _type)
		with
			Not_found -> false

	method get _name =
		Hashtbl.find properties _name

	method add _name _type =
		Hashtbl.add properties _name
		(
			match _type with
			  BoolProperty -> (new boolProperty :> baseProperty)
			(* how come the coercion isn't needed on these? *)
			| IntProperty -> new intProperty
			| StringProperty -> new stringProperty
			| _ -> raise Invalid_type
		)
end

(* Singleton objects containing our main data stores. *)

let objectStore = new objectStoreClass

(* Keeps track of the visible (and invisible) stars in the galaxy. *)

class galaxyClass = object
	val mutable visible = []

	method addstar _id =
		visible <- _id :: visible
		
	method findbyname _name =
		let predicate _id =
			let _star = objectStore#deref _id in
			((_star#get "name")#getstring) = _name
		in List.find predicate visible

	method visibleStars =
		visible
end
let galaxy = new galaxyClass

(* Event queue code *)

let rec eventQueue = ref ([]: event list)

and sendevent _event =
	let rec insert _list =
		match _list with
		  [] -> [_event]
		| _e::_es ->
			if (_event#timestamp < _e#timestamp) then
				_event::_e::_es
			else
				_e::(insert _es)
	in eventQueue := insert !eventQueue

and getevent _interface =
	let now = Unix.time () in
	match (!eventQueue) with
	  _e::_es when (_e#timestamp <= now) ->
		eventQueue := _es;
		_e
	| _e::_es ->
		_interface#wait (_e#timestamp -. now);
		getevent _interface
	| [] ->
		(* should really block here for the
		 * next timed event. *)
		_interface#wait 3600.0;
		getevent _interface

and showeventqueue () =
	printf "Event queue:\n";
	List.iter (function _e -> _e#show) !eventQueue

(* Revision history
 * $Log: Engine.ml,v $
 * Revision 1.2  2004/05/28 23:27:26  dtrg
 * Rewrote entirely, now using objects and a much cleaner design. It works!
 *
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *)
