Object subclass: Boolean []

Boolean subclass: True [
	ifTrue: block                         [ ^ block value ]
	ifFalse: block                        [ ]
	ifTrue: trueBlock ifFalse: falseBlock [ ^ trueBlock value ]
	printString                           [ ^ 'true' ]
]

Boolean subclass: False [
	ifTrue: block                         [ ]
	ifFalse: block                        [ ^ block value ]
	ifTrue: trueBlock ifFalse: falseBlock [ ^ falseBlock value ]
	printString                           [ ^ 'false' ]
]

