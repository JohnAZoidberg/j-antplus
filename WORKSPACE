load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

rules_kotlin_version = "v1.5.0-beta-4"
rules_kotlin_sha = "6cbd4e5768bdfae1598662e40272729ec9ece8b7bded8f0d2c81c8ff96dc139d"
http_archive(
    name = "io_bazel_rules_kotlin",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % rules_kotlin_version],
    sha256 = rules_kotlin_sha,
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories", "versions")

kotlin_repositories()

load("@io_bazel_rules_kotlin//kotlin:core.bzl", "kt_register_toolchains")

kt_register_toolchains()

http_archive(
    name = "rules_jvm_external",
    sha256 = versions.RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % versions.RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % versions.RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    # TODO: Try to update the versions
    artifacts = [
        "javax.usb:usb-api:1.0.2",
        # slf4j already present above
        "io.projectreactor:reactor-core:3.3.4.RELEASE",
        "org.reactivestreams:reactive-streams:1.0.3",
        "org.usb4java:usb4java:1.3.0",
        "org.usb4java:usb4java-javax:1.3.0",
        "ch.qos.logback:logback-core:1.2.3",
        "ch.qos.logback:logback-classic:1.2.3",
        "junit:junit:4.13", # Only for testing
    ],
    repositories = [
        "https://maven-central.storage.googleapis.com/repos/central/data/",
        "https://repo1.maven.org/maven2",
        "https://jitpack.io",
    ],
)

http_archive(
    name = "rules_pkg",
    sha256 = "4ba8f4ab0ff85f2484287ab06c0d871dcb31cc54d439457d28fd4ae14b18450a",
    url = "https://github.com/bazelbuild/rules_pkg/releases/download/0.2.4/rules_pkg-0.2.4.tar.gz",
)
