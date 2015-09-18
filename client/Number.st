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

	to: e             [ ^ Interval new initFrom: self to: e ]
	to: e do: b       [ ^ (self to: e) do: b ]
	to: e by: s       [ ^ Interval new initFrom: self to: e by: s ]
	to: e by: s do: b [ ^ (self to: e by: s) do: b ]

	asString          <<< return self.toString(); >>>
	printString       <<< debugger; return self.toString(); >>>

	max: o            <<< return Math.max(self, $o); >>>
	min: o            <<< return Math.min(self, $o); >>>
	bitShift: o       <<< return ($o > 0) ? (self << $o) : (self >> -$o); >>>
]


