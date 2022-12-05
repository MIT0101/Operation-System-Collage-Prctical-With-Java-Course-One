
import java.util.*;


public class ProcessesSate {


    public static void main(String[] args) {
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,3);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        MyProcess.SortByArivTime_ASC(processes);
         MyProcess.SortByBursTimeGroups(processes);
        ArrayList<ProcessAdv> processAdvs=ProcessAdv.advProcessFromNormalProcesses_FillTimes(processes);

        ProcessAdv.CalculateStateWithPrint(processAdvs);

        for (ProcessAdv pAdv:processAdvs) {
            System.out.println(pAdv);
        }
    }

    public static class ProcessAdv extends MyProcess {

        private ProcessState processState;
        private int waitTime=-1;
        private  int precessorTime=-1;
        private static int maxProcessTime=-1;

        public ProcessState getProcessState() {
            return processState;
        }

        public void setProcessState(ProcessState processState) {
            this.processState = processState;
        }

        public int getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }

        public int getPrecessorTime() {
            return precessorTime;
        }

        public void setPrecessorTime(int precessorTime) {
            this.precessorTime = precessorTime;
        }

        public ProcessAdv(String id, int ariveTime, int toDoneTime, ProcessState processState) {
            super(id,ariveTime,toDoneTime);
            this.processState = processState;
        }

        public ProcessAdv(MyProcess myProcess, ProcessState processState) {
            super(myProcess.id,myProcess.ariveTime,myProcess.toDoneTime);
            this.processState = processState;
        }

        //calculate processes State must sorted
        public static void CalculateStateWithPrint(ArrayList<ProcessAdv> processAdvs){
            CalculateState(processAdvs,defaultStateListener_Print());
        }


        //new Algo

