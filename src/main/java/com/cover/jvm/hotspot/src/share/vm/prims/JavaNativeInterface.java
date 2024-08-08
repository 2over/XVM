package com.cover.jvm.hotspot.src.share.vm.prims;

import com.cover.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaNativeInterface {


    private static final Logger logger = LoggerFactory.getLogger(JavaNativeInterface.class);

    public static InstanceKlass findClass() {
        return null;
    }

    public static MethodInfo getMethodID(InstanceKlass klass, String name, String descriptorName) {
        MethodInfo[] methods = klass.getMethods();
        for (MethodInfo method : methods) {
            String tmpName = (String) klass.getConstantPool().getDataMap().get(method.getNameIndex());
            String tmpDescriptor = (String) klass.getConstantPool().getDataMap().get(method.getDescriptorIndex());

            if (tmpName.equals(name) && tmpDescriptor.equals(descriptorName)) {
                logger.info("找到了方法: " + name + "#" + descriptorName);
                return method;
            }
        }
        logger.error("没有找到方法: " + name + "#" + descriptorName);

        return null;

    }
}
