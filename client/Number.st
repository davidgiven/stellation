Number extend [
	hash [
		^ self
	]

	- n       <<< return self - $n; >>>
	+ n       <<< return self + $n; >>>
	* n       <<< return self * $n; >>>
	/ n       <<< return self / $n; >>>
	// n      <<< return Math.floor(self / $n) >>>
	quo: n    <<< return (self / $n)|0 >>>
	\\ n      <<< return self % $n; >>>
	> n       <<< return self > $n; >>>
	< n       <<< return self < $n; >>>
	>= n      <<< return self >= $n; >>>
	<= n      <<< return self <= $n; >>>
	= n       <<< return self === $n; >>>
	~= n      <<< return self !== $n; >>>

	to: end          [ ^ Interval new initFrom: self to: end ]
	to: end by: step [ ^ Interval new initFrom: self to: end by: step ]

	asString         <<< return self.toString(); >>>
	printString      <<< return self.toString(); >>>
]


