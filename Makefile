OBJ = .obj

all: $(OBJ)/client/main.js $(OBJ)/server

clean:
	rm -rf $(OBJ)

$(OBJ)/client/main.js: JSMain.hx Makefile
	haxe -js $@ -debug -main JSMain

$(OBJ)/server: ServerMain.hx Makefile
	haxe -cpp $(OBJ)/server-temp -debug -main ServerMain
	cp $(OBJ)/server-temp/ServerMain-debug $@

