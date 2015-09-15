Boolean extend [
	ifTrue: block [
		<<< if (self) LT.findMethod($block, "value")($block) >>>
	]

	ifTrue: block [
		<<< if (!self) LT.findMethod($block, "value")($block) >>>
	]

	ifTrue: trueBlock ifFalse: falseBlock [
		<<< if (self) {
		        LT.findMethod($trueBlock, "value")($trueBlock) 
			} else {
				LT.findMethod($falseBlock, "value")($falseBlock)
			} >>>
	]
]

