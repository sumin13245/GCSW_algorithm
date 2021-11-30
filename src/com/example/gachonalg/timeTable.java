package com.example.gachonalg;

public class timeTable {
	private lesson[] table;//monday 0-4,tuesday 5-9, ... friday 20-24
	private boolean[] days = {false,false,false,false,false};
	private int numOfLessons = 0;
	private int TotalCredit;

	private int[] index = new int[25];//monday 0-4,tuesday 5-9, ... friday 20-24 has lesson_num is no lesson, has 0
	
	timeTable(){
		table = new lesson[25];
		TotalCredit = 0;
	}
	
	public boolean addLesson(lesson lesson) {//If can put the lesson in the timetable, put it in the table and index.
		if(isCanPutIn(lesson)) {
			char date[] = lesson.getDate().toCharArray();
			int length = date.length;
			for(int i =0; i<length; i+=2) {
				if(i == 0) TotalCredit += lesson.getCredit(); //Avoid duplicating ADD for two-day classes
				date[0]= date[i];// for a two-day class
				int lessonIndex = lesson.dateToIndex(date);
				table[lessonIndex]=lesson;
				index[lessonIndex]=lesson.getLessonNum();
				countVars(lessonIndex);
			}
			return true;
		}
		else return false;
	}

	public boolean isDuplicate(lesson lesson) {//Check if there's the same lesson
		for(int i = 0; i<25; i++) {
			if(index[i]!=0) {
				if(index[i]==lesson.getLessonNum())return true;
				else if(table[i].getLessonName().substring(0, 7).equals(lesson.getLessonName().substring(0, 7)))return true;
			}
		}
		return false;
	}
	
	public boolean isCanPutIn(lesson lesson) {//Check if there are duplicate lesson and if there are other lesson in the that lecture's time to see if you can put them in or not.
		char date[] = lesson.getDate().toCharArray();
		int length = date.length;
		for(int i =0; i<length; i+=2) {
			date[0]= date[i];//for a two-day class
			int lessonIndex = lesson.dateToIndex(date);
			if(table[lessonIndex]!=null)return false;
		}
		if(this.isDuplicate(lesson))return false;
		return true;
	}
	
	public void print() {
		char[] week = new char[] {'월','화','수','목','금'};
		for(int i = 0; i<25; i+=5) {
			System.out.println(week[i/5]+"\n");
			System.out.printf("%-15s|  %s\n","09:00 ~ 11:00",(table[i]!=null)?(table[i].toString()):"");
			System.out.printf("%-15s|  %s\n","11:00 ~ 13:00",(table[i+1]!=null)?(table[i+1].toString()):"");
			System.out.printf("%-15s|  %s\n","13:00 ~ 15:00",(table[i+2]!=null)?(table[i+2].toString()):"");
			System.out.printf("%-15s|  %s\n","15:00 ~ 17:00",(table[i+3]!=null)?(table[i+3].toString()):"");
			System.out.printf("%-15s|  %s\n\n","17:30 ~ 19:30",(table[i+4]!=null)?(table[i+4].toString()):"");
		}
		System.out.println("\n 총 학점 : "+TotalCredit);
	}

	//method countVars for counting num of lessons and Check the days of the week.
	private  void countVars(int lessonIndex) {

		numOfLessons++;
		if( lessonIndex >= 0 && lessonIndex < 5)
		{
			days[0] = true;
		}
		else if( lessonIndex >= 5 && lessonIndex < 10)
		{
			days[1] = true;
		}
		else if( lessonIndex >= 10 && lessonIndex < 15)
		{
			days[2] = true;
		}
		else if( lessonIndex >= 15 && lessonIndex < 20)
		{
			days[3] = true;
		}
		else if( lessonIndex >= 20 && lessonIndex < 25)
		{
			days[4] = true;
		}
	}

	public lesson[] getTable(){return table;}
	public int getNumOfLesson() {
		return numOfLessons;
	}
	public int getTotalCredit() {
		return TotalCredit;
	}
	public boolean[] getDays() {
		return days;
	}

}
