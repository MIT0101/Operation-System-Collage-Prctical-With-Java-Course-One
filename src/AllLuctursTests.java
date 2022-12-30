import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AllLuctursTests {
    static String TEST_TEXT="Mohammed2";
    public static void main(String[] args) {
        System.out.println("This Is Test Class Test Is Started ....");
        printLine();
        //for Lecture One
        Write_Read_Test_L1();
        printLine();
        System.out.println("PROCESS WAIT AND CPU TIME TEST .....");
        ProcessWaitAndCpuTimeTest();
        printLine();
        System.out.println("PROCESS STATE TEST .....");
        ProcessStateTest();
        printLine();
        System.out.println("PROCESS STATE WITH NEW ALGO (INTERPUT) TEST ....");
        ProcessStateWithInterputTest();
        printLine();
//        System.out.println("PROCESS STATE WITH SJF PRIMITIVE ALGO (INTERPUT) TEST ....");
//        ProcessWaitAndCpuTimeUsingSJF_PRIMITVETest();
//        printLine();

        System.out.println("PROCESS STATE WITH SJF PRIMITIVE ALGO (INTERPUT) COLLAGE TEST ....");
        ProcessWaitAndCpuTimeUsingSJF_PRIMITVE_CollageTest();
        printLine();

        System.out.println("PRIORITY PRIMITIVE ALGO WITH AVGS (WAIT,TURN AROUND) AND CPU IDLE TIME COLLAGE TEST ....");
        ProcessWaitAndCpuTimeUsingPRIORITY_PRIMITVE_CollageTest();
        printLine();

    }

    /***************************************************---------PROCESS PRIORITY PRIMITIVE WITH AVGS , IDLE TIMES TEST---------*******************************/
    public static void ProcessWaitAndCpuTimeUsingPRIORITY_PRIMITVE_CollageTest() {
        MyProcess p1=new MyProcess("p1",0,2,0);
        MyProcess p2=new MyProcess("p2",1,4,0);
        MyProcess p3=new MyProcess("p3",2,5,3);
        MyProcess p4=new MyProcess("p4",4,4,1);

//     for make idle for 2 times (will be idle form 11 to 13)
//        MyProcess p4=new MyProcess("p4",13,4,1);


        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4));

        WithPriority.calculatePriority_Primitive_WITH_WAIT_TURN_AVG_IDLE(processes);

    }



        /***************************************************---------PROCESS SJF PRIMITIVE TEST---------*******************************/

    public static void ProcessWaitAndCpuTimeUsingSJF_PRIMITVE_CollageTest(){
        MyProcess p1=new MyProcess("p1",0,16);
        MyProcess p2=new MyProcess("p2",1,9);
        MyProcess p3=new MyProcess("p3",5,4);
        MyProcess p4=new MyProcess("p4",4,3);
        MyProcess p5=new MyProcess("p5",6,1);


        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        MyProcess.SortByArivTime_ASC(processes);
        MyProcess.SortByBursTimeGroups(processes);
        ArrayList<ProcessesSate.ProcessAdv> processAdvs= ProcessesSate.ProcessAdv.advProcessFromNormalProcesses_FillTimes(processes);
        MyProcess.SortByBursTimeGroups(processAdvs);

        ProcessesSate.ProcessAdv.WaitTimesAndProccesTimes_New_Cuted3(processAdvs, ProcessesSate.ProcessAdv.defaultStateListener_Print() );
//        MyProcess.WaitTimesAndProccesTimes(processes);

    }

    /***************************************************---------PROCESS SJF PRIMITVE TEST---------*******************************/

    public static void ProcessWaitAndCpuTimeUsingSJF_PRIMITVETest(){
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,2);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);


        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        MyProcess.SortByArivTime_ASC(processes);
        MyProcess.SortByBursTimeGroups(processes);
        ArrayList<ProcessesSate.ProcessAdv> processAdvs= ProcessesSate.ProcessAdv.advProcessFromNormalProcesses_FillTimes(processes);

        ProcessesSate.ProcessAdv.WaitTimesAndProccesTimes_New_Cuted3(processAdvs, ProcessesSate.ProcessAdv.defaultStateListener_Print() );
