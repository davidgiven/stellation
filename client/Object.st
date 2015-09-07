Behavior extend [
	subclass: name [
		| newclass |
		newclass := <<< LT.makeSubclass(self, $name) >>>.
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

Object                 subclass: Magnitude []
Magnitude              subclass: Number []

Object extend [
	class [
		^ <<< self._st_class >>>
	]

	hash [
		^ <<< self._st_number >>>
	]

	print [
		<<< console.log(self) >>>
	]
]

String extend [
	hash [
		^ self
	]
]

Behavior extend [
	new [
		^ <<< LT.makeObject(self) >>>
	]

	init [
	]
]

Class extend [
	inspect [
		<<< console.log(self._st_class._st_name) >>>
	]
]

Object extend [
	inspect [
		<<< console.log("a " + self._st_class._st_name) >>>
	]
]

