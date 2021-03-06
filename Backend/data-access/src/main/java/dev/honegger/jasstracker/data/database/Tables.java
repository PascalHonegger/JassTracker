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


/**
 * Convenience access to all tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.contract</code>.
     */
    public static final Contract CONTRACT = Contract.CONTRACT;

    /**
     * The table <code>public.game</code>.
     */
    public static final Game GAME = Game.GAME;

    /**
     * The table <code>public.game_participation</code>.
     */
    public static final GameParticipation GAME_PARTICIPATION = GameParticipation.GAME_PARTICIPATION;

    /**
     * The table <code>public.player</code>.
     */
    public static final Player PLAYER = Player.PLAYER;

    /**
     * The table <code>public.round</code>.
     */
    public static final Round ROUND = Round.ROUND;

    /**
     * The table <code>public.table</code>.
     */
    public static final Table TABLE = Table.TABLE;
}
