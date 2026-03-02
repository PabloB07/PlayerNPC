/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.spigot.metrics;

public class MetricCount {
    private int count = 0;

    public void addCount() {
        this.addCount(1);
    }

    public void addCount(int a) {
        this.count += a;
    }

    public void setCount(int a) {
        this.count = a;
    }

    public Integer getCount() {
        return this.count;
    }

    public Integer getFinalCount() {
        int cf = this.count;
        this.count = 0;
        return cf;
    }
}

