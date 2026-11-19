-- Drop UUID auto-generation defaults from all tables.
-- The application (services) will generate UUIDs instead.

ALTER TABLE users ALTER COLUMN id DROP DEFAULT;
ALTER TABLE projects ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tasks ALTER COLUMN id DROP DEFAULT;
