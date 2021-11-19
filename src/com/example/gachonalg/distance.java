package com.example.gachonalg;
public class distance {

    private double getDistance(String src, String des){
        double distance;

        switch (src){
            case "비전타워": // 출발지
                if(des.equals("IT대학"))
                    distance = 1.0;
                else if(des.equals("가천관"))
                    distance = 2.0;
                else if(des.equals("글로벌센터"))
                    distance = 1.5;
                else
                    distance = 0.0;
                break;
            case "IT대학":
                if(des.equals("비전타워"))
                    distance = 1.0;
                else if(des.equals("가천관"))
                    distance = 1.5;
                else if(des.equals("글로벌센터"))
                    distance = 0.5;
                else
                    distance = 0.0;
                break;
            case "가천관":
                if(des.equals("비전타워"))
                    distance = 2.0;
                else if(des.equals("IT대학"))
                    distance = 1.5;
                else if(des.equals("글로벌센터"))
                    distance = 1.7;
                else
                    distance = 0.0;
                break;
            case "글로벌센터":
                if(des.equals("비전타워"))
                    distance = 1.5;
                else if(des.equals("가천관"))
                    distance = 1.7;
                else if(des.equals("IT대학"))
                    distance = 0.5;
                else
                    distance = 0.0;
                break;
            default:
                distance = 0.0;
                break;
        }
        return distance;
    }
}
