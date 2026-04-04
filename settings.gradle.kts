pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BrokenTelephone"
include(":app")
include(":data")
include(":nav_api")
include(":features:welcome_api")
include(":features:sign_up_api")
include(":features:sign_in")
include(":features:choose_avatar")
include(":features:choose_username")
include(":features:dashboard")
include(":features:bottom_nav_bar")
include(":essentials")
include(":domain")
include(":features:welcome")
include(":features:sign_up")
include(":features:forgot_password")
include(":features:edit_bio")
include(":features:edit_email")
include(":features:user_details")
include(":features:friends")
include(":features:user_friends")
include(":features:add_friend")
include(":features:notifications")
include(":core")
include(":network")
include(":features:sign_in_api")
include(":features:forgot_password_api")
include(":features:choose_avatar_api")
include(":features:choose_username_api")
include(":features:dashboard_api")
include(":features:profile")
include(":features:post_details")
include(":features:profile_api")
include(":features:post_details_api")
include(":features:draw")
include(":features:draw_api")
