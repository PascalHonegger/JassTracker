create table "table" (
    id uuid not null primary key,
    name varchar(30) not null,
    owner_id uuid not null references player(id),
    game_id_temp uuid not null references game(id)
);

insert into "table" (id, name, owner_id, game_id_temp)
    select gen_random_uuid(), name, owner_id, id
    from game;

alter table game add column table_id uuid null references "table"(id);

update game set table_id = (select id from "table" where game_id_temp = game.id);

alter table "table" drop column game_id_temp;
alter table game
    drop column name,
    drop column owner_id,
    alter column table_id set not null;
