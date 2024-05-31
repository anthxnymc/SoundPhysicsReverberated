import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import gg.essential.gradle.util.noServerRunConfigs

plugins {
    alias(libs.plugins.kotlin)
    id(egt.plugins.multiversion.get().pluginId)
    id(egt.plugins.defaults.get().pluginId)
    alias(libs.plugins.shadow)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.cursegradle)
    id("systems.manifold.manifold-gradle-plugin") version "0.0.2-alpha"
}

val mcVersions = listOf("1.18.2", "1.19.2", "1.20.1")

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project
version = mod_version
group = "toni"

manifold { manifoldVersion = "2024.1.3" }

base {
    archivesName.set("$mod_name (${getMcVersionStr()}-${platform.loaderStr})")
}

loom.noServerRunConfigs()
loom {
    if (project.platform.isLegacyForge) runConfigs {
        "client" { programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker") }
    }
    if (project.platform.isForge) forge {
        mixinConfig("mixins.${mod_id}.json")
        accessTransformer("../../src/main/resources/META-INF/accesstransformer.cfg")
    }

    if (project.platform.isFabric) {
        accessWidenerPath = file("../../src/main/resources/${mod_id}.accesswidener")
    }

    mixin.defaultRefmapName.set("mixins.${mod_id}.refmap.json")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://repo.essential.gg/repository/maven-public/")
    maven("https://maven.dediamondpro.dev/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://api.modrinth.com/maven")
    maven("https://cursemaven.com")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.maxhenkel.de/repository/public")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
}

// Creates the shade/shadow configuration, so we can include libraries inside our mod, rather than having to add them separately.
val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // apply the Manifold processor, do not remove this
    annotationProcessor("systems.manifold:manifold-preprocessor:${manifold.manifoldVersion.get()}")

    // sodium fabric/forge
    if (platform.isFabric) {
        when (project.platform.mcVersionStr) {
            "1.20.1" -> "mc1.20.1-0.5.8"
            "1.19.2" -> "mc1.19.2-0.4.4"
            "1.18.2" -> "mc1.18.2-0.4.1"
            else -> error("Unknown Sodium Version!")
        }.let {
            "modImplementation"("maven.modrinth:sodium:$it")
        }
    }
    else {
        when (project.platform.mcVersionStr) {
            "1.20.1" -> "5142143"
            "1.19.2" -> "5130559"
            "1.18.2" -> "5130561"
            else -> error("Unknown Embeddium Version!")
        }.let {
            "modImplementation"("curse.maven:embeddium-908741:$it")
        }
    }

    // cloth config
    when (project.platform.mcVersionStr) {
        "1.20.1" -> "11.0.99"
        "1.19.2" -> "8.3.115"
        "1.18.2" -> "6.5.116"
        else -> error("Unknown Cloth Config Version!")
    }.let {

        if (platform.isFabric) {
            "modImplementation"("me.shedaniel.cloth:cloth-config-fabric:$it")
        }
        else {
            "modImplementation"("me.shedaniel.cloth:cloth-config-forge:$it")
        }
    }

    implementation("de.maxhenkel.configbuilder:configbuilder:2.0.1")
    shade("de.maxhenkel.configbuilder:configbuilder:2.0.1")
}

// This task enables templating expansion for mods.toml and fabric.mod.json
tasks.processResources {
    inputs.property("id", mod_id)
    inputs.property("name", mod_name)
    inputs.property("java", 17)
    inputs.property("java_level", "JAVA_17")
    inputs.property("version", mod_version)
    inputs.property("mcVersionStr", project.platform.mcVersionStr)
    // if you want to add more files to the expansion, you must add them to this list
    filesMatching(listOf("mcmod.info", "mods.toml", "fabric.mod.json")) {
        expand(
            mapOf(
                "id" to mod_id,
                "name" to mod_name,
                "java" to 17,
                "java_level" to "JAVA_17",
                "version" to mod_version,
                "mcVersionStr" to getMcVersionStr()
            )
        )
    }
}


