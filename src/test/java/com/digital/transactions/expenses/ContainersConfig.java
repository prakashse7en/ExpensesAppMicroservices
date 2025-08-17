package com.digital.transactions.expenses;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

  static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:25.0";
  static String realmImportFile = "/keycloaktcdemo-realm.json";
  static String realmName = "keycloaktcdemo";

  @Bean
  KeycloakContainer keycloak() {
    var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
            .withRealmImportFile(realmImportFile);


    return keycloak;
  }



}