//        MyProcess.WaitTimesAndProccesTimes(processes);

    }

    /***************************************************---------PROCESS STATE WITH NRE ALGO (INTERPUT) TEST---------*******************************/

    public static void ProcessStateWithInterputTest(){
        MyProcess p1=new MyProcess("p1",10,10);
        MyProcess p2=new MyProcess("p2",0,12);
        MyProcess p3=new MyProcess("p3",3,8);
        MyProcess p4=new MyProcess("p4",5,4);
        MyProcess p5=new MyProcess("p5",12,5);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        MyProcess.SortByArivTime_ASC(processes);
        ArrayList<ProcessesSate.ProcessAdv> processAdvs= ProcessesSate.ProcessAdv.advProcessFromNormalProcesses_FillTimes_NoCpuTime(processes);


        ProcessesSate.ProcessAdv.WaitTimesAndProccesTimes_New_Cuted2(processAdvs, ProcessesSate.ProcessAdv.defaultStateListener_Print());
        for (ProcessesSate.ProcessAdv pAdv:processAdvs) {
            System.out.println(pAdv);
        }
    }

    /***************************************************---------PROCESS STATE TEST---------*******************************/

    public static void ProcessStateTest(){
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,3);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        ArrayList<ProcessesSate.ProcessAdv> processAdvs= ProcessesSate.ProcessAdv.advProcessFromNormalProcesses_FillTimes(processes);

        ProcessesSate.ProcessAdv.CalculateStateWithPrint(processAdvs);
        for (ProcessesSate.ProcessAdv pAdv:processAdvs) {
            System.out.println(pAdv);
        }


    }




    /***************************************************---------PROCESS WAUT AND CPU TIME TEST---------*******************************/

    public static void ProcessWaitAndCpuTimeTest(){
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,3);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);


        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        MyProcess.SortByArivTime_ASC(processes);
        MyProcess.WaitTimesAndProccesTimes(processes);

    }
    /***************************************************---------READ AND WRITE  TEST---------*******************************/

    public static void Write_Read_Test_L1(){
        //Test Fields
        String textToTest=TEST_TEXT;
        String outputFileName="l1_output.txt";
        String outputCopyFileName=outputFileName.substring(0,outputFileName.indexOf('.'))+"_Copy.txt";

        System.out.println("Write Read To File Test Start..");
        //Write Test
        System.out.println("Write ("+textToTest+") To File ");
        WriteReadFromFile_L1 _writeReadFromFileL1 =new WriteReadFromFile_L1();
        boolean isWriteDone= _writeReadFromFileL1.writeStrIn(textToTest, outputFileName, new WriteReadFromFile_L1.CharWriterLister() {
            @Override
            public void OnCharPassed(FileWriter currentFileWriter, char currentChar) throws IOException {
                currentFileWriter.write(currentChar);
            }
        });
        if(isWriteDone){
            System.out.println("Write is Done ");
        }else {
            System.out.println("Write is Failed?! ");
        }

        //read Test
        System.out.println("Read Data Line By line From  ("+outputFileName+") File ");
        String dataReadedFromFile=_writeReadFromFileL1.getTextFromFileLineByLine(outputFileName);
        System.out.println("File Contains : "+dataReadedFromFile);

        //copy Test
        System.out.println("write to "+outputFileName+" and copy to "+outputCopyFileName);
       boolean isCopyDone= _writeReadFromFileL1.EXC_1_WriteAndCopy_L1(textToTest,outputFileName,outputCopyFileName);
        if(isCopyDone){
            System.out.println("Copy is Done ");
        }else {
            System.out.println("Copy is Failed?! ");
        }

    }

    private static void printLine(){
        System.out.println("--------------------------------------");
    }
}
