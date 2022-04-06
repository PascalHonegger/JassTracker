/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.database;


import dev.honegger.jasstracker.database.tables.Contract;
import dev.honegger.jasstracker.database.tables.Game;
import dev.honegger.jasstracker.database.tables.GameParticipation;
import dev.honegger.jasstracker.database.tables.Player;
import dev.honegger.jasstracker.database.tables.Round;
import dev.honegger.jasstracker.database.tables.Table;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.contract</code>.
     */
    public final Contract CONTRACT = Contract.CONTRACT;

    /**
     * The table <code>public.game</code>.
     */
    public final Game GAME = Game.GAME;

    /**
     * The table <code>public.game_participation</code>.
     */
    public final GameParticipation GAME_PARTICIPATION = GameParticipation.GAME_PARTICIPATION;

    /**
     * The table <code>public.player</code>.
     */
    public final Player PLAYER = Player.PLAYER;

    /**
     * The table <code>public.round</code>.
     */
    public final Round ROUND = Round.ROUND;

    /**
     * The table <code>public.table</code>.
     */
    public final Table TABLE = Table.TABLE;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<org.jooq.Table<?>> getTables() {
        return Arrays.asList(
            Contract.CONTRACT,
            Game.GAME,
            GameParticipation.GAME_PARTICIPATION,
            Player.PLAYER,
            Round.ROUND,
            Table.TABLE
        );
    }
}