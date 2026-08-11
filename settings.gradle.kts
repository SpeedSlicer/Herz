rootProject.name = "Herz"

include("src:test:test-version:test-inner")
include("src:test:test-version")

includeBuild("src/eag/1_8_8/eag-1_8")
includeBuild("src/eag/1_8_8")

include("mixins")
