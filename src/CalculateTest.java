
import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculateTest {
    public static void main(String[] args) throws Exception {
        int[] intNums = new int[20000000];
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\number.txt"))));
        String line="";
        int  i=0;
        long beginTime=System.currentTimeMillis();
        while((line=br.readLine())!=null){
            String[] strings=line.split(" ");
            for (int j = 0; j <strings.length ; j++) {
                intNums[i]=Integer.valueOf(strings[j]);
                i++;
            }
        }
        System.out.println("Calculate result:" + Calculate.calculate(intNums));
        long endTime=System.currentTimeMillis();
        long useTime=endTime-beginTime;
        System.out.println("开始时间："+beginTime);
        System.out.println("结束时间："+endTime);
        System.out.println("使用时间："+useTime);
    }
}

class Calculate {
    static int				poolSize	= 5;
    static ExecutorService	executor	= Executors.newFixedThreadPool(2);

    /**
     *
     * @param targetNum
     * @return
     * @description 使用多线程对targetNum数组求和
     * @author chenzehe
     * @todo
     */
    public static int calculate(int[] targetNum) {
        int result = 0;
        try {
            // 返回结果Future放到CopyOnWriteArrayList中用于结果集计算
            List<Future<Integer>> futures = new CopyOnWriteArrayList<Future<Integer>>();
            for (int i = 0; i < poolSize; i++) {
                // 提交任务
                futures.add(executor.submit(new CalculateCallable(targetNum, poolSize, i)));
            }
            // 等待返回结果
            while (true) {
                if (futures.size() == 0) {
                    break;// 全部返回则跳出
                }
                for (Future<Integer> future : futures) {
                   // System.out.println("waiting...");
                    if (future.isDone()) {
                        result += future.get();
                        futures.remove(future);// 有返回结果就移出
                    }
                }
            }
            executor.shutdown();
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

class CalculateCallable implements Callable<Integer> {
    int[]	targetNum;
    int		threadNum;
    int		poolSize;

    public CalculateCallable(int[] targetNum, int poolSize, int threadNum) {
        this.targetNum = targetNum;
        this.poolSize = poolSize;
        this.threadNum = threadNum;
    }

    @Override
    public Integer call() {
        // 根据某种算法算出这里需要读取某部分数据，此处只是简单平均
        int result = 0;
        int eachSize = (targetNum.length + poolSize) / poolSize;
        int start = eachSize * threadNum;
        int end = start + eachSize;
        for (int i = start; i < end && i < targetNum.length; i++) {
            result += targetNum[i];
        }
        return result;
    }
}
