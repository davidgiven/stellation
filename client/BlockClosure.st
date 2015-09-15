BlockClosure extend [
	whileTrue: other [
		| v_value o_value |
		v_value := <<< LT.findMethod(self, 'value') >>>.
		o_value := <<< LT.findMethod($other, 'value') >>>.
		<<< while ($v_value(self)) { $o_value($other); } >>>
	]

	value [
		^ <<< self() >>>
	]

	value: a1 [
		^ <<< self($a1) >>>
	]
]

