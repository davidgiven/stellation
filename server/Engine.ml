(* server/Engine.ml
 * The main game engine.
 * $Source: /cvsroot/stellation/stellation2/server/Attic/Engine.ml,v $
 * $State: Exp $
 *)

open Printf;;

(* Defines the global object store; this is the mapping between object ids and
 * actual objects. *)

module rec ObjectStore:
	sig
		type id = int

		val add: Object.t -> id
		val sub: id -> unit
		val deref: id -> Object.t
	end =
	struct
		type id = int

		let objectStore = Hashtbl.create 1000
		let objectid = ref 0
		
		let add _o =
			let _i = !objectid in
			objectid := !objectid + 1;
			Hashtbl.add objectStore _i _o;
			_i
			
		let sub _id =
			Hashtbl.remove objectStore _id

		let deref _id =
			Hashtbl.find objectStore _id
	end

(* Encapsulates an event that is sent between two objects, plus the event
 * queue itself. Events have a timestamp that may be in the future. *)
 
and Event:
	sig
		type messageType = [ `NoopMsg ]
		and t = {source: ObjectStore.id; destination: ObjectStore.id;
			timestamp: float; message: messageType}
		and commandHandlerType = float -> unit

		val setcommandhandler: commandHandlerType -> unit

		val make: ObjectStore.id -> ObjectStore.id -> float
			-> messageType -> t
		val sendevent: t -> unit
		val getevent: unit -> t
		
		val string_of_event: t -> string
		val showevent: t -> unit
		val showeventqueue: unit -> unit
	end =
	struct
		type messageType = [ `NoopMsg ]
		and t = {source: ObjectStore.id; destination: ObjectStore.id;
			timestamp: float; message: messageType}
		and commandHandlerType = float -> unit
			
		let defaultcommandhandler _timeout =
			ignore (Unix.select [] [] [] _timeout)

		let commandhandler = ref defaultcommandhandler

		let setcommandhandler _h =
			commandhandler := _h

		let make _source _destination _timestamp _message = 
			let _timestamp = _timestamp +. Unix.time ()
			in {
				source = _source;
				destination = _destination;
				timestamp = _timestamp;
				message = _message
			}
		
		(* Event queue handling. *)
		 
		let eventQueue = ref []
		
		let sendevent _event =
			let rec insert _list =
				match _list with
				  [] -> [_event]
				| _e::_es ->
					if (_event.timestamp < _e.timestamp) then
						_event::_e::_es
					else
						_e::(insert _es)
			in eventQueue := insert !eventQueue

		let rec getevent () =
			let now = Unix.time () in
			match (!eventQueue) with
			  _e::_es when (_e.timestamp <= now) ->
				eventQueue := _es;
				_e
			| _e::_es ->
				!commandhandler (_e.timestamp -. now);
				getevent ()
			| [] ->
				(* should really block here for the
				 * next timed event. *)
				!commandhandler 3600.0;
				getevent ()

		let string_of_event _e =
			"source=" ^ (string_of_int _e.source) ^
			" dest=" ^ (string_of_int _e.destination) ^
			" timestamp=" ^ (string_of_float _e.timestamp)
			^ " " ^ match _e.message with
			  `NoopMsg -> "NoopMsg"
			| _ -> "UnknownMsg"

		(* Debug functions. *)

		let showevent _e =
			printf "%s\n" (string_of_event _e)

		let showeventqueue () =
			printf "Event queue:\n";
			List.iter showevent !eventQueue
	end

(* Represents an actual object. Objects have an event handler, plus a bag of
 * properties defining their state, and that's it. *)
 
and Object: sig
		type propertyType =
			NoProp
			| StringProp of string
			| IntProp of int
			| FloatProp of float
			| IdSetProp of ObjectStore.id array
			
		and hashtableType = (string, propertyType) Hashtbl.t
		
		and eventHandlerType = t -> Event.t -> unit
		and scopeHandlerType = t -> t -> ObjectStore.id list

		and t = {
			properties: hashtableType;
			eventHandler: eventHandlerType;
			scopeHandler: scopeHandlerType
		}
		
		val make : eventHandlerType -> scopeHandlerType -> t
		val eventHandler: eventHandlerType
		val scopeHandler: scopeHandlerType

		val getprop : t -> string -> propertyType
		val getstringprop : t -> string -> string
		val getintprop : t -> string -> int
		val getidsetprop : t -> string -> ObjectStore.id array
		val setprop : t -> string -> propertyType -> unit
		val setidsetprop : t -> string -> ObjectStore.id array -> unit
	end =
	struct
		type propertyType =
			NoProp
			| StringProp of string
			| IntProp of int
			| FloatProp of float
			| IdSetProp of ObjectStore.id array
			
		and hashtableType = (string, propertyType) Hashtbl.t
		
		and eventHandlerType = t -> Event.t -> unit
		and scopeHandlerType = t -> t -> ObjectStore.id list

		and t = {
			properties: hashtableType;
			eventHandler: eventHandlerType;
			scopeHandler: scopeHandlerType;
		}

		let make _e _s = {
			properties = Hashtbl.create 10;
			eventHandler = _e;
			scopeHandler = _s;
		}
		
		(* Default object handlers. *)

		let eventHandler (_object: t) (_event: Event.t) =
			printf "Unhandled event!\n"

		let scopeHandler (_object: t) (_player: t) =
			[]

		(* Property access. *)

		let getprop _object _name =
			Hashtbl.find _object.properties _name

		let getstringprop _object _name =
			match (getprop _object _name) with
			  StringProp _s -> _s
			| _ -> ""

		let getintprop _object _name =
			match (getprop _object _name) with
			  IntProp _i -> _i
			| _ -> 0

		let getidsetprop _object _name =
			match (getprop _object _name) with
			  IdSetProp _s -> _s
			| _ -> [| |]

		let setprop _object _name _value =
			Hashtbl.add _object.properties _name _value
			
		let setidsetprop _object _name _set =
			setprop _object _name (IdSetProp _set)

	end

(* Revision history
 * $Log: Engine.ml,v $
 * Revision 1.1  2004/05/26 00:19:59  dtrg
 * First working version.
 *
 *)
