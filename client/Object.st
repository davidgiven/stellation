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
	printString  [ ^ ('an instance of ', self class toString) ]

	isNil        [ ^ false ]

	= other      [ ^ self == other ]
	~= other     [ ^ self ~~ other ]
	== other     [ ^ <<< self === $other >>> ]
	~~ other     [ ^ <<< self !== $other >>> ]

	print [
		| s |
		s := self printString.
		<<< console.log($s) >>>
	]

	inspect      [ self print ]
	ifNil: block [ self isNil ifTrue: block ]

]

UndefinedObject extend [
	isNil        [ ^ true ]
	printString  [ ^ 'nil' ]
]

Behavior extend [
	new          [ ^ <<< LT.makeObject(self) >>> ]
	init         [ ]
]

Class extend [
	printString <<< return self._st_class._st_name; >>>
]

