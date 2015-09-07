Number extend [
	hash [
		^ self
	]

	- n <<< return self - $n; >>>
	+ n <<< return self + $n; >>>
	* n <<< return self * $n; >>>
	/ n <<< return self / $n; >>>
	% n <<< return self % $n; >>>

	to: end [
		^ Interval new initFrom: self to: end
	]
]


