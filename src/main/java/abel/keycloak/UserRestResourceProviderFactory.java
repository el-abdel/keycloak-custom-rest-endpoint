package abel.keycloak;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class UserRestResourceProviderFactory implements RealmResourceProviderFactory {

    public static final String ID = "get-users-by-custom-attr";

    @Override
    public String getId() {

        return ID;
    }

    @Override
    public RealmResourceProvider create(KeycloakSession session) {

        return new UserRestResourceProvider(session);
    }

    @Override
    public void init(Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }

}