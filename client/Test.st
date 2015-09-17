Object subclass: Test [
	| block name |

	init: n doing: b [
		block := b.
		name := n
	]

	init: b [
		block := b.
		name := nil
	]

	name [ ^ name ]
	run [ block value ]
]

Object extend [
	assertEquals: v [
		(self = v) ifFalse: [
			Test fail: ('want: ', v, '; got: ', self)
		]
	]
]

Boolean extend [
	assertTrue [ self assertEquals: true ]
	assertFalse [ self assertEquals: false ]
]

BlockClosure extend [
	assertTrue [ (self value) assertTrue ]
	assertFalse [ (self value) assertFalse ]
]

Test class extend [
	| tests failed current printedHeader |

	setup [
		tests ifNil: [
			tests := Array new.
			failed := false
		]
	]

	add: block [
		^ self add: nil doing: block
	]

	add: name doing: block [
		| t |
		self setup.
		t := Test new init: name doing: block.
		tests add: t.
		^t
	]

	fail: message [
		printedHeader ifFalse: [
			('Failed: ', current name) print.
			printedHeader := true
		].
		message print.
		failed := true
	]

	runAll [
		'Running tests' print.
		tests do: [ :t |
			current := t.
			printedHeader := false.
			t run
		].
		
		failed ifTrue: [
			'Tests failed!' print
		] ifFalse: [
			'Tests passed' print
		]
	]
]

