package com.cover.jvm.hotspot.src.share.vm.interpreter;

import com.cover.jvm.hotspot.src.share.vm.classfile.DescriptorStream2;
import com.cover.jvm.hotspot.src.share.vm.oops.AttributeInfo;
import com.cover.jvm.hotspot.src.share.vm.oops.BootstrapMethods;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaEngine {
    
    private MethodInfo method;
    
    private int index;
    
    public LambdaEngine(MethodInfo method, int index) {
        this.method = method;
        this.index = index;
    }
    
    public Object createObject() {
        /**
         * 获取常量池项JVM_CONSTANT_InvokeDynamic中的信息 run
         */
        String sourceMethodName = method.getBelongKlass().getConstantPool().getMethodNameByDynamicInfo(index);
        
        // 动态创建的对象类型() Lcom/cover/jvm/example/java/lang/lambda/CustomLambda;
        String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByDynamicInfo(index);

        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
        
        descriptorStream.parseMethod();
        
        // BootstrapMethods中的index，即当前lambda表达爱是对应的是第几个BootstrapMethod;
        int bootMethodIndex = method.getBelongKlass().getConstantPool().getBootMethodIndexByDynamicInfo(index);
        
        // ==== 
        BootstrapMethods bootstrapMethods = (BootstrapMethods) method.getBelongKlass().getAttributeInfos().get("BootstrapMethods");

        BootstrapMethods.Item item = bootstrapMethods.getBootstrapMethods().get(bootMethodIndex);
        int methodHandleIndex = item.getBootstrapArguments()[1];
        
        // ====
        String className = method.getBelongKlass().getConstantPool().getClassNameByMethodHandleInfo(methodHandleIndex);
        String lambdaMethodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodHandleInfo(methodHandleIndex);
        String lambdaDescriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodHandleInfo(methodHandleIndex);

        DescriptorStream2 lambdaDescriptorStream = new DescriptorStream2(lambdaDescriptorName);
        lambdaDescriptorStream.parseMethod();

        Class<?>[] paramsClass = lambdaDescriptorStream.getParamsType();
        
        // ====
        try {
            Class<?> returnClazz = Class.forName(descriptorStream.getReturnElement().getTypeDesc().replace('/', '.'));
            Class<?> callerClazz = Class.forName(className.replace('/', '.'));

            MethodHandles.Lookup lookup = getLookup(callerClazz);
            System.out.println("\t 创建MethodHandles.Lookup");

            Method method = callerClazz.getDeclaredMethod(lambdaMethodName, paramsClass);
            MethodHandle unreflect = lookup.unreflect(method);

            MethodType type = unreflect.type();
            MethodType factoryType = MethodType.methodType(returnClazz);
            CallSite callSite = LambdaMetafactory.metafactory(lookup, sourceMethodName, factoryType, type, unreflect, type);

            System.out.println("\t 创建dynamic call site");

            MethodHandle target = callSite.getTarget();

            System.out.println("\t 创建Lambda对象");
            return target.invoke();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 完成运行
     */
    public void run() {
        /**
         * 获取常量池项JVM_CONSTANT_InvokeDynamic中的信息
         */
        String sourceMethodName = method.getBelongKlass().getConstantPool().getMethodNameByDynamicInfo(index);
        // 动态创建的对象类型
        String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByDynamicInfo(index);

        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
        descriptorStream.parseMethod();
        
        // BootstrapMethods中的index,即当前lambda表达式对应的第几个BootstrapMethod
        int bootMethodIndex = method.getBelongKlass().getConstantPool().getBootMethodIndexByDynamicInfo(index);
        
        // ====
        BootstrapMethods bootstrapMethods = (BootstrapMethods)method.getBelongKlass().getAttributeInfos().get("BootstrapMethods");

        BootstrapMethods.Item item = bootstrapMethods.getBootstrapMethods().get(bootMethodIndex);
        int methodHandleIndex = item.getBootstrapArguments()[1];
        
        // ====
        String className = method.getBelongKlass().getConstantPool().getClassNameByMethodHandleInfo(methodHandleIndex);
        String lambdaMethodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodHandleInfo(methodHandleIndex);
        String lambdaDescriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodHandleInfo(methodHandleIndex);

        DescriptorStream2 lambdaDescriptorStream = new DescriptorStream2(lambdaDescriptorName);
        lambdaDescriptorStream.parseMethod();;

        Class<?>[] paramsClass = lambdaDescriptorStream.getParamsType();
        // ====

        try {
            Class<?> returnClazz = Class.forName(descriptorStream.getReturnElement().getTypeDesc().replace('/', '.'));
            Class<?> callerClazz = Class.forName(className.replace('/', '.'));

            MethodHandles.Lookup lookup = getLookup(callerClazz);
            
            Method method = callerClazz.getDeclaredMethod(lambdaMethodName, paramsClass);
            
            MethodHandle unreflect = lookup.unreflect(method);
            MethodType type = unreflect.type();

            MethodType factoryType = MethodType.methodType(returnClazz);
            CallSite callSite = LambdaMetafactory.metafactory(lookup, sourceMethodName, factoryType, type, unreflect, type);

            MethodHandle target = callSite.getTarget();

            Method callMethod = returnClazz.getDeclaredMethod(sourceMethodName, paramsClass);
            callMethod.invoke(target.invoke());
            
            // 无参数，这样调用也可以
//            MethodHandle methodHandle = lookup.findVirtual(returnClazz, sourceMethodName, MethodType.methodType(void.class));
//            methodHandle.invoke(target.invoke());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        System.exit(-1);
    }
    
    
    private MethodHandles.Lookup getLookup(Class<?> callerClazz) throws Exception {
        Class<?> c = Class.forName(MethodHandles.Lookup.class.getName());
        Field implLookup = c.getDeclaredField("IMPL_LOOKUP");
        implLookup.setAccessible(true);

        Object o = implLookup.get(MethodHandles.Lookup.class);

        Method method = c.getDeclaredMethod("in", Class.class);
        
        return (MethodHandles.Lookup) method.invoke(o, callerClazz);
    }


}
