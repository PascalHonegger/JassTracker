/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.data.database.tables;


import dev.honegger.jasstracker.data.database.Keys;
import dev.honegger.jasstracker.data.database.Public;
import dev.honegger.jasstracker.data.database.tables.records.GameParticipationRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.jooq.Check;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GameParticipation extends TableImpl<GameParticipationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.game_participation</code>
     */
    public static final GameParticipation GAME_PARTICIPATION = new GameParticipation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameParticipationRecord> getRecordType() {
        return GameParticipationRecord.class;
    }

    /**
     * The column <code>public.game_participation.game_id</code>.
     */
    public final TableField<GameParticipationRecord, UUID> GAME_ID = createField(DSL.name("game_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.game_participation.player_id</code>.
     */
    public final TableField<GameParticipationRecord, UUID> PLAYER_ID = createField(DSL.name("player_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.game_participation.table_position</code>.
     */
    public final TableField<GameParticipationRecord, Integer> TABLE_POSITION = createField(DSL.name("table_position"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.game_participation.player_name</code>.
     */
    public final TableField<GameParticipationRecord, String> PLAYER_NAME = createField(DSL.name("player_name"), SQLDataType.VARCHAR(30).nullable(false), this, "");

    private GameParticipation(Name alias, Table<GameParticipationRecord> aliased) {
        this(alias, aliased, null);
    }

    private GameParticipation(Name alias, Table<GameParticipationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.game_participation</code> table reference
     */
    public GameParticipation(String alias) {
        this(DSL.name(alias), GAME_PARTICIPATION);
    }

    /**
     * Create an aliased <code>public.game_participation</code> table reference
     */
    public GameParticipation(Name alias) {
        this(alias, GAME_PARTICIPATION);
    }

    /**
     * Create a <code>public.game_participation</code> table reference
     */
    public GameParticipation() {
        this(DSL.name("game_participation"), null);
    }

    public <O extends Record> GameParticipation(Table<O> child, ForeignKey<O, GameParticipationRecord> key) {
        super(child, key, GAME_PARTICIPATION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<GameParticipationRecord> getPrimaryKey() {
        return Keys.GAME_PARTICIPATION_PKEY;
    }

    @Override
    public List<UniqueKey<GameParticipationRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.GAME_PARTICIPATION_GAME_ID_TABLE_POSITION_KEY);
    }

    @Override
    public List<ForeignKey<GameParticipationRecord, ?>> getReferences() {
        return Arrays.asList(Keys.GAME_PARTICIPATION__GAME_PARTICIPATION_GAME_ID_FKEY, Keys.GAME_PARTICIPATION__GAME_PARTICIPATION_PLAYER_ID_FKEY);
    }

    private transient Game _game;
    private transient Player _player;

    /**
     * Get the implicit join path to the <code>public.game</code> table.
     */
    public Game game() {
        if (_game == null)
            _game = new Game(this, Keys.GAME_PARTICIPATION__GAME_PARTICIPATION_GAME_ID_FKEY);

        return _game;
    }

    /**
     * Get the implicit join path to the <code>public.player</code> table.
     */
    public Player player() {
        if (_player == null)
            _player = new Player(this, Keys.GAME_PARTICIPATION__GAME_PARTICIPATION_PLAYER_ID_FKEY);

        return _player;
    }

    @Override
    public List<Check<GameParticipationRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("game_participation_table_position_check"), "(((table_position >= 0) AND (table_position <= 3)))", true)
        );
    }

    @Override
    public GameParticipation as(String alias) {
        return new GameParticipation(DSL.name(alias), this);
    }

    @Override
    public GameParticipation as(Name alias) {
        return new GameParticipation(alias, this);
    }

    @Override
    public GameParticipation as(Table<?> alias) {
        return new GameParticipation(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public GameParticipation rename(String name) {
        return new GameParticipation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public GameParticipation rename(Name name) {
        return new GameParticipation(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public GameParticipation rename(Table<?> name) {
        return new GameParticipation(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, UUID, Integer, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super UUID, ? super UUID, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super UUID, ? super UUID, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
