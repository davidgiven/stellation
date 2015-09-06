/* This attempts to implement the language described here:
 * http://www.gnu.org/software/smalltalk/manual/gst.html#Syntax
 */

start
	= toplevel*

toplevel
	= cd:class_definition
		{ return cd; }
	/ ss:statements
		{ return { type: 'statements', body: ss }; }

class_definition
	= c:identifier EXTEND OPEN_SQ b:class_body CLOSE_SQ
		{ return { type: 'extend', class: c, body: b }; }
	/ c:identifier SUBCLASS i:identifier OPEN_SQ b:class_body CLOSE_SQ
		{ return { type: 'subclass', class: c, name: i, body: b }; }

class_body
	= es:class_body_element*
		{ return es; }

class_body_element
	= variables
	/ method_definition

variables
	= BAR ids:identifier* BAR
		{ return { type: 'variables', identifiers: ids }; }

method_definition
	= p:pattern OPEN_SQ b:method_body CLOSE_SQ
		{ return { type: 'method', pattern: p, body: b }; }

method_body
	= v:variables? ss:statements?
		{
			if (!ss)
				ss = [];
			if (v)
				ss.unshift(v);
			return ss;
		}

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
	= left:statement DOT right:statements
		{ right.unshift(left); return right; }
	/ left:statement
		{ return [left]; }

statement
	= id:identifier ASSIGN e:expression
		{ return { type: 'assign', name: id, expression: e }; }
	/ CARET e:expression
		{ return { type: 'return', expression: e }; }
	/ e:expression
		{ return { type: 'expression', expression: e }; }

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
	= keyword_method_call
	/ operator_method_call
	/ unary_method_call
	/ leaf

keyword_method_call
	= r:operator_method_call ms:method_element*
		{
			var name = [];
			var args = [];
			for (var i=0; i<ms.length; i++) {
				name.push(ms[i].name);
				args.push(ms[i].args);
			}
			return { type: 'call', name: name.join(""), args: args };
		}

method_element
	= _? w:word ':' a:operator_method_call
		{ return { type: 'method_element', name: w+":", arg: a }; }

operator_method_call
	= r:unary_method_call op:operator a:unary_method_call
		{ return { type: 'call', name: op, receiver: r, args: [a] }; }

unary_method_call
	= r:leaf id:identifier
		{ return { type: 'call', name: id, receiver: r, args: [] }; }

leaf
	= NIL
		{ return { type: 'javascript', body: 'null' }; }
	/ SELF
		{ return { type: 'javascript', body: '_self' }; }
	/ TRUE
		{ return { type: 'javascript', body: 'true' }; }
	/ FALSE
		{ return { type: 'javascript', body: 'false' }; }
	/ JLEFT b:(!JRIGHT .)* JRIGHT
		{
			var f = [];
			for (var i=0; i<b.length; i++) {
				f.push(b[i][1]);
			}
			return { type: 'javascript', body: f.join("") };
		}
	/ _? m:'-'? base:[0-9]+ 'r' num:[0-9a-zA-Z]+ _?
		{
			var b = base.join("");
			var s = num.join("");
			var n = parseInt(s, b) * (m ? -1 : 1);
			return { type: 'javascript', body: n };
		}
	/ _? m:'-'? num:[0-9]+ _?
		{
			var s = num.join("");
			return { type: 'javascript', body: s|0 };
		}
	/ id:identifier
		{ return id; }

ASSIGN = _? ':=' _?
BAR = _? '|' _?
CARET = _? '^' _?
CLOSE_SQ = _? ']' _?
DOT = _? '.' _?
EXTEND = _? 'extend' _?
FALSE = _? 'false' _?
NIL = _? 'nil' _?
OPEN_SQ = _? '[' _?
SELF = _? 'self' _?
SUBCLASS = _? 'subclass:' _?
TRUE = _? 'true' _?
JLEFT = _? '<<<' _?
JRIGHT = _? '>>>' _?

_ = [ \t\r\n]+
