create type contract_type as enum ('acorns', 'roses', 'shields', 'bells', 'tops_down', 'bottoms_up', 'joker', 'slalom', 'guschti');

create table contract (
    id uuid not null primary key,
    name text not null,
    multiplier int not null,
    type contract_type not null
);

create table player (
    id uuid not null primary key,
    username varchar(30) null unique,
    display_name varchar(30) null,
    password text null,
    is_guest bool generated always as (username is null) stored
);

create table game (
    id uuid not null primary key,
    start_time timestamp not null,
    end_time timestamp null,
    name varchar(30) not null,
    owner_id uuid not null references player(id)
);

create table game_participation (
    game_id uuid not null references game(id) on delete cascade,
    player_id uuid not null references player(id),
    table_position int not null check (table_position between 0 and 3),
    primary key (game_id, player_id),
    unique (game_id, table_position)
);

create table round (
    id uuid not null primary key,
    number int not null,
    score int not null,
    game_id uuid not null,
    player_id uuid not null,
    contract_id uuid not null references contract(id),
    foreign key (game_id, player_id) references game_participation (game_id, player_id) on delete cascade
);

insert into contract (id, name, multiplier, type) values
    ('58bae0f8-8c59-4a40-aa2d-9c6a489366b3', 'Eicheln', 1, 'acorns'),
    ('d895b400-3d89-48db-a7ed-5e593f54b7f6', 'Rösli', 2, 'roses'),
    ('41c7bd00-3da4-4926-bcb6-08e12aafbe6d', 'Schilten', 3, 'shields'),
    ('38fb8cbb-b22d-40f7-b9a1-b4adc1740075', 'Schellen', 4, 'bells'),
    ('62aeb3b0-7b2d-4670-9789-6acd23fb8609', 'Obenabe', 5, 'tops_down'),
    ('5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2', 'Undenufe', 6, 'bottoms_up'),
    ('168b6602-07c3-4600-b39a-d08aca3323b0', 'Joker', 7, 'joker'),
    ('02c40574-edd7-4a5b-baeb-e15cd50b1387', 'Joker', 8, 'joker'),
    ('28d6e9ac-fc8e-4dad-8af8-2ae126b8d691', 'Slalom', 9, 'slalom'),
    ('345bde8f-a316-4952-b021-7cbe7ad62306', 'Guschti', 10, 'guschti');
