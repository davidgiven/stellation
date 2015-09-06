Behavior extend [
	new [
		^ <<< LT.makeObject(this) >>>
	]

	init [
	]
]

Class extend [
	inspect [
		<<< console.log(this._st_class._st_name); >>>
	]
]

Object extend [
	inspect [
		<<< console.log("a " + this._st_class._st_name); >>>
	]
]

o := Object new.
o inspect

