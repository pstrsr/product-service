package com.github.almostfamiliar;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = TestConfig.PACKAGE_ROOT)
public class PortsArchitectureTest {

  @ArchTest
  static final ArchRule should_EndWithUseCaseOrCRUD_When_IsInPort =
      classes()
          .that()
          .resideInAPackage("..in")
          .should()
          .haveSimpleNameEndingWith("UseCase")
          .orShould()
          .haveSimpleNameEndingWith("CRUD");

  @ArchTest
  static final ArchRule should_EndWithCommand_When_IsCommandModel =
      classes().that().resideInAPackage("..command").should().haveSimpleNameEndingWith("Cmd");
}
