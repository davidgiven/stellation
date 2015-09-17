Object subclass: Boolean [ ] 

Boolean subclass: True [
	ifTrue: block                         [ ^ block value ]
	ifFalse: block                        [ ]
	ifTrue: trueBlock ifFalse: falseBlock [ ^ trueBlock value ]
	printString                           [ ^ 'true' ]
	not                                   [ ^ false ]
	& o                                   [ ^ o ]
	| o                                   [ ^ true ]
]

Boolean subclass: False [
	ifTrue: block                         [ ]
	ifFalse: block                        [ ^ block value ]
	ifTrue: trueBlock ifFalse: falseBlock [ ^ falseBlock value ]
	printString                           [ ^ 'false' ]
	not                                   [ ^ true ]
	& o                                   [ ^ false ]
	| o                                   [ ^ o ]
]

