import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Function;

public class WriteReadFromFile_L1 {
    static Scanner scanner=new Scanner(System.in);
    public static void main(String[] args) {

        WriteReadFromFile_L1 currentClass=new WriteReadFromFile_L1();

        System.out.println("Enter Text To Write in File");
        boolean isSaved=currentClass.writeStrIn(scanner.next(), "output1.txt", new CharWriterLister() {
            @Override
            public void OnCharPassed(FileWriter currentFileWriter, char currentChar) throws IOException {
                currentFileWriter.write(currentChar+"\n");
            }
        });

        if (isSaved){
            System.out.println("Write Done");
        }
        else {

            System.out.println("Write Faild");
        }
        System.out.println(currentClass.getTextFromFileLineByLine("output1.txt"));



    }

    public  String getTextFromFileLineByLine(String fileName){
        StringBuilder result=new StringBuilder();
        try {

            File file=new File(fileName);
            Scanner fileReaderScanner=new Scanner(file);
            String line="";
            while (fileReaderScanner.hasNextLine()){
                line=fileReaderScanner.nextLine();
                result.append(line+"\n");
            }
            fileReaderScanner.close();
            return result.toString();

        }catch (Exception exception){
            return result.toString();
        }
    }
    public  boolean writeStrIn(String text,String fileName,CharWriterLister charWriterLister){
        try {
            FileWriter fileWriter=new FileWriter(fileName);

            for (char ch:text.toCharArray()) {
                charWriterLister.OnCharPassed(fileWriter,ch);
            }
            fileWriter.close();
            return true;

        }catch (Exception ex){
            return false;
        }
    }
    //for pass method
     interface CharWriterLister{
        void OnCharPassed(FileWriter currentFileWriter,char currentChar) throws IOException;
    }

    //Excisie insert data to file and then copy it to another file

    public boolean EXC_1_WriteAndCopy_L1(String text,String writeToFileName,String copyToFileName){
        try {
            CharWriterLister charWriterLister=new CharWriterLister() {
                @Override
                public void OnCharPassed(FileWriter currentFileWriter, char currentChar) throws IOException {
                    currentFileWriter.write(currentChar);

                }
            };
           boolean isWriteDone= this.writeStrIn(text, writeToFileName, charWriterLister);
           String textToCopy = this.getTextFromFileLineByLine(writeToFileName);
           boolean isCopyDone=this.writeStrIn(textToCopy, copyToFileName,charWriterLister);
           return isWriteDone&&isCopyDone;
        }catch (Exception ex){
            return false;
        }
    }
}
