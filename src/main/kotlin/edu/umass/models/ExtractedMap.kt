package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * @property semesterPdf The semester that the PDF was extracted from.
 * @property courses The courses extracted from the PDF.
 */
@Serializable
data class ExtractedMap(
    val semesterPdf: Semester,
    val courses: List<ExtractedCourse>,
)
