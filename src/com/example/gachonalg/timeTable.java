package com.example.gachonalg;

import java.io.*;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args){
	boolean userWantFirstLesson;
	boolean userWantLunchTime;
	boolean userWantLastLesson;
        lesson[] lessonData = new lesson[200]; //Array to hold data from input.txt
        int userWant;
        timeTable cdInputTimeTable = new timeTable();
        timeTable doInputTimeTable = new timeTable();
        timeTable myTimeTable = new timeTable();
        
        int numOfLesson = readLessonDataFromFile("input.txt",lessonData);
		if (numOfLesson != -1) {
			Scanner input = new Scanner(System.in);
			for (int j = 0; j < 3;) {
				int key;
				System.out.println("원하는 강의 학수번호를 입력하세요");
				userWant = input.nextInt();
				key = queryAsNum(userWant, lessonData, numOfLesson);
				if (key != -1)
					if ((cdInputTimeTable.addLesson(lessonData[key]) && doInputTimeTable.addLesson(lessonData[key]))&&myTimeTable.addLesson(lessonData[key]))
						j++;

			}
			int reqCredit = 0;
			while (reqCredit < 12 || reqCredit > 21) { // Maximum and Minimum Credits
				System.out.println("원하는 학점을력하세요");
				reqCredit = input.nextInt();
			}
			System.out.println("1교시 좋으세요");
			userWantFirstLesson = (input.nextInt()==1)?(true):(false);
			System.out.println("점심시간을 원하세요");
			userWantLunchTime = (input.nextInt()==1)?(true):(false);
			System.out.println("마지막 강의 좋으세요");
			userWantLastLesson = (input.nextInt()==1)?(true):(false);

			System.out.println("closeDistance 시간표\n");
			timeTable cdOffTimeTable = closeDistance(cdInputTimeTable, numOfLesson, lessonData, reqCredit);
			cdOffTimeTable.print();

			System.out.println("daysOff 시간표\n");
			timeTable doTimeTable = daysOff(doInputTimeTable, lessonData, reqCredit);
			doTimeTable.print();

			System.out.println("test 시간표\n");
			makeTimeTable(myTimeTable, lessonData, numOfLesson, userWantFirstLesson, userWantLunchTime,userWantLastLesson,reqCredit);
			myTimeTable.print();

			input.close();
		}
    }
    
    private static int readLessonDataFromFile(String fileName, lesson[] lessonData) {//read all lesson from file, store in lesson data And return num of lesson
		int numOfLesson = 0;
		try {
			InputStreamReader fileInput = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			BufferedReader fileReader = new BufferedReader(fileInput);
			String data;//lessonName, professorName, date, building,building_num, lesson_num;
			while ((data = fileReader.readLine()) != null) {
				String[] dataSplit = data.split(" ");
				dataSplit[0] = dataSplit[0].replace("!", " ");
				dataSplit[1] = dataSplit[1].replace("!", " ");
				lessonData[numOfLesson++] = new lesson(dataSplit[0], dataSplit[1], dataSplit[2], dataSplit[3], Integer.parseInt(dataSplit[4]), Integer.parseInt(dataSplit[5]),Integer.parseInt(dataSplit[6]));
			}
		} catch (IOException e) {
			System.out.println("Error occurred with file : "+fileName);
			return -1;
		}
		return numOfLesson;
    }
    
    private static void makeTimeTable(timeTable timeTable, lesson[] lessonData, int numOfLesson, boolean userWantFirstLesson, boolean userWantLunchTime, boolean userWantLastLesson, int goalCredit) {
    	boolean[] dayHasLesson=timeTable.getDays();
    	for(int i =0; i<5; i++)if(dayHasLesson[i]) makeDailyTimeTable(timeTable, lessonData, numOfLesson, i, userWantFirstLesson, userWantLunchTime, userWantLastLesson, goalCredit);
    	while(timeTable.getTotalCredit()<=goalCredit-2) {
    		timeTable.print();
    		System.out.println("학점이 부족합니다. 현재 학점 :"+timeTable.getTotalCredit());
    		System.out.println("다른 요일에 강의를 더 넣으시겠습니까?");
    		System.out.println("yes: 1 , no: 0");
            Scanner userInput = new Scanner(System.in);
            int choice = userInput.nextInt();
            if(choice==0)break;
    		 System.out.println("어느 요일을 추가하시겠습니까? 숫자로 입력해주세요");
             boolean[] Days = timeTable.getDays();
             char[] week = new char[]{'월', '화', '수', '목', '금'};
             for (int i = 0; i < 5; i++) {
                 if (!Days[i]) System.out.print(i + ":" + week[i] + " ");
             }
             int wantDay = userInput.nextInt();
			 for (int i = 0; i < numOfLesson; i++) {
				 int time = (wantDay*5)+2;
					if (lessonData[i].dateToIndex(lessonData[i].getDate().substring(0, 2).toCharArray()) == time)if(timeTable.isCanPutIn(lessonData[i])){
						if(timeTable.getTotalCredit()+lessonData[i].getCredit()<=goalCredit) {
							timeTable.addLesson(lessonData[i]);
							break;
						}
					}
			 }
             makeDailyTimeTable(timeTable, lessonData, numOfLesson, wantDay, userWantFirstLesson, userWantLunchTime, userWantLastLesson, goalCredit);
    	}
    }
    
    private static void makeDailyTimeTable(timeTable timeTable, lesson[] lessonData, int numOfLesson, int dayChose ,boolean userWantFirstLesson, boolean userWantLunchTime, boolean userWantLastLesson, int goalCredit) {
    	lesson[] table = timeTable.getTable();
    	int[] timeHaveLesson = {0,0,0,0,0};//0-didn't decided 1-user selected lesson 2 - need input  -1 - don't input
    	for(int i = dayChose*5;i<(dayChose+1)*5;i++)if(table[i]!=null)timeHaveLesson[i%5]=1;
    	if (userWantLunchTime) {
    		switch (timeHaveLesson[1]+timeHaveLesson[2]) {
    			case 0:
    				timeHaveLesson[1]=2;
    				timeHaveLesson[2]=-1;
    				break;
    			case 1:
    				if(timeHaveLesson[1]==0)timeHaveLesson[1]=-1;
    				else timeHaveLesson[2]=-1;
    				break;
    			case 2:
    				if(timeHaveLesson[3]==0&&timeHaveLesson[4]==0)timeHaveLesson[3]=-1;
    				if(timeHaveLesson[4]==0)timeHaveLesson[4]=-1;
    				break;
    		}if(timeHaveLesson[1]!=-1)timeHaveLesson[0]=2;
    		else if(userWantFirstLesson)timeHaveLesson[0]=2;
        	if(timeHaveLesson[3]!=-1&&timeHaveLesson[3]!=1)timeHaveLesson[3]=2;
        	if(timeHaveLesson[4]!=-1&&timeHaveLesson[4]!=1)timeHaveLesson[4]=2;
    	}else for(int i=0; i<5;i++)if(timeHaveLesson[i]!=1)timeHaveLesson[i]=2;
    	if(!userWantFirstLesson)if(timeHaveLesson[0]!=1)timeHaveLesson[0]=-1;
    	if(!userWantLastLesson)if(timeHaveLesson[4]!=1)timeHaveLesson[4]=-1;
    	addLessonWithCondition(timeTable,timeHaveLesson,lessonData,numOfLesson,dayChose,userWantLunchTime,goalCredit);
    }
    
	private static void addLessonWithCondition(timeTable timeTable, int[] dailyTable, lesson[] lessonData,int numOfLesson, int dayChose, boolean userWantLunchTime, int goalCredit) {
		int cursor = 4;
		while (true) {
			if (dailyTable[cursor] == 1) {
				addLessonWithDistance(timeTable, dailyTable, lessonData, numOfLesson, dayChose, cursor, goalCredit, +1);
				addLessonWithDistance(timeTable, dailyTable, lessonData, numOfLesson, dayChose, cursor, goalCredit, -1);
			}
			cursor--;
			if (cursor < 0)
				break;
		}
	}
    
    private static void addLessonWithDistance(timeTable timeTable, int[] dailyTable, lesson[] lessonData, int numOfLesson, int dayChose,int cursor ,int goalCredit, int direction) {
    	while(true) {
        	if(cursor+direction<0||cursor+direction>4)break;
        	lesson[] table = timeTable.getTable();
        	if(dailyTable[cursor+direction]==2) {
        		lesson lessonToAdd = findShortestLesson(timeTable,dailyTable,lessonData,numOfLesson,table[(dayChose*5)+(cursor)],dayChose,direction);
        		if(lessonToAdd==null)break;
        		if(timeTable.getTotalCredit()+lessonToAdd.getCredit()<=goalCredit) {
        			timeTable.addLesson(lessonToAdd);
        			dailyTable[cursor+direction]=1;
        		}else if(timeTable.getTotalCredit()>=goalCredit-2)break;
        	}else if(dailyTable[cursor+direction]==1) break;
        	else if(0<=cursor+(direction*2)&&cursor+(direction*2)<5) {
        		if(dailyTable[cursor+(direction*2)]==2) {
            		lesson lessonToAdd = findShortestLesson(timeTable,dailyTable,lessonData,numOfLesson,null,dayChose,direction);
            		if(lessonToAdd==null)break;
            		if(timeTable.getTotalCredit()+lessonToAdd.getCredit()<=goalCredit) {
            			timeTable.addLesson(lessonToAdd);
            			dailyTable[cursor+(direction*2)]=1;
            			break;
            		}
        		}
        	}
        	cursor+=direction;
    	}
    }
    
	private static lesson findShortestLesson(timeTable timeTable, int[] dailyTable, lesson[] lessonData, int numOfLesson,lesson criterion, int dayChose, int direction) {
		if (criterion == null) {
			for (int k = 2; k >= 0 && k < 5; k += direction) {
				if(dailyTable[k]==2&&dailyTable[k-direction]==-1)
					for (int i = 0; i < numOfLesson; i++) {
						int time = (dayChose*5)+k;
						if (lessonData[i].dateToIndex(lessonData[i].getDate().substring(0, 2).toCharArray()) == time)if(timeTable.isCanPutIn(lessonData[i]))return lessonData[i];
				}
			}
		} else {
			distance distanceCalculator = new distance();
			double minDistance = 2;
			int minIndex = -1;
			int time = criterion.dateToIndex(criterion.getDate().substring(0, 2).toCharArray());
			if (dayChose * 5 < time && time <= (dayChose + 1) * 5)time += direction;
			else time = criterion.dateToIndex(criterion.getDate().substring(2, 4).toCharArray()) + direction;
			for (int i = 0; i < numOfLesson; i++) {
				if (lessonData[i].dateToIndex(lessonData[i].getDate().substring(0, 2).toCharArray()) == time) {
					double temp = distanceCalculator.getDistance(criterion.getBuilding(), lessonData[i].getBuilding(),criterion.getBuildingNum(), lessonData[i].getBuildingNum());
					if (minDistance > temp && timeTable.isCanPutIn(lessonData[i])) {
						minIndex = i;
						minDistance = temp;
					}
				}
			}
			return (minIndex != -1) ? (lessonData[minIndex]) : (null);
		}
		return null;
	}
    

    private static timeTable closeDistance(timeTable inputTT,int max,lesson[] lessonsData,int reqCredit){ //  max value is lessonsData value
        distance Distance = new distance();// context for search
        lesson[] tableData = inputTT.getTable();
        calculateCD(inputTT, lessonsData, tableData, Distance, max,reqCredit);

        while (inputTT.getTotalCredit()<reqCredit) {
            System.out.println("학점이 부족합니다 어느 요일을 추가하시겠습니까? 숫자로 입력해주세요");
            boolean[] Days = inputTT.getDays();
            char[] week = new char[]{'월', '화', '수', '목', '금'};
            for (int i = 0; i < 5; i++) {
                if (!Days[i]) System.out.print(i + ":" + week[i] + " ");
            }
            Scanner input = new Scanner(System.in);
            int wantDay = input.nextInt();
            boolean hasAdded = false;
            for (int i = 0; i < max && !hasAdded; i++) {
                if (lessonsData[i].getDate().contains(String.valueOf(week[wantDay])) && inputTT.isCanPutIn(lessonsData[i])) {
                    inputTT.addLesson(lessonsData[i]);
                    hasAdded = true;
                }
            }
            calculateCD(inputTT, lessonsData, tableData, Distance, max,reqCredit);
        }
        return inputTT;
    }
    

    private static void calculateCD(timeTable inputTT, lesson[] lessonsData, lesson[] tableData, distance distance, int max ,int reqCredit) {
        char[] date;
        int index;
        for(int chack = 0; chack<5; chack++) {
            for (int i = 0; i < 25; i++) {  // check the data inside Table
                if (inputTT.getTotalCredit() < reqCredit) {//If more than the required grades
                    if (tableData[i] != null) {
                        char[] reqDate = new char[2];
                        date = tableData[i].getDate().toCharArray();
                        for (int cp = 0; cp < date.length; cp += 2) {
                            date[0] = date[cp];
                            reqDate[0] = date[0];
                            index = tableData[i].dateToIndex(date);
                            switch (index % 5) {
                                case 0: // putMdLesson: Find the closest class and add it to the table
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, 1);//If it is a lesson in the first lesson, only the time after that is considered
                                    break;
                                case 4:
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, -1);//In the case of lesson 5 in lesson 5, only the previous time is considered.
                                    break;
                                default:// Consider both before and after times for 2 3 4
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, -1);
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, 1);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void putMdLesson(distance distance,timeTable inputTT, lesson[] tableData, lesson[] lessonsData,char[] date, int index, int i, char[] reqDate,int reqCredit, int max,int mode) {
        int tableIndex;
        if(tableData[index+mode]==null) { // If there is a place to put a lesson at any time you want
            reqDate[1] = (char) ((int) date[1] +mode);//Calculate reqDate[1]
            tableIndex = queryMinDistance(distance, inputTT, tableData[i], lessonsData, reqDate,reqCredit, max); //find the nearest
            if (tableIndex != -1) inputTT.addLesson(lessonsData[tableIndex]);//Add if valid
        }
    }

    //method daysOff with input timetable and max number of lessons and lessonData array
    private static timeTable daysOff(timeTable inputTT, lesson[] lessonsData, int reqCredit) {
        // add maximum lesson when same days.
        for (int i = 0; i < 5; i++) {
            if (inputTT.getDays()[i]) {
                addLessonSameDay(i, reqCredit, inputTT, lessonsData);
            }
        }
        // if not enough num of lesson, add lessons when another days.
        if ( inputTT.getTotalCredit() < reqCredit) {
            for (int i = 0; i < 5; i++) {
                if (!inputTT.getDays()[i]) {
                    addLessonSameDay(i, reqCredit, inputTT, lessonsData);
                }
            }
        }
        return inputTT; //return result table
    }

    private static void addLessonSameDay(int day, int reqCredit, timeTable inputTT, lesson[] lessonsData) {
        int j = 0;
        while (j < 157 && inputTT.getTotalCredit() < reqCredit) {
            if(lessonsData[j].dateToIndex(lessonsData[j].getDate().toCharArray()) <= (day*5) && lessonsData[j].dateToIndex(lessonsData[j].getDate().toCharArray()) < (day+1)*5)inputTT.addLesson(lessonsData[j]);
            j++;
        }
    }

    private static int queryMinDistance(distance Distance,timeTable inputTT,lesson userLs,lesson[] lessonData,char[] reqDate ,int reqCredit,int dataSize){
        int minIndex = -1;
        double minDis = 10000;
        double disInfo;
        String sReqDate = new String(reqDate);

        for(int k = 0;k<dataSize;k++) {
            if (lessonData[k].getDate().contains(sReqDate)&&inputTT.getTotalCredit()+lessonData[k].getCredit()<=reqCredit) { //If the class is held during reqDate, Also, if adding this lesson does not exceed reqCredit
                disInfo = Distance.getDistance(userLs.getBuilding(), lessonData[k].getBuilding(), userLs.getBuildingNum(), lessonData[k].getBuildingNum());//check the distance
                    if (minDis > disInfo && inputTT.isCanPutIn(lessonData[k])) {//If the distance is less compared to the minimum distance, change the minimum distance.
                        minIndex = k;
                        minDis = disInfo;
                    }
            }
        }
        return  minIndex;
    }

    private static int queryAsNum(int userWant,lesson[] lessonData,int dataSize){ //Find lesson index by class number
        for(int k = 0;k<dataSize;k++) {
            if (lessonData[k].getLessonNum() == userWant)
                return k;
        }
        return  -1;
    }

}
/*데이터 set
인터페이스
shortest Travel Distance
days off
recommand
ppt 제작
input - 학수번호 3개 입력,
data set - 수업들 ( name, 교수이름, 강의 시간 ( 1~ 6 교시), 건물 ( 건물 번호 1~n ), 강의실(101 ~ 820) ), 학수번호(00000000) - file I/O 메모장 사용
output - 시간표 ( 수업들의 2차원 배열[5][8] ) - print 화면에 출력
시간표 - 수업 구조체[5][6]
main class {
input ( File I/O , 학수 번호 3개 입력)
시간표 객체화 3개
output
}
class distance { weight[n][n] -> 거리 객체에 하드코딩
}
class 수업 { } - 그냥 구조체
class 시간표 {
수업 2차원 배열 - instance로 소유
shortest Travel Distance  (input에서 학수번호 3개, weight[][] 배열 arguments, 시간표( 수업 2차원 배열 ) return)
days off (input에서 학수번호 3개 arguments, 시간표( 수업 2차원 배열 ) return)
recommand (input에서 학수번호 3개 arguments, 시간표( 수업 2차원 배열 ) return)
}*/
