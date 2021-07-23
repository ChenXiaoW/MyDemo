package ink.cwblog.poiexport.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author chenw
 * @date 2021/4/2 17:33
 */
public class CommonUtil {

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成指定长度的 纯数字字符串
     *
     * @param length 长度
     * @return
     */
    public static String generateCode(int length) {
        Random random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder codeBuf = new StringBuilder();
        codeBuf.append(random.nextInt(8) + 1);
        for (int i = 0; i < length - 1; i++) {
            codeBuf.append(random.nextInt(9));
        }
        return codeBuf.toString();
    }


    /**
     * 获取一天剩余时间秒数
     *
     * @param currentDate
     * @return
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * 时间戳转date
     *
     * @param time 时间戳
     * @return date
     */
    public static Date timestampToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(sdf.format(new Date(Long.parseLong(time))));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
