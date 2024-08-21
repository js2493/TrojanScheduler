DAY_LIST = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]

def encode_days(days_string):
    if days_string.lower() == "tba":
        return 0
    if days_string.lower() == "mwf":
        days_string = "Mon, Wed, Fri"
    days = set([day.lower().strip()[:3] for day in days_string.split(",")])
    values = {day.lower().strip()[:3]: 2 ** i for i, day in enumerate(DAY_LIST)}
    try:
        return sum(values[day] for day in days)
    except KeyError:
        return 0


def decode_days(value):
    if value == 0:
        return ["TBA"]
    days = []
    for i in range(len(DAY_LIST) - 1, -1, -1):
        if value >= 2 ** i:
            days.append(DAY_LIST[i])
            value = value % (2 ** i)
            if not value:
                break
    return list(reversed(days))
