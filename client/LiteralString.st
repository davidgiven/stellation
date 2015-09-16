LiteralString extend [
	hash        [ ^ self ]
	asString    [ ^ self ]
	printString [ ^ ('''', self, '''') ]

	, other [
		| s |
		s := other asString.
		^ <<< self + $s >>>
	]
]

