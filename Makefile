# $Source: /cvsroot/stellation/stellation/Makefile,v $
# $State: Exp $

SRCS = \
	_init.moo		\
	admin.moo		\
	stringutils.moo		\
	patch.moo		\
	object.moo		\
	numutils.moo		\
	startup.moo		\
	server_options.moo	\
	player.moo		\
	metaplayer.moo		\
	god.moo			\
	http_server.moo		\
	namegen.moo		\
	star.moo		\
	galaxy.moo		\
	unit.moo		\
	ship.moo		\
	warship.moo		\
	jumpship.moo		\
	fleet.moo		\
	mapper.moo		\
	factory.moo		\
	refinery.moo		\
	metalmine.moo		\
	solarrefinery.moo	\
	antimatterdistillery.moo \
	hydroponicsplant.moo	\
	tug.moo			\
	cargoship.moo		\
	asteroids.moo		\
	messagebuoy.moo		\
	novacannon.moo		\
	basicfactory.moo	\
	transit.moo		\
	deepspace.moo		\
	frm.moo			\
	diplomacy.moo		\
	stats.moo		\
	_shutdown.moo

MOO = /usr/sbin/lambdamoo
MOOS = moo-single
RM = rm -f
PORT = 9876
RUNPORT = 7777
TELNET = nc

all: stellation

run: stellation
	$(MOO) stellation.db stellation.1.db $(RUNPORT)

stellation: stellation.db

stellation.db: $(SRCS) Makefile
	cat $(SRCS) | $(MOOS) minimal.db stellation.db

# Revision History
# $Log: Makefile,v $
# Revision 1.2  2000/07/30 21:20:19  dtrg
# Updated all the .patch lines to contain the correct line numbers.
# Cosmetic makeover; we should now hopefully look marginally better.
# Bit more work on the nova cannon.
# A few minor bug fixes.
#
# Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
# Initial checkin.
#
# Revision 1.1.1.1  2000/07/29 17:10:25  dtrg
# Initial checkin.
	
