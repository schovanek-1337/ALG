/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg07_calendar;

import java.util.Scanner;

/**
 *
 * @author plesio
 */
public class Kalendar {

	private int day;
	private int month;
	private int year;

	/**
	 * Non-leap year data.
	 */
	private static final int NL_MONTH_DAY_COUNT[] = {31, 28, 31, 30, 31, 30,
		31, 31, 30, 31, 30, 31};

	final String[] MONTH_DATA = {"January", "February", "March", "April", "May",
		"June", "July", "August", "September", "October", "November", "December"};

	final String[] DAY_OF_WEEK = {"Saturday", "Sunday", "Monday",
		"Tuesday", "Wednesday", "Thursday", "Friday"};

	/**
	 * Initialize the class using provided values. Useful comments all the way.
	 *
	 * @param day
	 * @param month
	 * @param year
	 */
	public Kalendar(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && year % 100 != 0) {
			return true;
		} else if ((year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static int calculateDaysInYear(int year) {
		return (isLeapYear(year)) ? (366) : (365); //hard calculations only
	}

	/**
	 * Use return value as an index to DAY_OF_WEEK array.
	 *
	 * @return
	 */
	private int weekDay() {

		if (month == 1) {
			month = 13;
		} else if (month == 2) {
			month = 14;
		}

		int j = year / 100;

		int k = year % 100;

		double h = (day + ((26 * (month + 1)) / 10) + k + (k / 4)
				+ (j / 4) + (5 * j)) % 7;

		int ans = (int) h;

		return ans;
	}

	/**
	 * Use return value as an index to DAY_OF_WEEK array.
	 *
	 * @return
	 */
	private int weekDayInt(int day, int month, int year) {

		if (month == 1) {
			month = 13;
		} else if (month == 2) {
			month = 14;
		}

		int j = year / 100;

		int k = year % 100;

		double h = (day + ((26 * (month + 1)) / 10) + k + (k / 4)
				+ (j / 4) + (5 * j)) % 7;

		int ans = (int) h;

		return ans;
	}

	private String weekDayString() {
		return DAY_OF_WEEK[weekDay()];
	}

	private String getCurrentMonthCalendar() {
		StringBuilder calendarData = new StringBuilder();

		calendarData.append(MONTH_DATA[month - 1]).append(" of ").append(year)
				.append("\n");

		calendarData.append("S  " + "S  " + "M  " + "T  " + "W  " + "T  "
				+ "F  \n");

		int numberOfDays = 30;

		int startPos = weekDayInt(1, month, year);

		int counter = 1;
		for (int i = 0; i < 7; i++) {
			if (i < startPos) {
				calendarData.append("   ");
			} else {
				calendarData.append(counter).append("  ");
				counter++;
			}
		}
		calendarData.append("\n");

		for (int i = 1; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				if (counter <= numberOfDays) {
					if (String.valueOf(counter).length() > 1) {
						calendarData.append(counter).append(" ");
					} else {
						calendarData.append(counter).append("  ");
					}
					counter++;
				} else {
					calendarData.append("   ");
				}
			}
			calendarData.append("\n");
		}

		return calendarData.toString();
	}

	public String getCalendar() {
		return getCurrentMonthCalendar();
	}

	/**
	 * Increment only by one please.
	 *
	 * @param amount
	 */
	private void updateMonth(boolean up) {
		day = 1;

		if (up) {
			if ((month + 1) > 12) {
				month = 1;
				year++;
			} else {
				month += 1;
			}
		}

		if (!up) {
			if (month - 1 < 1) {
				month = 12;
				year--;
			} else {
				month -= 1;
			}
		}

		System.out.println(month);
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input the desired date: ");
		int day = sc.nextInt();
		int month = sc.nextInt();
		int year = sc.nextInt();

		Kalendar k = new Kalendar(day, month, year);

		System.out.println("The day is: " + k.weekDayString());
		System.out.println("Here's your calendar for the month.");
		System.out.println(k.getCalendar());

		System.out.println("Controls: < : Previous Month, > : Next Month, "
				+ "q : end");

		for (;;) {
			char nextCommand = sc.next().charAt(0);
			switch (nextCommand) {
				case '<':
					k.updateMonth(false);
					System.out.println(k.getCalendar());
					break;
				case '>':
					k.updateMonth(true);
					System.out.println(k.getCalendar());
					break;
				case 'q':
					return;
				default:
					System.out.println("Unknown command.\n");
			}
		}
	}

}
