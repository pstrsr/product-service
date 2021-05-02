package com.github.almostfamiliar;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = TestConfig.PACKAGE_ROOT)
public class DomainArchitectureTest {

  @ArchTest
  static final ArchRule should_OnlyDependOnLombokAndExceptions =
      classes()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .onlyDependOnClassesThat()
          .resideInAnyPackage("..lombok..", "..domain..", "java..", "..exception..");
}
