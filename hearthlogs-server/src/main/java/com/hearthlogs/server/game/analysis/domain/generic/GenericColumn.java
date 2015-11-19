package com.hearthlogs.server.game.analysis.domain.generic;

import java.util.Collection;

public class GenericColumn<T> {

    private String data;
    private String data2;

    private Collection<T> datas;

    public GenericColumn(Collection<T> datas) {
        this.datas = datas;
    }

    public GenericColumn(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData() {
        return data;
    }

    public Collection<T> getDatas() {
        return datas;
    }

    public void setDatas(Collection<T> datas) {
        this.datas = datas;
    }
}
