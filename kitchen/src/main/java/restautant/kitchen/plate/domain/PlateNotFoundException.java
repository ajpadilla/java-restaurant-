package restautant.kitchen.plate.domain;

public final class PlateNotFoundException extends RuntimeException{

    public PlateNotFoundException(String message) {
        super(message);
    }

    public PlateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
