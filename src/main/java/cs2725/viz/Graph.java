/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.viz;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Graph {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    private static final int ID_LEN = 6;

    private Map<String, Object> refs;
    private transient String dotStr = "";
    private String graphName = "ds_viz";
    private boolean isTopDown = false;

    private transient static Map<String, Graph> instances = new HashMap<>();
    private transient static String currentGraph = null;
    private static boolean isEnabled = false;

    private Graph() {
        refs = new HashMap<>();
    }

    /**
     * This method should be called at the top of the main method to enable the to
     * enable the graph for visualization. The reason for having this method is to
     * avoid running graph visualization during test runs.
     */
    public static void enable() {
        isEnabled = true;
    }

    /**
     * This method can be used to disable the graph visualization. This is useful
     * for graph visualization tests.
     */
    public static void disable() {
        isEnabled = false;
    }

    public static Graph i(String name) {
        if (!instances.containsKey(name)) {
            instances.put(name, new Graph());
            currentGraph = name;
            if (instances.size() == 1) {
                startDotUpdateLoop();
            }
        }
        return instances.get(name);
    }

    public static Graph i() {
        return i(null);
    }

    public static void setCurrentGraph(String str) {
        currentGraph = str;
    }

    public static void startDotUpdateLoop() {
        // Initialize update loop.
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    instances.get(currentGraph).update();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.err.println("Update loop interrupted: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    System.err.println("Error during file update: " + e.getMessage());
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public void makeTopDown() {
        this.isTopDown = true;
    }

    public void makeLeftToRight() {
        this.isTopDown = false;
    }

    public void update() {
        // Graph has to be enabled to be able to update the graph.
        if (!isEnabled) {
            return;
        }
        traverse();
        save();
    }

    private void save() {
        String fileName = String.format("%s.dot", graphName);
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName)) {
            writer.write(dotStr);
        } catch (java.io.IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public String getDotStr() {
        update();
        return dotStr;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public void setRef(String refName, Object ref) {
        refs.put(refName, ref);
        update();
    }

    public void setRef(String refName, Object array, int index) {
        refs.put(refName, new ArrayIndex(array, index));
        update();
    }

    public void clearRef(String refName) {
        refs.remove(refName);
        update();
    }

    private String id(Object obj) {
        // Numeric identifiers should be in quotes.
        // Otherwise they could be interpreted in unintended ways.
        return String.format("\"%d\"", obj.hashCode());
    }

    private String makeRefDot(String id, String name) {
        return String.format("%s[shape=none, width=0, height=0, margin=0, fontcolor=red, label=\"%s\"];\n",
                id, escape(name));
    }

    private String makeRefDotBlack(String id, String name) {
        return String.format("%s[shape=none, width=0, height=0, margin=0, label=\"%s\"];\n",
                id, escape(name));
    }

    private String makeLinkDot(String src, String dst) {
        return String.format("%s -> %s;\n", src, dst);
    }

    /**
     * This is intended to create a free form link. However, this seems to cause
     * unintended graph behavior like missing links.
     */
    private String makeFreeLinkDot(String src, String dst) {
        return String.format("%s -> %s [constraint=false];\n", src, dst);
    }

    private String makeLinkToNullDot(String src) {
        String dstId = genId();
        // Note: makeRefDot adds ;\n at the end by itself.
        return String.format("%s%s -> %s;\n",
                makeRefDotBlack(dstId, "null"), src, dstId);
    }

    private String makeArrayIndexDot(String refId, ArrayIndex arrayIndex) {
        return makeLinkDot(refId,
                String.format("%s:%d", id(arrayIndex.getArray()), arrayIndex.getIndex()));
    }

    private void traverse() {
        StringBuilder sb = new StringBuilder();
        RANDOM.setSeed(42);
        sb.append("digraph ObjectMap {\n");
        if (isTopDown) {
            sb.append("rankdir=TB;\n");
        } else {
            sb.append("rankdir=LR;\n");
        }
        Set<Integer> visited = new HashSet<>();
        for (Map.Entry<String, Object> ref : refs.entrySet()) {
            String refName = ref.getKey();
            Object obj = ref.getValue();
            String refId = genId();
            sb.append(makeRefDot(refId, refName));
            if (obj == null) {
                sb.append(makeLinkToNullDot(refId));
                continue;
            }
            if (ArrayIndex.class.isInstance(obj)) {
                ArrayIndex arrayIndex = (ArrayIndex) obj;
                if (arrayIndex.isIndexValid()) {
                    sb.append(makeArrayIndexDot(refId, arrayIndex));
                    continue;
                } else {
                    // Show the index as an external variable through traverseRec.
                    obj = arrayIndex.getIndex();
                }
            }
            sb.append(makeLinkDot(refId, id(obj)));
            traverseRec(obj, visited, sb);
        }
        sb.append("}");
        dotStr = sb.toString();
    }

    private String genId() {
        StringBuilder result = new StringBuilder(ID_LEN);
        for (int i = 0; i < ID_LEN; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        // Numeric identifiers should be in quotes.
        // Otherwise they could be interpreted in unintended ways.
        return String.format("\"%s\"", result.toString());
    }

    public static boolean isPrimitiveLike(Object obj) {
        if (obj == null)
            return true;
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Long.class ||
                clazz == Short.class ||
                clazz == Byte.class ||
                clazz == Boolean.class ||
                clazz == Character.class;
    }

    private String escape(String str) {
        return str.replaceAll("\"", "\\\"");
    }

    /**
     * Treat object as a String and escape for labels by removing special characters
     * that may conflict with Graphviz notation.
     */
    private String label(Object obj) {
        String str = String.valueOf(obj);
        str = str.replace("{", "&#123;")
                .replace("}", "&#125;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("[", "&#91;")
                .replace("]", "&#93;");
        return escape(str);
    }

    private void makeArrayDot(Object arr, Set<Integer> visited, StringBuilder sb) {
        // Note: further visiting must be done via traverseRec to avoid revisits.
        if (!arr.getClass().isArray()) {
            throw new IllegalArgumentException("Input is not an array.");
        }

        int length = Array.getLength(arr);
        List<String> labelParts = new ArrayList<>();

        String arrId = id(arr);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(arr, i);
            if (isPrimitiveLike(element)) {
                labelParts.add(String.format("<%d> %s", i, label(element)));
            } else {
                labelParts.add(String.format("<%d> ref", i));
                sb.append(makeLinkDot(String.format("%s:%s", arrId, i), id(element)));
                traverseRec(element, visited, sb);
            }
        }

        String arrRecLabel = String.join(" | ", labelParts);

        sb.append(String.format("%s[shape=record, label=\"{%s}\"];\n",
                arrId, arrRecLabel));
    }

    private void traverseType(Object obj, Class<?> type,
            List<String> fieldParts, Set<Integer> visited, StringBuilder sb) {
        for (Field field : type.getDeclaredFields()) {

            // Skip static or final fields.
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            try {
                // Makes even the private fields accessible.
                field.setAccessible(true);

                Object fieldValue = field.get(obj);
                String fieldName = field.getName();

                if (isPrimitiveLike(fieldValue)) {
                    fieldParts.add(
                            String.format("{%s | <val> %s}", fieldName, label(fieldValue)));
                } else {
                    String fieldRefId = id(fieldValue);
                    fieldParts.add(String.format("{%s | <%s> ref}", fieldName, fieldName));
                    sb.append(makeLinkDot(String.format("%s:%s", id(obj), fieldName), fieldRefId));
                    traverseRec(fieldValue, visited, sb);
                }
            } catch (IllegalAccessException | InaccessibleObjectException e) {
                // Do nothing.
                // System.err.println("Error accessing field: " + field.getName() + " - " + e.getMessage());
            }
        }
    }

    private void makeComplexObjDot(Object obj, Set<Integer> visited, StringBuilder sb) {
        // Note: further visiting must be done via traverseRec to avoid revisits.
        if (isPrimitiveLike(obj) || obj.getClass().isArray()) {
            throw new IllegalArgumentException("Input is not a complex object.");
        }

        List<String> fieldParts = new ArrayList<>();

        Class<?> currentClass = obj.getClass();

        // Traverse class hierarchy.
        while (currentClass != null && currentClass != Object.class) { // Stop at Object
            // Traverse fields of the current type in the inheritance hierarchy.
            traverseType(obj, currentClass, fieldParts, visited, sb);
            currentClass = currentClass.getSuperclass(); // Move to the superclass
        }

        String objRecLabel = String.join(" | ", fieldParts);

        sb.append(String.format("%s[shape=record, label=\"%s\"];\n", id(obj), objRecLabel));
    }

    private void makeListNode(Object obj, Set<Integer> visited, StringBuilder sb) {
        // Note: further visiting must be done via traverseRec to avoid revisits.
        Field valField = Annotations.findFieldsWithAnnotation(obj.getClass(), Value.class).get(0);
        Field nextField = Annotations.findFieldsWithAnnotation(obj.getClass(), Next.class).get(0);

        // Makes even the private fields accessible.
        valField.setAccessible(true);
        nextField.setAccessible(true);

        try {
            Object value = valField.get(obj);
            Object nextNode = nextField.get(obj);

            if (isPrimitiveLike(value)) {
                sb.append(String.format("%s[shape=box, label=\"%s\"];\n",
                        id(obj), label(value)));
            } else {
                sb.append(String.format("%s[shape=box, label=\"ref\"];\n", id(obj)));
                sb.append(makeLinkDot(id(obj), id(value)));
                traverseRec(value, visited, sb);
            }

            if (nextNode != null) {
                sb.append(makeLinkDot(id(obj), id(nextNode)));
                traverseRec(nextNode, visited, sb);
            } else {
                sb.append(makeLinkToNullDot(id(obj)));
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing field: " + valField.getName() + " - " + e.getMessage());
        }
    }

    private void makeTreeNode(Object obj, Set<Integer> visited, StringBuilder sb) {
        // Retrieve annotated fields
        Field valField = Annotations.findFieldsWithAnnotation(obj.getClass(), Value.class).get(0);
        valField.setAccessible(true);

        try {
            Object value = valField.get(obj);

            // Handle value node
            if (isPrimitiveLike(value)) {
                sb.append(String.format("%s[shape=box, label=\"%s\"];\n",
                        id(obj), label(value)));
            } else {
                sb.append(String.format("%s[shape=box, label=\"ref\"];\n", id(obj)));
                sb.append(makeLinkDot(id(obj), id(value)));
                traverseRec(value, visited, sb);
            }

            // Check for binary tree structure (Left/Right)
            List<Field> leftFields = Annotations.findFieldsWithAnnotation(obj.getClass(), Left.class);
            List<Field> rightFields = Annotations.findFieldsWithAnnotation(obj.getClass(), Right.class);
            List<Field> childrenFields = Annotations.findFieldsWithAnnotation(obj.getClass(), Children.class);

            if (!childrenFields.isEmpty()) {
                Field childrenField = childrenFields.get(0);
                childrenField.setAccessible(true);

                List<?> children = (List<?>) childrenField.get(obj);
                if (children != null) {
                    for (Object child : children) {
                        sb.append(makeLinkDot(id(obj), id(child)));
                        traverseRec(child, visited, sb);
                    }
                }
            } else if (!leftFields.isEmpty() && !rightFields.isEmpty()) {
                Field leftField = leftFields.get(0);
                Field rightField = rightFields.get(0);
                leftField.setAccessible(true);
                rightField.setAccessible(true);

                Object leftNode = leftField.get(obj);
                Object rightNode = rightField.get(obj);

                if (leftNode != null) {
                    sb.append(makeLinkDot(id(obj), id(leftNode)));
                    traverseRec(leftNode, visited, sb);
                } else {
                    // This helps with identifying the left and right nodes in asymmetric trees.
                    sb.append(makeLinkToNullDot(id(obj)));
                }

                if (rightNode != null) {
                    sb.append(makeLinkDot(id(obj), id(rightNode)));
                    traverseRec(rightNode, visited, sb);
                } else {
                    // This helps with identifying the left and right nodes in asymmetric trees.
                    sb.append(makeLinkToNullDot(id(obj)));
                }
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing field: " + e.getMessage());
        }
    }

    private void traverseRec(Object obj, Set<Integer> visited, StringBuilder sb) {
        if (visited.contains(obj.hashCode()))
            return;
        visited.add(obj.hashCode());
        Annotations.validateAnnotations(obj.getClass());
        if (obj.getClass().isArray()) {
            makeArrayDot(obj, visited, sb);
        } else if (obj.getClass().isAnnotationPresent(ListNode.class)) {
            makeListNode(obj, visited, sb);
        } else if (obj.getClass().isAnnotationPresent(TreeNode.class)) {
            makeTreeNode(obj, visited, sb);
        } else if (isPrimitiveLike(obj)) {
            String node = makeRefDot(id(obj), label(obj));
            sb.append(node);
        } else {
            // The obj is considered a class instance.
            // This will be drawn as a table.
            makeComplexObjDot(obj, visited, sb);
        }
    }

}
