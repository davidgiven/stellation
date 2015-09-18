"* The Computer Language Benchmarks Game
    http://benchmarksgame.alioth.debian.org/
    contributed by Isaac Gouy
    modified by Eliot Miranda
	ported to LT by David Given *"

"This should produce output looking like this:
args = 20

PROGRAM OUTPUT:
stretch tree of depth 21	 check: -1
2097152	 trees of depth 4	 check: -2097152
524288	 trees of depth 6	 check: -524288
131072	 trees of depth 8	 check: -131072
32768	 trees of depth 10	 check: -32768
8192	 trees of depth 12	 check: -8192
2048	 trees of depth 14	 check: -2048
512	 trees of depth 16	 check: -512
128	 trees of depth 18	 check: -128
32	 trees of depth 20	 check: -32
long lived tree of depth 20	 check: -1
"

Object subclass: TreeNode [
	| left right item |

	left: leftChild right: rightChild item: anItem [
		left := leftChild.
		right := rightChild.
		item := anItem
	]

	itemCheck [
		^left isNil 
			ifTrue: [item] ifFalse: [item + (left itemCheck - right itemCheck)]
	]
]

TreeNode class extend [
	bottomUpTree: anItem depth: anInteger [
	   ^(anInteger > 0) 
		  ifTrue: [
			 self 
				left: (self bottomUpTree: 2*anItem - 1 depth: anInteger - 1) 
				right: (self bottomUpTree: 2*anItem depth: anInteger - 1)  
				item: anItem
			 ]
		  ifFalse: [self left: nil right: nil item: anItem]
	]

	left: leftChild right: rightChild item: anItem [
		^(self new) left: leftChild right: rightChild item: anItem
	]
]

Test class extend [
	binarytrees: n [
		| minDepth maxDepth stretchDepth check longLivedTree iterations |
		minDepth := 4.
		maxDepth := minDepth + 2 max: n.
		stretchDepth := maxDepth + 1.

		check := (TreeNode bottomUpTree: 0 depth: stretchDepth) itemCheck.
		('stretch tree of depth ', stretchDepth asString, ' check: ', check asString) displayNl.

		longLivedTree := TreeNode bottomUpTree: 0 depth: maxDepth.
		minDepth to: maxDepth by: 2 do: [:depth|
			iterations := 1 bitShift: maxDepth - depth + minDepth.

			check := 0.
			1 to: iterations do: [:i|
				check := check + (TreeNode bottomUpTree: i depth: depth) itemCheck.
				check := check + (TreeNode bottomUpTree: -1*i depth: depth) itemCheck
			].

			((2*iterations) asString, ' trees of depth ', depth asString, ' check: ', check) displayNl
		].

		('long lived tree of depth ', maxDepth asString, ' check: ', longLivedTree itemCheck asString) displayNl
	]

	binarytrees [
	   | s e |
	   s := Time millisecondClock.
	   self binarytrees: 10.
	   e := Time millisecondClock.
	   ('time taken: ', (e-s) asString, ' ms') displayNl
	]
]

