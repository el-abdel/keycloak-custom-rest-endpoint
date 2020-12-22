Keycloak Custom REST Endpoint
=============================

Keycloak custom REST endpoint that search for users by custom user attribute, providing a JWT access token and based on a realm role.

## Keycloak Integration

Build the jar by running mvn install and copy the jar to keycloak's deployment folder.

```
${KEYCLOAK_HOME}/standalone/deployments
```

> Keycloak support hot deployment so as soon jar file copied to above location it will create <jar name>.deployed file. <br>
> Once application deployed correctly it will be added to keycloak server and visible under `server Info > providers` details.

## Testing

1. Create a realm (if you don't have an active one) and add few users with a custom attribute to the realm.
2. Add role `fetch_users` to your realm.
3. Lookup Users
    ```
    curl -i --request GET http://localhost:8080/auth/realms/<realm-name>/get-users-by-custom-attr?key=attr_key&value=attr_value --header "Accept: application/json" --header "Authorization: Bearer $ACCESS_TOKEN";
    ``` 
> This endpoint is accessible just for authenticated users. REST request must be authenticated with bearer access token of authenticated user and the user must be in realm role `fetch_users` in order to access the resource.

