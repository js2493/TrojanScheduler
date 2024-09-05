function generateTimeOptions(startTime = 360, endTime = 1380, stepSize = 30) {
    const options = [];
    let current = startTime;

    while (current <= endTime) {
        const hours = Math.floor(current / 60);
        const minutes = current % 60;
        const period = hours < 12 ? 'am' : 'pm';
        const displayHour = hours % 12 === 0 ? 12 : hours % 12; 

        const totalMinutes = hours * 60 + minutes;

        options.push({
            displayTime: `${displayHour}:${minutes.toString().padStart(2, '0')}${period}`,
            value: totalMinutes,
        });

        current += stepSize;
    }

    return options;
}

function TimeSelector({ name, value, className, startTime = 360, endTime = 1380, stepSize = 30, onChange }) {
    const timeOptions = generateTimeOptions(startTime, endTime, stepSize);

    return (
        <div className="time-select-wrapper">
            <div>{name}</div>
            <select name={name} className={className} value={value} onChange={(e) => onChange(e.target.value)}>
                {/* <option value="">&nbsp;</option>   */}
                {timeOptions.map((option, index) => (
                    <option key={index} value={option.displayTime}>
                        {option.displayTime}
                    </option>
                ))}
            </select>
        </div>
    );
}

export default TimeSelector;