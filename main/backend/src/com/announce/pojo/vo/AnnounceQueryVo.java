package com.announce.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnounceQueryVo implements Serializable {
    private String keyWords;
    private Integer type;
    private Integer pageNum;
    private Integer pageSize;
}
