import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public  class MyProcess{
    String id;
    int ariveTime;
    int toDoneTime;

    public MyProcess(String id, int ariveTime, int toDoneTime) {
        this.id = id;
        this.ariveTime = ariveTime;
        this.toDoneTime = toDoneTime;
    }

    public static void SortByArivTime_ASC(ArrayList<MyProcess> processes) {
        Collections.sort(processes, new Comparator<MyProcess>() {
            @Override
            public int compare(MyProcess o1, MyProcess o2) {
                return o1.ariveTime-o2.ariveTime;
            }
        });

    }

    public static void SortByArivTime_DEC(ArrayList<MyProcess> processes) {
        Collections.sort(processes, new Comparator<MyProcess>() {
            @Override
            public int compare(MyProcess o1, MyProcess o2) {
                return o2.ariveTime-o1.ariveTime;
            }
        });

    }

    @Override
    public String toString() {
        return "id : "+this.id+" arive time : "+this.ariveTime+" to doneTime : "+this.toDoneTime;
    }

    public static void PrintProcesses(ArrayList<MyProcess> processes){
        for (MyProcess p: processes) {
            System.out.println(p);
        }
    }

    //
    public static void  WaitTimesAndProccesTimes(ArrayList<MyProcess> processes){
        int cpuTime=0;
        for (MyProcess p: processes) {
            int waitTime=cpuTime-p.ariveTime;
            System.out.print("Wait Time "+p.id+" => "+waitTime);
            System.out.println(" , Cpu Time "+p.id+" => "+(cpuTime+p.toDoneTime));
            cpuTime+=p.toDoneTime;
        }

    }
}