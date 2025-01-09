package pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.logging.Logger;

@Provider
public class MyEntityNotFoundExceptionMapper implements ExceptionMapper<MyEntityNotFoundException> {

    private static final Logger LOGGER = Logger.getLogger(MyEntityNotFoundException.class.getCanonicalName());

    @Override
    public Response toResponse(MyEntityNotFoundException e) {
        String errorMsg = e.getMessage();
        LOGGER.warning("Error: " + errorMsg);
        return Response.status(Response.Status.CONFLICT).entity(errorMsg).build();
    }
}
