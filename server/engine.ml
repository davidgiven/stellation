open Printf;;

(* Defines the global object store; this is the mapping between object ids and
 * actual objects. *)

module ObjectStore =
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
	end
;;

(* Encapsulates an event that is sent between two objects, plus the event
 * queue itself. Events have a timestamp that may be in the future. *)
 
module Event =
	struct
		type messageType =
			NoopMsg
			| MessageMsg of string
			| MoveMsg of string
			
		type t = {source: ObjectStore.id; destination: ObjectStore.id;
			timestamp: int; message: messageType}
			
		let make _source _destination ?(_timestamp=0) _message = {
			source = _source;
			destination = _destination;
			timestamp = _timestamp;
			message = _message
		}
		
		(* There has to be a better way of doing a priority queue than this.
		 *)
		 
		let eventQueue = ref []
		
		let sendevent _event =
			let comparisonFunction _event1 _event2 =
				_event1.timestamp - _event2.timestamp in
			eventQueue := List.sort comparisonFunction (_event :: !eventQueue)

		let getevent =
			match (!eventQueue) with
				_e :: [] ->
					eventQueue := [];
					_e
			|	_e :: _es ->
					eventQueue := _es;
					_e
			|	[] ->
					(* should really block here for the next timed event. *)
					printf "Event queue empty!\n";
					raise Not_found
	end
;;

(* Represents an actual object. Objects have an event handler, plus a bag of
 * properties defining their state, and that's it. *)
 
module Object =
	struct
		type propertyType =
			NoProp
			| StringProp of string
			| IntProp of int
			| FloatProp of float
			
		type hashtableType = (string, propertyType) Hashtbl.t
		
		type t = {properties: hashtableType; eventHandler: t -> Event.t -> unit}
		
		let make _e = {
			properties = Hashtbl.create 10;
			eventHandler = _e
		}
		
		let eventHandler (_object: t) (_event: Event.t) =
			printf "Unhandled event!\n"
	end
;;

(* And some test code. *) 
	 
let id = ObjectStore.add (Object.make Object.eventHandler);;

