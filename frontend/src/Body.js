import React, { useState } from 'react';
import ClassSelector from './SelectClass';
import Calendar from './Calendar';
import "./Body.css"


function Body() {

    const [showCalendar, setShowCalendar] = useState(false); // State to toggle between calendar and search

    // Function to toggle between calendar and search
    const toggleView = () => {
        setShowCalendar(!showCalendar);
    };

    return (
        <div id="body-outer">
            <button className="toggle-button" onClick={toggleView}>
                    {showCalendar ? 'Show Course Search' : 'Show Calendars'}
            </button>
            <div id="body-inner">
                <div className={`calendar ${showCalendar ? '' : 'hidden-mobile'}`}>
                    <Calendar />
                </div>
                <div className={`class-selector ${showCalendar ? 'hidden-mobile' : ''}`}>
                    <ClassSelector />
                </div>
            </div>
        </div>
    ) 
}

export default Body;
