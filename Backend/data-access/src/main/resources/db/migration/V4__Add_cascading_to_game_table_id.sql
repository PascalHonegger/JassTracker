alter table game
    drop constraint game_table_id_fkey,
    add constraint game_table_id_fkey foreign key (table_id) references "table" (id) on delete cascade;
