package abel.keycloak;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class UserRestResourceProvider implements RealmResourceProvider {

    private KeycloakSession session;

    public UserRestResourceProvider(KeycloakSession session) {

        this.session = session;
    }

    @Override
    public Object getResource() {

        return new UserRestRessource(session);
    }

    @Override
    public void close() { }

}
