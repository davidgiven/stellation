ArrayedCollection subclass: Interval [
	| start end delta |

	initFrom: s to: e [
		start := s.
		end := e.
		delta := 1
	]
]

