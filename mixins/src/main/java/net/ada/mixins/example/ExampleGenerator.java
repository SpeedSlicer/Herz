package net.ada.mixins.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ada.manifest.MixinSourceObj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExampleGenerator {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if(Objects.equals(args[0], "--example")) {
                MixinSourceObj mixinSourceObj = new MixinSourceObj(
                        List.of("eagler/1.14"),
                        true,
                        "net.ada.mixins",
                        "JAVA_17",
                        List.of("test.Example"),
                        Map.of("default", 1),
                        "none"
                );
                Path examplePath = Path.of(args[1]);
                Files.createDirectories(examplePath.getParent());
                Files.write(examplePath, gson.toJson(mixinSourceObj).getBytes());
        }
    }
}
