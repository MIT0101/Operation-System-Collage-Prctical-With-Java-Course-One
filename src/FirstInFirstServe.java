import java.util.*;

public class FirstInFirstServe {

    public static void main(String[] args) {
        MyProcess p1=new MyProcess("p1",3,6);
        MyProcess p2=new MyProcess("p2",6,3);
        MyProcess p3=new MyProcess("p3",2,8);
        MyProcess p4=new MyProcess("p4",1,3);
        MyProcess p5=new MyProcess("p5",5,4);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));

//        ArrayList<Process> processes=new ArrayList<>();
//        processes.add(p1);
//        processes.add(p2);
//        processes.add(p3);
//        processes.add(p4);
//        processes.add(p5);


        //Print processes before sorting
        MyProcess.PrintProcesses(processes);
        //sort Processes
        MyProcess.SortByArivTime_ASC(processes);
        System.out.println("After Sort--------------------------");

        MyProcess.PrintProcesses(processes);
        MyProcess.WaitTimesAndProccesTimes(processes);


    }

    public FirstInFirstServe() {
    }


}
