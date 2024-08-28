import React from 'react';
import './CourseCard.css';


function CourseCard({ course }) {

    function convertDaysToString(days) {
        const dayMap = {
            1: 'Su',
            2: 'M',
            4: 'Tu',
            8: 'W',
            16: 'Th',
            32: 'F',
            64: 'Sa'
        };
    
        let result = '';
    
        for (const [bit, day] of Object.entries(dayMap)) {
            if (days & bit) {
                result += day;
            }
        }
        return result;
    }

    function convertTimeToString(startTime, endTime) {
        let start_h = Math.floor(startTime / 60)
        let end_h = Math.floor (endTime / 60)
        let start_m = startTime % 60;
        let end_m = endTime % 60;
        let period = end_h >= 12 ? "pm" : "am"
        if ((start_h) > 12) start_h -= 12;
        if ((end_h) > 12) end_h -= 12;

        return start_h + ":" + start_m.toString().padStart(2, "0") + "-" + end_h + ":" + end_m.toString().padStart(2, "0") + period;
    }

    return (
        <div className="course-card">
            <div className="course-header">
                {/* <span className="course-id">{course.id}</span> */}
                <span className="course-name">{course.department} {course.courseNumber} {course.courseName}</span>
                <span className="course-units">{course.units}</span>
            </div>
            <div className="course-desc-wrapper">
                <span className="course-desc">{course.description}</span>
            </div>
            <div className="sections-table-wrapper">
                <table className="sections-table">
                    <thead>
                        <tr>
                            <th>Section</th>
                            <th>Type</th>
                            <th>Days</th>
                            <th>Time</th>
                            <th>Location</th>
                            <th>Instructor</th>
                        </tr>
                    </thead>
                    <tbody>
                        {course.sections.map((section) => (
                            <tr key={section.id} className={section.isFull ? "section-full" : ""}>
                                <td>{section.section}</td>
                                <td>{section.type}</td>
                                <td>{convertDaysToString(section.days)}</td>
                                <td>{convertTimeToString(section.startTime, section.endTime)}</td>
                                <td>{section.location}</td>
                                <td>{section.instructor}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default CourseCard;