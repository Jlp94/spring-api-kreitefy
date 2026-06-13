package com.kreitefy.api;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.kreitefy.api", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    // REGLA 1: El dominio NUNCA depende de aplicación ni infraestructura
    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..");

    // REGLA 2: La aplicación NUNCA depende de infraestructura
    @ArchTest
    public static final ArchRule application_should_not_depend_on_infrastructure =
            noClasses().that().resideInAPackage("..application..")
                    .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    // REGLA 3: Los controladores REST solo existen en infrastructure.rest
    @ArchTest
    public static final ArchRule controllers_should_only_be_in_infrastructure_rest =
            classes().that().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().resideInAPackage("..infrastructure.rest..");

    // REGLA 4: Los adaptadores de persistencia solo existen en infrastructure.persistence
    @ArchTest
    public static final ArchRule repositories_should_only_be_in_infrastructure_persistence =
            classes().that().areAnnotatedWith(org.springframework.stereotype.Repository.class)
                    .should().resideInAPackage("..infrastructure.persistence..");

    // REGLA 5: Los @RestController no deben acceder directamente a repositorios JPA
    @ArchTest
    public static final ArchRule controllers_should_not_access_jpa_repositories_directly =
            noClasses().that().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().dependOnClassesThat().resideInAPackage("..persistence.jpa..");

    // REGLA 6: Los @RestController no deben acceder directamente a entidades JPA
    @ArchTest
    public static final ArchRule controllers_should_not_access_jpa_entities_directly =
            noClasses().that().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().dependOnClassesThat().resideInAPackage("..persistence.entity..");

    // REGLA 7: Las entidades JPA solo existen en infrastructure.persistence.entity
    @ArchTest
    public static final ArchRule jpa_entities_should_only_be_in_persistence_entity =
            classes().that().areAnnotatedWith(jakarta.persistence.Entity.class)
                    .should().resideInAPackage("..infrastructure.persistence.entity..");
}
