# Makefile
# Top-level makefile.
# $Source: /cvsroot/stellation/stellation2/Attic/Makefile,v $
# $State: Exp $

TOPLEVEL = ./toplevel
OCAMLLIB = /usr/lib/ocaml/3.07
OCAMLFLAGS = -I server

LIBS = \
	$(OCAMLLIB)/unix.cma

OBJS = \
	server/Engine.cmo \
	server/Interface.cmo \
	server/Galaxy.cmo \
	server/Star.cmo

run: $(TOPLEVEL)
	$(TOPLEVEL) $(OCAMLFLAGS) server/main.ml

clean:
	$(RM) $(TOPLEVEL) $(OBJS)

$(TOPLEVEL): $(OBJS)
	ocamlmktop -o $(TOPLEVEL) $(LIBS) $(OBJS)

%.cmi %.cmo: %.ml
	ocamlc $(OCAMLFLAGS) -c $<

# Revision history
# $Log: Makefile,v $
# Revision 1.1  2004/05/26 00:19:59  dtrg
# First working version.
#

