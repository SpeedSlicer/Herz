package net.ada.mixins.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ada.manifest.MixinSourceObj;
import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.additionalclassprovider.PathClassProvider;
import net.lenni0451.classtransform.mixinstranslator.MixinsTranslator;
import net.lenni0451.classtransform.utils.tree.BasicClassProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Transformer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Beginning Transformer");

        Path jarPath = Path.of(
                Transformer.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
        );

        Path rootPath = jarPath.getParent().getParent();

        Gson gson = new GsonBuilder().create();

        Path classesPath = rootPath.resolve(args[0]).normalize();
        Path mixinRefPath = rootPath.resolve(args[1]).normalize();

        System.out.println("classesPath: " + classesPath);
        System.out.println("mixinRefPath: " + mixinRefPath);

        BasicClassProvider javaProvider = new BasicClassProvider();
        PathClassProvider classProvider =
                new PathClassProvider(classesPath, javaProvider);

        TransformerManager transformerManager =
                new TransformerManager(classProvider);

        MixinSourceObj mixinSourceObj = gson.fromJson(
                Files.readString(mixinRefPath),
                MixinSourceObj.class
        );

        transformerManager.addTransformerPreprocessor(
                new MixinsTranslator()
        );

        for (String mixin : mixinSourceObj.mixins()) {
            String mixinClass =
                    mixinSourceObj.mixinPackage() + "." + mixin;

            System.out.println("Mixin Loaded: " + mixinClass);
            transformerManager.addTransformer(mixinClass);
        }

        transform(
                classesPath,
                transformerManager,
                mixinSourceObj.mixinPackage()
        );
    }

    private static void transform(
            Path classesPath,
            TransformerManager transformerManager,
            String mixinPackage
    ) throws IOException {

        Map<Path, byte[]> originalClasses = new LinkedHashMap<>();

        try (Stream<Path> stream = Files.walk(classesPath)) {
            stream
                    .filter(Transformer::isClassFile)
                    .forEach(path -> {
                        try {
                            originalClasses.put(
                                    path,
                                    Files.readAllBytes(path)
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        Map<Path, byte[]> transformedClasses = new LinkedHashMap<>();

        for (Map.Entry<Path, byte[]> entry : originalClasses.entrySet()) {
            Path path = entry.getKey();
            byte[] bytecode = entry.getValue();

            Path relativePath = classesPath.relativize(path);
            String className =
                    convRelPathToJava(relativePath.toString());

            if (className.equals(mixinPackage)
                    || className.startsWith(mixinPackage + ".")) {
                continue;
            }

            try {
                byte[] transformed =
                        transformerManager.transform(
                                className,
                                bytecode
                        );

                if (!Arrays.equals(bytecode, transformed)) {
                    transformedClasses.put(path, transformed);
                }

            } catch (Exception e) {
                System.err.println(
                        "Failed to transform: " + className
                );
                e.printStackTrace();

                throw new RuntimeException(
                        "Transformation failed for " + className,
                        e
                );
            }
        }

        for (Map.Entry<Path, byte[]> entry
                : transformedClasses.entrySet()) {
            if (entry.getValue() != null) {
                Files.write(
                        entry.getKey(),
                        entry.getValue()
                );
            }
            else {
                // pass because its not transformed
                // screw you whoever has left me with this bug. I am in so much pain after searching for millenia of where this issue could be
            }

        }
    }

    public static String convRelPathToJava(String relativePath) {
        return relativePath
                .replace('\\', '.')
                .replace('/', '.')
                .replaceAll("\\.class$", "");
    }

    public static boolean isClassFile(Path path) {
        return Files.isRegularFile(path)
                && path.getFileName()
                .toString()
                .endsWith(".class");
    }
}