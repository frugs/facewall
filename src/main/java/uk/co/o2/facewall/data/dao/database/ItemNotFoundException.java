package uk.co.o2.facewall.data.dao.database;

public class ItemNotFoundException extends Exception {
    ItemNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
