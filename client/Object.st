Behavior extend [
	subclass: name [
		| newclass |
		newclass := <<< LT.makeSubclass(this, $name) >>>.
		^newclass
	]
]

Object                 subclass: Iterable []
Iterable               subclass: Collection []
Collection             subclass: SequenceableCollection []
SequenceableCollection subclass: ArrayedCollection []
ArrayedCollection      subclass: CharacterArray []
CharacterArray         subclass: String []
String                 subclass: LiteralString []

[<<< String.prototype._st_class = LT.systemDictionary.$LiteralString >>>]

Object                 subclass: Magnitude []
Magnitude              subclass: Number []

[<<< Number.prototype._st_class = LT.systemDictionary.$Number >>>]

Object extend [
	class [
		^ <<< this._st_class >>>
	]

	hash [
		^ <<< this._st_number >>>
	]

	print [
		<<< console.log(this) >>>
	]
]

Number extend [
	hash [
		^ self
	]
]

String extend [
	hash [
		^ self
	]
]

Behavior extend [
	new [
		^ <<< LT.makeObject(this) >>>
	]

	init [
	]
]

Class extend [
	inspect [
		<<< console.log(this._st_class._st_name) >>>
	]
]

Object extend [
	inspect [
		<<< console.log("a " + this._st_class._st_name) >>>
	]
]

[
	| i |
	i := 8.
	i hash print
]
