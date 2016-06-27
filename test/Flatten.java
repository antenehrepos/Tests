package test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaklilu on 6/27/16.
 */
public class Flatten {

    public static void main(String[] args){


        System.out.println("Test [[1,2,[3]],4] -> [1,2,3,4]");
        Object[] nestedArray = new Object[]{
                new Object[]{1,2, new Object[]{3}}, 4
        };
        test(nestedArray);
        System.out.println("----------------------------------");

        nestedArray =new Object[]{
                new Object[]{new Object[]{1,2,3}, new Object[]{4, new Object[]{5},6, new Object[]{7, new Object[]{8}}}, 9}, 10
        };

        System.out.println("Test [[[1,2,3],[4,[5],6,[7, [8]]], 9], 10] -> [1,2,3,4,5,6,7,8,9,10]");
        test(nestedArray);
        System.out.println("----------------------------------");
    }

    private static void test(Object[] nestedArray){

        Integer[] flattenArray =  new Flatten().flattenArray(nestedArray);
        System.out.print("[");
        for(int i=0; i< flattenArray.length; i++){
            System.out.print(String.format("%s%d",(i>0? ",": ""), flattenArray[i]));
        }
        System.out.println("]\n");
    }

    private Integer [] flattenArray(Object[] nestedArray){

        if(nestedArray.length == 0){

            return new Integer []{};
        }

        List<Integer> items = new ArrayList<>();

        for(Object obj: nestedArray){

            if(obj instanceof Integer){

                items.add(Integer.valueOf(obj.toString()));

            }else{

                Integer[] innerItems = flattenArray((Object[])obj);

                for(Integer item: innerItems){

                    items.add(item);
                }
            }
        }

        return items.toArray(new Integer[items.size()]);
    }
}
