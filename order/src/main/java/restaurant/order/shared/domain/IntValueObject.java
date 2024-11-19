package restaurant.order.shared.domain;

public class IntValueObject {
    private final Integer value;

    public IntValueObject(Integer value) {
        this.value = value;
    }

    public int getValue() {
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
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        IntValueObject that = (IntValueObject) obj;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
