/* This attempts to implement the language described here:
 * http://www.gnu.org/software/smalltalk/manual/gst.html#Syntax
 */

start
	= toplevel*

toplevel
	= cd:class_definition
		{ return cd; }
	/ ss:statements
		{ return ss; }

class_definition
	= c:expression EXTEND OPEN_SQ b:class_body CLOSE_SQ
		{ return { type: 'extend', class: c, body: b }; }
	/ c:expression SUBCLASS i:identifier OPEN_SQ b:class_body CLOSE_SQ
		{ return { type: 'subclass', class: c, name: i, body: b }; }

class_body
	= es:class_body_element*
		{ return es; }

class_body_element
	= instance_variables
	/ method_definition

instance_variables
	= BAR ids:identifier* BAR
		{ return { type: 'instance_variables', identifiers: ids }; }

method_definition
	= p:pattern OPEN_SQ b:class_body CLOSE_SQ
		{ return { type: 'method', pattern: p, body: b }; }

pattern
	= id:identifier !':'
		{ return { type: 'pattern', name: id.name, vars: [] }; }
	/ op:operator v:identifier
		{ return { type: 'pattern', name: op.name, vars: [v] }; }
	/ ps:pattern_element*
		{
			var name = [];
			var vars = [];
			for (var i=0; i<ps.length; i++) {
				name.push(ps[i].name);
				vars.push(ps[i].var);
			}
			return { type: 'pattern', name: name.join(""), vars: vars };
		}

pattern_element
	= _? w:word ':' v:identifier
		{ return { type: 'pattern_element', name: w+":", var: v }; }

statements
	= left:statement
		{ return [left]; }
	/ left:statement DOT right:statements
		{ right.unshift(left); return right; }

statement
	= expression

word
	= first:[A-Za-z_$] last:[A-Za-z0-9_$]*
		{ return first + last.join(""); }

identifier
	= _? w:word _?
		{ return { type: 'identifier', name: w }; }

operator
	= _? s:[-~!@%&*+=|\<>,?/]+ _?
		{ return { type: 'identifier', name: s.join("") }; }

expression
	= id:identifier
		{ return id; }

BAR = _? '|' _?
CLOSE_SQ = _? ']' _?
DOT = _? '.' _?
EXTEND = _? 'extend' _?
OPEN_SQ = _? '[' _?
SUBCLASS = _? 'subclass:' _?

_ = [ \t\r\n]+
