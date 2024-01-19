import java.util.*;

public class Exam {

    private static int calculateNumberOfPoints(int[] center, long d){
        Arrays.sort(center);
        //Need to keep count for each index :
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int index = 0; index < center.length; ++index){
            map.put(center[index], map.getOrDefault(center[index], 0) + 1);
        }
        //1. Calculate the distance from the center :
        //d[x] = d[0] + 2*(m - n)*(x[i] - x[0]) :
        //Initial d[0] :
        long[] distanceArr = new long[center.length];
        for(int index = 1; index < distanceArr.length; ++index){
            distanceArr[0] += 2*(center[index] - center[0]);
        }

        int leftElements = 1;
        int rightElements = center.length - 1;
        for(int index = 1; index < center.length; ++index){
            distanceArr[index] = distanceArr[index - 1] + 2*(leftElements - rightElements)*(center[index] - center[index - 1]);
            leftElements++;
            rightElements--;
        }
        //Calculate the number of points :
        int result = 0;
        leftElements = 1;
        rightElements = center.length - 1;
        for(int index = 0; index < center.length; ++index){
            int minRange = center[index];
            int maxRange = (index + 1 < center.length)?center[index + 1]:1_000_000_000;
            //Required x :
            if(leftElements > rightElements){
                int distance = minRange + (int)Math.floor((double)(d - distanceArr[index])/(2*(leftElements - rightElements)));
                maxRange = Math.min(maxRange, distance);
                if(minRange <= maxRange){
                    result += maxRange - minRange + 1;
                }
            }else if(leftElements == rightElements){
                if(distanceArr[leftElements] <= d){
                    result += maxRange - minRange + 1;
                }
            }else{
                int distance = minRange + (int)Math.ceil(((double)(d - distanceArr[index]))/(2*(leftElements - rightElements)));
                minRange = Math.max(minRange, distance);
                if(maxRange >= minRange){
                    result += maxRange - minRange + 1;
                }
            }
            if(index < center.length - 1){
                leftElements += map.get(center[index + 1]);
                rightElements -= map.get(center[index + 1]);
                index += map.get(center[index + 1]) - 1;
            }
        }
        //Now, there are Elements which will be present at the exact points :
        //Prevent Double Count
        for(int index = 0; index < distanceArr.length; ++index){
            if(distanceArr[index] <= d)result--;
            index += map.get(center[index]) - 1;
        }
        //Edge Case :
        int minRange = 0;
        int maxRange = center[0];
        maxRange = (int)Math.min(center[0], (d - distanceArr[0])/(2*center.length));
        if(maxRange >= minRange){
            result += maxRange - minRange + 1;
            if(distanceArr[0] <= d)result--;
        }

        System.out.println(Arrays.toString(distanceArr));
        return result;
    }

    public static void main(String[] args){

        System.out.println(calculateNumberOfPoints(new int[]{-3, 2, 2}, 8));
    }
}
