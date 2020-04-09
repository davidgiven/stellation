OBJ = .obj

COMMON_LIBS = \
	-lib mconsole

JS_LIBS =

CPP_LIBS =

all: $(OBJ)/client/main.js $(OBJ)/server

clean:
	rm -rf $(OBJ)

$(OBJ)/client/main.js: JSMain.hx Makefile .haxelib
	haxe -js $@ -debug $(COMMON_LIBS) $(JS_LIBS) -main JSMain

$(OBJ)/server: ServerMain.hx Makefile .haxelib
	haxe -cpp $(OBJ)/server-temp -debug $(COMMON_LIBS) $(CPP_LIBS) -main ServerMain
	cp $(OBJ)/server-temp/ServerMain-debug $@

.haxelib:
	haxelib newrepo
	haxelib install hxcpp
	haxelib install mconsole

