alter table game_participation add column player_name varchar(30) null;

update game_participation set player_name = (select coalesce(display_name, username, 'Guest') from player where id = player_id);

alter table game_participation alter column player_name set not null;
