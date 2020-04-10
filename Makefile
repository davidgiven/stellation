OBJ = .obj

COMMON_LIBS = \
	-lib mconsole

JS_LIBS =

CPP_LIBS =

SRCS = \
	$(shell find src tests -name "*.hx")

all: haxe

clean:
	rm -rf $(OBJ) .haxelib

haxe:: build.hxml .haxelib $(SRCS)
	haxe build.hxml
	cp $(OBJ)/server-temp/Main-debug $@

.haxelib:
	haxelib newrepo
	haxelib install hxcpp
	haxelib install mconsole
	haxelib install hx3compat
	haxelib install hamcrest

