package basarab.olexandr.springfinalproject.exceptions;

import java.util.function.Supplier;

public class NoSuchFriendshipException extends RuntimeException {

    public NoSuchFriendshipException(String message) {
        super(message);
    }
}
