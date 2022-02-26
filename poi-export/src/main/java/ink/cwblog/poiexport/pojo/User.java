package ink.cwblog.poiexport.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenw
 * @since 2021-07-23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private Integer age;

    private String tel;

    private String address;

    private String detail;

    private Date createTime;

    private String sex;

    private String city;

    private String province;


}
