delete from "round" where game_id in ('3de81ae0-792e-43b0-838b-acad78f29ba6', '85df0ff4-6c8b-4846-b8e6-400940660f0b');

delete from "game_participation" where game_id in ('3de81ae0-792e-43b0-838b-acad78f29ba6', '85df0ff4-6c8b-4846-b8e6-400940660f0b');

delete from "game" where id in ('3de81ae0-792e-43b0-838b-acad78f29ba6', '85df0ff4-6c8b-4846-b8e6-400940660f0b');

delete from "table" where id in ('92968e55-6df0-4f21-a7cc-a243025e5f87', 'de940c47-9881-4e95-bc3d-6014ad1902e1');

delete from "player" where id in (
    '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0',
    '3095c042-d0a9-4219-9f65-53d4565fd1e6',
    '283c0a20-b293-40e7-8858-da098a53b756',
    'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797',
    '665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3',
    '7dad81d3-62db-4553-9d48-f38f404f1596'
);

insert into "player" (id, username, display_name, password) values
	('27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', 'pascal', 'Pascal', 'honegger'),
	('3095c042-d0a9-4219-9f65-53d4565fd1e6', 'marcel', 'Marcel', 'joss'),
	('283c0a20-b293-40e7-8858-da098a53b756', 'david', 'David', 'kalchofner'),
	('cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', 'jamie', 'Jamie', 'maier'),
	('665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3', null, null, null),
	('7dad81d3-62db-4553-9d48-f38f404f1596', null, null, null);

