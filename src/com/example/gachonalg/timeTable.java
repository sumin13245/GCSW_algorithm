package com.example.gachonalg;

public class timeTable {
	private lesson[] table;//monday 0-4,tuesday 5-9, ... friday 20-24
	
	private int[] index = new int[25];//monday 0-4,tuesday 5-9, ... friday 20-24 has lesson_num is no lesson, has 0
	
	timeTable(){
		table = new lesson[25];
	}
	
	public void addLesson(lesson lesson) {//If can put the lesson in the timetable, put it in the table and index.
		if(isCanPutIn(lesson)) {
			int length = lesson.getDate().length();
			for(int i =0; i<length; i+=2) {
				int lessonIndex = dateToIndex(lesson.getDate().substring(i,i+2));
				table[lessonIndex]=lesson;
				index[lessonIndex]=lesson.getLesson_num();
			}
		}else  System.out.println("Can't do that");
	}
	
	public boolean isDuplicate(lesson lesson) {//Check if there's the same lesson
		for(int i = 0; i<25; i++) {
			if(index[i]!=0) {
				if(index[i]==lesson.getLesson_num())return true;
				else if(table[i].getLessonName().equals(lesson.getLessonName()))return true;
			}
		}
		return false;
	}
	
	public boolean isCanPutIn(lesson lesson) {//Check if there are duplicate lesson and if there are other lesson in the that lecture's time to see if you can put them in or not.
		int length = lesson.getDate().length();
		for(int i =0; i<length; i+=2) {
			int lessonIndex = dateToIndex(lesson.getDate().substring(i,i+2));
			if(table[lessonIndex]!=null)return false;
		}
		if(this.isDuplicate(lesson))return false;
		return true;
	}
	
	public int dateToIndex(String date) {//receive the value like 월1,화2... ,return the correct index
		int lessonIndex;
		char[] temp = date.toCharArray();
		if(temp[0]=='월')lessonIndex = -1+(temp[1]-'0');
		else if(temp[0]=='화')lessonIndex = 4+(temp[1]-'0');
		else if(temp[0]=='수')lessonIndex = 9+(temp[1]-'0');
		else if(temp[0]=='목')lessonIndex = 14+(temp[1]-'0');
		else lessonIndex = 19+(temp[1]-'0');
		return lessonIndex;
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
	}
	
}
