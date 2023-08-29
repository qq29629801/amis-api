package com.yuyaogc.lowcode.engine.container;

import java.util.*;

public class Graph {
    private int V;
    private List<List<Integer>> adj;

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    public List<Integer> topologicalSort() {
        List<Integer> result = new ArrayList<>();
        int[] indegree = new int[V];

        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                indegree[neighbor]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            for (int neighbor : adj.get(node)) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != V) {
            throw new RuntimeException("Graph contains a cycle");
        }

        return result;
    }

    public List<Integer> successors(int node) {
        List<Integer> successors = new ArrayList<>();
        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();

        stack.push(node);
        visited[node] = true;

        while (!stack.isEmpty()) {
            int currNode = stack.pop();
            successors.add(currNode);

            for (int neighbor : adj.get(currNode)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    visited[neighbor] = true;
                }
            }
        }

        return successors;
    }

    public List<Integer> predecessors(int node) {
        List<Integer> predecessors = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                if (neighbor == node) {
                    predecessors.add(i);
                }
            }
        }
        return predecessors;
    }


    public boolean hasCycle() {
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (hasCycle(i, visited, recStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(int node, boolean[] visited, boolean[] recStack) {
        if (recStack[node]) {
            return true;
        }

        if (visited[node]) {
            return false;
        }

        visited[node] = true;
        recStack[node] = true;

        for (int neighbor : adj.get(node)) {
            if (hasCycle(neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack[node] = false;

        return false;
    }

    public void transpose() {
        List<List<Integer>> newAdj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            newAdj.add(new ArrayList<>());
        }

        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                newAdj.get(neighbor).add(i);
            }
        }

        adj = newAdj;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        //graph.addEdge(5, 2);

        System.out.println("Topological Sort: " + graph.topologicalSort());
        System.out.println("Has Cycle: " + graph.hasCycle());

        System.out.println("successors : " + graph.successors(1));
        System.out.println("predecessors : " + graph.predecessors(1));
        graph.transpose();
        System.out.println("Transposed Graph: " + graph.topologicalSort());

        System.out.println("successors : " + graph.successors(1));
        System.out.println("predecessors : " + graph.predecessors(1));

    }
}