insert into "table" (id, name, owner_id) values
  ('92968e55-6df0-4f21-a7cc-a243025e5f87', 'table1', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0'),
  ('de940c47-9881-4e95-bc3d-6014ad1902e1', 'table2', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0' );

insert into "game" (id, start_time, end_time, table_id) values
  ('3de81ae0-792e-43b0-838b-acad78f29ba6', '2022-03-31 14:08:59.654385+02', '2022-04-01 14:08:59.654385+02', '92968e55-6df0-4f21-a7cc-a243025e5f87'),
  ('85df0ff4-6c8b-4846-b8e6-400940660f0b', '2022-03-31 14:08:59.654385+02', NULL, 'de940c47-9881-4e95-bc3d-6014ad1902e1');

insert into "game_participation" (game_id, player_id, table_position, player_name) values
	('3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', 0, 'Pascal'),
    ('3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', 1, 'David'),
	('3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', 2, 'Marcel'),
	('3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', 3, 'Jamie'),
	('85df0ff4-6c8b-4846-b8e6-400940660f0b', '665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3', 0, 'Guest 1'),
    ('85df0ff4-6c8b-4846-b8e6-400940660f0b', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', 1, 'Pascal'),
	('85df0ff4-6c8b-4846-b8e6-400940660f0b', '7dad81d3-62db-4553-9d48-f38f404f1596', 2, 'Guest 2'),
	('85df0ff4-6c8b-4846-b8e6-400940660f0b', '3095c042-d0a9-4219-9f65-53d4565fd1e6', 3, 'Marcel');

insert into "round" (id, number, score, game_id, player_id, contract_id) values
	('c1dbf7ae-719f-4acc-a6e6-03c37534e8a4',1, 120,'3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', '58bae0f8-8c59-4a40-aa2d-9c6a489366b3'),
	('22e854d0-b00b-4dc5-bf72-1ec97ebd0fdf',2, 73,'3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', 'd895b400-3d89-48db-a7ed-5e593f54b7f6'),
	('dc4a1ebc-68d1-4130-86f7-142d293af28a',3, 89,'3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', '41c7bd00-3da4-4926-bcb6-08e12aafbe6d'),
	('623a1692-fe3a-42da-977e-dea4bb872112',4, 117,'3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', '38fb8cbb-b22d-40f7-b9a1-b4adc1740075'),
	('94615cd2-1cd7-4298-b7e1-7db6d5e5fb8e',5, 157,'3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', '62aeb3b0-7b2d-4670-9789-6acd23fb8609'),
	('fddcb3aa-6f8f-4656-be2e-ab9d82b5b675',6, 146,'3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', '5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2'),
	('69884b8a-df0c-48fa-9ad8-065cc16740f5',7, 104,'3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', '168b6602-07c3-4600-b39a-d08aca3323b0'),
	('e5847e97-61bd-4b41-813b-80995e4ea2b9',8, 73,'3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', '02c40574-edd7-4a5b-baeb-e15cd50b1387'),
	('0efda358-f340-4c43-85b1-f69def6b34d1',9, 157,'3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', '28d6e9ac-fc8e-4dad-8af8-2ae126b8d691'),
	('6af5adf2-aaa5-4583-8101-c16122bb347f',10, 146,'3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', '345bde8f-a316-4952-b021-7cbe7ad62306'),
	('1705f2cb-0ff8-4361-9bcb-4ff1a2e3f7dd',11, 89,'3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', '345bde8f-a316-4952-b021-7cbe7ad62306'),
	('d695e65a-a546-4ef8-ac11-8806f3e35a0c',12, 73,'3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', '28d6e9ac-fc8e-4dad-8af8-2ae126b8d691'),
	('fdd91c1f-af05-40a1-8b39-ecf5d3291556',13, 157,'3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', '5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2'),
	('4c364d4a-7b86-4f72-9336-f86754b0a928',14, 146,'3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', '62aeb3b0-7b2d-4670-9789-6acd23fb8609'),
	('ce2c29ce-cd3f-4140-b439-0cde812f2598',15, 120,'3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', '02c40574-edd7-4a5b-baeb-e15cd50b1387'),
	('8cff9836-17f1-4b30-9457-f7ae59088f70',16, 157,'3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', '168b6602-07c3-4600-b39a-d08aca3323b0'),
	('96ad63e5-c942-4b7c-bcc3-0f7b89b47468',17, 157,'3de81ae0-792e-43b0-838b-acad78f29ba6', '27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0', 'd895b400-3d89-48db-a7ed-5e593f54b7f6'),
	('8d708031-46af-4475-8697-be8692043357',18, 104,'3de81ae0-792e-43b0-838b-acad78f29ba6', '3095c042-d0a9-4219-9f65-53d4565fd1e6', '58bae0f8-8c59-4a40-aa2d-9c6a489366b3'),
	('98de7a69-f7ff-45fa-9aa5-10e02728ee2c',19, 73,'3de81ae0-792e-43b0-838b-acad78f29ba6', '283c0a20-b293-40e7-8858-da098a53b756', '38fb8cbb-b22d-40f7-b9a1-b4adc1740075'),
	('2e4887c9-3fbe-441a-9992-0da5f5a2fc8e',20, 89,'3de81ae0-792e-43b0-838b-acad78f29ba6', 'cdfa5ae5-d182-4e11-b7f1-a173b2b4b797', '41c7bd00-3da4-4926-bcb6-08e12aafbe6d'),
	('e1eb6701-f163-4e54-aadd-8239476972f6',1, 157,'85df0ff4-6c8b-4846-b8e6-400940660f0b', '3095c042-d0a9-4219-9f65-53d4565fd1e6', '58bae0f8-8c59-4a40-aa2d-9c6a489366b3'),
	('e515e48e-ffe9-4ac4-b5bc-c9a74568ffa5',2, 89,'85df0ff4-6c8b-4846-b8e6-400940660f0b', '665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3', '38fb8cbb-b22d-40f7-b9a1-b4adc1740075'),
	('e74dfd78-d14f-468f-b9a0-423264725d3b',3, 73,'85df0ff4-6c8b-4846-b8e6-400940660f0b', '7dad81d3-62db-4553-9d48-f38f404f1596', 'd895b400-3d89-48db-a7ed-5e593f54b7f6');
