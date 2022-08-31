package io.noves.profiles.util;

import ca.uhn.fhir.context.FhirContext;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Entry point of the application. The annotation {@code @Slfj} is a feature from lombok and creates
 * automatically a logger (can be used as variable "log").
 */
@Slf4j
public class FshToDemisConversion {

    public void convert(String projectId, Path fshBasis) throws IOException {
        var resourceDir = fshBasis.resolve("fsh/fsh-generated/resources");
        var basePath = Paths.get("./" + projectId);

        // Remove existing structure
        remove(basePath);

        // Create new Directories
        var base = Files.createDirectory(basePath);
        var examples = Files.createDirectory(basePath.resolve("ExampleResources"));
        var ig = Files.createDirectory(basePath.resolve("ImplementationGuideSupportingResources"));

        var meta = Files.createDirectory(basePath.resolve("MetadataResources"));

        var cs = Files.createDirectory(meta.resolve("CodeSystem"));
        var vs = Files.createDirectory(meta.resolve("ValueSet"));
        var qs = Files.createDirectory(meta.resolve("Questionnaire"));

        var sd = Files.createDirectory(meta.resolve("StructureDefinition"));

        var bundle = Files.createDirectory(sd.resolve("Bundle"));
        var composition = Files.createDirectory(sd.resolve("Composition"));
        var practitioner = Files.createDirectory(sd.resolve("Practitioner"));
        var immunization = Files.createDirectory(sd.resolve("Immunization"));
        var extension = Files.createDirectory(sd.resolve("Extension"));
        var questionnaireResponse = Files.createDirectory(sd.resolve("QuestionnaireResponse"));
        var specimen = Files.createDirectory(sd.resolve("specimen"));

        // Copy resources
        copyResources(examples, e -> e.getFileName().toString().endsWith("Example.json"), resourceDir);
        copyResources(vs, e -> e.getFileName().toString().startsWith("ValueSet"), resourceDir);
        copyResources(cs, e -> e.getFileName().toString().startsWith("CodeSystem"), resourceDir);
        copyResources(qs, e -> e.getFileName().toString().startsWith("Questionnaire"), resourceDir);

        copyResources(bundle, "Bundle", resourceDir);
        copyResources(composition, "Composition", resourceDir);
        copyResources(practitioner, "Practitioner", resourceDir);
        copyResources(immunization, "Immunization", resourceDir);
        copyResources(extension, "Extension", resourceDir);
        copyResources(questionnaireResponse, "QuestionnaireResponse", resourceDir);
        copyResources(specimen, "Specimen", resourceDir);
    }

    private void copyResources(Path target, String type, Path fshBasis) throws IOException {
        try (Stream<Path> walk = Files.walk(fshBasis)) {
            walk.filter(e -> resourceOfType(e, type))
                    .forEach(
                            e -> {
                                try {
                                    Files.copy(e, target.resolve(e.getFileName()));
                                } catch (IOException ex) {
                                    throw new RuntimeException("Failed for type " + type, ex);
                                }
                            }
                    );
        }
    }


    private boolean resourceOfType(Path resource, String type) {
        if (resource.toFile().isDirectory()) {
            return false;
        }
        try {
            var parser = FhirContext.forR4().newJsonParser();
            var fhirResource = parser.parseResource(new FileInputStream(resource.toFile()));
            if (fhirResource instanceof StructureDefinition &&
                    ((StructureDefinition) fhirResource).getType().equals(type)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed reading " + resource, e);
        }
    }

    private void copyResources(Path target, Predicate<Path> filtering, Path fshBasis) throws IOException {
        try (Stream<Path> walk = Files.walk(fshBasis)) {
            walk.filter(filtering)
                    .forEach(e -> {
                        try {
                            Files.copy(e, target.resolve(e.getFileName()));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        }
    }

    private void remove(Path path) throws IOException {
        if (path.toFile().exists()) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please provide the ID of the project!");
        }
        else {
            var fshBasis = args.length > 1 ? args[1] : "./";
            new FshToDemisConversion().convert(args[0], Paths.get(fshBasis));
        }
    }

}
