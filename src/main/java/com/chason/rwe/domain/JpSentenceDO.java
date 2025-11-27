package com.chason.rwe.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JpSentenceDO {

    // 单句 key 练习类型 value 该类型下的所有单句
    private List<SingleDO> singles;

    // 对话
    private List<DialogDO> dialogs;

    public List<SingleDO> getSingles() {
        if (this.singles == null)
            singles =  new ArrayList<>();
        return singles;
    }

    public List<DialogDO> getDialogs() {
        if (dialogs == null)
            dialogs = new ArrayList<>();
        return dialogs;
    }
}
