import java.util.*;

public  class MyProcess{
    public static void main(String[] args) {
//        MyProcess p1=new MyProcess("p1",2,6);
//        MyProcess p2=new MyProcess("p2",5,3);
//        MyProcess p3=new MyProcess("p3",1,8);
//        MyProcess p4=new MyProcess("p4",0,3);
//        MyProcess p5=new MyProcess("p5",4,4);

        MyProcess p1=new MyProcess("p1",0,7);
        MyProcess p2=new MyProcess("p2",2,4);
        MyProcess p3=new MyProcess("p3",4,1);
        MyProcess p4=new MyProcess("p4",5,4);
        MyProcess p5=new MyProcess("p5",0,2);
        MyProcess p6=new MyProcess("p6",2,1);




        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5,p6));
        MyProcess.SortByArivTime_ASC(processes);
        MyProcess.WaitTimesAndProccesTimes(processes);
        System.out.println("-----------------------------");
        WaitTimesAndProccesTimesSJF(processes);
    }

    String id;
    int ariveTime;
    int toDoneTime;
    int priority;
    int waitTime;
    int turnAroundTime;

    public int getStaticBurstTime() {
        return staticBurstTime;
    }

    private int staticBurstTime;

    public MyProcess(String id, int ariveTime, int toDoneTime,int priority) {
        this.id = id;
        this.ariveTime = ariveTime;
        this.toDoneTime = toDoneTime;
        this.priority=priority;
        staticBurstTime=toDoneTime;
    }
    public MyProcess(String id, int ariveTime, int toDoneTime) {
        this.id = id;
        this.ariveTime = ariveTime;
        this.toDoneTime = toDoneTime;
        staticBurstTime=toDoneTime;
    }

//add withoud duplicate based on
    public static<T extends MyProcess> void AddWithouDuplicate(ArrayList<T> processes,T newProcess,ProcessCompartor<T> compartor){
        for (T p:processes) {
            if(compartor.onCompaerProcesses(p,newProcess)){
                return;
            }
        }
        processes.add(newProcess);
    }
    @FunctionalInterface
    public interface ProcessCompartor <T extends MyProcess>{
        boolean onCompaerProcesses(T p1,T p2);
    }
    public static <T extends MyProcess> void SortByArriveAndPriorityGroups(ArrayList<T> processes){
        //must be sort by aarive time
        MyProcess.SortByArivTime_ASC(processes);
        ArrayList<T> finalResult=new ArrayList<>();
        Iterator<T> myProcessIterator= processes.iterator();
        ArrayList<T> group=new ArrayList<>();

        int lastArriveTime=Integer.MIN_VALUE;
        while (myProcessIterator.hasNext()){
            T myProcess=myProcessIterator.next();
            //group changed must sort
            if(lastArriveTime!=Integer.MIN_VALUE&&lastArriveTime!=myProcess.ariveTime){
                MyProcess.SortByPriority_ASC(group);
                finalResult.addAll(group);
                group.clear();
                group.add(myProcess);

            }
            else{
                group.add(myProcess);
            }
            lastArriveTime=myProcess.ariveTime;
//            myProcessIterator.remove();
        }
        if(group.size()>0){
            MyProcess.SortByPriority_ASC(group);
            finalResult.addAll(group);
        }
        processes=finalResult;
    }


//sort by arrive time groups
    public static <T extends MyProcess> void SortByBursTimeGroups(ArrayList<T> processes){
        ArrayList<T> finalResult=new ArrayList<>();
        Iterator<T> myProcessIterator= processes.iterator();
        ArrayList<T> group=new ArrayList<>();

        int lastArriveTime=Integer.MIN_VALUE;
        while (myProcessIterator.hasNext()){
            T myProcess=myProcessIterator.next();
            //group changed must sort
            if(lastArriveTime!=Integer.MIN_VALUE&&lastArriveTime!=myProcess.ariveTime){
                MyProcess.SortByBursTime_ASC(group);
                finalResult.addAll(group);
                group.clear();
                group.add(myProcess);

            }
            else{
                group.add(myProcess);
            }
            lastArriveTime=myProcess.ariveTime;
//            myProcessIterator.remove();
        }
        if(group.size()>0){
            MyProcess.SortByBursTime_ASC(group);
            finalResult.addAll(group);
        }
        processes=finalResult;
    }

    public static <T extends MyProcess> void SortByPriority_ASC(ArrayList<T> processes){
        Collections.sort(processes, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) { return o1.priority-o2.priority; }
        });
    }

    public static <T extends MyProcess> void SortByBursTime_ASC(ArrayList<T> processes){
        Collections.sort(processes, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) { return o1.toDoneTime-o2.toDoneTime; }
        });
    }

    public static <T extends MyProcess> void SortByArivTime_ASC(ArrayList<T> processes) {
        Collections.sort(processes, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) { return o1.ariveTime-o2.ariveTime; }
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
        return "id : "+this.id+" arive time : "+this.ariveTime+" to doneTime : "+this.toDoneTime +" Priority : "+this.priority;
    }

    public static void PrintProcesses(ArrayList<MyProcess> processes){
        for (MyProcess p: processes) {
            System.out.println(p);
        }
    }

    public static void  WaitTimesAndProccesTimesSJF(ArrayList<MyProcess> processes) {
       MyProcess.SortByBursTimeGroups(processes);
        MyProcess.WaitTimesAndProccesTimes(processes);
    }

        //
    public static <T extends MyProcess> void  WaitTimesAndProccesTimes(ArrayList<T> processes){
        int cpuTime=0;
        for (T p: processes) {
            int waitTime=cpuTime-p.ariveTime;
            System.out.print("Wait Time "+p.id+" => "+waitTime);
            System.out.println(" , Cpu Time "+p.id+" => "+(cpuTime+p.toDoneTime));
            cpuTime+=p.toDoneTime;
        }

    }
}