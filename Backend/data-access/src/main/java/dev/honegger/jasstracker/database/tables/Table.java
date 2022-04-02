/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.database.tables;


import dev.honegger.jasstracker.database.Keys;
import dev.honegger.jasstracker.database.Public;
import dev.honegger.jasstracker.database.tables.records.TableRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
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
public class Table extends TableImpl<TableRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.table</code>
     */
    public static final Table TABLE = new Table();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TableRecord> getRecordType() {
        return TableRecord.class;
    }

    /**
     * The column <code>public.table.id</code>.
     */
    public final TableField<TableRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.table.name</code>.
     */
    public final TableField<TableRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>public.table.owner_id</code>.
     */
    public final TableField<TableRecord, UUID> OWNER_ID = createField(DSL.name("owner_id"), SQLDataType.UUID.nullable(false), this, "");

    private Table(Name alias, org.jooq.Table<TableRecord> aliased) {
        this(alias, aliased, null);
    }

    private Table(Name alias, org.jooq.Table<TableRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.table</code> table reference
     */
    public Table(String alias) {
        this(DSL.name(alias), TABLE);
    }

    /**
     * Create an aliased <code>public.table</code> table reference
     */
    public Table(Name alias) {
        this(alias, TABLE);
    }

    /**
     * Create a <code>public.table</code> table reference
     */
    public Table() {
        this(DSL.name("table"), null);
    }

    public <O extends Record> Table(org.jooq.Table<O> child, ForeignKey<O, TableRecord> key) {
        super(child, key, TABLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<TableRecord> getPrimaryKey() {
        return Keys.TABLE_PKEY;
    }

    @Override
    public List<ForeignKey<TableRecord, ?>> getReferences() {
        return Arrays.asList(Keys.TABLE__TABLE_OWNER_ID_FKEY);
    }

    private transient Player _player;

    /**
     * Get the implicit join path to the <code>public.player</code> table.
     */
    public Player player() {
        if (_player == null)
            _player = new Player(this, Keys.TABLE__TABLE_OWNER_ID_FKEY);

        return _player;
    }

    @Override
    public Table as(String alias) {
        return new Table(DSL.name(alias), this);
    }

    @Override
    public Table as(Name alias) {
        return new Table(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Table rename(String name) {
        return new Table(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Table rename(Name name) {
        return new Table(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<UUID, String, UUID> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
