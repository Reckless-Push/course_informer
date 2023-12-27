"""
This script extracts the course details from a PDF file using regular expressions.
The PDF file is a course catalog from the UMass Amherst Computer Science department.
The script extracts the course ID, name, description, credits, instructors, and prerequisites.
The extracted details are stored as a list of dictionaries.
"""

import argparse
import json
import re

import fitz  # PyMuPDF


class Course:
    """
    This class represents a course.
    """

    def __init__(self, cics_id, department, name, description, course_credits, instructors, prerequisites):
        self.cics_id = cics_id
        self.department = department
        self.name = name
        self.description = description
        self.credits = course_credits
        self.instructors = instructors
        self.prerequisites = prerequisites

    def __repr__(self):
        return {
            "cicsId": self.cics_id,
            "department": self.department,
            "name": self.name,
            "description": self.description,
            "credits": self.credits,
            "instructors": self.instructors,
            "prerequisites": self.prerequisites,
        }


class Semester:
    """
    This class represents a semester.
    :param semester: The semester
    :param year: The year
    :return: None
    """

    def __init__(self, semester, year):
        self.season = semester
        self.year = year

    def to_dict(self):
        """
        This function converts the semester to a dictionary.
        :return: The semester as a dictionary
        """
        return {
            'season': self.season,
            'year': self.year
        }


class PDFExtractor:
    """
    This class extracts text from a PDF file and processes it.
    :param file_path: The path of the PDF file
    :return: None
    """

    def __init__(self, file_path):
        self.file_path = file_path
        self.extracted_text = ""
        self.semester = None

    def extract_text(self):
        """
        This function extracts text from a PDF file.
        :return: None
        """
        pdf_document = fitz.open(self.file_path)
        for page_num in range(len(pdf_document)):
            page = pdf_document.load_page(page_num)
            self.extracted_text += page.get_text()
        pdf_document.close()

    def process_text(self):
        """
        This function processes the extracted text.
        :return: The processed text
        """
        header_pattern = r"^Course Descriptions\n(\d{4}) (Spring|Summer|Winter|Fall)\n.*?UMassAmherst\n"
        single_page_data_pattern = (
            r"page \d{1,2}( ---)?\n---\n\d{4} (Spring|Summer|Winter|Fall)( ---)?\n\d{1,2}/\d{1,2}/\d{4}\n"
        )
        double_page_data_pattern = (
            r"page \d{1,2}( ---)?\n\d{4} (Spring|Summer|Winter|Fall)( ---)?\n\d{1,2}/\d{1,2}/\d{4}\n"
        )
        header_match = re.search(header_pattern, self.extracted_text, flags=re.DOTALL)
        self.semester = Semester(header_match.group(2), header_match.group(1))
        text_without_header = re.sub(header_pattern, "", self.extracted_text, flags=re.DOTALL)
        processed_text = re.sub(single_page_data_pattern, "", text_without_header)
        processed_text = re.sub(double_page_data_pattern, "", processed_text)
        return processed_text


class CourseExtractor:
    """
    This class extracts the course details from the processed text.
    :param text: The processed text
    :return: None
    """

    def __init__(self, text):
        self.text = text

    def extract_course_blocks(self):
        """
        This function extracts the course blocks from the processed text.
        :return: The course blocks
        """
        course_block_pattern = r"(.*?)(?=\nINSTRUCTOR\(S\):\s.*\n(CICS|COMPSCI|INFO) \d{3}  |\Z)"
        course_blocks = re.findall(course_block_pattern, self.text, re.DOTALL)
        cleaned_courses = [course[0].strip() for course in course_blocks if course[0].strip()]
        second_pass = [
            cleaned_courses[i - 1]
            + "\n"
            + cleaned_courses[i].split("\n")[0]
            + "\n"
            + cleaned_courses[i].split("\n")[1]
            + "\n"
            for i in range(1, len(cleaned_courses))
        ]
        last_course = cleaned_courses[-1] + "\n"
        second_pass.append(last_course)

        third_pass = [
            " ".join(course.split("\n")[2:-3]) + "\n" + course.split("\n")[-3] + "\n" + course.split("\n")[-2]
            for course in second_pass[1:]
        ]
        third_pass.insert(
            0,
            " ".join(second_pass[0].split("\n")[:-3])
            + "\n"
            + second_pass[0].split("\n")[-3]
            + "\n"
            + second_pass[0].split("\n")[-2],
            )
        return third_pass

    @staticmethod
    def extract_course_details(course_text):
        """
        This function extracts the course details from the course text.
        :param course_text: The text for a single course
        :return: The course details
        """
        # Extract course ID and name
        course_pattern = r"\n(CICS|COMPSCI|INFO) (H?\d{3}[A-Za-z]?[A-Za-z]?)\s+(.*)"
        matches = re.findall(course_pattern, course_text)

        if matches:
            last_match = matches[-1]
            cics_id = last_match[1]
            name = last_match[2]
            department = last_match[0]
        else:
            cics_id = None
            name = None
            department = None

        # Extract instructors
        instructors_pattern = r"\nINSTRUCTOR\(S\):\s(.*?)(?=(CICS|COMPSCI|INFO) H?\d{3})"
        instructors_match = re.search(instructors_pattern, course_text, re.DOTALL)
        if not instructors_match:
            instructors_pattern = r"\nINSTRUCTOR\(S\):\s(.*?)\n"
            instructors_match = re.search(instructors_pattern, course_text, re.DOTALL)
        instructors = (
            [instructor.strip() for instructor in instructors_match.group(1).split(",")]
            if instructors_match
            else []
        )

        # Extract course_credits
        credits_pattern = r"(\d+)\scredits?\.?"
        credits_match = re.search(credits_pattern, course_text)
        course_credits = int(credits_match.group(1)) if credits_match else None

        # Extract prerequisites
        prerequisites_pattern = r"Prerequisite[s]?:\s(.*?)(?=(\d+)\scredits?\.?)"
        prerequisites_match = re.search(prerequisites_pattern, course_text)
        prerequisites = prerequisites_match.group(1).strip() if prerequisites_match else "None"

        # Extract description
        description_pattern = (
            r"^(.*?)(?=((\d+)\scredits?\.?))"
            if prerequisites == "None"
            else r"^(.*?)(?=(Prerequisite[s]?:\s.*))"
        )
        description_match = re.search(description_pattern, course_text, re.DOTALL)
        description = description_match.group(0).strip() if description_match else ""

        return Course(
            cics_id, department, name, description, course_credits, instructors, prerequisites
        ).__repr__()


def main():
    """
    This function is the entry point for the script.
    """
    parser = argparse.ArgumentParser(description="Extract course details from a PDF file.")
    parser.add_argument("pdf_file_path", type=str, help="The path of the PDF file")
    parser.add_argument("output_file_path", type=str, help="The path of the output JSON file")

    args = parser.parse_args()

    pdf_file_path = args.pdf_file_path
    json_file_path = args.output_file_path

    pdf_extractor = PDFExtractor(pdf_file_path)
    pdf_extractor.extract_text()
    processed_text = pdf_extractor.process_text()

    course_extractor = CourseExtractor(processed_text)
    cleaned_courses = course_extractor.extract_course_blocks()
    extracted_courses_final = [course_extractor.extract_course_details(course) for course in cleaned_courses]
    extracted_courses_final = {"semesterPdf": pdf_extractor.semester.to_dict(), "courses": extracted_courses_final}

    with open(json_file_path, "w") as json_file:
        json.dump(extracted_courses_final, json_file)

    print(f"Extracted courses have been saved to {json_file_path}")


if __name__ == "__main__":
    main()
