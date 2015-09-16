SequenceableCollection extend [
	do: block [
		self keys do: [ :i |
			block value: (self at: i)
		]
	]
]

