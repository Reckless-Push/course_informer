package edu.umass.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SemesterSeasonSerializer : KSerializer<SemesterSeason> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SemesterSeason", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: SemesterSeason,
    ) {
        encoder.encodeString(value.season)
    }

    override fun deserialize(decoder: Decoder): SemesterSeason {
        val season = decoder.decodeString()
        return SemesterSeason.fromString(season) ?: throw IllegalArgumentException("Invalid season")
    }
}
