/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.database.tables.records;


import dev.honegger.jasstracker.database.tables.Table;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TableRecord extends UpdatableRecordImpl<TableRecord> implements Record3<UUID, String, UUID> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.table.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.table.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.table.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.table.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.table.owner_id</code>.
     */
    public void setOwnerId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.table.owner_id</code>.
     */
    public UUID getOwnerId() {
        return (UUID) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<UUID, String, UUID> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<UUID, String, UUID> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Table.TABLE.ID;
    }

    @Override
    public Field<String> field2() {
        return Table.TABLE.NAME;
    }

    @Override
    public Field<UUID> field3() {
        return Table.TABLE.OWNER_ID;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public UUID component3() {
        return getOwnerId();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public UUID value3() {
        return getOwnerId();
    }

    @Override
    public TableRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public TableRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TableRecord value3(UUID value) {
        setOwnerId(value);
        return this;
    }

    @Override
    public TableRecord values(UUID value1, String value2, UUID value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TableRecord
     */
    public TableRecord() {
        super(Table.TABLE);
    }

    /**
     * Create a detached, initialised TableRecord
     */
    public TableRecord(UUID id, String name, UUID ownerId) {
        super(Table.TABLE);

        setId(id);
        setName(name);
        setOwnerId(ownerId);
    }
}
