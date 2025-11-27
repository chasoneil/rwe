package com.chason.rwe.domain;

import lombok.Data;

/**
 * 单句
 */
@Data
public class SingleDO {

    // 这个单句练习的名称
    private String title;

    private String content;

    public SingleDO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
