package net.ada.mixins.transformer;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.additionalclassprovider.FileSystemClassProvider;
import net.lenni0451.classtransform.additionalclassprovider.PathClassProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        Path srcPath = Path.of(args[0]);
        Path mixinPath = Path.of(args[1]);
        Path targetPath = Path.of(args[2]);
        System.out.println("srcPath: " + srcPath);
        System.out.println("mixinPath: " + mixinPath);
        System.out.println("targetPath: " + targetPath);
        PathClassProvider provider = new PathClassProvider(mixinPath);
        TransformerManager transformerManager = new TransformerManager(provider);

        for (String mixin : provider.getAllClasses().keySet()) {
            System.out.println("Loading mixin: " + convRelPathToJava(mixin));
            transformerManager.addTransformer(convRelPathToJava(mixin));
        }
        transformClasses(srcPath, targetPath, transformerManager);
    }
    private static void transformClasses(
            Path srcPath,
            Path targetPath,
            TransformerManager transformerManager
    ) throws IOException {
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(path ->
                         path.toString().endsWith(".class")
                    ).forEach(path -> {
                        try {
                            Path pathName = srcPath.relativize(path);
                            String className = convRelPathToJava(pathName.toString());
                            byte[] bytecode = Files.readAllBytes(path);
                            byte[] transformed = transformerManager.transform(className, bytecode);
                            if (transformed == null) {
                                transformed = bytecode;
                            }
                            Path output = targetPath.resolve(pathName);

                            Files.createDirectories(output.getParent());
                            Files.write(output, transformed);

                            System.out.println("Wrote: " + pathName);
                        }
                        catch (Exception e) {
                            System.out.println("Error: " + e.getMessage() + " run into while attempted to parse:" + path);
                            e.printStackTrace();
                        }
                    });
        }
    }

    /**
     * Converts relative class path to java package name. im lazy, ok?
     */
    public static String convRelPathToJava(String relativePath) {
            return relativePath.replace('\\', '.')
                .replace('/', '.')
                .replaceAll("\\.class$", "");
    }
}