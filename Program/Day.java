public class Day implements Comparable<Day>, Cloneable {
	private int year;
	private int month;
	private int day;

	private static final String MonthNames = "JanFebMarAprMayJunJulAugSepOctNovDec";

	// Constructor
	public Day(int y, int m, int d) {
		this.year = y;
		this.month = m;
		this.day = d;
	}

	// Constructor, simply call set(sDay)
	public Day(String sDay) {
		set(sDay);
	}

	// check if a given year is a leap year
	static private boolean isLeapYear(int y) {
		if (y % 400 == 0)
			return true;
		else if (y % 100 == 0)
			return false;
		else if (y % 4 == 0)
			return true;
		else
			return false;
	}

	// check if y,m,d valid
	static private boolean valid(int y, int m, int d) {
		if (m < 1 || m > 12 || d < 1)
			return false;
		switch (m) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return d <= 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return d <= 30;
			case 2:
				if (isLeapYear(y))
					return d <= 29;
				else
					return d <= 28;
		}
		return false;
	}

	// Check if the current object is the end of a month
	private boolean isEndOfAMonth() {
		if (valid(year, month, day + 1))
			return false;
		else
			return true;
	}

	private boolean isEndOfAMonth(int year, int month, int day) {
		if (valid(year, month, day + 1))
			return false;
		else
			return true;
	}

	// Set year,month,day based on a string like 01-Jan-2021
	public void set(String sDay) {
		String[] sDayParts = sDay.split("-");
		this.year = Integer.parseInt(sDayParts[2]);
		this.day = Integer.parseInt(sDayParts[0]);
		this.month = MonthNames.indexOf(sDayParts[1]) / 3 + 1;
	}

	// Get the day after the specific duration
	// e.g: current: 27 Feb 2022 , duration: 7
	// return: 6 March 2022
	public Day advanaceDay(int duration) {

		// If it is the end of the month, the return day will be
		// the first day of the next month and day is the duration
		if (isEndOfAMonth()) {
			if (month == 12)
				return new Day(year + 1, 1, duration);
			else
				return new Day(year, month + 1, duration);
		}

		// If it is not the end of the month, check the valid day and month by
		// repeat adding the duration one day by one day
		else {
			int m = this.month; // 2
			int d = this.day; // 27
			int dayPassed = 0;
			while (dayPassed < duration) // 0 1 2 3 4 5 6
			{
				if (isEndOfAMonth(this.year, m, d)) {
					if (month == 12)
						return new Day(year + 1, 1, duration - dayPassed);
					else
						return new Day(year, month + 1, duration - dayPassed);
				}
		
				d++; // 2/28 (2/29->3/1) 3/2 3/3 3/4 3/5 3/6
				if (!valid(this.year, m, d)) {
					m++;
					d = 1;
				}
				dayPassed++;
			}
			return new Day(this.year, m, d);
		}

	}

	// Check whether the String is in correct date format
	public void checkDateFormat(String sDay) throws ExInvalidDateFormat {
		String[] sDayParts = sDay.split("-");

		// Missing year or month or day
		if (sDayParts.length < 3)
			throw new ExInvalidDateFormat();

		// year and day are not only contain digits
		if (!sDayParts[2].chars().allMatch(c -> Character.isDigit(c)) || 
			!sDayParts[0].chars().allMatch(c -> Character.isDigit(c)))
			throw new ExInvalidDateFormat();

		// Incorrect month name in the command
		if (!MonthNames.contains(sDayParts[1]))
			throw new ExInvalidDateFormat();

		// Invalid date
		if (!valid(Integer.parseInt(sDayParts[2]), MonthNames.indexOf(sDayParts[1]) / 3 + 1,
				   Integer.parseInt(sDayParts[0])))
			throw new ExInvalidDateFormat();
	}

	@Override
	public String toString() {
		return day + "-" + MonthNames.substring((month - 1) * 3, month * 3) + "-" + year; // (month-1)*3,(month)*3
	}

	@Override
	public Day clone() {
		Day copy = null;
		copy = new Day(this.toString());
		return copy;

	}

	@Override
	public int compareTo(Day anotherDay) {

		if ((this.year > anotherDay.year) || (this.year == anotherDay.year && this.month > anotherDay.month) ||
			(this.year == anotherDay.year && this.month == anotherDay.month && this.day > anotherDay.day))
			return 1;
		else
			return -1;
	}
}