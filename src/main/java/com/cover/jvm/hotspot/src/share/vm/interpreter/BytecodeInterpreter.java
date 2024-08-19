package com.cover.jvm.hotspot.src.share.vm.interpreter;

import com.cover.jvm.hotspot.src.share.vm.classfile.BootClassLoader;
import com.cover.jvm.hotspot.src.share.vm.classfile.DescriptorStream2;
import com.cover.jvm.hotspot.src.share.vm.memory.StackObj;
import com.cover.jvm.hotspot.src.share.vm.oops.ArrayOop;
import com.cover.jvm.hotspot.src.share.vm.oops.ConstantPool;
import com.cover.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import com.cover.jvm.hotspot.src.share.vm.prims.JavaNativeInterface;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaThread;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.cover.jvm.hotspot.src.share.vm.runtime.StackValue;
import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BytecodeInterpreter extends StackObj {

    private static Logger logger = LoggerFactory.getLogger(BytecodeInterpreter.class);

    public static void run(JavaThread thread, MethodInfo method) {

        // 得到字节码指令
        BytecodeStream code = method.getAttributes()[0].getCode();

        // 得到栈帧
        JavaVFrame frame = (JavaVFrame) thread.getStack().peek();

        int c;

        while (!code.end()) {
            c = code.getU1Code();

            switch (c) {
                case Bytecodes.ILLEGAL: {
                    logger.info("执行指令: ILLEGAL");
                    break;
                }
                case Bytecodes.NOP: {
                    logger.info("执行指令: NOP");
                    break;
                }
                case Bytecodes.ACONST_NULL: {
                    logger.info("执行指令: ACONST_NULL");
                    frame.getStack().pushNull(frame);
                    break;
                }
                case Bytecodes.ICONST_0: {
                    logger.info("执行指令: ICONST_0");

                    frame.getStack().push(new StackValue(BasicType.T_INT, 0));
                    break;
                }

                case Bytecodes.ICONST_1: {
                    logger.info("执行指令: ICONST_1");
                    frame.getStack().push(new StackValue(BasicType.T_INT, 1));
                }
                case Bytecodes.ICONST_2: {
                    logger.info("执行指令: ICONST_2");
                    frame.getStack().push(new StackValue(BasicType.T_INT, 2));
                    break;
                }
                case Bytecodes.ICONST_3: {
                    logger.info("执行指令: ICONST_3");
                    frame.getStack().push(new StackValue(BasicType.T_INT, 3));
                    break;
                }
                case Bytecodes.ICONST_4: {
                    logger.info("执行指令: ICONST_4");
                    frame.getStack().push(new StackValue(BasicType.T_INT, 4));
                    break;
                }
                case Bytecodes.ICONST_5: {
                    logger.info("执行指令: ICONST_5");
                    frame.getStack().push(new StackValue(BasicType.T_INT, 5));
                    break;
                }
                case Bytecodes.LCONST_0: {
                    logger.info("执行指令: LCONST_0");
                    /**
                     * 这里一定要强转成long,否则会当成int处理
                     */
                    frame.getStack().push(new StackValue(BasicType.T_LONG, (long) 0));
                    break;
                }
                case Bytecodes.LCONST_1: {
                    logger.info("执行指令: LCONST_1");
                    /**
                     * 这里一定要强转成long,否则会当成int处理
                     */
                    frame.getStack().push(new StackValue(BasicType.T_LONG, (long) 1));
                    break;
                }
                case Bytecodes.FCONST_0: {
                    logger.info("执行指令： FCONST_0");
                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, 0f));
                    break;
                }
                case Bytecodes.FCONST_1: {
                    logger.info("执行指令： FCONST_1");
                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, 1f));
                    break;
                }
                case Bytecodes.FCONST_2: {
                    logger.info("执行指令: FCONST_2");
                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, 2f));
                    break;
                }
                case Bytecodes.DCONST_0: {
                    logger.info("执行指令: DCONST_0");
                    frame.getStack().pushDouble(0);
                    break;
                }
                case Bytecodes.DCONST_1: {
                    logger.info("执行指令: DCONST_1");
                    frame.getStack().pushDouble(1);
                    break;
                }
                case Bytecodes.BIPUSH: {
                    logger.info("执行指令: BIPUSH");
                    // 获取操作数
                    int val = code.getU1Code();

                    // 操作数入栈(操作数栈)
                    frame.getStack().push(new StackValue(BasicType.T_INT, val));
                    break;
                }
                case Bytecodes.SIPUSH: {
                    logger.info("执行指令: SIPUSH");

                    // 获取操作数栈
                    int val = code.getU1Code();

                    // 操作数入栈(操作数栈)
                    frame.getStack().push(new StackValue(BasicType.T_INT, val));
                    break;
                }
                case Bytecodes.LDC: {
                    logger.info("执行指令： LDC");

                    // 取出操作数栈
                    int operand = code.getU1Code();

                    // 取出常量池中的信息
                    int tag = method.getBelongKlass().getConstantPool().getTag()[operand];
                    switch (tag) {
                        case ConstantPool.JVM_CONSTANT_Float: {
                            // 取出数值
                            float f = (float) method.getBelongKlass().getConstantPool().getDataMap().get(operand);

                            // 压入栈
                            frame.getStack().push(new StackValue(BasicType.T_FLOAT, f));
                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_String: {
                            int index = (int) method.getBelongKlass().getConstantPool().getDataMap().get(operand);

                            String content = (String) method.getBelongKlass().getConstantPool().getDataMap().get(index);

                            frame.getStack().push(new StackValue(BasicType.T_OBJECT, content));
                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_Class: {
                            String content = method.getBelongKlass().getConstantPool().getClassName(operand);

                            try {
                                frame.getStack().push(new StackValue(BasicType.T_OBJECT, Class.forName(content.replace('/', '.'))));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        default: {
                            logger.error("未知类型");
                            break;
                        }
                    }
                    break;
                }
                case Bytecodes.LDC_W: {
                    throw new Error("未做处理: LDC_W");
                }
                case Bytecodes.ILOAD_0: {
                    logger.info("执行指令: ILOAD_0");
                    // 取出变量表的数据
                    StackValue value = frame.getLocals().get(0);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.LLOAD: {
                    logger.info("执行指令: LLOAD");
                    int val = code.getU1Code();
                    StackValue value = frame.getLocals().get(val);
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.ILOAD_1: {
                    logger.info("执行指令: ILOAD_1");
                    // 取出局部变量表的数据
                    StackValue value = frame.getLocals().get(1);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.ILOAD_2: {
                    logger.info("执行指令: ILOAD_2");
                    // 取出局部变量表的数据
                    StackValue value = frame.getLocals().get(2);
                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.ILOAD_3: {
                    logger.info("执行指令: ILOAD_3");
                    // 取出局部变量表的数据
                    StackValue value = frame.getLocals().get(3);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                /**
                 * 将常量池中的long、double类型的数值推送至栈顶
                 * 当前的JVM因为在解析常量池中合并了，所以读一次就可以了
                 * Hotspot中解析常量池的时候没合，会读两次,push两次
                 */
                case Bytecodes.LDC2_W: {
                    logger.info("执行指令: LDC2_W");

                    // 取出操作数
                    int operand = code.getUnsignedShort();

                    /**
                     * 数值入栈，这边实现方式略有差别
                     * long是用8字节的byte数组存储的
                     * double是用两个slot存储的
                     */
                    int tag = method.getBelongKlass().getConstantPool().getTag()[operand];
                    if (ConstantPool.JVM_CONSTANT_Long == tag) {
                        long l = (long) method.getBelongKlass().getConstantPool().getDataMap().get(operand);
                        frame.getStack().push(new StackValue(BasicType.T_LONG, l));
                    } else if (ConstantPool.JVM_CONSTANT_Double == tag) {
                        double d = (double) method.getBelongKlass().getConstantPool().getDataMap().get(operand);
                        frame.getStack().pushDouble(d);
                    } else {
                        throw new Error("无法识别的格式");
                    }

                    break;
                }
                case Bytecodes.LLOAD_0: {
                    logger.info("执行指令: LLOAD_0");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(0);

                    // 压入栈
                    frame.getStack().push(value);

                    break;
                }
                case Bytecodes.LLOAD_1: {
                    logger.info("执行指令: LLOAD_1");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(1);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.LLOAD_2: {
                    logger.info("执行指令: LLOAD_2");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(2);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.LLOAD_3: {
                    logger.info("执行指令: LLOAD_3");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(3);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.FLOAD_0: {
                    logger.info("执行指令： FLOAD_0");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(0);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.FLOAD_1: {
                    logger.info("执行指令: FLOAD_1");
                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(1);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.FLOAD_2: {
                    logger.info("执行指令: FLOAD_2");

                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(2);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.FLOAD_3: {
                    logger.info("执行指令: FLOAD_3");

                    // 取出局部变量表中的数据
                    StackValue value = frame.getLocals().get(3);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.DLOAD_0: {
                    logger.info("执行指令: DLOAD_0");
                    // 取出数据
                    StackValue value1 = frame.getLocals().get(0);
                    StackValue value2 = frame.getLocals().get(1);

                    // 压入栈
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);
                    break;
                }
                case Bytecodes.DLOAD_1: {
                    logger.info("执行指令: DLOAD_1");
                    // 取出数据
                    StackValue value1 = frame.getLocals().get(1);
                    StackValue value2 = frame.getLocals().get(2);

                    // 压入栈
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                case Bytecodes.DLOAD_3: {
                    logger.info("执行指令: DLOAD_3");
                    // 取出数据
                    StackValue value1 = frame.getLocals().get(3);
                    StackValue value2 = frame.getLocals().get(4);

                    break;
                }
                case Bytecodes.ALOAD_0: {
                    logger.info("执行指令: ALOAD_0");
                    // 从局部变量表取出数据
                    StackValue value = frame.getLocals().get(0);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.ALOAD_1: {
                    logger.info("执行指令: ALOAD_1");
                    // 从局部变量表取出数据
                    StackValue value = frame.getLocals().get(1);

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                case Bytecodes.ALOAD_2: {
                    logger.info("执行指令: ALOAD_2");
                    // 从局部变量表取出数据
                    StackValue value = frame.getLocals().get(2);

                    // 压入栈
                    frame.getStack().push(value);

                    break;
                }
                case Bytecodes.ALOAD_3: {
                    logger.info("执行指令: ALOAD_3");
                    // 从局部变量表取出数据
                    StackValue value = frame.getLocals().get(3);

                    // 压入栈
                    frame.getStack().push(value);

                    break;
                }
                case Bytecodes.IALOAD: {
                    logger.info("执行指令: IALOAD");
                    int index = frame.getStack().pop().getVal();

                    ArrayOop oop = frame.getStack().popArray(frame);

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }
                    int v = (int) oop.getData().get(index);

                    frame.getStack().pushInt(v, frame);
                    break;
                }
                case Bytecodes.DALOAD: {
                    logger.info("执行指令: DALOAD");
                    int index = frame.getStack().pop().getVal();
                    ArrayOop oop = frame.getStack().popArray(frame);

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    frame.getStack().pushDouble((Double) oop.getData().get(index));
                    break;

                }
                case Bytecodes.AALOAD: {
                    logger.info("执行指令: AALOAD");
                    int index = frame.getStack().pop().getVal();
                    ArrayOop oop = frame.getStack().popArray(frame);
                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    frame.getStack().push(new StackValue(BasicType.T_OBJECT, oop.getData().get(index)));
                    break;
                }
                case Bytecodes.BALOAD: {
                    logger.info("执行指令: BALOAD");
                    int index = frame.getStack().pop().getVal();

                    ArrayOop oop = frame.getStack().popArray(frame);

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    int v = (int) oop.getData().get(index);

                    frame.getStack().pushInt(v, frame);
                    break;
                }
                case Bytecodes.ISTORE: {
                    logger.info("执行指令: ISTORE");
                    // 获取操作数
                    int index = code.getU1Code();

                    // 取出数据
                    StackValue values = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(index, values);
                    break;
                }
                case Bytecodes.LSTORE: {
                    logger.info("执行指令: DSTORE");
                    // 获取操作数
                    int index = code.getU1Code();

                    // 取出数据
                    StackValue values = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(index, values);
                    break;
                }
                case Bytecodes.FSTORE: {
                    throw new Error("未做处理: FSTORE");
                }
                case Bytecodes.DSTORE: {
                    logger.info("执行指令: DSTORE");
                    // 获取操作数
                    int index = code.getU1Code();

                    // 取出数据
                    StackValue[] values = frame.getStack().popDouble2();

                    // 存入局部变量表
                    frame.getLocals().add(index, values[0]);
                    frame.getLocals().add(index + 1, values[1]);
                    break;
                }
                case Bytecodes.ASTORE: {
                    logger.info("执行指令: DSTORE");
                    // 获取操作数
                    int index = code.getU1Code();

                    // 取出数据
                    StackValue values = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(index, values);
                    break;
                }
                case Bytecodes.ISTORE_0: {
                    logger.info("执行指令: ISTORE_0");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(0, value);
                    break;
                }
                case Bytecodes.ISTORE_1: {
                    logger.info("执行指令: ISTORE_1");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(1, value);
                    break;
                }
                case Bytecodes.ISTORE_2: {
                    logger.info("执行指令: ISTORE_2");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(2, value);
                    break;
                }
                case Bytecodes.ISTORE_3: {
                    logger.info("执行指: ISTORE_3");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(3, value);
                    break;
                }
                case Bytecodes.LSTORE_0: {
                    logger.info("执行指令: LSTORE_0");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(0, value);
                    break;
                }
                case Bytecodes.LSTORE_1: {
                    logger.info("执行指令: LSTORE_1");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(1, value);
                    break;
                }
                case Bytecodes.LSTORE_2: {
                    logger.info("执行指令： LSTORE_2");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(2, value);
                    break;
                }
                case Bytecodes.LSTORE_3: {
                    logger.info("执行指令: LSTORE_3");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(3, value);
                    break;
                }
                case Bytecodes.FSTORE_0: {
                    logger.info("执行指令: FSTORE_0");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(0, value);
                    break;
                }
                case Bytecodes.FSTORE_1: {
                    logger.info("执行指令: FSTORE_1");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(1, value);
                    break;
                }
                case Bytecodes.FSTORE_2: {
                    logger.info("执行指令： FSOTRE_2");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(2, value);
                    break;
                }
                case Bytecodes.FSTORE_3: {
                    logger.info("执行指令: FSOTRE_3");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(3, value);
                    break;
                }
                case Bytecodes.DSTORE_0: {
                    logger.info("执行指令: DSTORE_0");
                    // 取出数据
                    StackValue[] values = frame.getStack().popDouble2();
                    // 存入局部变量表
                    frame.getLocals().add(0, values[1]);
                    frame.getLocals().add(1, values[0]);
                    break;
                }
                case Bytecodes.DSTORE_1: {
                    logger.info("执行指令: DSTORE_1");
                    // 取出数据
                    StackValue[] values = frame.getStack().popDouble2();

                    // 存入局部变量表
                    frame.getLocals().add(1, values[1]);
                    frame.getLocals().add(2, values[0]);
                    break;
                }
                case Bytecodes.DSTORE_2: {
                    logger.info("执行指令: DSTORE_2");
                    // 取出数据
                    StackValue[] values = frame.getStack().popDouble2();

                    // 存入局部变量表
                    frame.getLocals().add(2, values[1]);
                    frame.getLocals().add(3, values[0]);
                    break;
                }
                case Bytecodes.DSTORE_3: {
                    logger.info("执行指令: DSTORE_3");
                    // 取出数据
                    StackValue[] values = frame.getStack().popDouble2();

                    // 存入局部变量表
                    frame.getLocals().add(3, values[1]);
                    frame.getLocals().add(4, values[0]);
                    break;
                }
                case Bytecodes.ASTORE_0: {
                    logger.info("执行指令: ASTORE_0");
                    // 取出数据
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(0, value);
                    break;
                }
                case Bytecodes.ASTORE_1: {
                    logger.info("执行指令: ASTORE_1");
                    // 取出数据
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(1, value);
                    break;
                }
                case Bytecodes.ASTORE_2: {
                    logger.info("执行指令: ASTORE_2");
                    // 取出数据
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(2, value);
                    break;
                }
                case Bytecodes.ASTORE_3: {
                    logger.info("执行指令: ASTORE_3");
                    // 取出数据
                    StackValue value = frame.getStack().pop();

                    // 存入局部变量表
                    frame.getLocals().add(3, value);
                    break;
                }
                case Bytecodes.IASTORE: {
                    logger.info("执行指令: IASTORE");
                    int val = frame.getStack().pop().getVal();
                    int index = frame.getStack().pop().getVal();

                    ArrayOop oop = (ArrayOop) frame.getStack().pop().getObject();
                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    try {
                        oop.getData().get(index);
                        oop.getData().set(index, val);
                    } catch (Exception e) {
                        oop.getData().add(val);
                    }

                    // 因为想数组中添加元素、修改元素都是这个指令，所以这样写会出问题
                    // oop.getData().add(index, val);
                    break;

                }
                case Bytecodes.DASTORE: {
                    logger.info("执行指令: DASTORE");
                    double val = frame.getStack().popDouble();
                    int index = frame.getStack().pop().getVal();

                    ArrayOop oop = (ArrayOop) frame.getStack().pop().getObject();

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    try {
                        oop.getData().get(index);
                        oop.getData().set(index, val);
                    } catch (Exception e) {
                        oop.getData().add(val);
                    }

                    break;
                }
                case Bytecodes.AASTORE: {
                    logger.info("执行指令: AASTORE");
                    StackValue value = frame.getStack().pop();
                    int index = frame.getStack().pop().getVal();
                    ArrayOop oop = (ArrayOop) frame.getStack().pop().getObject();

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    try {
                        oop.getData().get(index);
                        oop.getData().set(index, value.getObject());
                    } catch (Exception e) {
                        oop.getData().add(value.getObject());
                    }

                    break;
                }
                case Bytecodes.BASTORE: {
                    logger.info("执行指令: BASTORE");
                    int val = frame.getStack().pop().getVal();
                    int index = frame.getStack().pop().getVal();
                    ArrayOop oop = (ArrayOop) frame.getStack().pop().getObject();

                    if (index > oop.getSize() - 1) {
                        throw new Error("数组访问越界");
                    }

                    try {
                        oop.getData().get(index);
                        oop.getData().set(index, val);
                    } catch (Exception e) {
                        oop.getData().add(val);
                    }

                    break;
                }
                case Bytecodes.DUP: {
                    logger.info("执行指令： DUP");
                    // 取出栈顶元素
                    StackValue value = frame.getStack().peek();

                    // 压入栈
                    frame.getStack().push(value);
                    break;
                }
                /**
                 * 复制栈顶一个long或double类型的数据
                 */
                case Bytecodes.DUP2: {
                    logger.info("执行指令: DUP2");
                    StackValue value = frame.getStack().peek();
                    /**
                     * 需要通过栈顶元素来判断是long还是idouble
                     * long只需要取一次
                     * double需要取两次
                     * stack用的是C++ STL库中的，无法取第二个元素，所以这块处理麻烦些，直接取出double数值，两次压栈
                     */

                    if (BasicType.T_DOUBLE == value.getType()) {
                        double d = frame.getStack().popDouble();

                        frame.getStack().pushDouble(d);
                        frame.getStack().pushDouble(d);
                    } else if (BasicType.T_LONG == value.getType()) {
                        frame.getStack().push(value);
                    } else {
                        throw new Error("无法识别的类型");
                    }

                    break;
                }
                case Bytecodes.IADD: {
                    logger.info("执行指令: IADD");
                    // 取出操作数栈
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 检查操作数类型
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 运算
                    int ret = (int) value1.getData() + (int) value2.getData();
                    logger.info("执行指令: IADD, 运行结果: " + ret);

                    // 压入栈
                    frame.getStack().push(new StackValue(BasicType.T_INT, ret));
                    break;
                }
                case Bytecodes.LADD: {
                    logger.info("执行指令: LADD");
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    // 检查操作数类型
                    if (value1.getType() != BasicType.T_LONG || value2.getType() != BasicType.T_LONG) {
                        logger.error("不匹配的数据类型");
                        throw new Error("不匹配的数据类型");
                    }

                    long ret = (long) value1.getData() + (long) value2.getData();
                    logger.info("执行指令: LADD, 运行结果: " + ret);
                    frame.getStack().push(new StackValue(BasicType.T_LONG, ret));
                    break;
                }
                case Bytecodes.FADD: {
                    logger.info("执行指令: FADD");
                    // 取出操作数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 检查操作数类型
                    if (value1.getType() != BasicType.T_FLOAT || value2.getType() != BasicType.T_FLOAT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 运算
                    float ret = (float) value1.getData() + (float) value2.getData();
                    logger.info("执行指令: FADD, 运行结果: " + ret);

                    // 压入栈
                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, ret));
                    break;
                }
                case Bytecodes.DADD: {
                    logger.info("执行指令: DADD");
                    // 取出数据
                    double v1 = frame.getStack().popDouble();
                    double v2 = frame.getStack().popDouble();

                    double ret = v1 + v2;

                    logger.info("执行指令: DADD, 结果: " + ret);

                    frame.getStack().pushDouble(ret);
                    break;
                }
                case Bytecodes.ISUB: {
                    logger.info("执行指令: ISUB");

                    // 取出操作数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 检查操作数栈
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 运算
                    int ret = (int) value2.getData() - (int) value1.getData();

                    logger.info("\t 执行指令: ISUB, 运行结果: " + ret);

                    // 压入栈
                    frame.getStack().push(new StackValue(BasicType.T_INT, ret));

                    break;
                }
                case Bytecodes.LSUB: {
                    logger.info("执行指令: LSUB");
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 检查操作数类型
                    if (value1.getType() != BasicType.T_LONG || value2.getType() != BasicType.T_LONG) {
                        logger.error("不匹配的数据类型");
                        throw new Error("不匹配的数据类型");
                    }

                    long ret = (long) value2.getData() - (long) value1.getData();

                    logger.info("执行指令: LSUB, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_LONG, ret));
                    break;
                }
                case Bytecodes.FSUB: {
                    logger.info("执行指令: FSUB");

                    // 取出操作数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 检查操作数类型
                    if (value1.getType() != BasicType.T_FLOAT || value2.getType() != BasicType.T_FLOAT) {
                        logger.error("不匹配的数据类型");
                        throw new Error("不匹配的数据类型");
                    }

                    // 运算
                    float ret = (float) value2.getData() - (float) value1.getData();

                    logger.info("执行指令：FSUB, 运行结果: " + ret);

                    // 压入栈
                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, ret));

                    break;
                }
                case Bytecodes.DSUB: {
                    logger.info("执行指令: DSUB");

                    // 取出数据
                    double v1 = frame.getStack().popDouble();
                    double v2 = frame.getStack().popDouble();

                    double ret = v2 - v1;

                    logger.info("执行指令: DSUB, 结果: " + ret);

                    frame.getStack().pushDouble(ret);
                    break;
                }
                case Bytecodes.IMUL: {
                    logger.info("执行指令: IMUL");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    int ret = (int) value2.getData() * (int) value1.getData();

                    logger.info("\t 执行指令: IMUL, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_INT, ret));

                    break;

                }
                case Bytecodes.LMUL: {
                    logger.info("执行指令: LMUL");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_LONG || value2.getType() != BasicType.T_LONG) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    long ret = (long) value2.getData() * (long) value1.getData();

                    logger.info("执行指令: LMUL, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_LONG, ret));
                    break;
                }
                case Bytecodes.FMUL: {
                    logger.info("执行指令: FMUL");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_FLOAT || value2.getType() != BasicType.T_FLOAT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    float ret = (float) value2.getData() * (float) value1.getData();

                    logger.info("执行指令: FMUL, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, ret));

                    break;
                }
                case Bytecodes.DMUL: {
                    logger.info("执行指令: DMUL");

                    double v1 = frame.getStack().popDouble();
                    double v2 = frame.getStack().popDouble();

                    double ret = v1 * v2;

                    logger.info("执行指令: DMUL, 结果: " + ret);

                    frame.getStack().pushDouble(ret);
                    break;
                }
                case Bytecodes.IDIV: {
                    logger.info("执行指令: IDIV");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    int ret = (int) value2.getData() / (int) value1.getData();

                    logger.info("\t 执行指令: IDIV, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_INT, ret));
                    break;
                }
                case Bytecodes.LDIV: {
                    logger.info("执行指令: LDIV");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_LONG || value2.getType() != BasicType.T_LONG) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    long ret = (long) value2.getData() / (long) value1.getData();

                    logger.info("执行指令: LDIV, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_LONG, ret));

                    break;
                }
                case Bytecodes.FDIV: {
                    logger.info("执行指令: FDIV");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_FLOAT || value2.getType() != BasicType.T_FLOAT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    float ret = (float) value2.getData() / (float) value1.getData();

                    logger.info("执行指令: FDIV, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, ret));
                    break;
                }
                case Bytecodes.DDIV: {
                    logger.info("执行指令: DDIV");
                    double v1 = frame.getStack().popDouble();
                    double v2 = frame.getStack().popDouble();

                    double ret = v2 / v1;

                    logger.info("执行指令: DDIV, 结果: " + ret);

                    frame.getStack().pushDouble(ret);

                    break;
                }
                case Bytecodes.IREM: {
                    logger.info("执行指令: IREM");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    int ret = (int) value2.getData() % (int) value1.getData();

                    logger.info("\t 执行指令: IREM, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_INT, ret));
                    break;
                }
                case Bytecodes.LREM: {
                    logger.info("执行指令: LREM");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_LONG || value2.getType() != BasicType.T_LONG) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    long ret = (long) value2.getData() % (long) value1.getData();

                    logger.info("执行指令: LREM, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_LONG, ret));
                    break;
                }
                case Bytecodes.FREM: {
                    logger.info("执行指令: FREM");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    if (value1.getType() != BasicType.T_FLOAT || value2.getType() != BasicType.T_FLOAT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    float ret = (float) value2.getData() % (float) value1.getData();

                    logger.info("执行指令: FREM, 运行结果: " + ret);

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, ret));

                    break;
                }
                case Bytecodes.DREM: {
                    logger.info("执行指令： DREM");

                    double v1 = frame.getStack().popDouble();
                    double v2 = frame.getStack().popDouble();

                    double ret = v2 % v1;

                    logger.info("执行指令: DREM, 结果: " + ret);

                    frame.getStack().pushDouble(ret);

                    break;
                }
                case Bytecodes.IINC: {
                    logger.info("执行指令: IINC");
                    // 第一个操作数: slot的index
                    int index = code.getU1Code();

                    // 第二个操作数:增加多少
                    int step = code.getU1Code2();

                    // 完成运算
                    int v = (int) frame.getLocals().get(index).getData();
                    v += step;

                    // 写回局部变量表
                    frame.getLocals().add(index, new StackValue(BasicType.T_INT, v));

                    break;
                }
                case Bytecodes.I2L: {
                    logger.info("执行指令: I2L");

                    int v = (int) frame.getStack().pop().getData();

                    long l = v;

                    StackValue value = new StackValue(BasicType.T_LONG, l);
                    frame.getStack().push(value);

                    break;
                }
                case Bytecodes.I2F: {
                    logger.info("执行指令: I2F");

                    int v = (int) frame.getStack().pop().getData();

                    float f = v;

                    StackValue value = new StackValue(BasicType.T_FLOAT, f);

                    frame.getStack().push(value);

                    break;
                }
                case Bytecodes.I2D: {
                    logger.info("执行指令: I2D");

                    int value = frame.getStack().pop().getVal();

                    double v = value;

                    frame.getStack().pushDouble(v);

                    break;
                }
                case Bytecodes.L2I: {
                    logger.info("执行指令: L2I");

                    long l = (long) frame.getStack().pop().getData();

                    int i = (int) l;

                    frame.getStack().pushInt(i, frame);

                    break;
                }
                case Bytecodes.L2F: {
                    logger.info("执行指令: L2F");

                    long l = (long) frame.getStack().pop().getData();

                    float f = l;

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, f));

                    break;
                }
                case Bytecodes.L2D: {
                    logger.info("执行指令: L2D");

                    long l = (long) frame.getStack().pop().getData();

                    double d = l;

                    frame.getStack().pushDouble(d);
                    break;
                }
                case Bytecodes.F2I: {
                    logger.info("执行指令: F2I");

                    float f = (float) frame.getStack().pop().getData();

                    long v = (long) f;

                    frame.getStack().push(new StackValue(BasicType.T_LONG, v));

                    break;
                }
                case Bytecodes.F2L: {
                    logger.info("执行指令: F2L");

                    float f = (float) frame.getStack().pop().getData();

                    long v = (long) f;

                    frame.getStack().push(new StackValue(BasicType.T_LONG, v));

                    break;
                }
                case Bytecodes.F2D: {
                    logger.info("执行指令: F2D");

                    float f = (float) frame.getStack().pop().getData();
                    double v = f;

                    frame.getStack().pushDouble(v);

                    break;
                }
                case Bytecodes.D2I: {
                    logger.info("执行指令: D2I");

                    double d = frame.getStack().popDouble();

                    int v = (int) d;

                    frame.getStack().pushInt(v, frame);

                    break;
                }
                case Bytecodes.D2L: {
                    logger.info("执行指令: D2L");

                    double d = frame.getStack().popDouble();
                    long v = (long) d;

                    frame.getStack().push(new StackValue(BasicType.T_LONG, v));

                    break;
                }
                case Bytecodes.D2F: {
                    logger.info("执行指令： D2F");

                    double d = frame.getStack().popDouble();

                    float v = (float) d;

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, v));

                    break;
                }
                case Bytecodes.I2B: {
                    logger.info("执行指令: I2B");

                    int i = (int) frame.getStack().pop().getData();

                    byte v = (byte) i;

                    frame.getStack().pushInt(v, frame);

                    break;
                }
                case Bytecodes.I2C: {
                    logger.info("执行指令: I2C");

                    int i = (int) frame.getStack().pop().getData();
                    char v = (char) i;

                    frame.getStack().pushInt(v, frame);

                    break;
                }
                case Bytecodes.I2S: {
                    logger.info("执行指令: I2S");

                    int i = (int) frame.getStack().pop().getData();

                    short v = (short) i;

                    frame.getStack().pushInt(v, frame);

                    break;
                }
                case Bytecodes.LCMP: {
                    logger.info("执行指令: LCMP");

                    long l1 = (long) frame.getStack().pop().getData();
                    long l2 = (long) frame.getStack().pop().getData();

                    if (l1 > l2) {
                        frame.getStack().pushInt(1, frame);
                    } else if (l1 == l2) {
                        frame.getStack().pushInt(0, frame);
                    } else {
                        frame.getStack().pushInt(-1, frame);
                    }

                    break;
                }
                case Bytecodes.FCMPL: {
                    logger.info("执行指令: FCMPL");

                    float f1 = (float) frame.getStack().pop().getData();
                    float f2 = (float) frame.getStack().pop().getData();

                    if (f1 > f2) {
                        frame.getStack().pushInt(1, frame);
                    } else if (f1 == f2) {
                        frame.getStack().pushInt(0, frame);
                    } else {
                        frame.getStack().pushInt(-1, frame);
                    }

                    break;
                }
                case Bytecodes.FCMPG: {
                    logger.info("执行指令: FCMPG");
                    throw new Error("未做处理: FCMPG");
                }
                case Bytecodes.DCMPL: {
                    logger.info("执行指令: DCMPL");

                    throw new Error("未做处理: DCMPL");
                }
                case Bytecodes.DCMPG: {
                    logger.info("执行指令: DCMPG");

                    throw new Error("未做处理: DCMPG");
                }
                case Bytecodes.IFEQ: {
                    logger.info("执行指令: IFEQ");

                    int i = (int) frame.getStack().pop().getData();
                    int operand = code.getUnsignedShort();

                    if (0 == i) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                /**
                 * 当栈顶int类型数据不等于0时跳转
                 */
                case Bytecodes.IFNE: {
                    logger.info("执行指令: IFNE");

                    int i = (int) frame.getStack().pop().getData();

                    int operand = code.getUnsignedShort();

                    if (0 != i) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IFLT: {
                    logger.info("执行指令: IFLT");

                    int i = (int) frame.getStack().pop().getData();
                    int operand = code.getUnsignedShort();

                    if (i < 0) {
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IFGE: {
                    logger.info("执行指令: IFGE");

                    int i = (int) frame.getStack().pop().getData();

                    int operand = code.getUnsignedShort();

                    if (i >= 0) {
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IFGT: {
                    logger.info("执行指令: IFGT");

                    int i = (int) frame.getStack().pop().getData();

                    int operand = code.getUnsignedShort();

                    if (i > 0) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IFLE: {
                    logger.info("执行指令: IFLE");

                    int i = (int) frame.getStack().pop().getData();

                    int operand = code.getUnsignedShort();

                    if (i <= 0) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IF_ICMPEQ: {
                    logger.info("执行指令: IF_ICMPEQ");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value1.getVal() == value2.getVal()) {
                        /**
                         * -1: 减去IF_ICMPEQ指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */

                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IF_ICMPNE: {
                    logger.info("执行指令: IF_ICMPNE");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }
                    // 比较
                    if (value1.getVal() != value2.getVal()) {
                        /**
                         * -1: 减去IF_ICMPNE指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IF_ICMPLT: {
                    logger.info("执行指令: IF_ICMPLT");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value2.getVal() < value1.getVal()) {
                        /**
                         * -1: 减去IF_ICMPLT指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IF_ICMPGE: {
                    logger.info("执行指令: IF_ICMPGE");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value2.getVal() >= value1.getVal()) {
                        /**
                         * -1: 减去IF_ICMPGE指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IF_ICMPGT: {
                    logger.info("执行指令: IF_ICMPGT");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value2.getVal() > value1.getVal()) {
                        /**
                         * -1: 减去IF_ICMPGT指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */

                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IF_ICMPLE: {
                    logger.info("执行指令: IF_ICMPLE");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_INT || value2.getType() != BasicType.T_INT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value2.getVal() <= value1.getVal()) {
                        /**
                         * -1: 减去IF_ICMPLE指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         */
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IF_ACMPEQ: {
                    logger.info("执行指令: IF_ACMPEQ");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_OBJECT || value2.getType() != BasicType.T_OBJECT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value1.getObject() == value2.getObject()) {
                        /**
                         * -1: 减去IF_ACMPEQ指令占用的1B
                         * -2: 减去操作数operand占用的2B
                         *
                         * 因为跳转的位置是从该条指令的起始位置开始算的
                         *
                         */
                        code.inc(operand - 1 - 2);
                    }
                    break;
                }
                case Bytecodes.IF_ACMPNE: {
                    logger.info("执行指令: IF_ACMPNE");

                    // 取出比较数
                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 基本验证
                    if (value1.getType() != BasicType.T_OBJECT || value2.getType() != BasicType.T_OBJECT) {
                        logger.error("不匹配的数据类型");

                        throw new Error("不匹配的数据类型");
                    }

                    // 比较
                    if (value1.getObject() != value2.getObject()) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.GOTO: {
                    logger.info("执行指令: GOTO");

                    short operand = code.getUnsignedShort();

                    code.inc(operand - 2 - 1);
                    break;
                }
                case Bytecodes.IRETURN: {
                    logger.info("执行指令: IRETUERN");

                    // 从当前栈帧中取出数据
                    StackValue value = frame.getStack().pop();

                    // 当前栈帧出栈:
                    thread.getStack().pop();

                    logger.info("\t 剩余栈帧数量: " + thread.getStack().size());

                    // 将返回值压入调用者的栈
                    ((JavaVFrame) thread.getStack().peek()).getStack().push(value);

                    break;
                }
                case Bytecodes.RETURN: {
                    logger.info("执行指令: RETURN");

                    // pop出栈帧
                    thread.getStack().pop();

                    logger.info("\t 剩余栈帧数量给: " + thread.getStack().size());

                    break;
                }
                case Bytecodes.GETSTATIC: {
                    logger.info("执行指令: GETSTATIC");

                    // 获取操作数
                    short operand = code.getUnsignedShort();

                    String className = method.getBelongKlass().getConstantPool().getClassNameByFieldInfo(operand);
                    String fieldName = method.getBelongKlass().getConstantPool().getFieldName(operand);

                    try {
                        Class<?> clazz = Class.forName(className.replace('/', '.'));
                        Field field = clazz.getField(fieldName);
                        frame.getStack().push(new StackValue(BasicType.T_OBJECT, field.get(null)));
                    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    break;

                }
                case Bytecodes.PUTSTATIC: {
                    throw new Error("未做处理: PUTSTATIC");
                }
                case Bytecodes.GETFIELD: {
                    logger.info("执行指令: GETFIELD");

                    short operand = code.getUnsignedShort();
                    // =====
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String fieldName = method.getBelongKlass().getConstantPool().getFieldName(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    logger.info("给属性赋值: " + className + ":" + fieldName + "#" + descriptorName);

                    // =====
                    DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
                    descriptorStream.parseField();

                    // =====
                    Object obj = frame.getStack().pop().getObject();

                    Class<?> clazz = null;

                    try {

                        if (null == obj || obj.equals("")) {
                            throw new Error("此种情况未做处理");
                        } else {
                            clazz = obj.getClass();
                        }
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);

                        descriptorStream.pushField(field.get(obj), frame);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case Bytecodes.PUTFIELD: {
                    logger.info("执行指令: PUTFIELD");

                    short operand = code.getUnsignedShort();

                    // ====
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String fieldName = method.getBelongKlass().getConstantPool().getFieldName(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    // ====
                    DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
                    descriptorStream.parseField();

                    Object params = descriptorStream.getFieldVal(frame);

                    // ====
                    Object obj = frame.getStack().pop().getObject();

                    Class<?> clazz = null;

                    try {
                        if (null == obj || obj.equals("")) {
                            throw new Error("此种情况未做处理");
                        } else {
                            clazz = obj.getClass();
                        }
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);

                        field.set(obj, params);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                }
                /**
                 * 调用虚方法
                 * 1.public修饰
                 * 2.protected修饰
                 */
                case Bytecodes.INVOKEVIRTUAL: {
                    logger.info("执行指令: INVOKEVIRTUAL");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 获取类名、方法名
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String methodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodInfo(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    /**
                     * 判断是系统类还是自定义类
                     * 系统类走反射
                     * 自定义的类自己处理
                     */

                    if (className.startsWith("java")) {
                        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);

                        descriptorStream.parseMethod();

                        Object[] params = descriptorStream.getParamsVal(frame);
                        Class<?>[] paramsClass = descriptorStream.getParamsType();

                        Object obj = frame.getStack().pop().getObject();

                        try {
                            Method fun = obj.getClass().getMethod(methodName, paramsClass);
                            /**
                             * 处理:
                             * 1.无返回值
                             * 2.有返回值
                             */

                            if (BasicType.T_VOID == descriptorStream.getReturnElement().getType()) {
                                fun.invoke(obj, params);
                            } else {
                                descriptorStream.pushField(fun.invoke(obj, params), frame);
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        InstanceKlass klass = BootClassLoader.findLoadedKlass(className.replace('/', '.'));

                        if (null == klass) {
                            throw new Error("类还未加载: " + className);
                        }

                        MethodInfo methodID = JavaNativeInterface.getMethodID(klass, methodName, descriptorName);
                        if (null == methodID) {
                            throw new Error("不存在的方法: " + methodName + "#" + descriptorName);
                        }

                        JavaNativeInterface.callMethod(methodID);
                    }
                    break;
                }
                /**
                 * 调用:
                 * 1.构造方法
                 * 2.私有方法
                 * 3.父类方法(super)
                 */
                case Bytecodes.INVOKESPECIAL: {
                    logger.info("执行指令: INVOKESEPCIAL( java体系的借助反射实现，自己定义的类自己实现)");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 获取类名、方法名、方法签名
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String methodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodInfo(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    logger.info("执行方法： " + className + ":" + methodName + "#" + descriptorName);

                    if (className.equals("java/lang/Object") && methodName.equals("<init>")) {
                        // code.reset();
                        // thread.getStack().pop();
                    }

                    if (className.startsWith("java")) {
                        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
                        descriptorStream.parseMethod();

                        Object[] params = descriptorStream.getParamsVal(frame);
                        Class[] paramClass = descriptorStream.getParamsType();

                        /**
                         * 1.为什么执行这步？
                         *      因为非静态方法调用前都会压入对象指针，构建环境时给this赋值
                         *      而Java体系，当前设计中走的是反射机制，所以需要手动完成出战，保持堆栈平衡
                         *  2.为什么要放在参数后面?因为参数在对象引用上面
                         *  | 参数1 |
                         *  --------
                         *  | 参数2 |
                         *  --------
                         *  | 对象引用 |
                         *  --------
                         */
                        StackValue stackValue = frame.getStack().pop();
                        if (BasicType.T_ADDRESS == stackValue.getType()) {
                            // 因为没有去调用Object的构造方法，所以需要手动弹出栈帧
                            // thread.getStack().pop();

                            break;
                        }

                        Object object = stackValue.getObject();

                        // 判断调用的是构造方法还是普通方法
                        if (methodName.equals("<init>")) {
                            try {
                                if (null == object || object.equals("")) {
                                    logger.info("\t NEW字节码指令未创建对象, 在这里创建");

                                    Class<?> clazz = Class.forName(className.replace('/', '.'));
                                    Constructor<?> constructor = clazz.getConstructor(paramClass);
                                    object = constructor.newInstance(params);
                                }

                                if (!className.equals("java/lang/Object")) {
                                    // 注意: 这里应该是给栈帧顶部的StackValue赋值，而不是创建新的压栈
                                    frame.getStack().peek().setObject(object);
                                }
                            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                                     InstantiationException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            // java体系，非构造方法
                            throw new Error("java体系，非构造方法，未做处理");
                        }

                    } else {
                        InstanceKlass klass = BootClassLoader.findLoadedKlass(className.replace('/', '.'));
                        if (null == klass) {
                            logger.info("\t 开始加载未加载的类: " + className);

                            klass = BootClassLoader.loadKlass(className.replace('/', '.'));

                        }

                        MethodInfo methodID = JavaNativeInterface.getMethodID(klass, methodName, descriptorName);
                        if (null == methodID) {
                            throw new Error("不存在的方法: " + methodName + "#" + descriptorName);
                        }

                        methodID.getAttributes()[0].getCode().reset();

                        JavaNativeInterface.callMethod(methodID);
                    }

                    break;
                }
                /**
                 * 调用静态方法，即static修饰的方法
                 */
                case Bytecodes.INVOKESTATIC: {
                    logger.info("执行指令: INVOKESTATIC");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    // 获取类名
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String methodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodInfo(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    if (className.startsWith("java")) {
                        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
                        descriptorStream.parseMethod();

                        Object[] params = descriptorStream.getParamsVal(frame);
                        Class<?>[] paramsClass = descriptorStream.getParamsType();

                        try {
                            Class<?> clazz = Class.forName(className.replace('/', '.'));
                            Method func = clazz.getMethod(methodName, paramsClass);
                            /**
                             * 处理:
                             * 1.无返回值
                             * 2.有返回值
                             */
                            if (BasicType.T_VOID == descriptorStream.getReturnElement().getType()) {
                                func.invoke(clazz, params);
                            } else {
                                descriptorStream.pushField(func.invoke(clazz, params), frame);
                            }
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        InstanceKlass klass = BootClassLoader.findLoadedKlass(className.replace('/', '.'));

                        // 触发类的加载
                        if (null == klass) {
                            logger.info("\t 开始加载未加载的类: " + className);

                            klass = BootClassLoader.loadKlass(className.replace('/', '.'));
                        }

                        MethodInfo methodID = JavaNativeInterface.getMethodID(klass, methodName, descriptorName);

                        if (null == methodID) {
                            throw new Error("不存在的方法: " + methodName + "#" + descriptorName);
                        }

                        // 不然方法重复调用会出错。因为程序计数器上次执行玩指向的是尾部
                        methodID.getAttributes()[0].getCode().reset();

                        // 调用
                        JavaNativeInterface.callStaticMethod(methodID);
                    }
                    break;
                }
                /**
                 * 调用接口方法
                 *
                 * 可以用两种方式实现:
                 * 1.借助反射
                 * 2.走自己的逻辑
                 */
                case Bytecodes.INVOKEINTERFACE: {
                    logger.info("执行指令: INVOKEINTERFACE");

                    short operand = code.getUnsignedShort();

                    /**
                     * 这个参数记录了调用方法的参数个数:long、double记2，其他记1
                     * 起始没太大必要，这个数据可以通过解析函数描述符获得
                     * 为什么还存在呢? 历史原因
                     */

                    code.getU1Code();

                    /**
                     * 为额外的运算元预留空间(没看懂)
                     * 也是历史原因，可以不用管
                     */
                    code.getU1Code();

                    // 获取类名、方法名、方法签名
                    String className = method.getBelongKlass().getConstantPool().getClassNameByMethodInfo(operand);
                    String methodName = method.getBelongKlass().getConstantPool().getMethodNameByMethodInfo(operand);
                    String descriptorName = method.getBelongKlass().getConstantPool().getDescriptorNameByMethodInfo(operand);

                    logger.info("执行接口方法： " + className + ":" + methodName + "#" + descriptorName);

                    boolean self = false;
                    if (self) {
                        throw new Error("未做处理");
                    } else {
                        DescriptorStream2 descriptorStream = new DescriptorStream2(descriptorName);
                        descriptorStream.parseMethod();

                        Object[] params = descriptorStream.getParamsVal(frame);
                        Class<?>[] paramsClass = descriptorStream.getParamsType();

                        Object obj = frame.getStack().pop().getObject();

                        try {
                            Class<?> clazz = Class.forName(className.replace('/', '.'));
                            Method fun = clazz.getMethod(methodName, paramsClass);

                            /**
                             * 处理:
                             * 1.无返回值
                             * 2.有返回值
                             */
                            if (BasicType.T_VOID == descriptorStream.getReturnElement().getType()) {
                                fun.invoke(obj, params);
                            } else {
                                descriptorStream.pushField(fun.invoke(obj, params), frame);
                            }
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    break;

                }
                case Bytecodes.INVOKEDYNAMIC: {
                    logger.info("执行指令: INVOKEDYNAMIC");

                    int code1 = code.getU1Code();
                    int code2 = code.getU1Code();
                    int code3 = code.getU1Code();
                    int code4 = code.getU1Code();

                    int index = code1 << 8 | code2;

                    Object object = new LambdaEngine(method, index).createObject();

                    frame.getStack().push(new StackValue(BasicType.T_OBJECT, object));

                    break;
                }
                case Bytecodes.ATHROW: {
                    logger.info("执行指令: ATHROW");

                    // 取出栈顶元素
                    Throwable throwable = (Throwable) frame.getStack().pop().getObject();

                    throwable.printStackTrace();

                    if (throwable instanceof Error) {
                        System.exit(-1);
                    }

                    break;
                }
                /**
                 * 只分配内存
                 * Integer:没有不带参数的构造函数
                 * String: 调用不带参数的构造函数，返回空
                 */
                case Bytecodes.NEW: {
                    logger.info("执行指令: NEW");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    String className = method.getBelongKlass().getConstantPool().getClassName(operand);

                    try {
                        Class<?> clazz = Class.forName(className.replace('/', '.'));
                        Constructor<?> constructor = clazz.getConstructor();

                        Object o = constructor.newInstance();

                        if (o instanceof Throwable) {
                            frame.getStack().push(new StackValue(BasicType.T_Throwable, o));
                        } else {
                            frame.getStack().push(new StackValue(BasicType.T_OBJECT, o));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        /**
                         * 如果没有无参构造函数，就传null,保证栈帧平衡
                         * 后面调用到构造方法的时候进行判断处理
                         */
                        frame.getStack().push(new StackValue(BasicType.T_OBJECT, null));
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case Bytecodes.NEWARRAY: {
                    logger.info("执行指令: NEWARRAY");

                    int arrSize = frame.getStack().pop().getVal();

                    int arrType = code.getU1Code();

                    ArrayOop array = new ArrayOop(arrType, arrSize);

                    frame.getStack().pushArray(array, frame);
                    break;
                }
                case Bytecodes.ANEWARRAY: {
                    logger.info("执行指令: ANEWARRAY");

                    int arrSize = frame.getStack().pop().getVal();

                    int arrTypeIndex = code.getUnsignedShort();

                    String name = method.getBelongKlass().getConstantPool().getClassName(arrTypeIndex);

                    ArrayOop array = new ArrayOop(BasicType.T_OBJECT, name, arrSize);

                    frame.getStack().pushArray(array, frame);

                    break;
                }
                case Bytecodes.ARRAYLENGTH: {
                    logger.info("执行指令: ARRAYLENGTH");

                    ArrayOop o = frame.getStack().popArray(frame);

                    frame.getStack().pushInt(o.getSize(), frame);

                    break;
                }
                case Bytecodes.CHECKCAST: {
                    logger.info("执行指令: CHECKCASE");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    String className = method.getBelongKlass().getConstantPool().getClassName(operand);

                    // 取出栈顶元素
//                    Object o = frame.getStack().pop().getObject();
//                    System.out.println(o.getClass().getSimpleName());
                    break;
                }
                case Bytecodes.IFNULL: {
                    logger.info("执行指令: IFNULL");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    Object o = frame.getStack().pop().getData();

                    // 比较
                    if (null == o) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                case Bytecodes.IFNONNULL: {
                    logger.info("执行指令: IFNONNULL");

                    // 取出操作数
                    short operand = code.getUnsignedShort();

                    Object o = frame.getStack().pop().getData();

                    // 比较
                    if (null != o) {
                        code.inc(operand - 1 - 2);
                    }

                    break;
                }
                default: {
                    throw new Error("无效指令");
                }
            } /* end switch */
        } /* end while */

    }
}
