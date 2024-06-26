import com.replaymod.gradle.preprocess.Node
import com.replaymod.gradle.preprocess.RootPreprocessExtension

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.shadow) apply false
    alias(egt.plugins.multiversionRoot)
}

preprocess {
    val fabric12001 = createNodeAndRegisterTask(this, "fabric", "1.20.1", 12001, "yarn")
    val forge12001 = createNodeAndRegisterTask(this, "forge", "1.20.1", 12001, "yarn")
    //val forge11902 = createNode("1.19.2-forge", 11902, "yarn")
    //val fabric11902 = createNode("1.19.2-fabric", 11902, "yarn")
    //val fabric11802 = createNode("1.18.2-fabric", 11802, "yarn")
    //val forge11802 = createNode("1.18.2-forge", 11802, "srg")

    fabric12001.link(forge12001)
    //forge12001.link(forge11902)
    //forge11902.link(forge11802)
}


fun createNodeAndRegisterTask(ths: RootPreprocessExtension, platform: String, mcVersion: String, versionNum: Int, mappings: String): Node {
    tasks.register<UpdateMainProject>("switch-to-$platform-$mcVersion", "$mcVersion-$platform").configure {
        group = "build version switcher"
    }

    return ths.createNode("$mcVersion-$platform", versionNum, mappings)
}

// Helper task to easily switch mainProject to each supported version (you will need to Gradle Sync afterwards)
abstract class UpdateMainProject @Inject constructor(private val newVersionString: String) : DefaultTask() {
    @TaskAction
    fun update() {
        println("Updating mainProject and reloading Manifold preprocessor directives...")
        File("./versions/mainProject").writeText(newVersionString)
    }
}

































