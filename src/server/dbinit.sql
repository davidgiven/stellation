PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = OFF;
PRAGMA foreign_keys = ON;
PRAGMA temp_store = MEMORY;

BEGIN;

CREATE TABLE IF NOT EXISTS variables
(
	key TEXT NOT NULL PRIMARY KEY,
	value TEXT
);
INSERT OR IGNORE INTO variables (key, value) VALUES ('timestamp', 0);

CREATE TABLE IF NOT EXISTS tokens
(
	id INTEGER PRIMARY KEY,
	value TEXT UNIQUE
);
CREATE INDEX IF NOT EXISTS tokens_byvalue ON tokens (value);

CREATE TABLE IF NOT EXISTS eav_Class
(
	oid INTEGER PRIMARY KEY AUTOINCREMENT,
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
CREATE INDEX IF NOT EXISTS logentriesindex ON logentries (time ASC);

CREATE TABLE IF NOT EXISTS visiblelogs
(
	player INTEGER REFERENCES eav_Class(oid),
	log INTEGER REFERENCES logentries(id)
);
CREATE INDEX IF NOT EXISTS visiblelogs_byplayer ON visiblelogs (player);

CREATE TEMPORARY TABLE pscopes
(
	kid INTEGER PRIMARY KEY,
	scope INTEGER
);

CREATE TEMPORARY TABLE vscopes
(
	oid INTEGER PRIMARY KEY,
	scope INTEGER
);

CREATE TEMPORARY TABLE seenby
(
	oid INTEGER,
	kid INTEGER,
	cookie INTEGER,
	UNIQUE (oid, kid, cookie) 
);
CREATE INDEX seenbyindex ON seenby (oid, kid);

COMMIT;