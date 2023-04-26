#!/bin/bash

## Add keycloak cert to keystore
if [ -n "$KEYCLOAK_ROOT_CA_URL" ]; then curl -k -s $KEYCLOAK_ROOT_CA_URL --output /tmp/root.ca.pem \
  && keytool -noprompt -import -keystore /tmp/truststore.jks -storepass changeit -alias keycloak.ca.pem -file /tmp/root.ca.pem &>/dev/null; fi
if [ -n "$KEYCLOAK_SUB_CA_URL" ]; then curl -k -s $KEYCLOAK_SUB_CA_URL --output /tmp/sub-root.ca.pem \
  && keytool -noprompt -import -keystore /tmp/truststore.jks -storepass changeit -alias keycloak.subca.pem -file /tmp/sub-root.ca.pem &>/dev/null; fi

exec "$@"
