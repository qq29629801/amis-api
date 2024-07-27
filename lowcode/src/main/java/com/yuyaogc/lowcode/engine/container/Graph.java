package com.yuyaogc.lowcode.engine.container;

import java.util.*;

public class Graph {
    private Map<Long, List<Long>> adj;

    public Graph() {
        adj = new HashMap<>();
    }

    public void addEdge(long u, long v) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
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

            for (long neighbor : adj.getOrDefault(currNode, new ArrayList<>())) {
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
        for (long i : adj.keySet()) {
            for (long neighbor : adj.get(i)) {
                if (neighbor == node) {
                    predecessors.add(i);
                }
            }
        }
        return predecessors;
    }

    public boolean hasCycle() {
        Set<Long> visited = new HashSet<>();
        Set<Long> recStack = new HashSet<>();

        for (long node : adj.keySet()) {
            if (hasCycle(node, visited, recStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(long node, Set<Long> visited, Set<Long> recStack) {
        if (recStack.contains(node)) {
            return true;
        }

        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recStack.add(node);

        for (long neighbor : adj.getOrDefault(node, new ArrayList<>())) {
            if (hasCycle(neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }


    public List<Long> topologicalSort() {
        List<Long> result = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        Stack<Long> stack = new Stack<>();

        for (long node : adj.keySet()) {
            if (!visited.contains(node)) {
                topologicalSortUtil(node, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private void topologicalSortUtil(long node, Set<Long> visited, Stack<Long> stack) {
        visited.add(node);

        for (long neighbor : adj.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }

        stack.push(node);
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        System.out.println("Has Cycle: " + graph.hasCycle());
        System.out.println("Successors: " + graph.successors(1));
        System.out.println("Predecessors: " + graph.predecessors(3));
        System.out.println("TopologicalSort: "+ graph.topologicalSort());
    }
}
