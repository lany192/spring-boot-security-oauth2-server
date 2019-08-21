package com.github.lany192.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class JsonObjects<T> implements Serializable {
    private List<T> data;
    private int draw;
    private long recordsFiltered;
    private long recordsTotal;

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
