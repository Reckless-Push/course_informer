package edu.umass.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LetterGradeSerializer : KSerializer<LetterGrade> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LetterGrade", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: LetterGrade,
    ) {
        encoder.encodeString(value.grade)
    }

    override fun deserialize(decoder: Decoder): LetterGrade {
        val grade = decoder.decodeString()
        return LetterGrade.fromString(grade) ?: throw IllegalArgumentException("Invalid grade")
    }
}
