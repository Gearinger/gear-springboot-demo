```sql
CREATE TABLE "public"."poi" (
  "id" int4 NOT NULL DEFAULT nextval('poi_id_seq'::regclass),
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "info" varchar(255) COLLATE "pg_catalog"."default",
  "location" geometry(GEOMETRY),
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "key_words" tsvector,
  "location_geography" geography(GEOMETRY),
  CONSTRAINT "poi_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."poi" 
  OWNER TO "postgres";

CREATE INDEX "poi_key_words_idx" ON "public"."poi" USING gin (
  "key_words" "pg_catalog"."tsvector_ops"
) WITH (GIN_PENDING_LIST_LIMIT = 1024);

CREATE INDEX "poi_location_geography_idx" ON "public"."poi" USING gist (
  "location_geography" "public"."gist_geography_ops"
);

CREATE INDEX "poi_location_idx" ON "public"."poi" USING gist (
  "location" "public"."gist_geometry_ops_2d"
);

CREATE INDEX "poi_name_idx" ON "public"."poi" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
```