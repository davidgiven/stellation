String extend [
	print [
		| s |
		s := self asString.
		<<< console.log($s) >>>
	]

	= other      [ ^ <<< self === $other >>> ]
	~= other     [ ^ <<< self !== $other >>> ]
]

