/*
 * BetterReports - build.gradle.kts
 *
 * Copyright (c) 2022 AusTech Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("org.ajoberstar.grgit") version "4.1.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.freefair.lombok") version "6.0.0-m2"
    id("net.kyori.indra") version "2.0.4" apply false
    id("net.kyori.indra.git") version "2.0.4"
}

group = "dev.austech"
version = "2.0.2-SNAPSHOT"

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("org.jetbrains:annotations:23.0.0")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}


fun getCommitsSinceLastTag(): Int {
    if (indraGit.git() == null || !indraGit.isPresent || indraGit.tags().isEmpty()) {
        return -1
    }

    var tags = indraGit.tags()
    var depth = 0
    val walk = org.eclipse.jgit.revwalk.RevWalk(indraGit.git()!!.repository)
    var commit = walk.parseCommit(indraGit.commit())

    while (true) {
        for (tag in tags) {
            if (walk.parseCommit(tag.getLeaf().getObjectId()) == commit) {
                walk.dispose()
                return depth
            }
        }
        depth++
        commit = walk.parseCommit(commit.getParents()[0])
    }
}

ext {
    val GIT_COMMIT = if (!indraGit.isPresent()) "unknown" else indraGit.commit()?.abbreviate(7)?.name() ?: "unknown"
    val GIT_DEPTH = getCommitsSinceLastTag()

    val fullVersion = "$version".replace("-SNAPSHOT", "-dev+${GIT_DEPTH}-${GIT_COMMIT}")

    set("fullVersion", fullVersion)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    expand("version" to project.ext["fullVersion"])
    outputs.upToDateWhen { false }
}

task<Copy>("copyJars") {
    from(tasks.findByPath("shadowJar"))
    rename(".*-all.jar", project.name + "-" + project.ext["fullVersion"] + ".jar")
    into("jars")
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.austech.betterreports.metrics")
}

fun register(name: String, path: String) {
    tasks.register<Copy>(name) {
        dependsOn(tasks.named("build"))
        from("jars", project.name + "-" + project.ext["fullVersion"] + ".jar")
        into(path)
    }
}

register("mnewt00", "C:\\Users\\mnewt\\Desktop\\Code\\Server\\1.19\\plugins")

task("cleanJars") {
    delete("jars")
}

tasks.named("clean") {
    dependsOn("cleanJars")
}

tasks.named("build") {
    dependsOn("shadowJar")
    dependsOn("copyJars")
}