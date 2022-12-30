import java.util.*;
import java.util.function.Function;

public class WithPriority {


    public static void main(String[] args) {
//            MyProcess p1=new MyProcess("p1",0,16,3);
//            MyProcess p2=new MyProcess("p2",0,9,2);
//            MyProcess p3=new MyProcess("p3",0,4,2);
//            MyProcess p4=new MyProcess("p4",1,3,1);
//            MyProcess p5=new MyProcess("p5",0,1,0);


        MyProcess p1=new MyProcess("p1",0,2,0);
        MyProcess p2=new MyProcess("p2",1,4,0);
        MyProcess p3=new MyProcess("p3",2,5,3);
        MyProcess p4=new MyProcess("p4",4,4,1);
 //     for make idle for 2 times (will be idle form 11 to 13)
//        MyProcess p4=new MyProcess("p4",13,4,1);


        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4));

        calculatePriority_Primitive_WITH_WAIT_TURN_AVG_IDLE(processes);

//        calculatePriority_SameTime_Non_Primitive(processes);


    }

    public static  <T extends MyProcess> void calculatePriority_Primitive_WITH_WAIT_TURN_AVG_IDLE(ArrayList<T> processes){

        ArrayList<T> endedProcesses=new ArrayList<>();

        //sort processes by arrive time
        MyProcess.SortByArivTime_ASC(processes);

        //create ready queue (smaller priority value mean its high priority)
        PriorityQueue<T> priorityQueue_ReadyQueue=new PriorityQueue<>((p1,p2)->{
            return p1.priority-p2.priority;
        });

        int realTime=0;

        int totalIdleTime=0;

        //set current Running Process
        T currentRunningProcess=null;

        while (!processes.isEmpty()||!priorityQueue_ReadyQueue.isEmpty()||currentRunningProcess!=null){
            //add all process to queue in currentTime
            while (!processes.isEmpty()&&processes.get(0).ariveTime<=realTime){
                T p=processes.remove(0);
                priorityQueue_ReadyQueue.offer(p);
            }

            // If the queue is empty, move the current time to the arrival time of the next process
            //cpu is idle
            if (priorityQueue_ReadyQueue.isEmpty()&&!processes.isEmpty()&&currentRunningProcess==null) {
                int temp=realTime;
                realTime = processes.get(0).ariveTime;
                totalIdleTime+=realTime-temp;
                continue;
            }
            //check if the priority in queue is higher than current running
            //interruption
            if(currentRunningProcess!=null&&!priorityQueue_ReadyQueue.isEmpty()
                    &&currentRunningProcess.priority>priorityQueue_ReadyQueue.peek().priority){

                priorityQueue_ReadyQueue.offer(currentRunningProcess);
                //make currentRunning is head of queue and remove it from queue
                currentRunningProcess=priorityQueue_ReadyQueue.poll();

            }

            //if there is no running process pick one from priority
            if(currentRunningProcess==null){
                if(!priorityQueue_ReadyQueue.isEmpty()){
                    currentRunningProcess=priorityQueue_ReadyQueue.poll();
                }
            }
            //decrees burst time
            if(currentRunningProcess!=null){
                currentRunningProcess.toDoneTime--;
            }

            System.out.println("End of Time "+realTime);
            if(currentRunningProcess!=null){
                System.out.println("Current Running Process is "+currentRunningProcess);

            }
            System.out.println("-----------------------------");

            //check if current running process is ended
            if(currentRunningProcess!=null&&currentRunningProcess.toDoneTime<=0){
                endedProcesses.add(currentRunningProcess);
                currentRunningProcess=null;
            }

            //update wait time for all process in queue
            for (T waitedProcess:priorityQueue_ReadyQueue) {
                waitedProcess.waitTime++;
            }
            realTime++;
        }

        //calculate avg for wait time and avg for turnAround time
        double totalWaitTime = 0,totalTurnAroundTime=0;
        for (T p:endedProcesses) {
            p.turnAroundTime=p.waitTime+ p.getStaticBurstTime();
            totalWaitTime+=p.waitTime;
            totalTurnAroundTime+=p.turnAroundTime;
        }

        double waitAvg=totalWaitTime/endedProcesses.size();
        double turnAroundAvg=totalTurnAroundTime/endedProcesses.size();

        System.out.println("Total Cpu Idle Time :"+totalIdleTime);
        System.out.println("Wait Time Avg :"+waitAvg);
        System.out.println("Turn Around Time Avg :"+turnAroundAvg);

    }

        public static  <T extends MyProcess> void calculatePriority_Primitive(ArrayList<T> processes){
        //start sorting by arive time groups and each group is sorted by priority
        TreeMap<Integer,ArrayList<T>> allProcesses=new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        for (T p:processes) {
            if(!allProcesses.containsKey(p.ariveTime)){
               allProcesses.put(p.ariveTime,new ArrayList<>(Arrays.asList(p)));
            }else{
                allProcesses.get(p.ariveTime).add(p);
            }
        }

        //sort Groups
        for (ArrayList<T> list: allProcesses.values()) {
            MyProcess.SortByPriority_ASC(list);
        }
        //end of sorting by groups
        Function<T,Boolean> getIFNotDone=new Function<T, Boolean>() {
            @Override
            public Boolean apply(T t) {
                if(t.toDoneTime>0){
                    return true;
                }
                return false;
            }
        };

        T currentProcess=getFirstOrDefaultProcess(processes, getIFNotDone);
        if(currentProcess==null){
            return;
        }
        //start algorithem
        HashSet<T> endedProcesses=new HashSet<>();
        ArrayList<T> inturptedProcesses=new ArrayList<>();
        int totalProcessCount=processes.size();
        int realProcessorTime=0;
        while (endedProcesses.size()<totalProcessCount){
            if(realProcessorTime==9){
                System.out.println();
            }

            if(currentProcess.toDoneTime<=0){
                endedProcesses.add(currentProcess);

            }
            if(allProcesses.containsKey(realProcessorTime)){
                if(currentProcess.toDoneTime<=0){
                    endedProcesses.add(currentProcess);
                }
                //inteption
                ArrayList<T> processInCurrentCpuTime=allProcesses.get(realProcessorTime);
                final T tempCurrent = currentProcess;
              T newRunningProcess= getFirstOrDefaultProcess(processInCurrentCpuTime, new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t) {
                        if(t.toDoneTime>0&&tempCurrent.priority<t.priority&&t!=tempCurrent){
                            return true;
                        }
                        return false;
                    }
                });

               //add process to inturpted process expet newProcessToRun
                for (T sameTime:processInCurrentCpuTime) {
                    if(sameTime!=newRunningProcess&&sameTime.toDoneTime>0){
//                        inturptedProcesses.add(sameTime);
                        MyProcess.AddWithouDuplicate(inturptedProcesses, sameTime, new MyProcess.ProcessCompartor<T>() {
                            @Override
                            public boolean onCompaerProcesses(T p1, T p2) {
                                return p1.id.equals(p2.id);
                            }
                        });

                    }
                }
                MyProcess.SortByPriority_ASC(inturptedProcesses);

                //check for switch if new is null thats mean
                if(newRunningProcess!=null){
                    //switch
                    currentProcess=newRunningProcess;
                }
                //decress currentProcess burstTime
            }
            if(currentProcess!=null&&currentProcess.toDoneTime<=0){
                endedProcesses.add(currentProcess);
                MyProcess.SortByPriority_ASC(inturptedProcesses);
                T newRunningProcess= getFirstOrDefaultProcess(inturptedProcesses, new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t) {
                        if(t.toDoneTime>0){
                            return true;
                        }
                        return false;
                    }
                });
                if(newRunningProcess!=null){
                    currentProcess=newRunningProcess;
                }

            }


            //print

            System.out.println("Time : "+realProcessorTime);
            System.out.println("----------------------------");
            for (T p:processes) {
                System.out.println(p);
            }



            realProcessorTime++;
            currentProcess.toDoneTime-=1;

        }



    }

    public static <T extends MyProcess> T getFirstOrDefaultProcess(ArrayList<T> processes, Function<T,Boolean> byFunction){
        for (T p:processes) {
            if(byFunction.apply(p)){
                return p;
            }
        }
        return null;

    }

    public static  <T extends MyProcess> void calculatePriority_SameTime_Non_Primitive(ArrayList<T> processes){
        MyProcess.SortByPriority_ASC(processes);
        MyProcess.WaitTimesAndProccesTimes(processes);
    }
}
