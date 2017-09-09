import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CreateTXT {
    public static  void  main(String[] args) throws  Exception{
        File file=new File("D:\\number.txt");
        if (file.exists()) {
            file.delete();
        }else {
            file.createNewFile();
        }
        BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        int count=1;
        while(count<=2000000){
            int[] number=new int[10];
            for (int i = 0; i <number.length ; i++) {
                number[i]=(int)(Math.random()*90+10);
            }
            for (int i = 0; i <number.length -1; i++) {
                bw.write(number[i]+" ");
            }
            bw.write(number[number.length-1]+"\n");
            count++;
        }
        bw.close();
        System.out.println("文件已生成");
    }
}
