package net.ada.transformer;

import net.lenni0451.classtransform.utils.tree.IClassProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TreeClassProvider implements IClassProvider {

    private final Path[] paths;

    public TreeClassProvider(Path... paths) {
        this.paths = paths;
    }

    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        String relative = name.replace('.', '/') + ".class";

        for (Path path : paths) {
            Path classPath = path.resolve(relative);

            if (Files.exists(classPath)) {
                try {
                    return Files.readAllBytes(classPath);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    public Map<String, Supplier<byte[]>> getAllClasses() {
        Map<String, Supplier<byte[]>> classes = new HashMap<>();

        for (Path root : paths) {
            try (Stream<Path> stream = Files.walk(root)) {
                stream
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".class"))
                        .forEach(file -> {
                            String className = root.relativize(file)
                                    .toString()
                                    .replace('\\', '.')
                                    .replace('/', '.')
                                    .replaceAll("\\.class$", "");

                            classes.put(className, () -> {
                                try {
                                    return getClass(className);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return classes;
    }
}