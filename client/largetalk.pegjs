/* This attempts to implement the language described here:
 * http://www.gnu.org/software/smalltalk/manual/gst.html#Syntax
 */

start
	= ss:statements
		{ return ss; }

statements
	= left:statement
		{ return [left]; }
	/ left:statement '.' right:statements
		{ right.unshift(left); return right; }

statement
	= left:expression op:operator 

