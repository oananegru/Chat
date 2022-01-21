package com.example.socialnetwork.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph<T> {

    private final HashMap<T, LinkedList<T>> adj;
    private final List<List<T>> components = new ArrayList<>();

    public Graph() {
        adj = new HashMap<>();
    }

    public void addNode(T node) {
        adj.put(node, new LinkedList<>());
    }

    public void addEdge(T u, T w) {
        adj.get(u).add(w);
        adj.get(w).add(u);
    }

    public void dfs() {
        HashMap<T, Boolean> visited = new HashMap<>();

        for (T key : adj.keySet()) {
            ArrayList<T> al = new ArrayList<>();
            if (visited.get(key) == null || !visited.get(key)) {
                dfsUtil(key, visited, al);
                components.add(al);
            }
        }
    }

    public int componentsNumber() { return components.size(); }

    public List<List<T>> getComponents() {
        return components;
    }

    private void dfsUtil(T v, HashMap<T, Boolean> visited, ArrayList<T> al) {
        visited.put(v, true);
        al.add(v);

        for (T n : adj.get(v)) {
            if (visited.get(n) == null || !visited.get(n))
                dfsUtil(n, visited, al);
        }
    }

}
