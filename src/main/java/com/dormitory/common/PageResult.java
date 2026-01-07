package com.dormitory.common;

import lombok.Data;

import java.util.List;

/**
 * 简单分页结果封装
 *
 * @author dormitory-system
 */
@Data
public class PageResult<T> {

    /**
     * 总条数
     */
    private long total;

    /**
     * 数据列表
     */
    private List<T> records;

    public PageResult(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
}







