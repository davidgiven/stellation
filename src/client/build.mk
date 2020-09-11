bin/stellation.js: $(OBJ)/build
	mkdir -p $(dir $@)
	cp $(OBJ)/client/main.js $@

$(OBJ)/client/style.css: src/client/style.scss
	sassc --style=nested $< $@

bin/client.html: \
		src/client/client.html \
		src/client/library.svg \
		$(OBJ)/client/style.css
	gpp -H -I src/client -I $(OBJ)/client $< -o $@

bin/%: src/client/resources/%
	cp $< $@

CLIENT = \
	bin/stellation.js \
	bin/client.html \
	bin/COMMODORE_PET.woff2 \
	bin/COMMODORE_PET_2y.woff \
	bin/galaxy.png \

