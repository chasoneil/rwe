
## 设计

> 设计模块

* 数据表设计 
  * 数据表名：tbl_trade
  * 字段：
    * trade_time datetime index '交易时间'
    * trade_type varchar(32) index '交易分类'
    * trade_obj  varchar(32) '交易对象'
    * obj_account varchar(32) '对方账号'
    * product varchar(128) '商品说明'
    * in_out varchar(4) index '收/支'
    * amount double index '金额'
    * pay_type varchar(32) '收付款方式'
    * trade_status varchar(4) '交易状态'
    * order_id varchar(128) '交易订单号'
    * seller_order_id varchar(128) '商家订单号'
    * trade_comment text '备注',
    * create_user_id bigint(20),
    * primary key (order_id, creat_user_id)
```mysql
create table tbl_trade (
    order_id varchar(128) primary key,
    trade_time datetime not null,
    trade_type varchar(32),
    trade_obj varchar(32),
    obj_account varchar(32),
    product varchar(128),
    in_out varchar(4),
    amount double,
    pay_type varchar(32),
    trade_status varchar(16),
    seller_order_id varchar(128),
    trade_comment text,
    checked int default 0,
    index (trade_time),
    index (trade_type),
    index (in_out),
    index (amount)
);
```

记账表  tbl_keep_account

```mysql
create table tbl_keep_account (
    id int primary key auto_increment comment '账单ID',
    user_id int not null comment '用户ID',
    amount double not null comment '金额',
    trade_time datetime not null comment '交易时间',
    trade_variety varchar(32) not null comment '交易类型（一级菜单）',
    trade_type varchar(32) not null comment '交易类型（二级菜单）',
    trade_statistics text not null comment '交易统计（第二维度统计）',
    pay_for varchar(32) not null comment '为谁付款',
    trade_status varchar(16) not null comment '交易状态',
    trade_comment text not null comment '交易备注',
    checked int default 0 comment '是否已确认',
    index (user_id),
    index (trade_time),
    index (trade_variety),
    index (trade_type),
    index (pay_for)
);
```
消费二级目录表

```mysql
create table tbl_consume_category (
    id int primary key auto_increment comment '消费分类ID',
    user_id int not null comment '用户ID',
    category_name varchar(32) not null comment '一级菜单',  
    category_type varchar(32) not null comment '二级菜单', 
    bill_type varchar(32) not null comment '账本类型', 
    deep_type varchar(32) not null comment '深度支出分类', 
    level int not null default 1 comment '分类级别',
    index (user_id),
    index (category_name),
    index (bill_type),
    index (deep_type)
);
```

深度支出分类表

```mysql  
create table tbl_deep_type(
    id             int primary key auto_increment comment '深度分类ID',
    user_id        int         not null comment '用户ID',
    deep_type_name varchar(32) not null comment '深度分类名称',
    index (user_id)
);
```