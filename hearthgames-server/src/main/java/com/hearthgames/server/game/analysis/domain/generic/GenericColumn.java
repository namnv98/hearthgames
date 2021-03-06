package com.hearthgames.server.game.analysis.domain.generic;

import java.util.Collection;

public class GenericColumn<T> {

    private String data;
    private String extraData;
    private String extraData2;

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

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getExtraData2() {
        return extraData2;
    }

    public void setExtraData2(String extraData2) {
        this.extraData2 = extraData2;
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
