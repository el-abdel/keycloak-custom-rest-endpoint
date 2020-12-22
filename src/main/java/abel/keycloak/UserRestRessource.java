package abel.keycloak;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserRestRessource {

    private final KeycloakSession session;
    private final AuthenticationManager.AuthResult auth;

    public UserRestRessource(KeycloakSession session) {
        this.session = session;
        this.auth = new AppAuthManager().authenticateBearerToken(session, session.getContext().getRealm());
    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserRepresentation> getUsersByAttr(
            @QueryParam("key") String attrKey,
            @QueryParam("value") String attrValue
    ) {
        checkRealmAccess();
        return session.users()
                .searchForUserByUserAttribute(attrKey, attrValue, session.getContext().getRealm())
                .stream()
                .map(userModel -> ModelToRepresentation.toRepresentation(session, session.getContext().getRealm(), userModel))
                .collect(toList());
    }

    private void checkRealmAccess() {
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getToken().getRealmAccess() == null || !auth.getToken().getRealmAccess().isUserInRole("fetch_users")) {
            throw new ForbiddenException("Does not have realm admin role");
        }
    }
}
