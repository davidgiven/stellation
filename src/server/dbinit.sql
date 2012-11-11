PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = ON;
PRAGMA foreign_keys = ON;

BEGIN;

CREATE TABLE IF NOT EXISTS tokens
(
	id INTEGER PRIMARY KEY,
	value TEXT UNIQUE
);
CREATE INDEX IF NOT EXISTS tokens_byvalue ON tokens (value);

CREATE TABLE IF NOT EXISTS eav
(
	oid INTEGER REFERENCES eav_Class(oid),
	kid INTEGER REFERENCES tokens(id),
	time INTEGER,
	PRIMARY KEY (oid, kid)
);
CREATE INDEX IF NOT EXISTS eav_byoid ON eav (oid);

CREATE TABLE IF NOT EXISTS eav_Class
(
	oid INTEGER PRIMARY KEY,
	value INTEGER
);

CREATE TABLE IF NOT EXISTS players
(
	email TEXT PRIMARY KEY,
	oid INTEGER REFERENCES eav_Class(oid)
);

CREATE TABLE IF NOT EXISTS timers
(
	id INTEGER PRIMARY KEY,
	time REAL,
	oid INTEGER REFERENCES eav_Class(oid),
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
	player INTEGER PRIMARY KEY REFERENCES eav_Class(oid),
	log INTEGER
);

COMMIT;