Behavior extend [
	subclass: name [
		| newclass |
		newclass := <<< LT.makeSubclass(self, $name) >>>.
		^newclass
	]
]

Object                 subclass: UndefinedObject []

Object                 subclass: Iterable []
Iterable               subclass: Collection []
Collection             subclass: SequenceableCollection []
SequenceableCollection subclass: ArrayedCollection []
ArrayedCollection      subclass: CharacterArray []
ArrayedCollection      subclass: Array []
CharacterArray         subclass: String []
String                 subclass: LiteralString []

Object                 subclass: Magnitude []
Magnitude              subclass: Number []

Object                 subclass: BlockClosure []

Object extend [
	class        [ ^ <<< self._st_class >>> ]
	hash         [ ^ <<< self._st_number >>> ]
	printString  [ ^ ('an instance of ', self class) ]
	asString     [ ^ self printString ]

	isNil        [ ^ false ]

	= other      [ ^ self == other ]
	~= other     [ ^ (self = other) not ]
	== other     [ ^ <<< self === $other >>> ]
	~~ other     [ ^ (self == other) not ]

	print [
		| s |
		s := self printString.
		<<< console.log($s) >>>
	]

	inspect      [ self print ]
	ifNil: block [ self isNil ifTrue: block ]

	doesNotUnderstand: n with: a [
		^ self doesNotUnderstand: (Message selector: n arguments: a)
	]

	doesNotUnderstand: m [
		| s |
		s := ('Unknown method ', (self class), '>>', (m selector)).
		s print.
		<<< debugger >>>
	]
]

UndefinedObject extend [
	isNil        [ ^ true ]
	printString  [ ^ 'nil' ]
]

Behavior extend [
	new          [ ^ <<< LT.makeObject(self) >>> ]
	init         [ ]
	printString  <<< return self._st_name; >>>
]

Class extend [
]

