package com.midtier.bonmunch.util;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class Guid {

    private UUID uuid;


    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public Guid(final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException();
        }
        this.uuid = uuid;
    }

    public Guid(final String s) {
        if (s == null) {
            throw new NullPointerException("GUID String parameter cannot be null");
        }
        if (s.length() != 32) {
            throw new IllegalArgumentException("GUID string parameter must be 32 characters long");
        }
        StringBuilder sb = new StringBuilder(s.substring(0, 8));
        sb.append('-');
        sb.append(s.substring(8, 12));
        sb.append('-');
        sb.append(s.substring(12, 16));
        sb.append('-');
        sb.append(s.substring(16, 20));
        sb.append('-');
        sb.append(s.substring(20));
        this.uuid = UUID.fromString(sb.toString());
    }

    public final String toString() {
        return uuid.toString().toUpperCase().replace("-", "");
    }

    public UUID getUUid() {
        return this.uuid;
    }

    public static byte[] getIdAsByte(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID frombyte(byte[] inputByteArray) {
        ByteBuffer bb = ByteBuffer.wrap(inputByteArray);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(obj, uuid);
    }


}
