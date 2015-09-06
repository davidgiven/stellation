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

	var system_dictionary = {};
	LT.systemDictionary = system_dictionary;

	function make_raw_class(name, superklass) {
		return {
			_st_number: serial_number++,
			_st_vars: {},
			_st_super: superklass,
			_st_methods: superklass ? Object.create(superklass._st_methods) : {},
			_st_name: name
		};
	}

	function make_system_class(name, superklassname) {
		var superklass = superklassname ? system_dictionary["$" + superklassname] : null;
		var o = make_raw_class(name, superklass);
		system_dictionary["$" + name] = o;
		return o;
	}

	make_system_class("Object", null);
	make_system_class("Behavior", "Object");
	make_system_class("ClassDescription", "Behavior");
	make_system_class("Class", "ClassDescription");
	var _Metaklass = make_system_class("Metaclass", "ClassDescription");

	function makevars(o) {
		var c = o._st_class;
		while (c) {
			o._st_vars[c._st_number] = {};
			c = c._st_super;
		}
	}

	function assign_metaclass(name) {
		var object = system_dictionary["$" + name];
		var superklass = object._st_super;
		if (superklass)
			superklass = superklass._st_class;
		else
			superklass = _Metaklass;

		var metaklass = make_raw_class(name + " class", superklass);
		object._st_class = metaklass;
		makevars(object);

		metaklass._st_class = _Metaklass;
		makevars(metaklass);
	}

	assign_metaclass("Object");
	assign_metaclass("Behavior");
	assign_metaclass("ClassDescription");
	assign_metaclass("Class");
	assign_metaclass("Metaclass");

	LT.makeObject = function(superklass) {
		var o = {
			_st_number: serial_number++,
			_st_vars: {},
			_st_class: superklass
		};
		makevars(o);
		return o;
	};

	LT.methodCall = function(receiver, name, args) {
		var c = receiver._st_class;
		if (!c) {
			throw new Error("Can't call methods on "+c+" yet");
		}

		return c._st_methods[name].apply(receiver, args);
	}

	/* =================================================================== */
	/*                              COMPILER                               */
	/* =================================================================== */

	function compile_expr(node) {
		switch (node.type) {
			case "javascript":
				return node.body;

			case "identifier":
				return "$" + node.name;

			case "call":
			{
				var f = [];
				f.push("LT.methodCall(");
				f.push(compile_expr(node.receiver));
				f.push(",");
				f.push("'" + node.name.name + "'");
				f.push(",[");
				for (var i=0; i<node.args.length; i++) {
					f.push(compile_expr(node.args[i]));
					f.push(",");
				}
				f.push("])");
				return f.join("");
			}

			default:
				throw new Error("Unknown expression node " + node.type);
		}
	}

	function compile_block(node) {
		var f = [];

		for (var i=0; i<node.body.length; i++) {
			var n = node.body[i];
			switch (n.type) {
				case "variables":
					for (var j=0; j<n.identifiers.length; j++) {
						var id = n.identifiers[j];
						f.push("var $" + id.name + " = null;");
					}
					break;

				case "return":
					f.push("retval.value = " + compile_expr(n.expression) + ";");
					f.push("throw retval;");
					break;

				case "assign":
					f.push("$" + n.name.name + " = " + compile_expr(n.expression) + ";");
					break;

				case "expression":
					f.push(compile_expr(n.expression) + ";");
					break;

				default:
					throw new Error("Unknown method body node " + n.type);
			}
		}

		return f;
	}

	function compile_method(klass, node) {
		var vars = node.pattern.vars.map(
			function (v) { return "$" + v.name; }
		);

		var f = [];
		f.push("with (LT.systemDictionary) {");
		f.push("return (function(" + vars.join(",") + ") {");
		f.push("var retval = {value: self};");
		f.push("try {");

		f.push("with (this._st_vars[" + klass._st_number + "]) {");
		f = f.concat(compile_block(node));
		f.push("}");

		f.push("} catch (e) {");
		f.push("if (e !== retval) throw e;");
		f.push("}");
		f.push("return retval.value;");
		f.push("});");
		f.push("}");

		var cf = new Function(f.join("\n"));
		var ccf = cf();

		return {
			name: node.pattern.name,
			callable: ccf
		};
	}

	function compile_toplevel_statements(node) {
		var f = [];
		f.push("with (LT.systemDictionary) {");
		f.push("var retval = null;");
		f = f.concat(compile_block(node));
		f.push("}");

		var cf = new Function(f.join("\n"));
		cf.call(null);
	}

	function compile_class_body(klass, nodes) {
		for (var i=0; i<nodes.length; i++) {
			var node = nodes[i];

			switch (node.type) {
				case "method":
					var m = compile_method(klass, node);
					klass._st_methods[m.name] = m.callable;
					break;

				default:
					throw new Error("Unsupport class body node " + node.type);
			}
		}
	}

	function compile_extend(node) {
		var klass = system_dictionary["$" + node.class.name];
		if (!klass)
			throw new Error("Undefined LT class '" + node.class.name + "'");

		compile_class_body(klass, node.body);
	}

	function compile_toplevel(node) {
		switch (node.type) {
			case "extend":
				compile_extend(node);
				break;

			case "statements":
				compile_toplevel_statements(node);
				break;
				
			default:
				throw new Error("Unsupported toplevel node " + node.type);
		}
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
		console.log(ast);

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
		if (!grammar_element._st_src != "") return;
		if (!grammar) {
			grammar = PEG.buildParser(grammar_element._st_src, {
			});
		}

		/* Consume any scripts that have finished loading (being careful to
		 * maintain the right order!) */

		for (;;) {
			var element = largetalk_elements[0];
			if (!element) break;
			if (!element._st_src) break;

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

