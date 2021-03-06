package com.codinginfinity.research.notification;

import com.codinginfinity.research.core.BaseEntity;

import java.time.LocalDateTime;

/**
 * Created by andrew on 2016/04/11.
 */
public class RepeatRequest extends BaseEntity {

    private static final long serialVersionUID = 786654864207103810L;

    private LocalDateTime endDate;
    private Interval interval;

    public RepeatRequest() {
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
}
