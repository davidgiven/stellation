Object subclass: TestDoesNotUnderstand [
	doesNotUnderstand: m [
		m selector assertEquals: 'thisIsABogusMethod:'.
		(m arguments) assertEquals: {7}.
		(m arguments first) assertEquals: 7.
		^ 9
	]
]

[
	| o |
	o := TestDoesNotUnderstand new.

	Test add: 'doesnotunderstand' doing: [
		| i |
		i := o thisIsABogusMethod: 7.
		i assertEquals: 9
	]
]

