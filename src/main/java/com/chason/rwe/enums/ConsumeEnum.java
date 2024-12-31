package com.chason.rwe.enums;

/**
 * 消费类型枚举  一级菜单
 * 消费的类型属于全部支出
 * @author Chason
 */
public enum ConsumeEnum {
    /**
     * 菜单分级 （分为1 2级）
     * 食品支出（包含双人的所有食品相关的支出）：
     *     外卖 零食 外食 食品采购 奶茶咖啡
     * 日用支出（包含日常用品的所有支出，日常用品指的是可以被使用完的消耗品）：
     *     日用采购  化妆品  护肤品
     * 交通支出（所有有关出行的支出，包括车辆相关的）：
     *     车辆养护 车位费 停车费 交通费 车贷 车辆保险 加油费 充电费 ETC 违法罚款
     * 娱乐支出：
     *     电影 KTV 游戏 首饰 电子用品 会员费 宠物
     * 投资支出：
     *     投资收益 基金 股票 债券 彩票
     * 房屋地产：
     *     房租/房贷 物业费 房屋维修 房屋保险
     * 健康医疗：
     *     医疗 健身 保养(健)品 保险 体检 美容美发 药品
     * 生活居家：
     *     快递费（自己的快递费，如果是父母的快递则是家人支出） 水电煤网 家居用品 通讯费 衣服 鞋子 包包
     * 父母支出：
     *     家人支出 父母日常
     * 旅行支出：
     *     旅行 住宿 交通 饮食 （所有旅行相关的）
     * 其他支出：
     *     代付 服务费 人情往来 学习 意外支付
     *
     */

    TAKEAWAY("食品支出","外卖", "日常支出"),
    SNACKS("食品支出","零食", "日常支出"),
    RESTAURANT("食品支出","外食", "日常支出"),

    DAILY_BUY("日用支出", "日用采购", "日常支出"),
    BEAUTIFUL("日用支出", "化妆品", "日常支出"),

    CAR_PARKING("交通支出", "停车费", "日常支出"),
    TRANSPORTATION("交通支出", "交通费", "日常支出"),

    RENT("房屋支出", "租金", "月支出"),
    BUY_HOUSE("房屋支出", "房屋买卖", "一次性支出"),
    PROPERTY_FEE("房屋支出", "物业费", "年支出"),

    ELECTRONIC("娱乐支出", "电子产品", "一次性支出"),
    MOVIE("娱乐支出", "电影", "一次性支出");

    private String firstLevelType;

    private String detailType;

    private String statisticalType;

    ConsumeEnum(String firstLevelType, String detailType, String statisticalType) {
        this.firstLevelType = firstLevelType;
        this.detailType = detailType;
        this.statisticalType = statisticalType;
    }

    public String getFirstLevelType() {
        return firstLevelType;
    }

    public String getDetailType() {
        return detailType;
    }

    public String getStatisticalType() {
        return statisticalType;
    }

    public static String[] getAllTypes(String enumType) {

        String[] types = new String[3];
        for (ConsumeEnum consumeEnum : ConsumeEnum.values()) {
            if (consumeEnum.name().equalsIgnoreCase(enumType)) {
                types[0] = consumeEnum.getFirstLevelType();
                types[1] = consumeEnum.getDetailType();
                types[2] = consumeEnum.getStatisticalType();
                return types;
            }
        }
        return null;
    }

}
