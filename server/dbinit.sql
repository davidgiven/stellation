PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = ON;

BEGIN;

CREATE TABLE 'tokens'
(
	'id' INTEGER PRIMARY KEY,
	'value' TEXT
);

CREATE TABLE IF NOT EXISTS eav_Class
(
	oid INTEGER PRIMARY KEY,
	value INTEGER,
	time INTEGER
);

CREATE TABLE 'players'
(
	'email' TEXT PRIMARY KEY,
	'oid' INTEGER
);

CREATE TABLE 'timers'
(
	'id' INTEGER PRIMARY KEY,
	'time' REAL,
	'oid' INTEGER,
	'command' INTEGER
);
CREATE INDEX 'timersindex' ON 'timers'
(
	'time' ASC
);

CREATE TABLE 'logentries'
(
	'id' INTEGER PRIMARY KEY,
	'time' REAL,
	'location' INTEGER,
	'entry' TEXT
);
CREATE INDEX 'logentriesindex' ON 'logentries'
(
	'time' ASC
);

CREATE TABLE 'visiblelogs'
(
	'player' INTEGER PRIMARY KEY,
	'log' INTEGER
);

COMMIT;