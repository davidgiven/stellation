/* This attempts to implement the language described here:
 * http://www.gnu.org/software/smalltalk/manual/gst.html#Syntax
 */

start
	= toplevel*

toplevel
	= class_definition
	/ block

class_definition
	= c:classidentifier EXTEND OPEN_SQ b:class_body CLOSE_SQ
		{ return { location: location(), type: 'extend', class: c, body: b }; }
	/ c:identifier EXTEND OPEN_SQ b:class_body CLOSE_SQ
		{ return { location: location(), type: 'extend', class: c, body: b }; }
	/ c:identifier SUBCLASS i:identifier OPEN_SQ b:class_body CLOSE_SQ
		{ return { location: location(), type: 'subclass', class: c, name: i, body: b }; }

class_body
	= es:class_body_element*
		{ return es; }

class_body_element
	= variables
	/ method_definition

method_definition
	= p:pattern OPEN_SQ v:variables? b:method_body CLOSE_SQ
		{
			if (v)
				b.unshift(v);
			return { location: location(), type: 'method', pattern: p, body: b };
		}
	/ p:pattern j:javascript
		{ return { location: location(), type: 'jmethod', pattern: p, body: j }; }

variables
	= BAR ids:identifier+ BAR
		{ return { location: location(), type: 'variables', identifiers: ids }; }

parameters_or_variables
	= ps:parameter+ BAR vs:identifier+ BAR
		{ return { location: location(), type: 'parameters_variables', parameters: ps, variables: vs }; }
	/ BAR vs:identifier+ BAR
		{ return { location: location(), type: 'parameters_variables', variables: vs }; }
	/ ps:parameter+ BAR
		{ return { location: location(), type: 'parameters_variables', parameters: ps }; }


method_body
	= ss:statements?
		{
			if (!ss)
				return [];
			return ss;
		}

pattern
	= id:identifier
		{ return { location: location(), type: 'pattern', name: id.name, vars: [] }; }
	/ op:operator v:identifier
		{ return { location: location(), type: 'pattern', name: op.name, vars: [v] }; }
	/ ps:pattern_element*
		{
			var name = [];
			var vars = [];
			for (var i=0; i<ps.length; i++) {
				name.push(ps[i].name);
				vars.push(ps[i].var);
			}
			return { location: location(), type: 'pattern', name: name.join(""), vars: vars };
		}

pattern_element
	= _? w:word ':' v:identifier
		{ return { location: location(), type: 'pattern_element', name: w+":", var: v }; }

block
	= OPEN_SQ p:parameters_or_variables? b:method_body CLOSE_SQ
		{
			if (!p)
				p = {};
			if (p.variables)
				b.unshift({ type: 'variables', identifiers: p.variables });
			return { location: location(), type: 'block', parameters: p.parameters, body: b };
		}

statements
	= left:statement DOT right:statements
		{ right.unshift(left); return right; }
	/ left:statement
		{ return [left]; }

statement
	= id:identifier ASSIGN e:expression
		{ return { location: location(), type: 'assign', name: id, expression: e }; }
	/ CARET e:expression
		{ return { location: location(), type: 'return', expression: e }; }
	/ e:expression
		{ return { location: location(), type: 'expression', expression: e }; }

word
	= first:[A-Za-z_$] last:[A-Za-z0-9_$]*
		{ return first + last.join(""); }

identifier
	= _? w:word !':' _?
		{ return { location: location(), type: 'identifier', name: w }; }

parameter
	= _? ':' w:word _?
		{ return { location: location(), type: 'identifier', name: w }; }

classidentifier
	= id:identifier CLASS
		{ return { location: location(), type: 'classidentifier', name: id.name }; }

operator
	= _? s:[-~!@%&*+=|\\<>,?/]+ _?
		{ return { location: location(), type: 'identifier', name: s.join("") }; }

expression
	= keyword_method_call

