ArrayedCollection subclass: Interval []

Interval class extend [
	from: s to: e [
		^ Interval new initFrom: s to: e by: 1
	]

	from: s to: e by: b [
		^ Interval new initFrom: s to: e by: b
	]
]

Interval extend [
	| start end step |

	initFrom: s to: e [
		self initFrom: s to: e by: 1
	]

	initFrom: s to: e by: b [
		start := s.
		end := e.
		step := b
	]

	at: i [
		^ start + (i * step)
	]

	reverse [
		^ Interval from: end to: start by: (step negate)
	]

	first [
		^ start
	]

	last [
		^ end
	]

	increment [
		^ step
	]

	do: block [
		| i c |
		i := start.

		(step > 0) ifTrue: [
			c := [i <= end]
		] ifFalse: [
			c := [i >= end]
		].

		c whileTrue: [
			block value: i.
			i := i + step
		]
	]
]

