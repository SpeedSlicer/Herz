package net.ada.transformer;

import net.lenni0451.classtransform.TransformerManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        Path srcPath = Path.of(args[0]);
        Path mixinPath = Path.of(args[1]);
        Path targetPath = Path.of(args[2]);

        TreeClassProvider classProvider =
                new TreeClassProvider(srcPath, mixinPath);

        TransformerManager transformerManager =
                new TransformerManager(classProvider);

        TreeClassProvider mixinProvider =
                new TreeClassProvider(mixinPath);

        for (String mixin : mixinProvider.getAllClasses().keySet()) {
            System.out.println("Loading mixin: " + mixin);

            transformerManager.addTransformer(mixin);
        }

        transformClasses(srcPath, targetPath, transformerManager);
    }
    private static void transformClasses(
            Path srcPath,
            Path targetPath,
            TransformerManager transformerManager
    ) throws IOException {

        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .forEach(file -> {
                        try {
                            // net/ada/Test.class
                            Path relativePath = srcPath.relativize(file);

                            // net.ada.Test
                            String className = relativePath
                                    .toString()
                                    .replace('\\', '.')
                                    .replace('/', '.')
                                    .replaceAll("\\.class$", "");

                            byte[] bytecode = Files.readAllBytes(file);

                            byte[] transformed = transformerManager.transform(
                                    className,
                                    bytecode
                            );

                            if (transformed != null) {
                                bytecode = transformed;
                            }

                            Path output = targetPath.resolve(relativePath);

                            Files.createDirectories(output.getParent());
                            Files.write(output, bytecode);

                            System.out.println("Wrote: " + className);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}