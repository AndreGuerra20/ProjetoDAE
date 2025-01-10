package pt.ipleiria.estg.dei.ei.dae.pmei.exceptions;

public class MyEntityExistsException extends RuntimeException {
    public MyEntityExistsException(String message) {
        super(message);
    }
}
