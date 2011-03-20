PRAGMA auto_vacuum = FULL;
PRAGMA encoding = "UTF-8";
PRAGMA synchronous = OFF;

BEGIN;

CREATE TABLE 'tokens'
(
	'id' INTEGER PRIMARY KEY,
	'value' TEXT
);

CREATE TABLE 'eav'
(
	'oid' INTEGER,
	'kid' INTEGER,
	'value' TEXT,
	'time' INTEGER,
	PRIMARY KEY('oid', 'kid')
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