package com.chason.rwe.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Jp练习中的对话
 */
@Data
public class DialogDO {

    private String title;

    private List<String> content;

    public DialogDO(String title) {
        this.title = title;
        this.content = new ArrayList<>();
    }

    public DialogDO(String title, List<String> content) {
        this.title = title;
        this.content = content;
    }
}
