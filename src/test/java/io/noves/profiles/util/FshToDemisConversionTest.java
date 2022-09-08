package io.noves.profiles.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FshToDemisConversionTest {

  @TempDir
  private File targetDir;

  private static final Path sourceDir = Paths.get("./src/test/resources/ExampleFishProject/fsh");

  @Test
  void testExampleProjectDefaultResponse() throws IOException {
    new FshToDemisConversion().convert(sourceDir, targetDir.toPath());
    assertEquals(countFiles(sourceDir), countFiles(targetDir.toPath()), "Number of files should be identical");
  }

  private long countFiles(Path path) throws IOException {
    try (Stream<Path> walk = Files.walk(path)) {
      return walk.filter(e -> e.toFile().isFile()).count();
    }
  }

}
