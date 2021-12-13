package abel.keycloak;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static java.util.stream.Collectors.toList;

public class UserRestRessource {

    private final KeycloakSession session;
    private final AuthenticationManager.AuthResult auth;

    public UserRestRessource(KeycloakSession session) {
        this.session = session;
        this.auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserRepresentation> getUsersByAttr(
            @QueryParam("key") String attrKey,
            @QueryParam("value") String attrValue,
			@QueryParam("firstResult") int firstResult,
			@DefaultValue("100") @QueryParam("maxResults") int maxResults
    ) {
        checkRealmAccess();
		Map<String, String> attributes = new HashMap<>();
        attributes.put(attrKey, attrValue);
        // userLocalStorage(): Get keycloak specific local storage for users.  No cache in front, this api talks directly to database configured for Keycloak
        // users(): Get a cached view of all users in system including  users loaded by UserStorageProviders
        // searchForUser(): Support Attributes since v15.1.0
        return session.userLocalStorage()
                .searchForUser(attributes, session.getContext().getRealm(), firstResult, maxResults)
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
