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

	function make_raw_class(name, superklass) {
		return {
			_st_number: serial_number++,
			_st_vars: {},
			_st_super: superklass,
			_st_methods: superklass ? Object.create(superklass._st_methods) : {}
		};
	}

	function make_system_class(name, superklassname) {
		var superklass = superklassname ? system_dictionary["_" + superklassname] : null;
		var o = make_raw_class(name, superklass);
		system_dictionary["_" + name] = o;
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
		var object = system_dictionary["_" + name];
		var superklass = object._st_super;

		var metaklass = make_raw_class(name, superklass);
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

	/* =================================================================== */
	/*                              COMPILER                               */
	/* =================================================================== */

	function compile_expr(klass, node) {
		switch (node.type) {
			case "javascript":
				return node.body;

			case "identifier":
				return "_" + node.name;

			default:
				throw new Error("Unknown expression node " + node.type);
		}
	}

	function compile_method(klass, node) {
		var vars = node.pattern.vars.map(
			function (v) { return "_" + v.name; }
		);
		vars.unshift("_self");

		var f = [];
		f.push("return (function(" + vars.join(",") + ") {");
		f.push("with (_self._st_vars[" + klass._st_number + "]) {");

		for (var i=0; i<node.body.length; i++) {
			var n = node.body[i];
			switch (n.type) {
				case "variables":
					for (var j=0; j<n.identifiers.length; j++) {
						var id = n.identifiers[j];
						f.push("var _" + id.name + " = null;");
					}
					break;

				case "return":
					f.push("return " + compile_expr(klass, n.expression) + ";");
					break;

				case "assign":
					f.push("_" + n.name.name + " = (" + compile_expr(klass, n.expression) + ");");
					break;

				default:
					throw new Error("Unknown method body node " + n.type);
			}
		}


		f.push("return _self;");
		f.push("}");
		f.push("});");

		var cf = new Function(f.join("\n"));
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

				default:
					throw new Error("Unsupport class body node " + node.type);
			}
		}
	}

	function compile_extend(node) {
		var klass = system_dictionary["_" + node.class.name];
		if (!klass)
			throw new Error("Undefined LT class '" + node.class.name + "'");

		compile_class_body(klass, node.body);
	}

	function compile_toplevel(node) {
		switch (node.type) {
			case "extend":
				compile_extend(node);
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

