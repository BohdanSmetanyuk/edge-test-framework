package org.thingsboard.edgetest.data.comparison;

import lombok.Getter;

@Getter
public class ComparisonDetails {

    private int attempts;
    private long delay;

    public ComparisonDetails(int attempts, long delay) {
        this.attempts = attempts;
        this.delay = delay;
    }

}
