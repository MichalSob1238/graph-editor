package suite;

import java.sql.Array;
import java.util.Arrays;
import java.util.Optional;

public class ResultStore {
    public long[] elementTimes = new long[250];
    public long[] connectionTimes = new long[250];

    public void addElementTime(int index, long time) {
        long oldValue = elementTimes[index];
        if (oldValue != 0 ){
            elementTimes[index] = (oldValue + time)/2;
        } else {
            elementTimes[index] = time;
        }
    }
    public void addConnectionTime(int index, long time) {
        long oldValue = connectionTimes[index];
        if (oldValue != 0 ){
            connectionTimes[index] = (oldValue + time)/2;
        } else {
            connectionTimes[index] = time;
        }
    }
    public ResultStore() {
        Arrays.fill(elementTimes, 0);
        Arrays.fill(connectionTimes, 0);
    }

    public void printElementTimes() {
        //System.out.println("Nr;Czas");
        for (int i = 0; i < elementTimes.length; i++) {
            //System.out.println(i+";"+elementTimes[i]);
        }
    }

    public void printConnectionTimes() {
        //System.out.println("Nr;Czas");
        for (int i = 0; i < connectionTimes.length; i++) {
            //System.out.println(i+";"+connectionTimes[i]);
        }
    }
}
