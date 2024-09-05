import React, {useState, useEffect} from "react";
import CourseCard from './CourseCard.js';
import TimeSelector from './TimeSelector.js';
import './ClassSelector.css'
import './CourseCard.css'


function ClassSelector(){
    const [term, setTerm] = useState('');
    const [schools, setSchools] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [selectedSchool, setSelectedSchool] = useState('');
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [filteredDepartments, setFilteredDepartments] = useState([]);
    const [courseCode, setCourseCode] = useState('');
    const [courseNumber, setCourseNumber] = useState('');
    const [courses, setCourses] = useState([]);
    const [searchActive, setSearchActive] = useState(true);
    const [startTime, setStartTime] = useState('6:00am');
    const [endTime, setEndTime] = useState('11:00pm');
    const [days, setDays] = useState(0);

    const BASE_URL = 'http://localhost:8080'

    const daysMapping = {
        Sunday: 1,
        Monday: 2,
        Tuesday: 4,
        Wednesday: 8,
        Thursday: 16,
        Friday: 32,
        Saturday: 64
    }

    useEffect(() => {
        Promise.all([
            fetch(BASE_URL + '/api/schools').then(response => response.json()),
            fetch(BASE_URL + '/api/departments').then(response => response.json())
        ])
        .then(([schoolsData, departmentsData]) => {
            setSchools(schoolsData);
            setDepartments(departmentsData);
        })
        .catch(error => console.error('Error fetching data:', error));
    }, []);

    useEffect(() => {
        if (selectedSchool) {
            const filteredDepartments = departments.filter(dep => Number(dep.schoolId) === Number(selectedSchool))
            setFilteredDepartments(filteredDepartments);
        }
        else {
            setFilteredDepartments(departments);
        }
    }, [selectedSchool, departments]);

    const convertTime = (readableTime) => {
        if (readableTime.length === 0) {
            return -1;
        }
        const colonInd = readableTime.indexOf(":");
        const periodInd = readableTime.indexOf("m", colonInd) - 1;

        let hours = parseInt(readableTime.substring(0, colonInd).trim());
        let minutes = parseInt(readableTime.substring(colonInd + 1, periodInd).trim());
        let isPm = readableTime.charAt(periodInd) === "p" ? true : false;

        return String(60 * hours + minutes + (isPm ? 720 : 0));
    }

    const handleSearch = () => {
        const queryParameters = new URLSearchParams({
            term,
            schoolId: selectedSchool,
            department: selectedDepartment,
            course_code: courseCode,
            course_number: courseNumber,
            days: String(days),
            start_time: convertTime(startTime),
            end_time: convertTime(endTime)
        })


        console.log('Search clicked');
        console.log({ term, selectedDepartment, selectedSchool, courseCode, courseNumber, days, startTime, endTime, start: convertTime(startTime), end: convertTime(endTime)});
        

        let courseUrl = `${BASE_URL}/api/courses?${queryParameters.toString()}`;
        console.log(courseUrl)
        fetch(courseUrl)
            .then(response => response.json())
            .then(data => {
                data.sort((a,b) => a.courseId - b.courseId);
                setCourses(data);
                setSearchActive(!searchActive);
            })
            .catch(error => {
                console.error('API Error:', error);
            });


    };

    const handleReset = () => {
        setTerm('');
        setSelectedDepartment('');
        setSelectedSchool('');
        setCourseCode('');
        setCourseNumber('');
        setStartTime('6:00am');
        setEndTime('11:00pm');
        setDays(0);
    };

    const handleBack = () => {
        setSearchActive(true); 
        setCourses([]); 
    };

    const handleCheckboxChange = (dayValue) => {
        setDays(days ^ dayValue); 
    };
   
    const courseSearchForm = (
        <div className="search-container">
            <h2 className="header">Schedule of Classes</h2>
            <div className="field">
                <label className="label">Term</label>
                <select value={term} onChange={(e) => setTerm(e.target.value)} className="select">
                    <option value="20243">2024 Fall Semester</option>
                    <option value="20242">2024 Summer Semester</option>
                    <option value="20241">2024 Spring Semester</option>
                </select>
            </div>

            <div className="field">
                <label className="label">School</label>
                <select value={selectedSchool} onChange={(e) => setSelectedSchool(e.target.value)} className="select">
                    <option value="0">Include All Schools</option>
                    {schools.map(school => (
                        <option key={school.id} value={school.id}>
                            {school.name}
                        </option>
                    ))}
                </select>
            </div>

            <div className="field">
                <label className="label">Department</label>
                <select value={selectedDepartment} onChange={(e) => setSelectedDepartment(e.target.value)} className="select">
                    <option value="">Include All Departments</option>
                    {filteredDepartments.map(department => (
                        <option key={department.code} value={department.code}>
                            {department.name}
                        </option>
                    ))}
                </select>
            </div>

            <div className="field input-group">
                
                <div className="flex-1">
                    <div class="course-num">
                        <label className="label">Course Number</label>
                        <input
                            type="text"
                            value={courseNumber}
                            onChange={(e) => setCourseNumber(e.target.value)}
                            placeholder="ex. H2A, 5, 10-20"
                            className="course-input"
                        />
                    </div>
                    {/* <div className="course-num">
                        <label className="label">Course Code</label>
                        <input
                            type="text"
                            value={courseCode}
                            onChange={(e) => setCourseCode(e.target.value)}
                            placeholder="ex. 14200, 29000-29100"
                            className="course-input"
                        />
                    </div> */}
                    <div className="flex-time">
                        <TimeSelector name="Start Time" value={startTime} className="startTime" onChange={setStartTime}/>
                        <TimeSelector name="End Time" value={endTime} className="endTime" onChange={setEndTime}/>
                    </div>
                    <button onClick={handleSearch} className="button submit-button">
                        Submit Search
                    </button>
                    <button onClick={handleReset} className="button reset-button">
                        Reset
                    </button>
                </div>
                <div className="flex-2">
                    <label className="label">Select Days<br></br>(leave empty for any day)</label>
                    <form className="days-form">
                        
                        {Object.entries(daysMapping).map(([day, value]) => (
                            <div key={day} className="checkbox-item">
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        name={day}
                                        className="checkbox-input"
                                        checked={(days & value) !== 0} 
                                        onChange={() => handleCheckboxChange(value)}
                                    />
                                    {day}
                                </label>
                            </div>
                        ))}
                    </form>
                </div>
            </div>
        </div>
    );

    const courseResults = (
        <div className="course-results">
            <button onClick={handleBack} className="button back-button">
                Back to Search
            </button>
            <div className="scrollable-container">
                <div className="courses-container">
                    {courses.map(course => (
                        <CourseCard key={course.courseId} course={course}/>
                    ))}
                </div>
            </div>
        </div>
    )

    return (<div id="course-search-container">{searchActive ? courseSearchForm : courseResults}</div>)
    
}


export default ClassSelector;