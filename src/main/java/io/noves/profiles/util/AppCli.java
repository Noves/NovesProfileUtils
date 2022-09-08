package io.noves.profiles.util;

import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@CommandLine.Command(
        name = "policy-validator",
        mixinStandardHelpOptions = true)
public class AppCli implements Runnable {

    @CommandLine.Option(
            names = {"-i", "--inputDirectory"},
            required = true,
            paramLabel = "INPUT_DIRECTORY_PATH",
            description = "Provide a path to the root directory of a FHIR Shorthand project " +
                    "(the './fsh.'-directory).")
    Path inputDir;

    @CommandLine.Option(
            names = {"-o", "--outputDirectory"},
            required = true,
            paramLabel = "OUTPUT_DIRECTORY_PATH",
            description = "Provide a path to copy the files to. ATTENTION! This will remove any" +
                    " content from an existing provided directory.")
    Path outputDir;

    @Override
    public void run() {
        try {
            new FshToDemisConversion().convert(inputDir, outputDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed executing resource mapping: ", e);
        }
    }

    protected int executeApp(String... args) {
        return new CommandLine(this).execute(args);
    }

    public static void main(String[] args) {
        int exitCode = new AppCli().executeApp(args);
        System.exit(exitCode);
    }

}
