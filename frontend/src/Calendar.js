import React, {useState, useEffect, useContext, useCallback} from "react";
import { AuthContext } from './AuthContext'; // Import AuthProvider


function Calendar() {

    const BASE_URL = 'http://localhost:8080'
    const { user } = useContext(AuthContext); 

    const [calendarList, setCalendarList] = useState([]);
    const [error, setError] = useState(null);

    

    const createCalendar = async () => {
        console.log(user.username)
        let url = BASE_URL + "/api/users/calendar?username=" + user.username;

        try {
            const response = await fetch(url, {
                method: 'POST', 
                headers: {
                    'Content-Type': 'application/json', 
                },
                credentials: 'include'
            });
            if (response.ok) {
                // const result = await response.json();
                
            } else {
                const errorData = await response.json();
                setError(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setError(`Error: ${error.message}`);
        }
        fetchCalendars();
    }


    const fetchCalendars = useCallback(async () => {
        if (user != null) {
            console.log(user.username)
            try {
                const response = await fetch(BASE_URL + "/api/users?username=" + user.username, {
                    credentials: 'include'
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const result = await response.json(); 
                setCalendarList(result); 
            } catch (error) {
                setError(error.message); 
            }
        }
    }, [user]);

    useEffect(() => {
        if (user && user.username) {
            fetchCalendars();
        } 
    }, [fetchCalendars, user]); 

    return (
        <div id="calendar-container">
            { user ? <button onClick={createCalendar}>
                Create Calendar
            </button> : <></>}
            {calendarList.map(calendar => (
                <div key={calendar.id}>
                    {calendar.name}
                </div> 
            ))}
        </div>
    );

}

export default Calendar;