PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = ON;

BEGIN;

CREATE TABLE IF NOT EXISTS tokens
(
	id INTEGER PRIMARY KEY,
	value TEXT
);

CREATE TABLE IF NOT EXISTS eav_Class
(
	oid INTEGER PRIMARY KEY,
	value INTEGER,
	time INTEGER
);

CREATE TABLE IF NOT EXISTS players
(
	email TEXT PRIMARY KEY,
	oid INTEGER
);

CREATE TABLE IF NOT EXISTS timers
(
	id INTEGER PRIMARY KEY,
	time REAL,
	oid INTEGER,
	command INTEGER
);
CREATE INDEX IF NOT EXISTS timersindex ON timers
(
	time ASC
);

CREATE TABLE IF NOT EXISTS logentries
(
	id INTEGER PRIMARY KEY,
	time REAL,
	location INTEGER,
	entry TEXT
);
CREATE INDEX IF NOT EXISTS logentriesindex ON logentries
(
	time ASC
);

CREATE TABLE IF NOT EXISTS visiblelogs
(
	player INTEGER PRIMARY KEY,
	log INTEGER
);

COMMIT;