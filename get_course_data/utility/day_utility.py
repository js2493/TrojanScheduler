DAY_LIST = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]


def encode_days(days_string):
    if days_string.lower() == "tba":
        return 0
    splits = [ind for ind, letter in enumerate(days_string) if letter.isupper()]
    parts = [days_string[i:j] for i, j in zip(splits, splits[1:] + [None])]
    days = set([part[0] if part[0].lower() != "t" else part[:2] for part in parts])

    values = {(day.lower().strip()[:1] if day.lower()[0] != "t" else day.lower().strip()[:2]): 2 ** i
              for i, day in enumerate(DAY_LIST)}
    try:
        return sum(values[day.lower()] for day in days)
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


print(encode_days("TuTh"))
print(encode_days("MTuWThF"))

print(decode_days(20))
print(decode_days(62))
