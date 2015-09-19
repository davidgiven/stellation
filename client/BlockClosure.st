BlockClosure extend [
	whileTrue: other 
		<<<
			var v_value = LT.findMethod(self, 'value');
			var o_value = LT.findMethod($other, 'value');
			while (v_value(self)) { o_value($other); }
		>>>

	value     <<< return self(); >>>
	value: a1 <<< return self($a1); >>>
]

