package com.cover.jvm.hotspot.src.share.vm.prims;

import com.cover.jvm.hotspot.src.share.vm.oops.CodeAttributeInfo;
import com.cover.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaThread;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.cover.jvm.hotspot.src.share.vm.runtime.Threads;
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
    
    public static void callStaticMethod(MethodInfo method) {
        JavaVFrame prevFrame = null;
        JavaThread thread = Threads.currentThread();
        
        if (!method.getAccessFlags().isStatic()) {
            throw new Error("只能调用静态方法");
        }

        /**
         * 需要获取上一个方法战阵的情况:需要传参
         */
        if (0 != method.getDescriptor().getMethodParamsSize()) {

            /**
             * 取到上一个栈帧
             * 因为实参在这个栈帧中
             * 判断是为了过滤main方法
             */
            if (0 != thread.getStack().size()) {
               logger.info("\t 从上一个栈帧取参数值");
               prevFrame = (JavaVFrame) thread.getStack().peek();
               
           }
        } else {
            logger.info("\t 方法 [" + method.getMethodName() + " ] 没有参数");
        }

        CodeAttributeInfo codeAttributeInfo = method.getAttributes()[0];
        
        // 创建栈帧
        JavaVFrame frame = new JavaVFrame(codeAttributeInfo.getMaxLocals(), method);
        
        if (null != prevFrame) {
            for (int i = 0; i < method.getDescriptor().getMethodParamsSize(); i++) {
                frame.getLocals().add(i, prevFrame.getStack().pop());
            }
        }
        
        thread.getStack().push(frame);
        
        logger.info("第 " + thread.getStack().size() + " 个栈帧");
        
        // 执行任务交给字节码解释器
        BytecodeI
    }
}
