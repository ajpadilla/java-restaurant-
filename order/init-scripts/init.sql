-- ./init-scripts/init.sql

-- Enable logical replication and set the number of replication slots
ALTER SYSTEM SET wal_level = 'logical';
ALTER SYSTEM SET max_replication_slots = 4;

-- Enable listening on all addresses
ALTER SYSTEM SET listen_addresses = '*';
