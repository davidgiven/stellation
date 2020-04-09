OBJ = .obj

COMMON_LIBS = \
	-lib mconsole

JS_LIBS =

CPP_LIBS =

SRCS = \
	$(wildcard src/utils/*.hx)

all: haxe

clean:
	rm -rf $(OBJ)

haxe:: build.hxml .haxelib $(SRCS)
	haxe build.hxml
	cp $(OBJ)/server-temp/Main-debug $@

.haxelib:
	haxelib newrepo
	haxelib install hxcpp
	haxelib install mconsole

