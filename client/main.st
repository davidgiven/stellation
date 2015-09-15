[
	| a b |
	a := 1 to: 10.
	a do: [ :i | i inspect ].

	a := 10 to: 1.
	a do: [ :i | i inspect ].

	a := 10 to: 1 by: -1.
	a do: [ :i | i inspect ]
]

