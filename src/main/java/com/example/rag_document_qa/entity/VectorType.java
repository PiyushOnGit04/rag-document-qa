package com.example.rag_document_qa.entity;

import com.pgvector.PGvector;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

public class VectorType implements UserType<float[]> {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<float[]> returnedClass() {
        return float[].class;
    }

    @Override
    public boolean equals(float[] x, float[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(float[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public float[] nullSafeGet(ResultSet rs, int position, WrapperOptions options) throws SQLException {
        Object value = rs.getObject(position);
        return value == null ? null : ((PGvector) value).toArray();
    }

    @Override
    public void nullSafeSet(PreparedStatement st, float[] value, int index, WrapperOptions options) throws SQLException {
        st.setObject(index, value == null ? null : new PGvector(value));
    }

    @Override
    public float[] deepCopy(float[] value) {
        return value == null ? null : value.clone();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(float[] value) {
        return value == null ? null : value.clone();
    }

    @Override
    public float[] assemble(Serializable cached, Object owner) {
        return cached == null ? null : ((float[]) cached).clone();
    }

    @Override
    public float[] replace(float[] detail, float[] target, Object owner) {
        return detail == null ? null : detail.clone();
    }
}