tasks {
    // modify the JavaCompile task and inject our auto-generated Manifold symbols
    withType<JavaCompile> {
        if(!this.name.startsWith("_")) { // check the name, so we don't inject into Forge internal compilation
            options.compilerArgs.add("-Xplugin:Manifold")
            setupManifoldPreprocessors(mcVersions, options.compilerArgs, platform.isFabric, projectDir, platform.mcVersionStr)
        }
    }

    withType<Jar> {
        if (project.platform.isFabric) {
            exclude("mcmod.info", "mods.toml", "pack.mcmeta")
        } else {
            exclude("fabric.mod.json")
            if (project.platform.isLegacyForge) {
                exclude("mods.toml")
            } else {
                exclude("mcmod.info")
            }
        }
        from(rootProject.file("LICENSE"))
        from(rootProject.file("LICENSE.LESSER"))
    }

    // define the shadow task
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
        finalizedBy("copyJar")
    }

    jar {
        if (project.platform.isLegacyForge) {
            manifest {
                attributes(
                    mapOf(
                        "ModSide" to "CLIENT",
                        "TweakOrder" to "0",
                        "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                        "ForceLoadAsMod" to true
                    )
                )
            }
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }
    register<Copy>("copyJar") {
        File("${project.rootDir}/jars").mkdir()
        from(remapJar.get().archiveFile)
        into("${project.rootDir}/jars")
    }

    clean { delete("${project.rootDir}/jars") }

    project.modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set("8KWb3iU0")
        versionNumber.set(mod_version)
        versionName.set("[${getMcVersionStr()}-${platform.loaderStr}] PridefulAnimals $mod_version")
        uploadFile.set(remapJar.get().archiveFile as Any)
        gameVersions.addAll(getAdditionalSupportedVersions())
        if (platform.isFabric) {
            loaders.add("fabric")
            loaders.add("quilt")
        } else if (platform.isForge) {
            loaders.add("forge")
            if (platform.mcMinor >= 20) loaders.add("neoforge")
        }
        changelog.set(file("../../changelog.md").readText())
        dependencies {
            if (platform.isFabric) required.project("fabric-api")
            required.project("yacl")
        }
    }
    project.curseforge {
        project(closureOf<CurseProject> {
            apiKey = System.getenv("CURSEFORGE_TOKEN")
            id = "973648"
            changelog = file("../../changelog.md")
            changelogType = "markdown"
            relations(closureOf<CurseRelation> {
                if (platform.isFabric) requiredDependency("fabric-api")
                requiredDependency("yacl")
            })
            gameVersionStrings.addAll(getAdditionalSupportedVersions())
            if (platform.isFabric) {
                addGameVersion("Fabric")
                addGameVersion("Quilt")
            } else if (platform.isForge) {
                addGameVersion("Forge")
                if (platform.mcMinor >= 20) addGameVersion("NeoForge")
            }
            releaseType = "release"
            mainArtifact(remapJar.get().archiveFile, closureOf<CurseArtifact> {
                displayName = "[${getMcVersionStr()}-${platform.loaderStr}] PridefulAnimals $mod_version"
            })
        })
        options(closureOf<Options> {
            javaVersionAutoDetect = false
            javaIntegration = false
            forgeGradleIntegration = false
        })
    }

    register("publish") {
        dependsOn(modrinth)
        dependsOn(curseforge)
    }
}

fun getMcVersionStr(): String {
    return when (project.platform.mcVersionStr) {
        else -> {
            val dots = project.platform.mcVersionStr.count { it == '.' }
            if (dots == 1) "${project.platform.mcVersionStr}.x"
            else "${project.platform.mcVersionStr.substringBeforeLast(".")}.x"
        }
    }
}

// allows adding multiple versions to the Curseforge/Modrinth release, besides the one compiled for
fun getAdditionalSupportedVersions(): List<String> {
    return when (project.platform.mcVersionStr) {
        "1.20.1" -> listOf("1.20", "1.20.1")
        else -> listOf(project.platform.mcVersionStr)
    }
}


/**
 * This function essentially takes an ordered list of Minecraft version strings, iterates through it,
 * and generates a build.properties file for each version folder that will be picked up by the Manifold
 * compiler plugin, replacing Essential's preprocessor. This has a number of advantages, including inline
 * preprocessor directives, easier editing, and the most important part--not having to mutate the source files
 * when changing the main project version.
 *
 * For each Minecraft version, five markers are created, using the minor and patch versions of the mcVersionStr:
 * - **BEFORE_20_1** (exclusive, <)
 * - **UPTO_20_1** (inclusive, <=)
 * - **NEWER_THAN_20_1** (exclusive, >)
 * - **AFTER_20_1** (inclusive, >=)
 * - **CURRENT_20_1**, ==, a standalone marker that will single out that version.
 *
 * It also generates **FABRIC** and **FORGE** markers for separating loader-specific code.
 */
fun setupManifoldPreprocessors(mcVers: List<String>, compilerArgs: MutableList<String>?, isFabric: Boolean, parent: File, mcString : String) {
    val mainProject = File(parent, "../../versions/mainProject").readText()
    val mcIndex = mcVers.indexOf(mcString);

    val argList = ArrayList<String>()
    for (i in mcVers.indices) {
        val mcStr = mcVers[i].replace(".", "_").substring(2)

        if (mcIndex < i) argList.add("BEFORE_$mcStr")
        if (mcIndex <= i) argList.add("UPTO_$mcStr")
        if (mcIndex == i) argList.add("CURRENT_$mcStr")
        if (mcIndex > i) argList.add("NEWER_THAN_$mcStr")
        if (mcIndex >= i) argList.add("AFTER_$mcStr")
    }

    argList.add(if (isFabric) "FABRIC" else "FORGE")

    val sb = StringBuilder().append("# DO NOT EDIT - GENERATED BY THE BUILD SCRIPT\n")
    for (arg in argList) {
        compilerArgs?.add("-A$arg")
        sb.append(arg).append("=\n")
    }

    File(parent, "build.properties").writeText(sb.toString())

    // if the project we're currently processing annotations for happens to be the
    // main project, we need to also copy the build.properties to the root folder
    val currentProject = mcString + "-" + (if (isFabric) "fabric" else "forge");
    if (mainProject == currentProject)
        File(parent, "../../src/main/build.properties").writeText(sb.toString())
}