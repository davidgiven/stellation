PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = OFF;
PRAGMA foreign_keys = ON;

BEGIN;

CREATE TABLE IF NOT EXISTS tokens
(
	id INTEGER PRIMARY KEY,
	value TEXT UNIQUE
);
CREATE INDEX IF NOT EXISTS tokens_byvalue ON tokens (value);

CREATE TABLE IF NOT EXISTS eav_Class
(
	oid INTEGER PRIMARY KEY,
	value INTEGER
);

CREATE TABLE IF NOT EXISTS eav
(
	oid INTEGER REFERENCES eav_Class(oid),
	kid INTEGER REFERENCES tokens(id),
	PRIMARY KEY (oid, kid)
);

CREATE TABLE IF NOT EXISTS players
(
	email TEXT PRIMARY KEY,
	oid INTEGER REFERENCES eav_Class(oid)
);

CREATE TABLE IF NOT EXISTS timers
(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	time REAL,
	oid INTEGER REFERENCES eav_Class(oid),
	command TEXT
);
CREATE INDEX IF NOT EXISTS timersindex ON timers (time ASC);

CREATE TABLE IF NOT EXISTS logentries
(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
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