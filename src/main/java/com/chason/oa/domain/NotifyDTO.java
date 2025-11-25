package com.chason.oa.domain;

import lombok.Data;

@Data
public class NotifyDTO extends NotifyDO {

    private static final long serialVersionUID = 1L;

    private String isRead;

    private String before;

    private String sender;

    @Override
    public String toString() {
        return "NotifyDTO{" +
                "isRead='" + isRead + '\'' +
                ", before='" + before + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
