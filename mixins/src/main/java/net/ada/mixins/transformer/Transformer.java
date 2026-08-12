package net.ada.mixins.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ada.manifest.MixinSourceObj;
import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.additionalclassprovider.PathClassProvider;
import net.lenni0451.classtransform.mixinstranslator.MixinsTranslator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.lenni0451.classtransform.utils.tree.BasicClassProvider;
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
        //  TODO please replace this, i just suck at gradle atm~
        Path srcPath = rootPath.resolve(args[0]).normalize();
        Path addonPath = rootPath.resolve(args[1]).normalize();
        Path mixinRefPath = rootPath.resolve(args[2]).normalize();
        Path targetPath = rootPath.resolve(args[3]).normalize();
        System.out.println("srcPath: " + srcPath);
        System.out.println("addonPath: " + addonPath);
        System.out.println("mixinRefPath: " + mixinRefPath);
        System.out.println("targetPath: " + targetPath);
        BasicClassProvider javaProvider = new BasicClassProvider();
        PathClassProvider srcProvider = new PathClassProvider(srcPath, javaProvider);
        PathClassProvider provider = new PathClassProvider(addonPath, srcProvider);
        TransformerManager transformerManager = new TransformerManager(provider);
        MixinSourceObj mixinSourceObj = gson.fromJson(new String(Files.readAllBytes(mixinRefPath)), MixinSourceObj.class);
        transformerManager.addTransformerPreprocessor(new MixinsTranslator());
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
                    return !className.equals(mixinPathName)
                            && !className.startsWith(mixinPathName + ".");
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