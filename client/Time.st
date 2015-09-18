Magnitude subclass: Time []

Time class extend [
	millisecondClock      <<< return new Date().getTime(); >>>
	millisecondClockValue [ ^ self millisecondClock ]
]
