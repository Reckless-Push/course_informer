import React, { useEffect, useState } from "react";
import * as material from "@mui/material";
import usePostData from "@/app/hooks/usePostData";
import useFetchData from "@/app/hooks/useFetchData";
import { Course, CourseResponse } from "@/types/course";
import { Professor, ProfessorResponse } from "@/types/professor";
import { Review } from "@/types/review";

import styles from "@/app/review/review.module.css";

const ResponseForm = (course_data: Course) => {
  const initialReviewState: Review = {
    id: 0,
    professor: null,
    course: null,
    userId: null,
    date: null,
    difficulty: 5,
    quality: 5,
    comment: "",
    fromRmp: false,
    forCredit: false,
    attendance: false,
    textbook: false,
    grade: "A",
  };

  const [review, setReview] = useState<Review>(initialReviewState);
  const [selectedProfessor, setSelectedProfessor] = useState<string>("");
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);
  const { data, loading, error } = usePostData<Review, Review>(
    "https://localhost:8443/review",
    review,
    isSubmitClicked
  );
  const { data: coursesData } = useFetchData<CourseResponse>(
    "https://localhost:8443/course"
  );
  const { data: professorsData } = useFetchData<ProfessorResponse>(
    "https://localhost:8443/professor"
  );

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setReview({ ...review, [e.target.name]: e.target.value });
  };

  const handleProfessorChange = (event: material.SelectChangeEvent) => {
    setSelectedProfessor(event.target.value as string);
  };

  useEffect(() => {
    if (data) {
      setIsSubmitClicked(false);
    }
  }, [data]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const selectedProfessorObject = professorsData?.professor_table.find(
      (prof) => prof.firstName === selectedProfessor
    );

    setReview({
      ...review,
      professor: selectedProfessorObject ? selectedProfessorObject : null,
      course: course_data ? course_data : null,
    });
    console.log(review); // Log the review object
    setIsSubmitClicked(true);
  };
  const grade = ["A", "A-", "B", "B-", "C+", "C", "C-", "D+", "D", "F"];
  return (
    <div>
      {/* <Navbar></Navbar> */}
      <div className={styles.main}>
        <div className={styles.content}>
          <div className={styles.CourseName}>
            COMPSCI {course_data.cicsId} {course_data.name}
          </div>
          <material.Box
            component="form"
            className={styles.FormContainer}
            onSubmit={handleSubmit}
          >
            <div className={styles.SelectProfessor}>
              <label className={styles.Text}>Select Professor</label>
              <material.Select
                className={styles.prof}
                value={selectedProfessor}
                onChange={handleProfessorChange}
                id="Professor"
              >
                {professorsData?.professor_table.map((professor: Professor) => (
                  <material.MenuItem
                    key={professor.id}
                    value={professor.firstName}
                  >
                    {professor.firstName} {professor.lastName}
                  </material.MenuItem>
                ))}
              </material.Select>
            </div>
            <div className={styles.SelectProfessor}>
              <label className={styles.Text}>Rate your Professor</label>
              <input
                type="range"
                min="1"
                max="5"
                step="1"
                defaultValue="4"
                className={styles.slider}
                id="quality"
                onChange={handleChange}
                required
                name="quality"
                value={review.quality}
              ></input>
            </div>
            <div className={styles.SelectProfessor}>
              <label className={styles.Text}>
                How difficult was this Professor
              </label>
              <input
                type="range"
                min="1"
                max="5"
                step="1"
                defaultValue="4"
                className={styles.slider}
                id="difficulty"
                onChange={handleChange}
                required
                name="difficulty"
                value={review.difficulty}
              ></input>
            </div>

            <div className={styles.SelectProfessor}>
              <label className={styles.Text}>Select grade</label>
              <material.Select
                className={styles.grades}
                value={review.grade}
                onChange={(e) =>
                  setReview({ ...review, grade: e.target.value })
                }
                id="grade"
              >
                {grade.map((value, index) => (
                  <material.MenuItem key={index} value={value}>
                    {value}
                  </material.MenuItem>
                ))}
              </material.Select>
            </div>
            <div className={styles.addComments}>
              <label className={styles.Text}>Additional comments</label>
              <material.TextField
                className={styles.addCommentstext}
                name="comment"
                type="text"
                value={review.comment}
                onChange={handleChange}
                id="comment"
                autoComplete="This course is great!"
                required
                fullWidth
              />
            </div>
            <div className={styles.SelectProfessor}>
              <material.FormControlLabel
                control={
                  <material.Checkbox
                    checked={review.forCredit}
                    onChange={(e) =>
                      setReview({ ...review, forCredit: e.target.checked })
                    }
                    name="forCredit"
                  />
                }
                label="For Credit"
              />

              <material.FormControlLabel
                control={
                  <material.Checkbox
                    checked={review.attendance}
                    onChange={(e) =>
                      setReview({ ...review, attendance: e.target.checked })
                    }
                    name="attendance"
                  />
                }
                label="Attendance Required"
              />

              <material.FormControlLabel
                control={
                  <material.Checkbox
                    checked={review.textbook}
                    onChange={(e) =>
                      setReview({ ...review, textbook: e.target.checked })
                    }
                    name="textbook"
                  />
                }
                label="Textbook Required"
              />
            </div>

            <div className={styles.RateThisCourse}>
              <material.Button
                type="submit"
                variant="contained"
                className={styles.RateThisCourseBtn}
              >
                Rate this Course
              </material.Button>
            </div>

            {loading && <material.Typography>Loading...</material.Typography>}
            {error && (
              <material.Typography color="error">
                Error: {error.message}
              </material.Typography>
            )}
            {data && <material.Typography>Success!</material.Typography>}
          </material.Box>
        </div>
      </div>
    </div>
  );
};
export default ResponseForm;
