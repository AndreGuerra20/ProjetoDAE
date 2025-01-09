package pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.NotAuthorizedException;

import java.util.logging.Logger;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    private static final Logger logger = Logger.getLogger(NotAuthorizedException.class.getCanonicalName());

    @Override
    public Response toResponse(NotAuthorizedException e) {
        String errorMsg = e.getMessage();
        logger.warning("Error: " + errorMsg);
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
    }

}
