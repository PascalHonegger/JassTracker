/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.data.database;


import dev.honegger.jasstracker.data.database.tables.Contract;
import dev.honegger.jasstracker.data.database.tables.Game;
import dev.honegger.jasstracker.data.database.tables.GameParticipation;
import dev.honegger.jasstracker.data.database.tables.Player;
import dev.honegger.jasstracker.data.database.tables.Round;
import dev.honegger.jasstracker.data.database.tables.Table;
import dev.honegger.jasstracker.data.database.tables.records.ContractRecord;
import dev.honegger.jasstracker.data.database.tables.records.GameParticipationRecord;
import dev.honegger.jasstracker.data.database.tables.records.GameRecord;
import dev.honegger.jasstracker.data.database.tables.records.PlayerRecord;
import dev.honegger.jasstracker.data.database.tables.records.RoundRecord;
import dev.honegger.jasstracker.data.database.tables.records.TableRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ContractRecord> CONTRACT_PKEY = Internal.createUniqueKey(Contract.CONTRACT, DSL.name("contract_pkey"), new TableField[] { Contract.CONTRACT.ID }, true);
    public static final UniqueKey<GameRecord> GAME_PKEY = Internal.createUniqueKey(Game.GAME, DSL.name("game_pkey"), new TableField[] { Game.GAME.ID }, true);
    public static final UniqueKey<GameParticipationRecord> GAME_PARTICIPATION_GAME_ID_TABLE_POSITION_KEY = Internal.createUniqueKey(GameParticipation.GAME_PARTICIPATION, DSL.name("game_participation_game_id_table_position_key"), new TableField[] { GameParticipation.GAME_PARTICIPATION.GAME_ID, GameParticipation.GAME_PARTICIPATION.TABLE_POSITION }, true);
    public static final UniqueKey<GameParticipationRecord> GAME_PARTICIPATION_PKEY = Internal.createUniqueKey(GameParticipation.GAME_PARTICIPATION, DSL.name("game_participation_pkey"), new TableField[] { GameParticipation.GAME_PARTICIPATION.GAME_ID, GameParticipation.GAME_PARTICIPATION.PLAYER_ID }, true);
    public static final UniqueKey<PlayerRecord> PLAYER_PKEY = Internal.createUniqueKey(Player.PLAYER, DSL.name("player_pkey"), new TableField[] { Player.PLAYER.ID }, true);
    public static final UniqueKey<PlayerRecord> PLAYER_USERNAME_KEY = Internal.createUniqueKey(Player.PLAYER, DSL.name("player_username_key"), new TableField[] { Player.PLAYER.USERNAME }, true);
    public static final UniqueKey<RoundRecord> ROUND_PKEY = Internal.createUniqueKey(Round.ROUND, DSL.name("round_pkey"), new TableField[] { Round.ROUND.ID }, true);
    public static final UniqueKey<TableRecord> TABLE_PKEY = Internal.createUniqueKey(Table.TABLE, DSL.name("table_pkey"), new TableField[] { Table.TABLE.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<GameRecord, TableRecord> GAME__GAME_TABLE_ID_FKEY = Internal.createForeignKey(Game.GAME, DSL.name("game_table_id_fkey"), new TableField[] { Game.GAME.TABLE_ID }, Keys.TABLE_PKEY, new TableField[] { Table.TABLE.ID }, true);
    public static final ForeignKey<GameParticipationRecord, GameRecord> GAME_PARTICIPATION__GAME_PARTICIPATION_GAME_ID_FKEY = Internal.createForeignKey(GameParticipation.GAME_PARTICIPATION, DSL.name("game_participation_game_id_fkey"), new TableField[] { GameParticipation.GAME_PARTICIPATION.GAME_ID }, Keys.GAME_PKEY, new TableField[] { Game.GAME.ID }, true);
    public static final ForeignKey<GameParticipationRecord, PlayerRecord> GAME_PARTICIPATION__GAME_PARTICIPATION_PLAYER_ID_FKEY = Internal.createForeignKey(GameParticipation.GAME_PARTICIPATION, DSL.name("game_participation_player_id_fkey"), new TableField[] { GameParticipation.GAME_PARTICIPATION.PLAYER_ID }, Keys.PLAYER_PKEY, new TableField[] { Player.PLAYER.ID }, true);
    public static final ForeignKey<RoundRecord, ContractRecord> ROUND__ROUND_CONTRACT_ID_FKEY = Internal.createForeignKey(Round.ROUND, DSL.name("round_contract_id_fkey"), new TableField[] { Round.ROUND.CONTRACT_ID }, Keys.CONTRACT_PKEY, new TableField[] { Contract.CONTRACT.ID }, true);
    public static final ForeignKey<RoundRecord, GameParticipationRecord> ROUND__ROUND_GAME_ID_PLAYER_ID_FKEY = Internal.createForeignKey(Round.ROUND, DSL.name("round_game_id_player_id_fkey"), new TableField[] { Round.ROUND.GAME_ID, Round.ROUND.PLAYER_ID }, Keys.GAME_PARTICIPATION_PKEY, new TableField[] { GameParticipation.GAME_PARTICIPATION.GAME_ID, GameParticipation.GAME_PARTICIPATION.PLAYER_ID }, true);
    public static final ForeignKey<TableRecord, PlayerRecord> TABLE__TABLE_OWNER_ID_FKEY = Internal.createForeignKey(Table.TABLE, DSL.name("table_owner_id_fkey"), new TableField[] { Table.TABLE.OWNER_ID }, Keys.PLAYER_PKEY, new TableField[] { Player.PLAYER.ID }, true);
}
