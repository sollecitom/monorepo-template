package conventions.task.dependency.update

import com.vdurmont.semver4j.Semver
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface DependencyVersion : Comparable<DependencyVersion> {

    val isStable: Boolean

    companion object
}

class SemverDependencyVersion(private val value: Semver) : DependencyVersion {

    override val isStable: Boolean get() = value.isStable

    override fun compareTo(other: DependencyVersion) = value.compareTo((other as SemverDependencyVersion).value)

    companion object {
        fun fromRawVersion(rawVersion: String) = Semver(rawVersion, Semver.SemverType.LOOSE).let(::SemverDependencyVersion)
    }
}

class DateDependencyVersion(private val releaseDate: LocalDate) : DependencyVersion {

    override val isStable = true

    override fun compareTo(other: DependencyVersion) = releaseDate.compareTo((other as DateDependencyVersion).releaseDate)

    companion object {

        fun fromRawVersion(rawVersion: String) = LocalDate.parse(rawVersion, DateTimeFormatter.BASIC_ISO_DATE).let(::DateDependencyVersion)
    }
}

operator fun DependencyVersion.Companion.invoke(rawVersion: String): DependencyVersion = runCatching { SemverDependencyVersion.fromRawVersion(rawVersion) }.getOrElse { DateDependencyVersion.fromRawVersion(rawVersion) }