/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.data.database.tables;


import dev.honegger.jasstracker.data.converters.LocalDateTimeConverter;
import dev.honegger.jasstracker.data.database.Keys;
import dev.honegger.jasstracker.data.database.Public;
import dev.honegger.jasstracker.data.database.tables.records.GameRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import kotlinx.datetime.LocalDateTime;

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
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Game extends TableImpl<GameRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.game</code>
     */
    public static final Game GAME = new Game();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameRecord> getRecordType() {
        return GameRecord.class;
    }

    /**
     * The column <code>public.game.id</code>.
     */
    public final TableField<GameRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.game.start_time</code>.
     */
    public final TableField<GameRecord, LocalDateTime> START_TIME = createField(DSL.name("start_time"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "", new LocalDateTimeConverter());

    /**
     * The column <code>public.game.end_time</code>.
     */
    public final TableField<GameRecord, LocalDateTime> END_TIME = createField(DSL.name("end_time"), SQLDataType.LOCALDATETIME(6), this, "", new LocalDateTimeConverter());

    /**
     * The column <code>public.game.table_id</code>.
     */
    public final TableField<GameRecord, UUID> TABLE_ID = createField(DSL.name("table_id"), SQLDataType.UUID.nullable(false), this, "");

    private Game(Name alias, Table<GameRecord> aliased) {
        this(alias, aliased, null);
    }

    private Game(Name alias, Table<GameRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.game</code> table reference
     */
    public Game(String alias) {
        this(DSL.name(alias), GAME);
    }

    /**
     * Create an aliased <code>public.game</code> table reference
     */
    public Game(Name alias) {
        this(alias, GAME);
    }

    /**
     * Create a <code>public.game</code> table reference
     */
    public Game() {
        this(DSL.name("game"), null);
    }

    public <O extends Record> Game(Table<O> child, ForeignKey<O, GameRecord> key) {
        super(child, key, GAME);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<GameRecord> getPrimaryKey() {
        return Keys.GAME_PKEY;
    }

    @Override
    public List<ForeignKey<GameRecord, ?>> getReferences() {
        return Arrays.asList(Keys.GAME__GAME_TABLE_ID_FKEY);
    }

    private transient dev.honegger.jasstracker.data.database.tables.Table _table;

    /**
     * Get the implicit join path to the <code>public.table</code> table.
     */
    public dev.honegger.jasstracker.data.database.tables.Table table() {
        if (_table == null)
            _table = new dev.honegger.jasstracker.data.database.tables.Table(this, Keys.GAME__GAME_TABLE_ID_FKEY);

        return _table;
    }

    @Override
    public Game as(String alias) {
        return new Game(DSL.name(alias), this);
    }

    @Override
    public Game as(Name alias) {
        return new Game(alias, this);
    }

    @Override
    public Game as(Table<?> alias) {
        return new Game(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(String name) {
        return new Game(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(Name name) {
        return new Game(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(Table<?> name) {
        return new Game(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, LocalDateTime, LocalDateTime, UUID> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super UUID, ? super LocalDateTime, ? super LocalDateTime, ? super UUID, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super UUID, ? super LocalDateTime, ? super LocalDateTime, ? super UUID, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
