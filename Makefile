# $Source: /cvsroot/stellation/gdrender/Makefile,v $
# $State: Exp $

CC = gcc
CFLAGS = -O2
RM = rm -f

OBJS = \
	main.o

gdrender: $(OBJS)
	$(CC) -o gdrender $(OBJS) -lgd

clean:
	$(RM) $(OBJS) gdrender

# Revision History
# $Log: Makefile,v $
# Revision 1.1  2000/07/29 17:10:25  dtrg
# Initial revision
#

