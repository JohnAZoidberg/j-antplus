load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "java_deps",
    exports = [
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:javax_usb_usb_api",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_reactivestreams_reactive_streams",
        "@maven//:org_usb4java_usb4java",
        "@maven//:org_usb4java_usb4java_javax",
        "@maven//:ch_qos_logback_logback_core",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_apache_commons_commons_lang3",
    ],
)

java_library(
    name = "testing_deps",
    exports = [
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "tests",
    srcs = glob(
        ["src/test/java/**/*.java"],
        # Exclude tests that are supposed to be run interactively with hardware
        exclude = ["src/test/java/**/anttest/**"]
    ),
    test_class = "AllTests",
    deps = [
        "//:testing_deps",
        "//:j_antplus",
    ],
)

java_library(
    visibility = ["//visibility:public"],
    name = "j_antplus",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    deps = ["//:java_deps"],
)
