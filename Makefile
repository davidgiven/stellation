# $Source: /cvsroot/stellation/gdrender/Makefile,v $
# $State: Exp $

CC = gcc
CFLAGS = -O2 -g
RM = rm -f

OBJS = \
	main.o		\
	webloader.o

gdrender: $(OBJS)
	$(CC) -o gdrender $(OBJS) /usr/lib/libgd.a

clean:
	$(RM) $(OBJS) gdrender

# Revision History
# $Log: Makefile,v $
# Revision 1.2  2000/07/31 23:37:10  dtrg
# Added the `url.' redirection function.
#
# Revision 1.1.1.1  2000/07/29 17:10:25  dtrg
# Initial checkin.
#

