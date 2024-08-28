def encode_time(time_string):
    if "-" not in time_string:
        return -1, -1
    start, end = time_string.split("-")
    start_h, start_m = [int(time) for time in start.split(":")]
    end_h, end_m = [int(time) for time in end[:-2].split(":")]
    start_h %= 12
    end_h %= 12
    if start_h > end_h:
        end_h += 12
    elif end[-2:].lower() == "pm":
        start_h += 12
        end_h += 12

    return start_h * 60 + start_m, end_h * 60 + end_m


def decode_time(start_time, end_time):
    start_h = start_time // 60
    start_m = start_time % 60
    end_h = end_time // 60
    end_m = end_time % 60

    return f"{start_h % 12 or 12}:{start_m:02}-{end_h % 12 or 12}:{end_m:02}" + ("pm" if end_h >= 12 else "am")

