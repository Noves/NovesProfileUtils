package io.noves.project.template.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is a test class with Unit tests for the class {@link ExampleProject}. The package of this
 * class has the exact same name to have access to the "protected" methods.
 */
class ExampleProjectTest {

  private static ExampleProject sut = new ExampleProject();

  @Test
  void testGetNumberIsOne() {
    assertEquals(1, sut.getNumber(), "Has to be 1, because you are special for me! <3");
  }
}
