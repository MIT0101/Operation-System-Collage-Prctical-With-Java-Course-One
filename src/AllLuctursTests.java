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
        System.out.println("PROCESS STATE TEST .....");
        ProcessStateTest();
        printLine();
    }

    /***************************************************---------PROCESS STATE TEST---------*******************************/

    public static void ProcessStateTest(){
        MyProcess p1=new MyProcess("p1",2,6);
        MyProcess p2=new MyProcess("p2",5,3);
        MyProcess p3=new MyProcess("p3",1,8);
        MyProcess p4=new MyProcess("p4",0,3);
        MyProcess p5=new MyProcess("p5",4,4);

        ArrayList<MyProcess> processes=new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5));
        ArrayList<ProcessesSate.ProcessAdv> processAdvs= ProcessesSate.ProcessAdv.advProcessFromNormalProcesses_Sort_FillTimes(processes);

        ProcessesSate.ProcessAdv.CalculateState(processAdvs);
        for (ProcessesSate.ProcessAdv pAdv:processAdvs) {
            System.out.println(pAdv);
        }
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
