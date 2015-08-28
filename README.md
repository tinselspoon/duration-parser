# duration-parser
Java parser for durations expressed in JIRA-style numbers with suffixes, e.g. "1h 30m"

## Usage
* Call `DurationParser.parseToSeconds` with a string to parse. Valid suffixes are d (days), h (hours), m (minutes) and s (seconds).
* Any amount of whitespace is allowed, but suffixes must be next to their numbers.
* Decimals are supported, but the final result is rounded to the nearest integer.
