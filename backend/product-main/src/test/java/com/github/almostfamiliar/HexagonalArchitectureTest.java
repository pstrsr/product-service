package com.github.almostfamiliar;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = TestConfig.PACKAGE_ROOT)
public class HexagonalArchitectureTest {

  public static final String DOMAIN_MODEL_PKG = TestConfig.PACKAGE_ROOT + ".domain..";
  public static final String SERIVCES_PKG = TestConfig.PACKAGE_ROOT;
  public static final String WEB_ADAPTER_PKG = TestConfig.PACKAGE_ROOT + ".product.web..";
  public static final String PERSISTENCE_ADAPTER_PACKAGE =
      TestConfig.PACKAGE_ROOT + ".product.persistence..";
  public static final String CURRENCY_COVNERTER_APDATER_PKG =
      TestConfig.PACKAGE_ROOT + ".product.currencyconverter..";

  /**
   * See diagram at https://www.archunit.org/userguide/html/000_Index.html#Onion%20Architecture for
   * what rules this enforces.
   */
  @ArchTest
  static final ArchRule should_FollowHexagonalRules =
      onionArchitecture()
          .domainModels(DOMAIN_MODEL_PKG)
          .domainServices(SERIVCES_PKG)
          .applicationServices(SERIVCES_PKG)
          .adapter("web", WEB_ADAPTER_PKG)
          .adapter("persistence", PERSISTENCE_ADAPTER_PACKAGE)
          .adapter("currency-converter", CURRENCY_COVNERTER_APDATER_PKG);
}
