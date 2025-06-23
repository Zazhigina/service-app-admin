#!/bin/bash

# Clear store
rm -f /tmp/truststore.jks

# Generate random key for store
export JKS_KEY=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

## Add keycloak cert to keystore
if [ -n "$KEYCLOAK_ROOT_CA_URL" ]; then curl -k -s $KEYCLOAK_ROOT_CA_URL --output /tmp/root.ca.pem \
  && keytool -noprompt -import -keystore /tmp/truststore.jks -storepass $JKS_KEY -alias keycloak.ca.pem -file /tmp/root.ca.pem &>/dev/null; fi
if [ -n "$KEYCLOAK_SUB_CA_URL" ]; then curl -k -s $KEYCLOAK_SUB_CA_URL --output /tmp/sub-root.ca.pem \
  && keytool -noprompt -import -keystore /tmp/truststore.jks -storepass $JKS_KEY -alias keycloak.subca.pem -file /tmp/sub-root.ca.pem &>/dev/null; fi

# Run
if [ -f /tmp/truststore.jks ]; then
  exec java -Djavax.net.ssl.trustStore=/tmp/truststore.jks -Djavax.net.ssl.trustStorePassword=$JKS_KEY "$@"
else
  exec java "$@"
fi
