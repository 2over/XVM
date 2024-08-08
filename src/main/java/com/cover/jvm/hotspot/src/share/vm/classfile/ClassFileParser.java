package com.cover.jvm.hotspot.src.share.vm.classfile;

import com.cover.jvm.hotspot.src.share.tools.DataTranslate;
import com.cover.jvm.hotspot.src.share.tools.Stream;
import com.cover.jvm.hotspot.src.share.vm.oops.*;
import com.cover.jvm.hotspot.src.share.vm.utilities.AccessFlags;
import com.cover.jvm.jdk.classes.sun.misc.AppClassLoader;
import com.sun.org.apache.bcel.internal.classfile.InnerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassFileParser {

    private static Logger logger = LoggerFactory.getLogger(ClassFileParser.class);

    public static InstanceKlass parseClassFile(AppClassLoader appClassLoader, byte[] content) {
        int index = 0;
        InstanceKlass klass = new InstanceKlass();
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];

        // 魔数 4B
        Stream.readU4Simple(content, 0, klass.getMagic());
        index += 4;

        // 次版本号 2B
        Stream.readU2Simple(content, index, klass.getMinorVersion());
        index += 2;

        // 主版本号 2B
        Stream.readU2Simple(content, index, klass.getMajorVersion());
        index += 2;

        // 常量池大小 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        klass.getConstantPool().setLength(DataTranslate.byteToUnsignedShort(u2Arr));
        klass.getConstantPool().initContainer();

        // 常量池 N字节
        index = parseContentPool(content, klass, index);
        
        // 类的访问庆权限 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setAccessFlag(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // 类名 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setThisClass(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // 父类名 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setSuperClass(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // 实现的接口个数 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setInterfacesLength(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // 实现的接口
        if (0 != klass.getInterfacesLength()) {
            logger.info("开始解析实现的接口信息: ");
            index = parseInterface(content, klass, index);
        }
        // 属性数量 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setFieldsLength(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // 属性
        index = parseFields(content, klass, index);
        
        // 方法数量 2B
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setMethodLength(DataTranslate.byteToUnsignedShort(u2Arr));
        
        klass.initMethodsContainer();
        
        // 方法
        index = parseMethods(content, klass, index);
        
        // 属性数量
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        klass.setAttributeLength(DataTranslate.byteToUnsignedShort(u2Arr));
        
        logger.info("开始解析类的属性, 数量: " + klass.getAttributeLength());
        
        // 属性
        for (int i = 0; i < klass.getAttributeLength(); i++) {
            Stream.readU2Simple(content, index, u2Arr);
            String attrName = (String)klass.getConstantPool().getDataMap().get(DataTranslate.byteToUnsignedShort(u2Arr));
            if (attrName.equals("SourceFile")) {
                index = parseSourceFile(content, index, klass, attrName);
            } else if (attrName.equals("RuntimeInvisibleAnnotations")) {
                index = parseRuntimeInvisibleAnnotations(content, index, klass, attrName);
            } else if (attrName.equals("RuntimeVisibleAnnotations")) {
                index = parseRuntimeVisibleAnnotations(content, index, klass, attrName);
            } else if (attrName.equals("InnerClasses")) {
                index = parseInnerClasses(content, index, klass, attrName);
            } else if (attrName.equals("BootstrapMethods")) {
                index = parseBootstrapMethods(content, index, klass, attrName);
            } else {
                throw new Error("无法识别的类属性:" + attrName);
            }
            
        }

        return klass;
    }

    private static int parseBootstrapMethods(byte[] content, int index, InstanceKlass klass, String attrName) {
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];

        BootstrapMethods bootstrapMethods = new BootstrapMethods();
        klass.getAttributeInfos().put(attrName, bootstrapMethods);

        // name index
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        bootstrapMethods.setAttrNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
        bootstrapMethods.setAttrName((String) klass.getConstantPool().getDataMap().get(bootstrapMethods.getAttrNameIndex()));

        // length
        Stream.readU4Simple(content, index, u4Arr);
        index += 4;


        bootstrapMethods.setAttrLength(DataTranslate.byteArrayToInt(u4Arr));

        // inner classes num
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        bootstrapMethods.setNumBootstrapMethods(DataTranslate.byteToUnsignedShort(u2Arr));

        logger.info("\t 第 " + klass.getAttributeInfos().size() + " 个属性: " +
                "name: " + bootstrapMethods.getAttrName()
                + ", num : " + bootstrapMethods.getNumBootstrapMethods());

        for (int i = 0; i < bootstrapMethods.getNumBootstrapMethods(); i++) {
            BootstrapMethods.Item item = bootstrapMethods.new Item();
            bootstrapMethods.getBootstrapMethods().add(item);

            // bootstrap method ref
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            item.setNumBootstrapArguments(DataTranslate.byteToUnsignedShort(u2Arr));
            item.initContainer();

            // bootstrap arguments
            for (int j = 0; j < item.getNumBootstrapArguments(); j++) {
                Stream.readU2Simple(content, index, u2Arr);
                index += 2;

                item.getBootstrapArguments()[j] = DataTranslate.byteToUnsignedShort(u2Arr);
            }
        }

        return index;
    }

    private static int parseInnerClasses(byte[] content, int index, InstanceKlass klass, String attrName) {
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];

        InnerClasses innerClasses = new InnerClasses();
        klass.getAttributeInfos().put(attrName, innerClasses);

        // name index
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        innerClasses.setAttrNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
        innerClasses.setAttrName((String) klass.getConstantPool().getDataMap().get(innerClasses.getAttrNameIndex()));

        // length
        Stream.readU4Simple(content, index, u4Arr);
        index += 4;

        innerClasses.setAttrLength(DataTranslate.byteArrayToInt(u4Arr));

        // inner classes num
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        innerClasses.setNumOfClasses(DataTranslate.byteToUnsignedShort(u2Arr));

        logger.info("\t 第 " + klass.getAttributeInfos().size() + " 个属性: "
                + " name: " + innerClasses.getAttrName()
                + ", classes num: " + innerClasses.getNumOfClasses());
        for (int i = 0; i < innerClasses.getNumOfClasses(); i++) {
            InnerClasses.Item item = innerClasses.new Item();

            innerClasses.getClasses().add(item);

            // inner class info index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            item.setInterClassInfoIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            // outer class info index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            item.setOuterClassInfoIndex(DataTranslate.byteToUnsignedShort(u2Arr));

            // inner class index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            item.setInnerNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));

            // access flags
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            item.setAccessFlags(new AccessFlags(DataTranslate.byteToUnsignedShort(u2Arr)));

            logger.info("\t\t inter class index: " + item.getInterClassInfoIndex()
                    + ", outer class index: " + item.getOuterClassInfoIndex()
                    + ", inner name index: " + item.getInnerNameIndex()
                    + ", acess flag : " + item.getAccessFlags().getFlag());
        }

        return index;
    }

    private static int parseRuntimeVisibleAnnotations(byte[] content, int index, InstanceKlass klass, String attrName) {
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];

        RuntimeVisibleAnnotations attributeInfo = new RuntimeVisibleAnnotations();
        klass.getAttributeInfos().put(attrName, attributeInfo);

        // name index
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        attributeInfo.setAttrNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
        attributeInfo.setAttrName((String) klass.getConstantPool().getDataMap().get(attributeInfo.getAttrNameIndex()));

        // length
        Stream.readU4Simple(content, index, u4Arr);
        index += 4;

        attributeInfo.setAttrLength(DataTranslate.byteArrayToInt(u4Arr));

        // annotation num
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        attributeInfo.setAnnotationsNum(DataTranslate.byteToUnsignedShort(u2Arr));

        // annotations
        for (int i = 0; i < attributeInfo.getAnnotationsNum(); i++) {
            Annotation annotation = new Annotation();
            attributeInfo.getAnnotations().add(annotation);

            // type index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            annotation.setTypeIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            annotation.setTypeStr((String) klass.getConstantPool().getDataMap().get(annotation.getTypeIndex()));

            // elements num
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            annotation.setElementsNum(DataTranslate.byteToUnsignedShort(u2Arr));
            if (0 != annotation.getElementsNum()) {
                throw new Error("未做处理");
            }

            logger.info("\t 属性: " + attributeInfo.getAttrName()
                    + ", name: " + annotation.getTypeStr()
                    + ", length: " + annotation.getElementsNum());
        }
        return index;
    }

    private static int parseRuntimeInvisibleAnnotations(byte[] content, int index, InstanceKlass klass, String attrName) {
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];

        RuntimeInvisibleAnnotations attributeInfo = new RuntimeInvisibleAnnotations();
        klass.getAttributeInfos().put(attrName, attributeInfo);

        // name index
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        attributeInfo.setAttrNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
        attributeInfo.setAttrName((String) klass.getConstantPool().getDataMap().get(attributeInfo.getAttrNameIndex()));

        // length
        Stream.readU4Simple(content, index, u4Arr);
        index += 4;

        attributeInfo.setAttrLength(DataTranslate.byteArrayToInt(u4Arr));

        // annotations num
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;

        // annotations
        for (int i = 0; i < attributeInfo.getAnnotationsNum(); i++) {
            Annotation annotation = new Annotation();
            attributeInfo.getAnnotations().add(annotation);

            // type index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            annotation.setTypeIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            annotation.setTypeStr((String) klass.getConstantPool().getDataMap().get(annotation.getTypeIndex()));

            // elements num
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            annotation.setElementsNum(DataTranslate.byteToUnsignedShort(u2Arr));
            if (0 != annotation.getElementsNum()) {
                throw new Error("未做处理");
            }

            logger.info("\t 第 " + attributeInfo.getAnnotations().size() + " 个属性:" + attributeInfo.getAttrName()
                    + " , name: " + annotation.getTypeStr()
                    + " , length: " + annotation.getElementsNum());
        }
        return index;
    }

    private static int parseSourceFile(byte[] content, int index, InstanceKlass klass, String attrName) {
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];
        
        AttributeInfo attributeInfo = new AttributeInfo();
        
        klass.getAttributeInfos().put(attrName, attributeInfo);
        
        // name index
        Stream.readU2Simple(content, index, u2Arr);
        index += 2;
        
        attributeInfo.setAttrNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
        
        // length
        Stream.readU4Simple(content, index, u4Arr);
        index += 4;
        attributeInfo.setAttrLength(DataTranslate.byteArrayToInt(u4Arr));
        
        attributeInfo.initContainer();
        
        // data
        Stream.readU2Simple(content, index, attributeInfo.getContainer());
        index += 2;
        
        logger.info("\t第 " + klass.getAttributeInfos().size() + " 个属性: " + klass.getConstantPool().getDataMap().get(attributeInfo.getAttrNameIndex())
                    + ", name index :" + attributeInfo.getAttrNameIndex()
                    + ", length: " + attributeInfo.getAttrLength()
                    + ", data: " + DataTranslate.byteToUnsignedShort(attributeInfo.getContainer()) 
                    + ", ( " + klass.getConstantPool().getDataMap().get(DataTranslate.byteToUnsignedShort(attributeInfo.getContainer())) + " )");
        
        return index;
    }

    private static int parseMethods(byte[] content, InstanceKlass klass, int index) {
        for (int i = 0; i < klass.getMethodLength(); i++) {
            byte[] u2Arr = new byte[2];
            byte[] u4Arr = new byte[4];

            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setBelongKlass(klass);
            klass.getMethods()[i] = methodInfo;
            
            // access flag
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            methodInfo.setAccessFlags(new AccessFlags(DataTranslate.byteToUnsignedShort(u2Arr)));
            
            // name index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            methodInfo.setNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            methodInfo.setMethodName((String)methodInfo.getBelongKlass().getConstantPool().getDataMap().get(methodInfo.getNameIndex()));
            
            logger.info("解析方法: " + methodInfo.getMethodName());
            
            // descriptor index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            methodInfo.setDescriptorIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            
        }
        
        return index;
        
    }

    private static int parseFields(byte[] content, InstanceKlass klass, int index) {
        logger.info("解析属性:");
        for (int i = 0; i < klass.getFieldsLength(); i++) {
            byte[] u2Arr = new byte[2];
            FieldInfo fieldInfo = new FieldInfo();
            klass.getFields().add(fieldInfo);
            
            // access flag
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            fieldInfo.setAccessFlags(DataTranslate.byteToUnsignedShort(u2Arr));
            
            // name index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            fieldInfo.setNameIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            
            // descriptor index
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            fieldInfo.setDescriptorIndex(DataTranslate.byteToUnsignedShort(u2Arr));
            
            // attribute count
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;
            
            fieldInfo.setAttributesCount(DataTranslate.byteToUnsignedShort(u2Arr));
            
            // attribute
            if (0 != fieldInfo.getAttributesCount()) {
                throw new Error("属性的attribute count != 0");
            }
            
            logger.info("\t第 " + " 个属性: access flag: " + fieldInfo.getAccessFlags()
                        + ", name index: " + fieldInfo.getNameIndex() 
                        + ", descriptor index: " + fieldInfo.getDescriptorIndex()
                        + ", attribute count: " + fieldInfo.getAttributesCount());
        }
        
        return index;
    }

    private static int parseInterface(byte[] content, InstanceKlass klass, int index) {
        byte[] u2Arr = new byte[2];
        for (int i = 0; i < klass.getInterfacesLength(); i++) {
            Stream.readU2Simple(content, index, u2Arr);
            index += 2;

            int val = DataTranslate.byteToUnsignedShort(u2Arr);
            String name = klass.getConstantPool().getClassName(val);

            InterfaceInfo interfaceInfo = new InterfaceInfo(val, name);
            klass.getInterfaceInfos().add(interfaceInfo);

            logger.info("\t 第 " + (i + 1) + " 个接口: " + name);
        }
        return index;
    }

    private static int parseContentPool(byte[] content, InstanceKlass klass, int index) {
        logger.info("解析常量池:");
        byte[] u2Arr = new byte[2];
        byte[] u4Arr = new byte[4];
        byte[] u8Arr = new byte[8];

        for (int i = 1; i < klass.getConstantPool().getLength(); i++) {
            int tag = Stream.readU1Simple(content, index);
            index += 1;

            switch (tag) {
                case ConstantPool.JVM_CONSTANT_Utf8: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Utf8;

                    // 字符串长度
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int len = DataTranslate.byteToUnsignedShort(u2Arr);
                    // 字符串内容
                    byte[] str = new byte[len];

                    Stream.readSimple(content, index, len, str);
                    index += len;

                    klass.getConstantPool().getDataMap().put(i, new String(str));
                    logger.info("\t 第 " + i + " 个: 类型: utf8, 值: " + klass.getConstantPool().getDataMap().get(i));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Integer: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Integer;
                    throw new Error("程序未做处理");
                }
                case ConstantPool.JVM_CONSTANT_Float: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Float;
                    Stream.readU4Simple(content, index, u4Arr);
                    index += 4;

                    klass.getConstantPool().getDataMap().put(i, DataTranslate.byteToFloat(u4Arr));
                    logger.info("\t第 " + i + " 个: 类型: Float, 值: " + klass.getConstantPool().getDataMap().get(i));
                    break;

                }
                case ConstantPool.JVM_CONSTANT_Long: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Long;
                    Stream.readU8Simple(content, index, u8Arr);

                    index += 8;
                    klass.getConstantPool().getDataMap().put(i, DataTranslate.bytesToLong(u8Arr));
                    logger.info("\t第 " + " 个: 类型: Long, 值: " + klass.getConstantPool().getDataMap().get(i));

                    /**
                     * 因为一个long在常量池中需要两个成员项目来存储
                     * 所以需要处理
                     */
                    klass.getConstantPool().getTag()[++i] = ConstantPool.JVM_CONSTANT_Long;
                    klass.getConstantPool().getDataMap().put(i, DataTranslate.bytesToLong(u8Arr));
                    logger.info("\t第 " + i + " 个: 类型: Long, 值: " + klass.getConstantPool().getDataMap().get(i));
                    break;
                }

                case ConstantPool.JVM_CONSTANT_Double: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Double;

                    Stream.readU8Simple(content, index, u8Arr);
                    index += 8;

                    klass.getConstantPool().getDataMap().put(i, DataTranslate.bytesToDouble(u8Arr, false));

                    logger.info("\t第 " + i + " 个: 类型: Double, 值: " + klass.getConstantPool().getDataMap().get(i));
                    /**
                     * 因为一个double在常量池中华需要两个成员项目来存储
                     * 所以需要处理
                     */
                    klass.getConstantPool().getTag()[++i] = ConstantPool.JVM_CONSTANT_Double;
                    klass.getConstantPool().getDataMap().put(i, DataTranslate.bytesToDouble(u8Arr, false));

                    logger.info("\t第 " + i + " 个: 类型: Double, 值： " + klass.getConstantPool().getDataMap().get(i));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Class: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Class;
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;
                    klass.getConstantPool().getDataMap().put(i, DataTranslate.byteToUnsignedShort(u2Arr));
                    logger.info("\t第 " + i + " 个: 类型: Class, 值: " + klass.getConstantPool().getDataMap().get(i));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_String: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_String;

                    // Utf8_info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;
                    klass.getConstantPool().getDataMap().put(i, DataTranslate.byteToUnsignedShort(u2Arr));
                    logger.info("\t第 " + i + " 个: 类型: String, 值无法获取, 因为字符串的内容还未解析到");
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Fieldref: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Fieldref;

                    // Class_info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int classIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // NameAndType info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int nameAndTypeIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    klass.getConstantPool().getDataMap().put(i, classIndex << 16 | nameAndTypeIndex);
                    logger.info("\t第 " + i + " 个: 类型: Field, 值: 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Methodref: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_Methodref;

                    // Class_info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int classIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // NameAndType info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int nameAndTypeIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // 将classIndex与nameAndTypeIndex拼成一个，前十六位是classIndex,后十六位是nameAndTypeIndex
                    klass.getConstantPool().getDataMap().put(i, classIndex << 16 | nameAndTypeIndex);
                    logger.info("\t第 " + i + " 个: 类型: Method, 值: 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));
                    break;

                }
                case ConstantPool.JVM_CONSTANT_InterfaceMethodref: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_InterfaceMethodref;

                    // Class_info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int classIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // NameAndType info
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int nameAndTypeIndex = DataTranslate.byteToUnsignedShort(u2Arr);
                    // 将classIndex与nameAndTypeIndex拼成一个，前十六位是classIndex,后十六位是nameAndTypeIndex
                    klass.getConstantPool().getDataMap().put(i, classIndex << 16 | nameAndTypeIndex);
                    logger.info("\t第 " + i + " 个: 类型: InterfaceMethodreg, 值: 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_NameAndType: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_NameAndType;

                    // 方法名
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int methodNameIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // 方法描述符
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int methodDescriptorIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    klass.getConstantPool().getDataMap().put(i, methodNameIndex << 16 | methodDescriptorIndex);

                    logger.info("\t第 " + i + " 个: 类型: NameAndType, 值: 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_MethodHandle: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_InvokeDynamic;

                    // reference kind
                    byte referenceKind = Stream.readU1Simple(content, index);
                    index += 1;

                    // reference index
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;
                    int referenceIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    klass.getConstantPool().getDataMap().put(i, referenceKind << 16 | referenceIndex);
                    logger.info("\t第 " + i + " 个: 类型: MethodHandle, 值: 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_MethodType: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_String;

                    // descriptor index
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    klass.getConstantPool().getDataMap().put(i, DataTranslate.byteToUnsignedShort(u2Arr));

                    logger.info("\t第 " + i + " 个 : 类型: MethodType, 值: " + klass.getConstantPool().getDataMap().get(i));
                    break;

                }
                case ConstantPool.JVM_CONSTANT_InvokeDynamic: {
                    klass.getConstantPool().getTag()[i] = ConstantPool.JVM_CONSTANT_InvokeDynamic;

                    // bootstrap method attr
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int bootstrapMethodAttrIndex = DataTranslate.byteToUnsignedShort(u2Arr);

                    // 方法描述符
                    Stream.readU2Simple(content, index, u2Arr);
                    index += 2;

                    int methodDescriptorIndex = DataTranslate.byteToUnsignedShort(u2Arr);
                    klass.getConstantPool().getDataMap().put(i, bootstrapMethodAttrIndex << 16 | methodDescriptorIndex);

                    logger.info("\t第 " + i + " 个: 类型: InvokeDynamic, 值 : 0x" + Integer.toHexString((int) klass.getConstantPool().getDataMap().get(i)));

                    break;
                }
                default:
                    throw new Error("无法识别的常量池项");
            }
        }
        return index;

    }
}
