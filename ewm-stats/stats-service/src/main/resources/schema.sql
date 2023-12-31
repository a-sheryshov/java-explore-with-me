DROP TABLE IF EXISTS hits, apps;

CREATE table IF NOT EXISTS apps(
    app_id bigint generated by default as identity primary key,
    app_name varchar(50) NOT NULL
);

CREATE unique index if not exists app_name_uindex ON apps (app_name);

CREATE table IF NOT EXISTS hits(
    hit_id bigint generated by default as identity primary key,
    app_id bigint REFERENCES apps (app_id) ON DELETE CASCADE,
    uri varchar(100) NOT NULL,
    ip varchar(50) NOT NULL,
    timestamp timestamp NOT NULL
);