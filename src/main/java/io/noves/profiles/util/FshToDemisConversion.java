package io.noves.profiles.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 * Entry point of the application. The annotation {@code @Slfj} is a feature from lombok and creates
 * automatically a logger (can be used as variable "log").
 */
public class FshToDemisConversion {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final List<String> STRUCTURE_DEFINITION_RESOURCES = List.of("Bundle", "Composition", "Practitioner",
            "Patient", "Organization", "Extension", "QuestionnaireResponse", "Specimen");

    /**
     * Copies the resources from a fsh-project into a target directory.
     * ATTENTION! The content of the target repository is deleted completely!
     * @param pathFshProject A path to the root directory of a FHIR Shorthand project ('./fsh').
     * @param targetDirectory The ID of the new project (e.g. 'de.basis.r4'). Is used to create a new directory
     * @throws IOException May fail during one of the many IO operations (create/copy directories & files).
     */
    public void convert(Path pathFshProject, Path targetDirectory) throws IOException {

        validateExistence(pathFshProject, "The provided fsh-directory does not exist: " + pathFshProject);

        var resourceDir = pathFshProject.resolve("fsh-generated/resources");
        validateExistence(resourceDir, "Expecting the directory 'fsh-generated/resources' to exist");

        // Remove existing files
        remove(targetDirectory);

        var meta = targetDirectory.resolve("MetadataResources");

        // Copy non StructureDefinition resources
        log.info("Copy terminology resources");
        copyResources(meta.resolve("CodeSystem"), e -> e.getFileName().toString().startsWith("ValueSet-"), resourceDir);
        copyResources(meta.resolve("ValueSet"), e -> e.getFileName().toString().startsWith("CodeSystem-"), resourceDir);
        copyResources(meta.resolve("NamingSystem"), e -> e.getFileName().toString().startsWith("NamingSystem-"), resourceDir);

        log.info("Copy Questionnaires");
        copyResources(meta.resolve("Questionnaire"), e -> e.getFileName().toString().startsWith("Questionnaire-"), resourceDir);

        // Copy StructureDefinition resources
        var sd = meta.resolve("StructureDefinition");
        log.info("Copy StructureDefinitions");
        for (var resource : STRUCTURE_DEFINITION_RESOURCES) {
            copyResources(sd.resolve(resource), e -> resourceOfType(e, resource), resourceDir);
        }

        // Copy example resources
        log.info("Copy example files");
        copyResources(targetDirectory.resolve("ExampleResources"), e -> e.getFileName().toString().endsWith("Example.json"), resourceDir);

        log.info("Finished copying resources!");
    }

    private void validateExistence(Path fshBasis, String s) throws NoSuchFileException {
        if (!fshBasis.toFile().exists()) {
            throw new NoSuchFileException(s);
        }
    }

    private boolean resourceOfType(Path resource, String type) {
        if (resource.toFile().isDirectory()) {
            return false;
        }
        try {
            var parser = FhirContext.forR4().newJsonParser();
            var fhirResource = parser.parseResource(new FileInputStream(resource.toFile()));
            return fhirResource instanceof StructureDefinition structureDefinition &&
                    structureDefinition.getType().equals(type);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException("Failed reading " + resource, e);
        }
    }

    private void copyResources(Path target, Predicate<Path> filtering, Path resourceDir) throws IOException {
        try (Stream<Path> walk = Files.walk(resourceDir)) {
            walk.filter(filtering)
                    .forEach(e -> {
                        createDir(target);
                        try {
                            Files.copy(e, target.resolve(e.getFileName()));
                        } catch (IOException ex) {
                            throw new UncheckedIOException(ex);
                        }
                    });
        }
    }

    private void createDir(Path target)  {
        if (!target.toFile().exists()) {
            try {
                Files.createDirectories(target);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private void remove(Path path) throws IOException {
        if (path.toFile().exists()) {
            log.info("Remove directory {} and any nested files.", path);
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

}
