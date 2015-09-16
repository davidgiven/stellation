(function() {
	"use strict";

	var MIMETYPE = "text/largetalk";
	var GRAMMAR_MIMETYPE = "text/largetalk-grammar";


	var grammar;

	var LT = {};
	window.LT = LT;

	/* =================================================================== */
	/*                             UTILITIES                               */
	/* =================================================================== */

	/* Converts an array-like into an array. */

	function arrayOf(o) {
		return Array.prototype.slice.call(o);
	}

	/* =================================================================== */
	/*                           STANDARD LIBRARY                          */
	/* =================================================================== */

	var serial_number = 0;

	function set(object, name, value) {
		Object.defineProperty(object, name, { value: value, enumerable: false });
	}

	function make_raw_class(name, superklass) {
		return {
			_st_number: serial_number++,
			_st_super: superklass,
			_st_methods: superklass ? Object.create(superklass._st_methods) : {},
			_st_name: name,
			_st_ivars: {}
		};
	}

	function make_named_class(name, superklassname) {
		var superklass = superklassname ? window["$" + superklassname] : null;
		var o = make_raw_class(name, superklass);
		window["$" + name] = o;
		return o;
	}

	make_named_class("Object", null);
	make_named_class("Behavior", "Object");
	make_named_class("ClassDescription", "Behavior");
	make_named_class("Class", "ClassDescription");
	var _Metaklass = make_named_class("Metaclass", "ClassDescription");

	function assign_metaclass(name) {
		var object = window["$" + name];
		var superklass = object._st_super;
		if (superklass)
			superklass = superklass._st_class;
		else
			superklass = _Metaklass;

		var metaklass = make_raw_class(name + " class", superklass);
		object._st_class = metaklass;
		metaklass._st_class = _Metaklass;
	}

	assign_metaclass("Object");
	assign_metaclass("Behavior");
	assign_metaclass("ClassDescription");
	assign_metaclass("Class");
	assign_metaclass("Metaclass");

	LT.makeObject = function(superklass, o) {
		if (!o)
			o = {};

		set(o, "_st_number", serial_number++);
		set(o, "_st_class", superklass);
		return o;
	};

	var primitive_table = {
		string: function(o) { return $LiteralString; },
		number: function(o) { return $Number; },
		boolean: function(o) { return $Boolean; },
		function: function(o) { return $BlockClosure; },
		undefined: function(o) { return $UndefinedObject; },

		object:
			function(o) {
				var c = (o instanceof Array) ? $Array : $JavascriptObject;

				set(o, "_st_number", serial_number++);
				set(o, "_st_class", c);
				return c;
			}
	};

	LT.findMethod = function(receiver, name) {
		var c;
		switch (receiver) {
			case null:
			case undefined:
				c = $UndefinedObject;
				break;

			case true:
				c = $True;
				break;

			case false:
				c = $False;
				break;

			default:
				c = receiver._st_class;
				if (!c) {
					c = primitive_table[typeof(receiver)];
					if (!c)
						throw new Error("Can't call methods on " + typeof(receiver) +" yet");
					c = c();
				}
				break;
		}

		var m = c._st_methods[name];
		if (m)
			return m;

		throw new Error("Unknown method " + c._st_name + ">>" + name);
	}

	LT.makeSubclass = function(superklass, name) {
		var klass = make_raw_class(name, superklass);
		window["$" + name] = klass;

		var metaklass = make_raw_class(name + " class", superklass._st_class);
		klass._st_class = metaklass;
		metaklass._st_class = _Metaklass;
		return klass;
	}

	/* =================================================================== */
	/*                              COMPILER                               */
	/* =================================================================== */

	function flatten(n) {
		var f = [];
		var lineno = 0;

		function flattena(n) {
			for (var i=0; i<n.length; i++) {
				var v = n[i];
				if (v instanceof Array)
					flattena(v);
				else if (v instanceof Object) {
					f.push(v.string);
				} else
					f.push(v);
			}
			return f;
		}

		return flattena(n).join(" ");
	}

	function pushs(f, n, s, o) {
		var a = {
			location: n.location,
			string: s
		};
		if (o)
			a.original = o;

		f.push(a);
	}

	function pushss(f, ss, c) {
		if (ss.length > 0) {
			f.push(ss[0]);
			for (var i=1; i<ss.length; i++) {
				f.push(c);
				f.push(ss[i]);
			}
		}
	}
			
	function compile_ref(context, node) {
		var v = context.varmap[node.name];
		if (!v)
			v = "$" + node.name;

		var f = [];
		pushs(f, node, v, node.name);
		return f;
	}

	function compile_expr(context, node) {
		switch (node.type) {
			case "javascript":
			{
				var f = [];
				pushs(f, node, node.body);
				return f;
			}

			case "identifier":
				return compile_ref(context, node);

			case "call":
			{
				var t = context.temporaries++;
				var f = [];
				f.push("(t" + t);
				f.push(" = (");
				f.push(compile_expr(context, node.receiver));
				f.push("), LT.findMethod(t" + t);
				f.push(",");
				pushs(f, node, "'" + node.name + "'", node.name);
				f.push(")(t" + t);
				for (var i=0; i<node.args.length; i++) {
					f.push(",");
					f.push(compile_expr(context, node.args[i]));
				}
				f.push("))");
				return f;
			}

			case "string":
			{
				var f = [];
				pushs(f, node, JSON.stringify(node.value));
				return f;
			}
				
			case "block":
				return compile_block(context, node);

			default:
				throw new Error("Unknown expression node " + node.type);
		}
	}

	function compile_raw_block(context, node) {
		var f = [];
		var maxtemporaries = 0;
		context.temporaries = 0;

		for (var i=0; i<node.body.length; i++) {
			var last = (i == node.body.length-1);
			var n = node.body[i];
			switch (n.type) {
				case "variables":
				{
					var newvarmap = Object.create(context.varmap);
					for (var j=0; j<n.identifiers.length; j++) {
						var id = n.identifiers[j];
						pushs(f, id, "var $" + id.name + " = null;", id.name);
						newvarmap[id.name] = "$" + id.name;
					}
					context.varmap = newvarmap;
					break;
				}

				case "return":
					if (context.block) {
						f.push("retval.value = ");
						f.push(compile_expr(context, n.expression));
						f.push(";");
						f.push("throw retval;");
					} else {
						f.push("return");
						f.push(compile_expr(context, n.expression));
						f.push(";");
					}
					break;

				case "assign":
					if (context.block && last)
						f.push("return");
					f.push(compile_ref(context, n.name));
					f.push("=");
					f.push(compile_expr(context, n.expression));
					f.push(";");
					break;

				case "expression":
					if (context.block && last)
						f.push("return");
					f.push(compile_expr(context, n.expression));
					f.push(";");
					break;

				default:
					throw new Error("Unknown method body node " + n.type);
			}

			maxtemporaries = Math.max(context.temporaries);
			context.temporaries = 0;
		}

		if (maxtemporaries > 0) {
			for (var i=0; i<maxtemporaries; i++)
				f.unshift("var t" + i + ";");
		}

		return f;
	}

	function compile_block(context, node) {
		var newvarmap = Object.create(context.varmap);
		var vars = [];

		if (node.parameters) {
			for (var i=0; i<node.parameters.length; i++) {
				var v = node.parameters[i].name;
				newvarmap[v] = "$" + v;
				vars.push("$" + v);
			}
		}

		var f = [];
		f.push("(function(");
		pushss(f, vars, ",");
		f.push(") {");

		var context = {
			varmap: newvarmap,
			block: true
		};
		f.push(compile_raw_block(context, node));

		f.push("})");
		return f;
	}

	function compile_method(klass, node) {
		var varmap = {};
		var vars = node.pattern.vars.map(
			function (v) { return "$" + v.name; }
		);
		vars.unshift("self");

		for (var v in klass._st_ivars)
			varmap[v] = "self." + v + "$" + klass._st_number;

		var f = [];
		f.push("return (function(");
		pushss(f, vars, ",");
		f.push(") {");
		f.push("var retval = {value: self};");
		f.push("try {");

		var context = {
			varmap: varmap,
			block: false
		};
		f.push(compile_raw_block(context, node));

		f.push("} catch (e) {");
		f.push("if (e !== retval) throw e;");
		f.push("}");
		f.push("return retval.value;");
		f.push("});");

		var cf = new Function(flatten(f));
		var ccf = cf();

		return {
			name: node.pattern.name,
			callable: ccf
		};
	}

	function compile_jmethod(klass, node) {
		var vars = node.pattern.vars.map(
			function (v) {
				var f = [];
				pushs(f, v, "$" + v.name, v.name);
				return f;
			}
		);
		vars.unshift("self");

		var f = [];
		f.push("return (function(");
		pushss(f, vars, ",");
		f.push(") {");
		f.push(node.body.body);
		f.push("});");

		var cf = new Function(flatten(f));
		var ccf = cf();

		return {
			name: node.pattern.name,
			callable: ccf
		};
	}

	function compile_class_body(klass, nodes) {
		for (var i=0; i<nodes.length; i++) {
			var node = nodes[i];

			switch (node.type) {
				case "method":
					var m = compile_method(klass, node);
					klass._st_methods[m.name] = m.callable;
					break;

				case "jmethod":
					var m = compile_jmethod(klass, node);
					klass._st_methods[m.name] = m.callable;
					break;

				case "variables":
					for (var j=0; j<node.identifiers.length; j++)
						klass._st_ivars[node.identifiers[j].name] = true;
					break;

				default:
					throw new Error("Unsupported class body node " + node.type);
			}
		}
	}

	var toplevel_nodes = {
		block:
			function(node) {
				var f = [];
				f.push("var retval = null;");

				var context = {
					varmap: {},
					block: true
				};
				f.push(compile_raw_block(context, node));

				var cf = new Function(flatten(f));
				cf.call(null);
			},

		extend:
			function(node) {
				var klass = window["$" + node.class.name];
				if (!klass)
					throw new Error("Undefined LT class '" + node.class.name + "'");
				if (node.class.type == "classidentifier")
					klass = klass._st_class;

				compile_class_body(klass, node.body);
			},

		subclass:
			function(node) {
				var klass = window["$" + node.class.name];
				if (!klass)
					throw new Error("Undefined LT class '" + node.class.name + "'");

				var subklass = LT.findMethod(klass, "subclass:")(klass, node.name.name);
				compile_class_body(subklass, node.body);
			}
	};

	function compile_toplevel(node) {
		var cb = toplevel_nodes[node.type];
		if (!cb)
			throw new Error("Unsupported toplevel node " + node.type);

		return cb(node);
	}

	function compile(source) {
		var ast;
		try {
			ast = grammar.parse(source);
		} catch (e) {
			if (e.name == "SyntaxError") {
				e.message = e.location.start.line + "." +
					e.location.start.column + ":" +
					e.message;
			}
			throw e;
		}

		for (var i=0; i<ast.length; i++)
			compile_toplevel(ast[i]);
	}

	/* =================================================================== */
	/*                              STARTUP                                */
	/* =================================================================== */

	/* Construct the list of candidate elements. */

	var largetalk_elements = [];
	var grammar_element;

	function start_loading_script(element) {
		if (element._st_req) return;

		var req = element._st_req = new XMLHttpRequest();
		req.open("GET", element.getAttribute("src"));
		req.onload = function(e) {
			element._st_src = req.responseText;
			done_loading_scripts();
		};
		req.send();
	}

	function done_loading_scripts() {
		/* Don't do ANYTHING until the grammar has loaded. */

		if (!grammar_element) return;
		if (grammar_element._st_src == null) return;
		if (!grammar) {
			grammar = PEG.buildParser(grammar_element._st_src, {
			});
		}

		/* Consume any scripts that have finished loading (being careful to
		 * maintain the right order!) */

		for (;;) {
			var element = largetalk_elements[0];
			if (!element) break;
			if (element._st_src == null) break;

			console.log("Compiling " + element.src);
			compile(element._st_src);

			largetalk_elements.shift();
		}
	}

	/* Ensure that any new script elements are found and handled. */

	var observer = new MutationObserver(function(mutations) {
		mutations.forEach(function(mutation) {
			arrayOf(mutation.addedNodes).forEach(
				function(element) {
					if (element.nodeName != "SCRIPT") return;

					switch (element.getAttribute("type")) {
						case MIMETYPE:
							largetalk_elements.push(element);
							start_loading_script(element);
							break;

						case GRAMMAR_MIMETYPE:
							grammar_element = element;
							start_loading_script(element);
							break;
					}
				}
			)
		});
	});

	observer.observe(document, {
		childList: true,
		subtree: true
	});
})();