keyword_method_call
	= r:operator_method_call ms:method_element+
		{
			var name = [];
			var args = [];
			for (var i=0; i<ms.length; i++) {
				name.push(ms[i].name);
				args.push(ms[i].arg);
			}
			return { location: location(), type: 'call', name: name.join(""), receiver: r, args: args };
		}
	/ operator_method_call

method_element
	= _? w:word ':' a:operator_method_call
		{ return { location: location(), type: 'method_element', name: w+":", arg: a }; }

operator_method_call
	= r:unary_method_call ops:(operator unary_method_call)+
		{
			var o = r;
			for (var i=0; i<ops.length; i++)
				o = { type: 'call', name: ops[i][0].name, receiver: o, args: [ops[i][1]] };
			return o;
		}
	/ unary_method_call

unary_method_call
	= r:leaf id:identifier ids:identifier*
		{ 
			var o = { location: location(), type: 'call', name: id.name, receiver: r, args: [] };
			for (var i=0; i<ids.length; i++)
				o = { location: location(), type: 'call', name: ids[i].name, receiver: o, args: [] };
			return o;
		}
	/ leaf

dot_expression
	= DOT e:expression
		{ return e; }

code_array_literal
	= OPEN_C f:(expression dot_expression*)? CLOSE_C
		{
			if (!f)
				return { location: location(), type: 'code_array', values: [] };

			var es = f[1];
			es.unshift(f[0]);

			return { location: location(), type: 'code_array', values: es };
		}

leaf
	= NIL
		{ return { location: location(), type: 'javascript', body: 'null' }; }
	/ SELF
		{ return { location: location(), type: 'javascript', body: 'self' }; }
	/ TRUE
		{ return { location: location(), type: 'javascript', body: 'true' }; }
	/ FALSE
		{ return { location: location(), type: 'javascript', body: 'false' }; }
	/ SUPER
		{ return { location: location(), type: 'super' }; }
	/ _? m:'-'? base:[0-9]+ 'r' num:[0-9a-zA-Z]+ _?
		{
			var b = base.join("");
			var s = num.join("");
			var n = parseInt(s, b) * (m ? -1 : 1);
			return { location: location(), type: 'javascript', body: n };
		}
	/ _? m:'-'? num:[0-9]+ _?
		{
			var s = num.join("");
			if (m)
				s = -s;
			return { location: location(), type: 'javascript', body: s|0 };
		}
	/ OPEN_PAREN e:expression CLOSE_PAREN
		{ return e; }
	/ code_array_literal
	/ block
	/ javascript
	/ string
	/ identifier

javascript
	= JLEFT b:(!JRIGHT .)* JRIGHT
		{
			var f = [];
			for (var i=0; i<b.length; i++) {
				f.push(b[i][1]);
			}
			return { location: location(), type: 'javascript', body: f.join("") };
		}

string
	= _? ss:string_segment+ _?
		{ return { location: location(), type: 'string', value: ss.join("'") }; }

string_segment
	= "'" ss:(!"'" .)* "'"
		{
			var f = [];
			for (var i=0; i<ss.length; i++)
				f.push(ss[i][1]);
			return f.join("");
		}

ASSIGN = _? ':=' _?
BAR = _? '|' _?
CARET = _? '^' _?
CLASS = _? 'class' !word _?
CLOSE_C = _? '}' _?
CLOSE_PAREN = _? ')' _?
CLOSE_SQ = _? ']' _?
DOT = _? '.' _?
EXTEND = _? 'extend' !word _?
FALSE = _? 'false' !word _?
JLEFT = _? '<<<' _?
JRIGHT = _? '>>>' _?
NIL = _? 'nil' !word _?
OPEN_C = _? '{' _?
OPEN_PAREN = _? '(' _?
OPEN_SQ = _? '[' _?
SELF = _? 'self' !word _?
SUBCLASS = _? 'subclass:' _?
SUPER = _? 'super' _?
TRUE = _? 'true' !word _?

_
	= [ \t\r\n]+ _*
	/ '"' [^"]* '"' _*

