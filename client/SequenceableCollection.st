SequenceableCollection extend [
	do: block [
		self keys do: [ :i |
			block value: (self at: i)
		]
	]

	first [ ^ self at: (self keys first) ]
	last  [ ^ self at: (self keys last) ]
	
	= other [
		| k |
		k := self keys.
		(k = other keys) ifFalse: [ ^false ].
		k do: [ :i |
			(self at: i) = (other at: i) ifFalse: [ ^false ]
		].
		^true
	]

	printString [
		| s |
		s := '('.
		self do: [ :i |
			s := s, i printString, ' '
		].
		s := s, ')'.
		^s
	]
]

