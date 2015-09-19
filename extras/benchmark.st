"* The Computer Language Benchmarks Game
    http://benchmarksgame.alioth.debian.org/
    contributed by Isaac Gouy
    modified by Eliot Miranda *"!


Object subclass: #TreeNode
   instanceVariableNames: 'left right item'
   classVariableNames: ''
   poolDictionaries: ''
   category: 'Shootout'!

!Tests class methodsFor: 'benchmarking'!
binarytrees: n
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
		 ((2*iterations) asString, ' trees of depth ', depth asString, ' check: ', check asString) displayNl
      ].

   	('long lived tree of depth ', maxDepth asString, ' check: ', longLivedTree itemCheck asString) displayNl
!!


!TreeNode methodsFor: 'initialize-release'!
left: leftChild right: rightChild item: anItem
   left := leftChild.
   right := rightChild.
   item := anItem! !

!TreeNode methodsFor: 'accessing'!
itemCheck
   ^left isNil 
      ifTrue: [item] ifFalse: [item + (left itemCheck - right itemCheck)]! !


!TreeNode class methodsFor: 'instance creation'!
bottomUpTree: anItem depth: anInteger
   ^(anInteger > 0) 
      ifTrue: [
         self 
            left: (self bottomUpTree: 2*anItem - 1 depth: anInteger - 1) 
            right: (self bottomUpTree: 2*anItem depth: anInteger - 1)  
            item: anItem
         ]
      ifFalse: [self left: nil right: nil item: anItem]! !

!TreeNode class methodsFor: 'instance creation'!
left: leftChild right: rightChild item: anItem      
   ^(super new) left: leftChild right: rightChild item: anItem! !

| s e |
s := Time millisecondClock.
Tests binarytrees: 12.
e := Time millisecondClock.
('time taken: ', (e-s) asString, ' ms') displayNl
!!


