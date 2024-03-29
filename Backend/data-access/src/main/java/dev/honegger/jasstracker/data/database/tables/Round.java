/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.data.database.tables;


import dev.honegger.jasstracker.data.database.Keys;
import dev.honegger.jasstracker.data.database.Public;
import dev.honegger.jasstracker.data.database.tables.records.RoundRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
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
public class Round extends TableImpl<RoundRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.round</code>
     */
    public static final Round ROUND = new Round();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundRecord> getRecordType() {
        return RoundRecord.class;
    }

    /**
     * The column <code>public.round.id</code>.
     */
    public final TableField<RoundRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.round.number</code>.
     */
    public final TableField<RoundRecord, Integer> NUMBER = createField(DSL.name("number"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.round.score</code>.
     */
    public final TableField<RoundRecord, Integer> SCORE = createField(DSL.name("score"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.round.game_id</code>.
     */
    public final TableField<RoundRecord, UUID> GAME_ID = createField(DSL.name("game_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.round.player_id</code>.
     */
    public final TableField<RoundRecord, UUID> PLAYER_ID = createField(DSL.name("player_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.round.contract_id</code>.
     */
    public final TableField<RoundRecord, UUID> CONTRACT_ID = createField(DSL.name("contract_id"), SQLDataType.UUID.nullable(false), this, "");

    private Round(Name alias, Table<RoundRecord> aliased) {
        this(alias, aliased, null);
    }

    private Round(Name alias, Table<RoundRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.round</code> table reference
     */
    public Round(String alias) {
        this(DSL.name(alias), ROUND);
    }

    /**
     * Create an aliased <code>public.round</code> table reference
     */
    public Round(Name alias) {
        this(alias, ROUND);
    }

    /**
     * Create a <code>public.round</code> table reference
     */
    public Round() {
        this(DSL.name("round"), null);
    }

    public <O extends Record> Round(Table<O> child, ForeignKey<O, RoundRecord> key) {
        super(child, key, ROUND);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<RoundRecord> getPrimaryKey() {
        return Keys.ROUND_PKEY;
    }

    @Override
    public List<ForeignKey<RoundRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ROUND__ROUND_GAME_ID_PLAYER_ID_FKEY, Keys.ROUND__ROUND_CONTRACT_ID_FKEY);
    }

    private transient GameParticipation _gameParticipation;
    private transient Contract _contract;

    /**
     * Get the implicit join path to the <code>public.game_participation</code>
     * table.
     */
    public GameParticipation gameParticipation() {
        if (_gameParticipation == null)
            _gameParticipation = new GameParticipation(this, Keys.ROUND__ROUND_GAME_ID_PLAYER_ID_FKEY);

        return _gameParticipation;
    }

    /**
     * Get the implicit join path to the <code>public.contract</code> table.
     */
    public Contract contract() {
        if (_contract == null)
            _contract = new Contract(this, Keys.ROUND__ROUND_CONTRACT_ID_FKEY);

        return _contract;
    }

    @Override
    public Round as(String alias) {
        return new Round(DSL.name(alias), this);
    }

    @Override
    public Round as(Name alias) {
        return new Round(alias, this);
    }

    @Override
    public Round as(Table<?> alias) {
        return new Round(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Round rename(String name) {
        return new Round(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Round rename(Name name) {
        return new Round(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Round rename(Table<?> name) {
        return new Round(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, Integer, Integer, UUID, UUID, UUID> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super UUID, ? super Integer, ? super Integer, ? super UUID, ? super UUID, ? super UUID, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super UUID, ? super Integer, ? super Integer, ? super UUID, ? super UUID, ? super UUID, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
