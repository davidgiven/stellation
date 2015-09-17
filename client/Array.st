Array class extend [
	new       [ ^ <<< LT.makeObject(self, []) >>> ]
]

Array extend [
	size      [ ^ <<< self.length >>> ]
	add: o    [ <<< self.push($o) >>> ]
	keys      [ ^ 0 to: <<< self.length - 1 >>> ]

	at: i [
		self boundsCheck: i.
		^ <<< self[$i] >>>
	]

	at: i put: v [
		self boundsCheck: i.
		^ <<< self[$i] = $v >>>
	]

	boundsCheck: i [
		<<< if (($i < 0) || ($i >= self.length)) throw new Error("out of bounds array access"); >>>
	]
]

