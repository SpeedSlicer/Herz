package net.ada.mixins.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ada.manifest.MixinSourceObj;
import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.additionalclassprovider.PathClassProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Transformer {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().create();
        Path srcPath = Path.of(args[0]);
        Path addonPath = Path.of(args[1]);
        Path mixinRefPath = Path.of(args[2]);
        Path targetPath = Path.of(args[3]);
        System.out.println("srcPath: " + srcPath);
        System.out.println("addonPath: " + addonPath);
        System.out.println("mixinRefPath: " + mixinRefPath);
        System.out.println("targetPath: " + targetPath);
        PathClassProvider provider = new PathClassProvider(addonPath);
        TransformerManager transformerManager = new TransformerManager(provider);
        MixinSourceObj mixinSourceObj = gson.fromJson(new String(Files.readAllBytes(mixinRefPath)), MixinSourceObj.class);
        for(String mixin : mixinSourceObj.mixins()) {
             transformerManager.addTransformer(mixinSourceObj.mixinPackage() + "." + mixin);
        }
        transformClasses(srcPath, targetPath, transformerManager);
        combineSrc(addonPath, targetPath, mixinSourceObj.mixinPackage());
    }
    private static void transformClasses(
            Path srcPath,
            Path targetPath,
            TransformerManager transformerManager
    ) throws IOException {
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(Transformer::isClassFile)
                    .forEach(path -> {
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
    private static void combineSrc(Path addonSrc, Path outSrc, String mixinPathName) throws IOException {
        try (Stream<Path> stream = Files.walk(addonSrc)) {
            stream.filter(Files::isRegularFile).filter(Files::isReadable).filter(Transformer::isClassFile).filter(path -> {
                try {
                    Path pathName = addonSrc.relativize(path);
                    String className = convRelPathToJava(pathName.toString());
                    return !className.startsWith(mixinPathName);
                }
                catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + " run into while attempted to parse:" + addonSrc);
                    e.printStackTrace();
                }
                return false;
            }).forEach(
                    path -> {
                        String pathName = addonSrc.relativize(path).toString();
                        Path newDir = outSrc.resolve(pathName);
                        try {
                            Files.createDirectories(newDir.getParent());
                            Files.write(newDir, Files.readAllBytes(path));
                        } catch (IOException e) {
                            System.out.println("New Directory: " + newDir);
                            System.out.println("Path Name: " + pathName);
                            System.out.println("Src Path : " + path);

                            throw new RuntimeException(e);
                        }
                    }
            );
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
    public static boolean isClassFile(Path path) {
        return Files.isRegularFile(path)
                && path.getFileName().toString().endsWith(".class");
    }
}