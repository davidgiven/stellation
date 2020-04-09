OBJ = .obj

COMMON_LIBS = \
	-lib mconsole

JS_LIBS =

CPP_LIBS =

all: $(OBJ)/client/main.js $(OBJ)/server

clean:
	rm -rf $(OBJ)

$(OBJ)/client/main.js: src/client/Main.hx Makefile .haxelib
	haxe -p src -js $@ -debug $(COMMON_LIBS) $(JS_LIBS) -main client.Main

$(OBJ)/server: src/server/Main.hx Makefile .haxelib
	haxe -p src -cpp $(OBJ)/server-temp -debug $(COMMON_LIBS) $(CPP_LIBS) -main server.Main
	cp $(OBJ)/server-temp/Main-debug $@

.haxelib:
	haxelib newrepo
	haxelib install hxcpp
	haxelib install mconsole

