package com.yuyaogc.lowcode.engine.container;

import java.util.*;

public class Graph {
    private int V;
    private Map<Long, List<Long>> adj;
    private Map<Long, Integer> indexMap;

    public Graph(int V) {
        this.V = V;
        adj = new HashMap<>();
        indexMap = new HashMap<>();
        for (long i = 0; i < V; i++) {
            adj.put(i, new ArrayList<>());
            indexMap.put(i, (int) i);
        }
    }

    public void addEdge(long u, long v) {
        adj.get(u).add(v);
    }

    public List<Long> topologicalSort() {
        List<Long> result = new ArrayList<>();
        Map<Long, Integer> indegree = new HashMap<>();

        for (long i = 0; i < V; i++) {
            indegree.put(i, 0);
        }

        for (long i = 0; i < V; i++) {
            for (long neighbor : adj.get(i)) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }

        Queue<Long> queue = new LinkedList<>();
        for (long i = 0; i < V; i++) {
            if (indegree.get(i) == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            long node = queue.poll();
            result.add(node);

            for (long neighbor : adj.get(node)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != V) {
            throw new RuntimeException("Graph contains a cycle");
        }

        return result;
    }

    public List<Long> successors(long node) {
        List<Long> successors = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        Stack<Long> stack = new Stack<>();

        stack.push(node);
        visited.add(node);

        while (!stack.isEmpty()) {
            long currNode = stack.pop();
            successors.add(currNode);

            for (long neighbor : adj.get(currNode)) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        return successors;
    }

    public List<Long> predecessors(long node) {
        List<Long> predecessors = new ArrayList<>();
        for (long i = 0; i < V; i++) {
            for (long neighbor : adj.get(i)) {
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

        for (long i = 0; i < V; i++) {
            if (hasCycle(indexMap.get(i), visited, recStack)) {
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

        for (long neighbor : adj.get((long) node)) {
            if (hasCycle(indexMap.get(neighbor), visited, recStack)) {
                return true;
            }
        }

        recStack[node] = false;

        return false;
    }

    public void transpose() {
        Map<Long, List<Long>> newAdj = new HashMap<>();
        for (long i = 0; i < V; i++) {
            newAdj.put(i, new ArrayList<>());
        }

        for (long i = 0; i < V; i++) {
            for (long neighbor : adj.get(i)) {
                newAdj.get(neighbor).add(i);
            }
        }

        adj = newAdj;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(0L, 1L);
        graph.addEdge(0L, 2L);
        graph.addEdge(1L, 3L);
        graph.addEdge(2L, 3L);
        graph.addEdge(2L, 4L);
        graph.addEdge(3L, 5L);
        //graph.addEdge(5L, 2L);

        System.out.println("Topological Sort: " + graph.topologicalSort());
        System.out.println("Has Cycle: " + graph.hasCycle());

        System.out.println("Successors: " + graph.successors(1L));
        System.out.println("Predecessors: " + graph.predecessors(1L));
        graph.transpose();
        System.out.println("Transposed Graph: " + graph.topologicalSort());

        System.out.println("Successors: " + graph.successors(1L));
        System.out.println("Predecessors: " + graph.predecessors(1L));
    }
}
