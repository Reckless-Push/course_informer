"use client";
import styles from "@/app/components/css/coursedesc.module.css";
import { Course } from "@/types/course";

function CourseDesc(data: Course) {
  console.log(data);

  return (
    <div className={styles.content}>
      {/* <button >Like</button>
                <button >Back</button> */}
      <div className={styles.CourseName}>
        COMPSCI {data.cicsId} {data.name}
      </div>
      <div className={styles.Credits}>Credits: {data.credits}</div>
      <div className={styles.Terms}>
        Terms Offered:
        {data.semestersOffered.map((x) => (
          <div key={x.season} className={styles.Term}>
            {x.season} {x.year}
          </div>
        ))}
        {/* <div className={styles.PrevTerm}>Previous Terms offered: Fall 2023, ...</div> */}
      </div>
      <div className={styles.ProfTotal}>
        <div className={styles.Prof}>
          INSTRUCTOR(S):
          {data.professors.map((x) => (
            <div key={x.id}>
              {x.firstName} {x.lastName},{" "}
            </div>
          ))}
        </div>
        {/* <div className={styles.PrevProf}>Previous Instructors: Heather Conboy ...</div> */}
      </div>
      <div className={styles.PreReqs}>
        Undergraduate Prerequisites:
        {data.undergraduateRequirement &&
          data.undergraduateRequirement.map((x) => (
            <div key={x.cicsId}>COMPSCI{x.cicsId}, </div>
          ))}
      </div>
      <div className={styles.Desc}>
        Description:
        <br />
        {data.description}{" "}
      </div>
    </div>
  );
}

export default CourseDesc;
