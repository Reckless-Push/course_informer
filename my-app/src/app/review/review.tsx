import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  MenuItem,
  FormLabel,
  Radio,
  RadioGroup,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
} from "@mui/material";
import usePostData from "@/app/hooks/usePostData";
import useFetchData from "@/app/hooks/useFetchData";
import { Course, CourseResponse } from "@/types/course";
import { Professor, ProfessorResponse } from "@/types/professor";
import { Review } from "@/types/review";

import styles from "@/app/review/review.module.css";
import { ComponentStates } from "@/types/ComponentStates";

interface ResponseFormProps {
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
    user: boolean;
  };
  courseData: Course;
}

function handleClick({
  onToggleComponent,
  onHome,
  componentStates,
  courseData,
}: ResponseFormProps) {
  onHome();
  onToggleComponent("reviews");
}

const ResponseForm = ({
  onToggleComponent,
  onHome,
  componentStates,
  courseData,
}: ResponseFormProps) => {
  const initialReviewState: Review = {
    id: 0,
    professor: null,
    course: null,
    userId: null,
    date: null,
    difficulty: 0,
    quality: 0,
    comment: "",
    fromRmp: false,
    forCredit: false,
    attendance: false,
    textbook: false,
    grade: "C",
  };

  const [review, setReview] = useState<Review>(initialReviewState);
  const [selectedCourse, setSelectedCourse] = useState<string>("");
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

  const handleCourseChange = (event: SelectChangeEvent) => {
    setSelectedCourse(event.target.value as string);
  };

  const handleProfessorChange = (event: SelectChangeEvent) => {
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

    const selectedCourseObject = courseData;

    setReview({
      ...review,
      professor: selectedProfessorObject ? selectedProfessorObject : null,
      course: selectedCourseObject ? selectedCourseObject : null,
    });
    console.log(review); // Log the review object
    setIsSubmitClicked(true);
  };
  const grade = ["A", "A-", "B", "B-"];
  return (
    <div>
      {/* <Navbar></Navbar> */}
      <div className={styles.main}>
        <div className={styles.CourseName}>
          COMPSCI 520 Thry + Practice; Software Engin
        </div>
        <Box
          component="form"
          className={styles.FormContainer}
          onSubmit={handleSubmit}
        >
          <div className={styles.SelectProfessor}>
            <label className={styles.Text}>Select Professor</label>
            <Select
              className={styles.prof}
              value={selectedProfessor}
              onChange={handleProfessorChange}
              id="Professor"
            >
              {professorsData?.professor_table.map((professor: Professor) => (
                <MenuItem key={professor.id} value={professor.firstName}>
                  {professor.firstName} {professor.lastName}
                </MenuItem>
              ))}
            </Select>

            {/* <Select
              className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
              value={selectedCourse}
              onChange={handleCourseChange}
            >
              {coursesData?.course_table.map((course: Course) => (
                <MenuItem key={course.cicsId} value={course.name}>
                  {course.name}
                </MenuItem>
              ))}
            </Select> */}
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
              id="myRange"
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
              id="myRange"
            ></input>
          </div>

          {/* <div className={styles.SelectProfessor}>
                <label className={styles.Text}>Was attendance mandatory?</label>
                <label className={styles.retake}>
                  <input
                    type="radio"
                    id={styles.RetakeCourseRadio}
                    value="Yes"
                    name="attendance"
                  ></input>
                  <span className={styles.checkmark}></span>Yes
                </label>
                <label className={styles.retake}>
                  <input
                    type="radio"
                    id={styles.RetakeCourseRadio}
                    value="No"
                    name="attendance"
                  ></input>
                  <span className={styles.checkmark}></span> No
                </label>
              </div> */}
          <div className={styles.SelectProfessor}>
            <label className={styles.Text}>Select grade</label>
            {/* <input list="browsers" name="browser" /> */}
            <Select className={styles.grades} value={selectedCourse} id="grade">
              {grade.map((value, index) => (
                <MenuItem key={index} value={value}>
                  {value}
                </MenuItem>
              ))}
            </Select>
          </div>
          <div className={styles.addComments}>
            <label className={styles.Text}>Additional comments</label>
            <TextField
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
            <FormControlLabel
              control={
                <Checkbox
                  checked={review.forCredit}
                  onChange={(e) =>
                    setReview({ ...review, forCredit: e.target.checked })
                  }
                  name="forCredit"
                />
              }
              label="For Credit"
            />

            <FormControlLabel
              control={
                <Checkbox
                  checked={review.attendance}
                  onChange={(e) =>
                    setReview({ ...review, attendance: e.target.checked })
                  }
                  name="attendance"
                />
              }
              label="Attendance Required"
            />

            <FormControlLabel
              control={
                <Checkbox
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

          <TextField
            className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
            name="quality"
            label="Quality"
            type="number"
            value={review.quality}
            onChange={handleChange}
            fullWidth
            margin="normal"
            id="quality"
            autoComplete="3"
            required
          />
          <TextField
            className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
            name="difficulty"
            label="Difficulty"
            type="number"
            value={review.difficulty}
            onChange={handleChange}
            fullWidth
            margin="normal"
            id="difficulty"
            autoComplete="5"
            required
          />
          <div className={styles.RateThisCourse}>
            <Button
              type="submit"
              variant="contained"
              // onClick={() =>
              //   handleClick({
              //     onToggleComponent,
              //     onHome,
              //     componentStates,
              //     courseData,
              //   })
              // }
              className={styles.RateThisCourseBtn}
            >
              Rate this Course
            </Button>
          </div>

          {loading && <Typography>Loading...</Typography>}
          {error && (
            <Typography color="error">Error: {error.message}</Typography>
          )}
          {data && <Typography>Success!</Typography>}
        </Box>
      </div>
    </div>
  );
};
export default ResponseForm;
