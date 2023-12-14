package edu.umass.models

import org.apache.pdfbox.io.RandomAccessReadBuffer
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.text.PDFTextStripper

/**
 * Extracts text from a PDF file.
 *
 * @property fileBytes The bytes of the PDF file.
 * @constructor Creates a new PdfExtractor.
 */
class PdfExtractor(private val fileBytes: ByteArray) {
    /**
     * Extracts text from a PDF file.
     *
     * @return The text extracted from the PDF file.
     */
    fun extractText(): String =
        PDFTextStripper().getText(PDFParser(RandomAccessReadBuffer(fileBytes)).parse())
}
