package com.chason.rwe.domain;

import lombok.Data;

@Data
public class ConsumeCategoryDO {

    private int id; // 消费分类ID

    private int parentId; // 父级分类ID

    private long userId; // 用户ID

    private String categoryName; // 一级菜单

    private String categoryType; // 二级菜单

    private String billType; // 账本类型

    private String deepType; // 深度支出分类

    private int level; // 分类层级

}
