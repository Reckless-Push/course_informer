package edu.umass.models

import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UuidSerializer : KSerializer<UUID?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: UUID?,
    ) {
        value?.let { encoder.encodeString(value.toString()) } ?: encoder.encodeString("")
    }

    override fun deserialize(decoder: Decoder): UUID? {
        val string = decoder.decodeString()
        return if (string.isEmpty()) {
            null
        } else {
            UUID.fromString(string)
        }
    }
}
