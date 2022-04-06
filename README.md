[![forthebadge](https://forthebadge.com/images/badges/open-source.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) 
![uses-spring-boot (2)](https://user-images.githubusercontent.com/66125882/150993441-590505b7-fd53-44df-9ac5-695d0fb59754.svg)
![uses-eclipse-ide (1)](https://user-images.githubusercontent.com/66125882/150993531-3f8d450c-0399-4c9f-920c-4296d0473f2d.svg)

# TourGuide
Api permettant aux voyageurs d'obtenir des offres de voyages personnalisés et des points de récompense pour les attractions touristiques visités. 

## Pour commencer:
- Cloner la derniere release du projet
- Ouvrer un terminal puis taper les commandes de démarrages.

## Démarrage:
- docker-compose up -d

ou les commandes suivantes dans l'ordre:

- docker run -d -p 9001:9001 --name gpsutil tourguide/gpsutil
- docker run -d -p 9002:9002 --name rewards tourguide/rewardcentral
- docker run -d -p 9003:9003 --name trippricer tourguide/trippricer
- docker run -d -p 8080:8080 --name tourguide tourguide/tourguide

## Technologies
1. Framework: Spring Boot v2.6.5
2. Java 8
3. Junit 5: v5.3.2
4. Jacoco v0.8.4
5. Maven v3.0
6. Eclipe IDE Version: 2020-09 (4.17.0)
7. Docker v20.10.12
8. Docker-compose v1.29.2

## Version
tourguide_0.0.1

## Autheur
- Aurélien NDJAMBA

## License

Apache Public License 2.0: Plexus Cipher: encryption/decryption Component, Plexus Security Dispatcher Component

Eclipse Public License 1.0: JUnit

The Apache License, Version 2.0: org.apiguardian:apiguardian-api, org.opentest4j:opentest4j

MIT License: JUL to SLF4J bridge, SLF4J API Module, json iterator

BSD License 3: Hamcrest, Hamcrest Core

Inconnu: Plexus I18N Component, Plexus Velocity Component, classworlds, commons-beanutils, commons-digester, oro

Eclipse Public License v2.0: JUnit Jupiter (Aggregator), JUnit Jupiter API, JUnit Jupiter Engine, JUnit Jupiter Params, JUnit Platform Commons, JUnit Platform Engine API

Eclipse Public License v1.0: JaCoCo :: Agent, JaCoCo :: Core, JaCoCo :: Maven Plugin, JaCoCo :: Report

GPL2 w/ CPE: Jakarta Annotations API

GNU Lesser General Public License: Logback Classic Module, Logback Core Module

BSD: asm, asm-analysis, asm-commons, asm-tree

EDL 1.0: Jakarta Activation API jar

Apache License 2.0: JSON library from Android SDK

The MIT License: Project Lombok, mockito-core, mockito-junit-jupiter

Apache License, Version 2.0: Apache Commons Codec, Apache Commons Lang, Apache Log4j API, Apache Log4j to SLF4J Adapter, AssertJ fluent assertions, Byte Buddy (without dependencies), Byte Buddy agent, JSR 354 (Money and Currency API), Objenesis, SnakeYAML, Spring AOP, Spring Beans, Spring Commons Logging Bridge, Spring Context, Spring Core, Spring Expression Language (SpEL), Spring TestContext Framework, Spring Web, Spring Web MVC, spring-boot, spring-boot-autoconfigure, spring-boot-devtools, spring-boot-starter, spring-boot-starter-json, spring-boot-starter-logging, spring-boot-starter-test, spring-boot-starter-tomcat, spring-boot-starter-web, spring-boot-test, spring-boot-test-autoconfigure, tomcat-embed-core, tomcat-embed-el, tomcat-embed-websocket, tourguide

Eclipse Distribution License - v 1.0: Jakarta XML Binding API

EPL 2.0: Jakarta Annotations API

CDDL + GPLv2 with classpath exception: javax.annotation API

Apache 2 License: Moneta (JSR 354 RI), Moneta Core, Moneta Currency Conversion, Moneta Currency Conversion - ECB Provider, Moneta Currency Conversion - IMF Provider

The Apache Software License, Version 2.0: ASM based accessors helper used by json-smart, Aether :: API, Aether :: Implementation, Aether :: SPI, Aether :: Utilities, Commons Lang, Doxia :: Core, Doxia :: FML Module, Doxia :: Logging API, Doxia :: Sink API, Doxia :: XHTML Module, Doxia Sitetools :: Decoration Model, Doxia Sitetools :: Site Renderer Component, JSON Small and Fast Parser, JSONassert, Jackson datatype: JSR310, Jackson datatype: jdk8, Jackson-annotations, Jackson-core, Jackson-module-parameter-names, Logging, Maven Aether Provider, Maven Artifact, Maven Core, Maven File Management API, Maven Model, Maven Model Builder, Maven Plugin API, Maven Plugin Registry Model, Maven Profile Model, Maven Project Builder, Maven Reporting API, Maven Reporting Implementation, Maven Repository Metadata Model, Maven Settings, Maven Settings Builder, Maven Shared I/O API, Plexus :: Component Annotations, Plexus Classworlds, Plexus Common Utilities, Plexus Interpolation API, Sisu - Guice, Sisu - Inject (JSR330 bean support), Sisu - Inject (Plexus bean support), Validator, XML Commons External Components XML APIs, jackson-databind, org.xmlunit:xmlunit-core, project ':json-path'

Eclipse Public License - v 1.0: Logback Classic Module, Logback Core Module
