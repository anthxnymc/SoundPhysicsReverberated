# Toni's Amazing and All-Powerful Multi-Version Multi-Loader Template Mod

Hello and welcome to the only documentation you're going to get for the 
godforsaken pieces of software used in this template.

This template uses [Essential Gradle Toolkit](https://github.com/EssentialGG/essential-gradle-toolkit), 
and is set up with the following:

- Fabric & Forge in one codebase
- Support for all modern 'LTS' versions, in one codebase (1.18.2, 1.19.2, 1.20.1)
- Mixins for both platforms
- Access Wideners / Transformers
- Cloth Config
- Shadow

In addition to all that, I've also set up Manifold preprocessor directives, which I find much 
easier to use than Essential toolkit's built-in preprocessor.

# Setting Up IntelliJ

You're going to need to download the [**Manifold IntelliJ plugin**](https://plugins.jetbrains.com/plugin/10057-manifold), as it is required for highlighting to 
work on the Manifold preprocessor directives. If you're not using IntelliJ, you're using the wrong editor and are on your own.

In the Gradle tasks menu in IntelliJ, you will want to go to "build setup" -> "init," right click the init task,
and set it to execute after sync. This is the best solution I could come up with to have the Manifold preprocessor
directives automatically update whenever you change mainProject and reload Gradle. Otherwise, you will have broken
intellisense until you build the project again.

You should also be using the Minecraft Dev plugin, like, for everything.

If you use auto import, I would highly recommend disabling Fabric/Forge namespaces from auto importing, since it will
usually need to be put in an #IF block.

You will also probably want to set up Live Templates for the preprocessor directives, as it makes it so much easier with tab-completion. You can just type #if and hit tab, and it will generate the whole expression. I've also added an #ifinline and #ifelse. Make sure you make a new template category and enable it everywhere.

![image](https://github.com/anthxnymc/SoundPhysicsReverberated/assets/67132971/a73da597-a0c3-48bb-9ff4-be010022eed0)

```
#if $expression$
$SELECTION$$END$ 
#endif
```
```
#if $expression$ $SELECTION$$END$ #endif
```
```
#if $expression$
$SELECTION$$END$
#else

#endif
```

# How to Use this Template

I'm going to assume you're familiar with how Gradle works, and are able to use IntelliJ, otherwise this would be very long.
