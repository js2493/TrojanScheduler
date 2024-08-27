import React, { useState } from 'react';
import './ClassSelector.css';

function ClassSelector() {
    const [term, setTerm] = useState('');
    const [department, setDepartment] = useState('');
    const [courseCode, setCourseCode] = useState('');
    const [courseNumber, setCourseNumber] = useState('');

    const handleSearch = () => {
        // Handle search logic here
        console.log('Search clicked');
        console.log({ term, department, courseCode, courseNumber });
    };

    const handleReset = () => {
        setTerm('');
        setDepartment('');
        setCourseCode('');
        setCourseNumber('');
    };

    return (
        <div className="container">
            <h2 className="header">Schedule of Classes</h2>
            <div className="field">
                <label className="label">Term</label>
                <select value={term} onChange={(e) => setTerm(e.target.value)} className="select">
                    <option value="">Select a term</option>
                    <option value="2024 Fall Quarter">2024 Fall Quarter</option>
                    <option value="2024 Winter Quarter">2024 Winter Quarter</option>
                    <option value="2024 Spring Quarter">2024 Spring Quarter</option>
                </select>
            </div>

            <div className="field">
                <label className="label">Department</label>
                <select value={department} onChange={(e) => setDepartment(e.target.value)} className="select">
                    <option value="">Include All Departments</option>
                    <option value="CSE">Computer Science and Engineering</option>
                    <option value="MATH">Mathematics</option>
                    <option value="PHYS">Physics</option>
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
               
