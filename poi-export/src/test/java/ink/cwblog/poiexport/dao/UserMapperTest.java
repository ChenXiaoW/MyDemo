package ink.cwblog.poiexport.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.cwblog.poiexport.enums.ExcelTypeEnums;
import ink.cwblog.poiexport.pojo.User;
import ink.cwblog.poiexport.util.CommonUtil;
import ink.cwblog.poiexport.util.ExcelUtil;
import net.minidev.json.JSONArray;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class UserMapperTest {


    @Autowired
    private UserMapper userMapper;

    @Test
    void insertTest(){
        User user = new User()
                .setAddress("布龙路").setCity("深圳").setProvince("广东")
                .setAge(12).setUsername("cw").setDetail("详情").setSex("男").setTel("211");
        userMapper.insert(user);
    }


    /**
     * 批量Mock数据
     * @throws InterruptedException
     */
    @Test
    void batchInsertTest() throws InterruptedException {
        //mock数据
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(30,100,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1500000));
       int maxNum = 1500000 ;

        for (int i = 0;i<maxNum;i++){
            int d= i;
            threadPoolExecutor.execute(()->{
                User user = new User()
                        .setAddress("布龙路").setCity("深圳").setProvince("广东")
                        .setAge(Integer.valueOf(CommonUtil.generateCode(2))).setUsername(d+"-"+CommonUtil.generateCode(5)).setDetail("第"+d+"个").setSex("男").setTel(d+"");
                userMapper.insert(user);
            });
        }

        while ((threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount())>0){
            System.out.println("当前剩余任务数："+(threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount()));
            threadPoolExecutor.awaitTermination(5,TimeUnit.SECONDS);
        }

        threadPoolExecutor.shutdown();
    }
    /**
     * 测试数据：150w
     */

    /**
     * 无处理
     * 直接OOM
     */
    @Test
    void createExcelTest1(){
        //大数据量导致OOM
        long beginTime =  System.currentTimeMillis();
        System.out.println("开始执行时间:"+beginTime);
        List<User> users1 = userMapper.selectList(Wrappers.<User>lambdaQuery());
        long getDataTime = System.currentTimeMillis();
        System.out.println("获取到数据时间:"+getDataTime+"   , 花费时间:"+(getDataTime-beginTime));
        String test1 = ExcelUtil.createExcel(users1, "F:\\idea_project\\MyDemo\\poi-export\\excel", "Test1", ExcelTypeEnums.XSSF);
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:"+endTime+"   , 花费时间:"+(endTime-beginTime));

    }

    /**
     * 多线程方式处理
     * 耗时：接近3分钟
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void createExcelTest2() throws ExecutionException, InterruptedException {
        long beginTime =  System.currentTimeMillis();
        System.out.println("开始执行时间:"+beginTime);
        //获取数据总量
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery());
        System.out.println("数据总量:"+count);
        //阈值 10w
        Integer thresholdValue = 100000;
        //计算多少页
        Integer pageCount = count%thresholdValue == 0? count/thresholdValue : (count/thresholdValue)+1;
        String []  paths= new String[pageCount] ;
        ExecutorService executorService = Executors.newFixedThreadPool(pageCount*2);
        final CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        for (int i = 0;i<pageCount;i++){
            int d = i;
            String limit = "limit "+i*thresholdValue+","+thresholdValue;
            try{
                paths[i] = executorService.submit(() -> {
                    List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().last(limit));
                    return ExcelUtil.createExcel(users, "F:\\idea_project\\MyDemo\\poi-export\\excel", "Test" + d, ExcelTypeEnums.XSSF);
                }).get();
            }finally {
                //处理完毕
                countDownLatch.countDown();
            }
        }
        //等待所有的线程处理完毕
        countDownLatch.await();
        executorService.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:"+endTime+"   , 花费时间:"+(endTime-beginTime));
        System.out.println("处理结果"+ JSONArray.toJSONString(Arrays.asList(paths)));
    }

    /**
     * 效果跟上面差不多，代码量少点
     * 耗时：接近3分钟
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void createExcelTest3() throws ExecutionException, InterruptedException {
        long beginTime =  System.currentTimeMillis();
        System.out.println("开始执行时间:"+beginTime);
        //获取数据总量
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery());
        System.out.println("数据总量:"+count);
        //阈值 10w
        Integer thresholdValue = 100000;
        //计算多少页
        Integer pageCount = count%thresholdValue == 0? count/thresholdValue : (count/thresholdValue)+1;
        List<String> paths = new ArrayList<>();
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0;i<pageCount;i++){
            int d = i;
            String limit = "limit "+i*thresholdValue+","+thresholdValue;
            CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().last(limit));
                return ExcelUtil.createExcel(users, "F:\\idea_project\\MyDemo\\poi-export\\excel", "Test" + d, ExcelTypeEnums.XSSF);
            });
            futures.add(stringCompletableFuture);
            paths.add(stringCompletableFuture.get());
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        voidCompletableFuture.join();
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:"+endTime+"   , 花费时间:"+(endTime-beginTime));
        System.out.println("处理结果"+ JSONArray.toJSONString(paths));
    }

    /**
     * 使用 SXSSFWorkbook
     * 耗时：28秒726ms
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void createExcelTest4() throws ExecutionException, InterruptedException {
        long beginTime =  System.currentTimeMillis();
        System.out.println("开始执行时间:"+beginTime);
        //获取数据总量
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery());
        System.out.println("数据总量:"+count);
        //阈值 10w
        Integer thresholdValue = 100000;
        //计算多少页
        Integer pageCount = count%thresholdValue == 0? count/thresholdValue : (count/thresholdValue)+1;
        List<String> paths = new ArrayList<>();
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0;i<pageCount;i++){
            int d = i;
            String limit = "limit "+i*thresholdValue+","+thresholdValue;
            CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().last(limit));
                return ExcelUtil.createExcel(users, "F:\\idea_project\\MyDemo\\poi-export\\excel", "Test" + d, ExcelTypeEnums.SXSSF);
            });
            futures.add(stringCompletableFuture);
            paths.add(stringCompletableFuture.get());
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        voidCompletableFuture.join();
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:"+endTime+"   , 花费时间:"+(endTime-beginTime));
        System.out.println("处理结果"+ JSONArray.toJSONString(paths));
    }

    /**
     * 生成单个excel
     * 耗时：28s377ms
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    @Test
    void createExcel5() throws ExecutionException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        long beginTime =  System.currentTimeMillis();
        System.out.println("开始执行时间:"+beginTime);
        //获取数据总量
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery());
        System.out.println("数据总量:"+count);
        //阈值 10w
        Integer thresholdValue = 100000;
        //计算多少页
        Integer pageCount = count%thresholdValue == 0? count/thresholdValue : (count/thresholdValue)+1;
        List<Boolean> paths = new ArrayList<>();
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        Workbook workbook = (Workbook)Class.forName(ExcelTypeEnums.SXSSF.getClassName()).newInstance();
        for (int i = 0;i<pageCount;i++){

            int d = i;
            Sheet sheet = workbook.createSheet(d+"");
            String limit = "limit "+i*thresholdValue+","+thresholdValue;
            CompletableFuture<Boolean> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().last(limit));
                return ExcelUtil.createSheet(sheet,users);
            });
            futures.add(stringCompletableFuture);
            paths.add(stringCompletableFuture.get());
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        voidCompletableFuture.join();
        String fileSavePath = "F:\\idea_project\\MyDemo\\poi-export\\excel"+File.separator+"cw."+ExcelTypeEnums.SXSSF.getSuffix();
        File file = new File(fileSavePath);
        if(!file.exists()){
            if (file.createNewFile()){
                System.out.println("创建文件成功");
            }else {
                System.out.println("创建文件失败");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:"+endTime+"   , 花费时间:"+(endTime-beginTime));
        System.out.println("处理结果"+ JSONArray.toJSONString(paths));
    }

}