import java.util.ArrayList;
import java.util.Arrays;

public class ProcessesSate {

    ArrayList<MyProcess> processes;

    public static void main(String[] args) {
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,3);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        ArrayList<ProcessAdv> processAdvs=ProcessAdv.advProcessFromNormalProcesses_Sort_FillTimes(processes);

        ProcessAdv.CalculateState(processAdvs);
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

        //calculate State
        public static void CalculateState(ArrayList<ProcessAdv> processAdvs){
            for (int i = 0; i <= maxProcessTime; i++) {
                System.out.println("Time : "+i);
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

                    System.out.println(p.id+" "+p.getProcessState());
                }

                System.out.println("---------------------------------------");
            }

        }

        public static ArrayList<ProcessAdv>  advProcessFromNormalProcesses_Sort_FillTimes(ArrayList<MyProcess> normalProcesses){
            MyProcess.SortByArivTime_ASC(normalProcesses);
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
