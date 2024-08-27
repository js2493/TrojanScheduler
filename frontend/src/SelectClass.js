import React, {useState, useEffect} from "react";
import './ClassSelector.css'


function ClassSelector(){
    const [term, setTerm] = useState('');
    const [schools, setSchools] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [selectedSchool, setSelectedSchool] = useState('');
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [filteredDepartments, setFilteredDepartments] = useState([]);
    const [courseCode, setCourseCode] = useState('');
    const [courseNumber, setCourseNumber] = useState('');

    useEffect(() => {
        Promise.all([
            fetch('http://localhost:8080/api/schools').then(response => response.json()),
            fetch('http://localhost:8080/api/departments').then(response => response.json())
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

    const handleSearch = () => {
        console.log('Search clicked');
        console.log({ term, selectedDepartment, selectedSchool, courseCode, courseNumber });
    
    };

    const handleReset = () => {
        setTerm('');
        setSelectedDepartment('');
        setSelectedSchool('');
        setCourseCode('');
        setCourseNumber('');
    };
   
    const courseSearchForm = (
        <div className="container">
            <h2 className="header">Schedule of Classes</h2>
            <div className="field">
                <label className="label">Term</label>
                <select value={term} onChange={(e) => setTerm(e.target.value)} className="select">
                    <option value="">Select a term</option>
                    <option value="2024 Fall Semester">2024 Fall Semester</option>
                    <option value="2024 Summer Semester">2024 Summer Semester</option>
                    <option value="2024 Spring Semester">2024 Spring Semester</option>
                </select>
            </div>

            <div className="field">
                <label className="label">School</label>
                <select value={selectedSchool} onChange={(e) => setSelectedSchool(e.target.value)} className="select">
                    <option value="">Include All Schools</option>
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
                    <label className="label">Course Code</label>
                    <input
                        type="text"
                        value={courseCode}
                        onChange={(e) => setCourseCode(e.target.value)}
                        placeholder="ex. 14200, 29000-29100"
                        className="input"
                    />
                </div>
                <div className="flex-1">
                    <label className="label">Course Number</label>
                    <input
                        type="text"
                        value={courseNumber}
                        onChange={(e) => setCourseNumber(e.target.value)}
                        placeholder="ex. H2A, 5, 10-20"
                        className="input"
                    />
                </div>
            </div>

            <div className="button-group">
                <button onClick={handleSearch} className="button submit-button">
                    Submit Search
                </button>
                <button onClick={handleReset} className="button reset-button">
                    Reset
                </button>
            </div>

            <a href="#" className="link">Show More Options</a>
        </div>
    );

    return courseSearchForm;
    
}


export default ClassSelector;