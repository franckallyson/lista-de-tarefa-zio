CREATE TABLE IF NOT EXISTS "TaskTable" (
    "uuid" uuid NOT NULL PRIMARY KEY,
    "title" VARCHAR(255),
    "description" VARCHAR(255),
    "isCompleted" BOOLEAN
);