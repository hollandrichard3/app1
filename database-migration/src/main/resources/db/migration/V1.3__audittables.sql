        --------------------------------------------------------------
        -- Filename:  V1.3__audittables.sql
        --------------------------------------------------------------

        -- Create the reports_aud table
        CREATE TABLE reports_aud
        (
          id                  INTEGER NOT NULL,
          version             INTEGER,
          description         TEXT,
          display_name        VARCHAR(255),
          reviewed            BOOLEAN,
          reference_source    INTEGER,
          priority            INTEGER,
          created_date        TIMESTAMP,
          last_modified_date  TIMESTAMP,
          is_custom_report    BOOLEAN DEFAULT FALSE,
          reserved            BOOLEAN,
          reserved_by         VARCHAR(255),
          username            VARCHAR(100) NOT NULL,
          timestamp           TIMESTAMP NOT NULL,
          rev                 INTEGER,
          rev_type            INTEGER
        );
