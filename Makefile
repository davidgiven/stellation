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

include src/client/build.mk

bin/cgi-bin/stellation.cgi: $(OBJ)/build
	mkdir -p $(dir $@)
	cp $(OBJ)/server-temp/Main-debug $@

$(OBJ)/build: build.hxml .haxelib $(SRCS)
	haxe build.hxml
	touch $@

haxe: bin/cgi-bin/stellation.cgi $(CLIENT)

serve:: haxe
	mini_httpd -D -c 'cgi-bin/*' -d bin -p 8080 -l /dev/stdout

.haxelib:
	haxelib newrepo
	haxelib install crypto
	haxelib install hamcrest
	haxelib install hx3compat
	haxelib install hxcpp
	haxelib install mconsole

