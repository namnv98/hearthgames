package com.hearthlogs.server.match.analysis.domain.generic;

import java.util.List;

public class GenericColumn<T> {

    private String data;
    private String data2;

    private List<T> datas;

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

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
