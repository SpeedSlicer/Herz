package net.ada.manifest;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public record MixinSourceObj
        (List<String> targetPlatforms,
         boolean required,
         @SerializedName(value = "package", alternate = "mixinPackage") String mixinPackage,
         String compatibilityLevel,
         List<String> mixins,
         Map<String, Integer> injectors, // dummy for Fabric
         String refmap // later feature, dummy for now
         ){
}
