PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Book(
book_id TEXT NOT NULL,
book_name TEXT,
author_id TEXT,
PRIMARY KEY (book_id)
);

CREATE TABLE IF NOT EXISTS BookStats(
book_id TEXT NOT NULL,
page_count INT NOT NULL,
PRIMARY KEY (book_id)
);


CREATE TABLE IF NOT EXISTS Author(
author_id TEXT NOT NULL,
author_name TEXT,
PRIMARY KEY (author_id)
);


PRAGMA foreign_keys = OFF;
