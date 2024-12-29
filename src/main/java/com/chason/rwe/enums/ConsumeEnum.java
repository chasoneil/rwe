package com.chason.rwe.enums;

/**
 * 消费类型枚举  一级菜单
 * 消费的类型属于全部支出
 * @author Chason
 */
public enum ConsumeEnum {

    /**
     *
     *
     *
     *
     */

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

    TAKEAWAY("外卖"),
    DAILY_USE("日用采购"),
    FOOD_FOR_MEAL("食品采购"),
    FLAT("房租/房贷"),
    TRANSPORTATION("交通费"),
    COMMUNICATION("通讯费"),
    FOR_FUN("娱乐支出"),
    SNACKS("零食"),
    RESTAURANT("外食"),
    PARENTS("父母日常"),
    CAR_PARKING("停车费"),
    CLOTHES("衣服"),
    SHOES("鞋子"),
    BAG("包包"),
    GIFT_FOR_FAMILY("家人礼物"),
    HAIRCUT("美发"),
    JEWELRY("首饰"),
    HEALTH_PRODUCT("保健品"),
    INSURANCE("保费"),
    SKIN_CARE("护肤品"),
    TRAVEL("旅行"),
    SOCIETY_EXPENSE("人情往来"),
    WUYE("物业费"),
    YILIAO("医疗"),
    DIANZIYONGPIN("电子用品"),
    JIASHEN("健身"),
    XUEXI("学习"),
    CHONGWU("宠物"),
    CHELIANGYANGHU("车辆养护"),
    SHUIDIANMEI("水电煤网"),
    JIAJU("家居用品"),
    DAIFU("代付"),
    KUAIDI("快递费"),
    SERVICE("服务费");



    private String type;

    ConsumeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
