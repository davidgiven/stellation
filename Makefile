# Makefile
# Top-level makefile.
# $Source: /cvsroot/stellation/stellation2/Attic/Makefile,v $
# $State: Exp $

SERVER = ./srv
CLIENT = ./dclnt
OCAMLLIB = /usr/lib/ocaml/3.07
OCAMLFLAGS = -I server

LIBS = \
	$(OCAMLLIB)/unix.cma

SERVERSRCS = \
	server/Engine.ml \
	server/Interface.ml \
	server/Star.ml \
	server/main.ml

CLIENTSRCS = \
	client/debug.ml

ALLSRCS = $(SERVERSRCS) $(CLIENTSRCS)

run: $(CLIENT) $(SERVER)
	$(SERVER)

clean:
	$(RM) $(SERVER) $(CLIENT) \
		$(ALLSRCS:.ml=.cmi) \
		$(ALLSRCS:.ml=.cmx) \
		$(ALLSRCS:.ml=.cmo) \
		$(ALLSRCS:.ml=.o)

$(SERVER): $(SERVERSRCS)
	ocamlmktop -o $(SERVER) -I server $(LIBS) $(SERVERSRCS)

$(CLIENT): $(CLIENTSRCS)
	ocamlmktop -o $(CLIENT) -I client $(LIBS) $(CLIENTSRCS)

%.cmx: %.ml
	ocamlopt $(OCAMLFLAGS) -c $<

# Revision history
# $Log: Makefile,v $
# Revision 1.2  2004/05/28 23:28:07  dtrg
# Updated with new changes. Doesn't attempt to incrementally compile anything any
# more.
#
# Revision 1.1  2004/05/26 00:19:59  dtrg
# First working version.
#

