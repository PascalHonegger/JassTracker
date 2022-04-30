/*
 * This file is generated by jOOQ.
 */
package dev.honegger.jasstracker.data.database.tables.records;


import dev.honegger.jasstracker.data.database.tables.Round;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoundRecord extends UpdatableRecordImpl<RoundRecord> implements Record6<UUID, Integer, Integer, UUID, UUID, UUID> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.round.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.round.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.round.number</code>.
     */
    public void setNumber(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.round.number</code>.
     */
    public Integer getNumber() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.round.score</code>.
     */
    public void setScore(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.round.score</code>.
     */
    public Integer getScore() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.round.game_id</code>.
     */
    public void setGameId(UUID value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.round.game_id</code>.
     */
    public UUID getGameId() {
        return (UUID) get(3);
    }

    /**
     * Setter for <code>public.round.player_id</code>.
     */
    public void setPlayerId(UUID value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.round.player_id</code>.
     */
    public UUID getPlayerId() {
        return (UUID) get(4);
    }

    /**
     * Setter for <code>public.round.contract_id</code>.
     */
    public void setContractId(UUID value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.round.contract_id</code>.
     */
    public UUID getContractId() {
        return (UUID) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, Integer, Integer, UUID, UUID, UUID> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UUID, Integer, Integer, UUID, UUID, UUID> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Round.ROUND.ID;
    }

    @Override
    public Field<Integer> field2() {
        return Round.ROUND.NUMBER;
    }

    @Override
    public Field<Integer> field3() {
        return Round.ROUND.SCORE;
    }

    @Override
    public Field<UUID> field4() {
        return Round.ROUND.GAME_ID;
    }

    @Override
    public Field<UUID> field5() {
        return Round.ROUND.PLAYER_ID;
    }

    @Override
    public Field<UUID> field6() {
        return Round.ROUND.CONTRACT_ID;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getNumber();
    }

    @Override
    public Integer component3() {
        return getScore();
    }

    @Override
    public UUID component4() {
        return getGameId();
    }

    @Override
    public UUID component5() {
        return getPlayerId();
    }

    @Override
    public UUID component6() {
        return getContractId();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getNumber();
    }

    @Override
    public Integer value3() {
        return getScore();
    }

    @Override
    public UUID value4() {
        return getGameId();
    }

    @Override
    public UUID value5() {
        return getPlayerId();
    }

    @Override
    public UUID value6() {
        return getContractId();
    }

    @Override
    public RoundRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public RoundRecord value2(Integer value) {
        setNumber(value);
        return this;
    }

    @Override
    public RoundRecord value3(Integer value) {
        setScore(value);
        return this;
    }

    @Override
    public RoundRecord value4(UUID value) {
        setGameId(value);
        return this;
    }

    @Override
    public RoundRecord value5(UUID value) {
        setPlayerId(value);
        return this;
    }

    @Override
    public RoundRecord value6(UUID value) {
        setContractId(value);
        return this;
    }

    @Override
    public RoundRecord values(UUID value1, Integer value2, Integer value3, UUID value4, UUID value5, UUID value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundRecord
     */
    public RoundRecord() {
        super(Round.ROUND);
    }

    /**
     * Create a detached, initialised RoundRecord
     */
    public RoundRecord(UUID id, Integer number, Integer score, UUID gameId, UUID playerId, UUID contractId) {
        super(Round.ROUND);

        setId(id);
        setNumber(number);
        setScore(score);
        setGameId(gameId);
        setPlayerId(playerId);
        setContractId(contractId);
    }
}