//        public static void  WaitTimesAndProccesTimes_New_Cuted(ArrayList<MyProcess> processes){
//            MyProcess.SortByArivTime_ASC(processes);
//            int size= processes.size();
//            int cpuTime=0;
//            for (int i = 0; i < size; i++) {
//                MyProcess current=processes.get(i);
//                int waitTime=cpuTime-current.ariveTime;
//                MyProcess nextProcess=null;
//                if(i+1<size){
//                    nextProcess=processes.get(i+1);
//                }
//                cpuTime+=cpuTime+current.toDoneTime;
//                MyProcess.SortByBursTimeGroups(processes);
//
//            }
//
//        }
public static void WaitTimesAndProccesTimes_New_Cuted3(ArrayList<ProcessAdv> processAdvs,ProcessesStateChangeListener processesS){
    ArrayList<ProcessAdv> intuptedProcess=new ArrayList<>();
    HashSet<String> endedProcesses=new HashSet<>();

    int startIndex=0;
    int size=processAdvs.size();
    ProcessAdv current=processAdvs.get(startIndex);
    ProcessAdv nextProcess=null;
    ProcessAdv currentRunning=null;
    int maxCpuTime=current.toDoneTime;
    current.setProcessState(ProcessState.Run);
    for (int realCpuTime = 0; realCpuTime <= maxCpuTime&&endedProcesses.size()!=size; realCpuTime++) {
        if(realCpuTime==4){
            System.out.print("");
        }
        if(startIndex+1<size){
            nextProcess=processAdvs.get(startIndex+1);
        }else{
            nextProcess=null;
        }

        if (nextProcess != null && nextProcess.ariveTime == realCpuTime) {
            //interption
            current.setProcessState(ProcessState.Ready);
            MyProcess.AddWithouDuplicate(intuptedProcess,current,(p1,p2)->{
                return p1.id.equals(p2.id);
            });
            MyProcess.AddWithouDuplicate(intuptedProcess,nextProcess,(p1,p2)->{
                return p1.id.equals(p2.id);
            });
            //sort interpted process
            MyProcess.SortByBursTime_ASC(intuptedProcess);
            //pic process by mini burtime
            ProcessAdv processToRun=null;
            int i=0;
            if(intuptedProcess.size()>0){
                for (ProcessAdv processInterpted:intuptedProcess) {
                    if(processInterpted.getProcessState()!=ProcessState.End){
                        processToRun=processInterpted;
                        break;
                    }
                    i++;
                }
            }
            if(i>=processAdvs.size()||processToRun==null){
                continue;
            }
            processToRun.setProcessState(ProcessState.Run);
            currentRunning=processToRun;
            //add without dublicate id
//            intuptedProcess.add(current);
//            MyProcess.AddWithouDuplicate(intuptedProcess,current,(p1,p2)->{
//                return p1.id.equals(p2.id);
//            });
            current=processToRun;
            current.toDoneTime-=1;
            maxCpuTime+=current.toDoneTime;
            startIndex++;

        }else if(current.toDoneTime > 0){
            //now its running normal
            current.setProcessState(ProcessState.Run);
            currentRunning = current;
            current.toDoneTime-=1;
            current.setPrecessorTime(Math.max(0,current.getPrecessorTime())+1);
        }else if(current.toDoneTime<=0) {
            //now process is ended
            //now pic one from interputed process baised on min burs time
            current.setProcessState(ProcessState.End);
            endedProcesses.add(current.id);
//          current.setPrecessorTime(Math.min(Math.max(-1,current.getPrecessorTime()),realCpuTime));
            intuptedProcess.remove(current);
            startIndex++;
            currentRunning = null;
            //here resort interputed
            MyProcess.SortByBursTime_ASC(intuptedProcess);
            if (intuptedProcess.size() > 0) {
                int inturptIndex = 0;
                current = intuptedProcess.get(inturptIndex);
                while (current.toDoneTime <= 0 && inturptIndex < intuptedProcess.size()) {
                    current=intuptedProcess.get(inturptIndex);
                    inturptIndex++;
                }
                if (inturptIndex<intuptedProcess.size()){
                    currentRunning = current;
                    current.toDoneTime--;
                    current.setProcessState(ProcessState.Run);
                    maxCpuTime+=current.toDoneTime;
                    startIndex--;
                }
            }

        }
        processesS.OnProcessesStateChange(realCpuTime,processAdvs);
    }

    if(intuptedProcess.size()>0){

        MyProcess.SortByBursTime_ASC(intuptedProcess);

        for (ProcessAdv inturpted:intuptedProcess) {
            if(inturpted.toDoneTime>0){
                maxCpuTime+=inturpted.toDoneTime;
            }
        }

    }
    for (ProcessAdv processAdv:processAdvs) {
        if(processAdv.toDoneTime>0&&processAdv.getProcessState()!=ProcessState.End){
            maxCpuTime+=processAdv.toDoneTime;
        }
    }

}

        //must sort by arrive time
        public static void WaitTimesAndProccesTimes_New_Cuted2(ArrayList<ProcessAdv> processAdvs,ProcessesStateChangeListener processesS){
            ArrayList<ProcessAdv> intuptedProcess=new ArrayList<>();
            HashSet<String> endedProcesses=new HashSet<>();

            int startIndex=0;
            int size=processAdvs.size();
            ProcessAdv current=processAdvs.get(startIndex);
            ProcessAdv nextProcess=null;
            ProcessAdv currentRunning=null;
            int maxCpuTime=current.toDoneTime;
            current.setProcessState(ProcessState.Run);
            for (int realCpuTime = 0; realCpuTime <= maxCpuTime&&endedProcesses.size()!=size; realCpuTime++) {
                if(startIndex+1<size){
                    nextProcess=processAdvs.get(startIndex+1);
                }else{
                    nextProcess=null;
                }


                if (nextProcess != null && nextProcess.ariveTime == realCpuTime) {
                    //interption
                    nextProcess.setProcessState(ProcessState.Run);
                    currentRunning=nextProcess;
                    current.setProcessState(ProcessState.Ready);
                    //add without dublicate id
//                    intuptedProcess.add(current);
                    MyProcess.AddWithouDuplicate(intuptedProcess,current,(p1,p2)->{
                        return p1.id.equals(p2.id);
                    });
                    current=nextProcess;
                    current.toDoneTime-=1;
                    maxCpuTime+=current.toDoneTime;

                    startIndex++;

                }else if(current.toDoneTime > 0){
                    //now its running
                    current.setProcessState(ProcessState.Run);
                    currentRunning = current;
                    current.toDoneTime-=1;
                    current.setPrecessorTime(Math.max(0,current.getPrecessorTime())+1);
                }else if(current.toDoneTime<=0) {
                    //now process is ended
                    //now pic one from interputed process baised on min burs time
                    current.setProcessState(ProcessState.End);
                    endedProcesses.add(current.id);
//                    current.setPrecessorTime(Math.min(Math.max(-1,current.getPrecessorTime()),realCpuTime));
                    intuptedProcess.remove(current);
                    startIndex++;
                    currentRunning = null;
                    //here sore interputed
                    MyProcess.SortByBursTime_ASC(intuptedProcess);
                    if (intuptedProcess.size() > 0) {
                        int inturptIndex = 0;
                        current = intuptedProcess.get(inturptIndex);
                        while (current.toDoneTime <= 0 && inturptIndex < intuptedProcess.size()) {
                            current=intuptedProcess.get(inturptIndex);
                            inturptIndex++;
                        }
                        if (inturptIndex<intuptedProcess.size()){
                            currentRunning = current;
                            current.toDoneTime--;
                            current.setProcessState(ProcessState.Run);
                            maxCpuTime+=current.toDoneTime;
                            startIndex--;
                        }
                    }

                }
                processesS.OnProcessesStateChange(realCpuTime,processAdvs);

            }

            if(intuptedProcess.size()>0){

                MyProcess.SortByBursTime_ASC(intuptedProcess);

                for (ProcessAdv inturpted:intuptedProcess) {
                    if(inturpted.toDoneTime>0){
                        maxCpuTime+=inturpted.toDoneTime;
                    }
                }

            }
            for (ProcessAdv processAdv:processAdvs) {
                if(processAdv.toDoneTime>0&&processAdv.getProcessState()!=ProcessState.End){
                    maxCpuTime+=processAdv.toDoneTime;
                }
            }

        }
        public static void WaitTimesAndProccesTimes_New_Cuted(ArrayList<ProcessAdv> processAdvs,ProcessesStateChangeListener processesStateChangeListener){
            ProcessAdv currentRunning=null;
            ProcessAdv nextProcess=null;
            int cpuTime=0;
            int size=processAdvs.size();
            for (int i = 0; i <size; i++) {
                ProcessAdv current=processAdvs.get(i);
                int waitTime=cpuTime-current.ariveTime;
                if(i+1<size){
                    nextProcess=processAdvs.get(i+1);
                }
                int workTime=current.toDoneTime;

                if(current.getProcessState()!=ProcessState.End) {

                    for (int j = 1; j <= workTime; j++) {
                        if (nextProcess != null && nextProcess.ariveTime == cpuTime) {
                            //interption
                            nextProcess.setProcessState(ProcessState.Run);
                            currentRunning=nextProcess;

                            current.setProcessState(ProcessState.Ready);
//                            nextProcess.toDoneTime--;

                            //resort items
                            MyProcess.SortByBursTimeGroups(processAdvs);
//                             i=0;
                            break;
                        } else if (current.toDoneTime > 0) {
                            current.setProcessState(ProcessState.Run);
                            currentRunning = current;
                            current.toDoneTime--;
                            current.setPrecessorTime(Math.max(0,current.getPrecessorTime())+1);
                            cpuTime++;
                        } else {
                            //here make cpu time like main cpu time
                            current.setProcessState(ProcessState.End);
                            currentRunning=null;
                            MyProcess.SortByBursTime_ASC(processAdvs);
//                            i=0;
                            break;
                        }
                    }
                }


            }

            System.out.println(cpuTime);

//            processesStateChangeListener.OnProcessesStateChange(i,processAdvs);


        }


        //calculate processes State
        public static void CalculateState(ArrayList<ProcessAdv> processAdvs,ProcessesStateChangeListener processesStateChangeListener){
            for (int i = 0; i <= maxProcessTime; i++) {
//                System.out.println("Time : "+i);
                for (ProcessAdv p:processAdvs) {

                    if(p.ariveTime==i){
                            p.setProcessState(ProcessState.New);
                    }
                    if(p.ariveTime<i&&(p.ariveTime+p.getWaitTime())>i&&p.getProcessState()==ProcessState.New){
                        p.setProcessState(ProcessState.Ready);
                    }
                    if((p.ariveTime+p.getWaitTime())<i&&i<p.precessorTime){
                        p.setProcessState(ProcessState.Run);
                    }
                    if(p.precessorTime<=i){
                        p.setProcessState(ProcessState.End);
                    }

//                    System.out.println(p.id+" "+p.getProcessState());
                }

                processesStateChangeListener.OnProcessesStateChange(i,processAdvs);
//                System.out.println("---------------------------------------");
            }

        }

        //for default Proccess Printer printer
        public static ProcessesStateChangeListener defaultStateListener_Print(){
            return (time,processAdvs)->{
                    System.out.println("Time : "+time);
                    for (ProcessAdv p:processAdvs) {
                        System.out.println(p.id+" "+p.getProcessState());
                    }
                    System.out.println("---------------------------------------");
            };
        }
        //for apply event after preocesses state are adjested
        @FunctionalInterface
        public interface ProcessesStateChangeListener{
            void OnProcessesStateChange(int time,ArrayList<ProcessAdv> processAdvs);
        }
        public static ArrayList<ProcessAdv>  advProcessFromNormalProcesses_FillTimes_NoCpuTime(ArrayList<MyProcess> normalProcesses){
            ArrayList<ProcessAdv> processAdvs=new ArrayList<>();

            for (MyProcess p: normalProcesses) {
                ProcessAdv pAdv=new ProcessAdv(p,ProcessState.Null);
                processAdvs.add(pAdv);
            }

            return processAdvs;
        }

        //for preper data befor applay state Clcukator
        public static ArrayList<ProcessAdv>  advProcessFromNormalProcesses_FillTimes(ArrayList<MyProcess> normalProcesses){
            ArrayList<ProcessAdv> processAdvs=new ArrayList<>();

            int cpuTime=0;
            for (MyProcess p: normalProcesses) {
                ProcessAdv pAdv=new ProcessAdv(p,ProcessState.Null);
                int waitTime=cpuTime-p.ariveTime;
                pAdv.setWaitTime(waitTime);
                int proceesCpuTime=(cpuTime+pAdv.toDoneTime);
                pAdv.setPrecessorTime(proceesCpuTime);
                cpuTime+=pAdv.toDoneTime;
                processAdvs.add(pAdv);
            }
            maxProcessTime=cpuTime;

            return processAdvs;
        }

        @Override
        public String toString(){
            return super.toString()+" wait Time : "+this.getWaitTime()+" cpu Time  "
                    +this.getPrecessorTime() +" State : "+this.getProcessState().toString();
        }
    }

    public enum ProcessState{
        Null,
        New,
        Wait,
        Ready,
        Run,
        End

    }
}
