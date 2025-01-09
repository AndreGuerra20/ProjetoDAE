package pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException>  {

    private static final Logger logger = Logger.getLogger(ForbiddenException.class.getCanonicalName());

    @Override
    public Response toResponse(ForbiddenException e) {
        String errorMsg = e.getMessage();
        logger.warning("Error: " + errorMsg);
        return Response.status(Response.Status.FORBIDDEN).entity(errorMsg).build();
    }

}
