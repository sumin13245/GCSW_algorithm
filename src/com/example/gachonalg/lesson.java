package com.example.gachonalg;

class lesson {
    private String lessonName;
    private String professorName;
    private String date;
    private String building;
    private int buildingNum;
    private int lessonNum;
    private int credit;
    
    
    lesson(String lessonName, String professorName, String date, String building, int buildingNum, int lessonNum,int credit) {
        this.lessonName = lessonName;
        this.professorName = professorName;
        this.date = date;
        this.building = building;
        this.buildingNum = buildingNum;
        this.lessonNum = lessonNum;
        this.credit = credit;
    }

    public int dateToIndex(char[] temp) {//receive the value like 월1,화2... ,return the correct index
        int lessonIndex;
        //char[] temp = date.toCharArray();
        if (temp[0] == '월') lessonIndex = -1 + (temp[1] - '0');
            else if (temp[0] == '화') lessonIndex = 4 + (temp[1] - '0');
            else if (temp[0] == '수') lessonIndex = 9 + (temp[1] - '0');
            else if (temp[0] == '목') lessonIndex = 14 + (temp[1] - '0');
            else lessonIndex = 19 + (temp[1] - '0');

        return lessonIndex;
    }

    public String getLessonName() {
        return lessonName;
    }
    public String getProfessorName() {
        return professorName;
    }
    public String getDate() {
        return date;
    }
    public String getBuilding() {
        return building;
    }
    public int getBuildingNum() {
        return buildingNum;
    }
    public int getLessonNum() {
        return lessonNum;
    }
    public int getCredit(){
        return  credit;
    }
    public String toString() {
		String buildingNumToPrint;
    	if(buildingNum<0)buildingNumToPrint = "B"+(buildingNum*-1);
    	else buildingNumToPrint = Integer.toString(buildingNum);
    	return  lessonName+" "+professorName+" "+date+" "+building+" "+buildingNumToPrint+" "+lessonNum+" "+credit+"학점";
    }
}
