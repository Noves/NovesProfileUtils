package io.noves.project.template.java;

import lombok.extern.slf4j.Slf4j;

/**
 * Entry point of the application. The annotation {@code @Slfj} is a feature from lombok and creates
 * automatically a logger (can be used as variable "log").
 */
@Slf4j
public class ExampleProject {

  private static final int NUMBER_FOR_THE_OUTPUT = 1;

  public static void main(String[] args) {
    var exampleProject = new ExampleProject();
    var numberForOutput = exampleProject.getNumber();

    log.info("Hello! You are my No. {}!", numberForOutput);
  }

  /**
   * This class has the modifier "protected", so that it can be tested via Unit-tests in {@code
   * ExampleProjectTest}.
   *
   * @return A number
   */
  protected int getNumber() {
    return NUMBER_FOR_THE_OUTPUT;
  }
}
