job("Publish ExternalAnnotations") {
    container(displayName = "Build", image = "mcr.microsoft.com/dotnet/sdk:6.0") {
        env["DOTNET_SKIP_FIRST_TIME_EXPERIENCE"] = "1"
        env["DOTNET_NOLOGO"] = "true"

        env["API_URL"] = Params("space.nuget.v2Url")
        env["API_TOKEN"] = Secrets("space.nuget.token")

        shellScript {
            interpreter = "bash"
            content = """
                set -e
                dotnet pack --output nuget
                find nuget -name '*.nupkg' | xargs -I file dotnet nuget push file --api-key "${'$'}API_TOKEN" --source "${'$'}API_URL"
            """.trimIndent()
        }
    }
}
