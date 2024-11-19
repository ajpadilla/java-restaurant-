package restautant.kitchen.shared.domain;

import java.util.Objects;

public abstract class StringValueObject {
    private final String value;

    public StringValueObject(String value) {
        this.value = value;
    }


    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringValueObject that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
