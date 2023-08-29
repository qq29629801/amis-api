package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.container.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassBuilder {


    private static EntityClass extendEntityClass(EntityClass entityClass, Container container) {
        List<EntityClass> bases = new ArrayList<>();

        String name = entityClass.getName();

        if (Objects.isNull(entityClass.getParent())) {
            return entityClass;
        }

        for (String parent : entityClass.getParent()) {
            EntityClass parentClass = getEntryClass(parent, container);
            if (parent == name) {
                for (EntityClass base : parentClass.getBases()) {
                    // 放到列表的最后
                    bases.remove(base);
                    bases.add(base);
                }
            } else {
                bases.remove(parentClass);
                bases.add(parentClass);
                parentClass.getInheritChildren().add(name);
            }
        }

        entityClass.setBases(bases);

        buildModelAttributes(container, entityClass);

        return entityClass;
    }

    public static EntityClass buildEntityClass(EntityClass entityClass, Container container) {
        return extendEntityClass(entityClass, container);
    }


    private static void buildModelAttributes(Container container, EntityClass meta) {
        List<EntityClass> mro = meta.getMro();

        for (int i = mro.size() - 1; i > 0; i--) {
            EntityClass base = mro.get(i);

            for (EntityField entityField : base.getFields()) {
                meta.addField(entityField.getName(), entityField);
            }
//            for (EntityMethod entityMethod : base.getMethods()) {
//                meta.addMethod(entityMethod.getName(), entityMethod);
//            }
            for (EntityField entityField : base.getPkFields()) {
                meta.addPkField(entityField.getName(), entityField);
            }
        }

        // recompute attributes of _inherit_children models
        for (String childName : meta.getInheritChildren()) {
            EntityClass childMeta = getEntryClass(childName, container);
            childMeta.resetMro();
            buildModelAttributes(container, childMeta);
        }
    }


    private static EntityClass getEntryClass(String name, Container container) {
        for (Application application : container.getApps()) {
            EntityClass entityClass = application.getEntity(name);
            if (!Objects.isNull(entityClass)) {
                return entityClass;
            }
        }
        return null;
    }

}
