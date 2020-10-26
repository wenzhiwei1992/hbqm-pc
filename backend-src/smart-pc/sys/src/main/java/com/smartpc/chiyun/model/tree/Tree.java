package com.smartpc.chiyun.model.tree;

import lombok.Data;

import java.util.List;

@Data
public class Tree {

    private String id;

    private String label;

    private List<Tree> children;

    public Tree() {

    }

    public Tree(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
