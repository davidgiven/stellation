Object subclass: Message [
	| selector arguments |

	selector  [ ^selector ]
	arguments [ ^arguments ]
	argument  [ ^ arguments first ]

	arguments: as [ arguments := as ]
	selector: s   [ selector := s ]
]

Object class extend [
	selector: n argument: a [
		^ self selector: n arguments: {a}
	]

	selector: n arguments: as [
		| s |
		s := Message new.
		s selector: n.
		s arguments: as.
		^s
	]
]

