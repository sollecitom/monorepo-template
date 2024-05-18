# Monorepo Template

A template repository for monorepo Kotlin projects.

## License

This project is licensed under the terms of the MIT license. Check the `LICENSE.md` file for more details.

## How to

### Build the project

```bash
./gradlew build

```

### Build the whole projects, including all submodule specific tasks e.g. container-based service tests

### Upgrade Gradle (example version)

```bash
./gradlew wrapper --gradle-version 8.2.1 --distribution-type all

```

### Update all dependencies if more recent versions exist, and remove unused ones (it will update `gradle/libs.versions.toml`)

```bash
./gradlew versionCatalogUpdate

```