/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.viz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Annotations {

    public static void validateAnnotations(Class<?> clazz) throws IllegalArgumentException {
        if (clazz.isAnnotationPresent(ListNode.class)) {
            validateListNode(clazz);
        } else if (clazz.isAnnotationPresent(TreeNode.class)) {
            validateTreeNode(clazz);
        }
    }

    private static void validateListNode(Class<?> clazz) {
        long valCount = findFieldsWithAnnotation(clazz, Value.class).size();
        long nextCount = findFieldsWithAnnotation(clazz, Next.class).size();

        if (valCount != 1 || nextCount != 1) {
            throw new IllegalArgumentException("@ListNode must have exactly one field " 
                            + "annotated with @Value and one with @Next.");
        }
    }

    private static void validateTreeNode(Class<?> clazz) {
        long valCount = findFieldsWithAnnotation(clazz, Value.class).size();
        long leftCount = findFieldsWithAnnotation(clazz, Left.class).size();
        long rightCount = findFieldsWithAnnotation(clazz, Right.class).size();
        long childrenCount = findFieldsWithAnnotation(clazz, Children.class).size();

        if (valCount != 1) {
            throw new IllegalArgumentException("@TreeNode must have exactly one field annotated with @Value.");
        }

        if ((leftCount != 1 || rightCount != 1) && childrenCount != 1) {
            throw new IllegalArgumentException("@TreeNode must have either one @Left and one @Right, "
                                                        + "or one @Children annotation.");
        }
    }

    /**
     * Finds all fields in the given class that are annotated with the specified annotation.
     * 
     * @param clazz the class to inspect
     * @param annotation the annotation class to look for
     * @return a list of fields annotated with the given annotation
     */
    public static List<Field> findFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredFields())
                     .filter(field -> field.isAnnotationPresent(annotation))
                     .collect(Collectors.toList());
    }
}
