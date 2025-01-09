package pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;

import java.util.logging.Logger;

@Provider
public class MyEntityExistsExceptionMapper implements ExceptionMapper<MyEntityExistsException> {

    private static final Logger LOGGER = Logger.getLogger(MyEntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(MyEntityExistsException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("Error: " + errorMsg);
        return Response.status(Response.Status.CONFLICT).entity(errorMsg).build();
    }
}
