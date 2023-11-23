CREATE KEYSPACE IF NOT EXISTS links
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE links;
