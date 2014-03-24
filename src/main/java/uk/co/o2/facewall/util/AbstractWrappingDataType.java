package uk.co.o2.facewall.util;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public abstract class AbstractWrappingDataType<T> {
    public final T value;

    protected AbstractWrappingDataType(T value) {
        this.value = value;
    }

    @Override final public boolean equals(Object obj) {
        return obj instanceof AbstractWrappingDataType && value.equals(((AbstractWrappingDataType) obj).value);
    }

    @Override final public int hashCode() {
        return value.hashCode();
    }
}
