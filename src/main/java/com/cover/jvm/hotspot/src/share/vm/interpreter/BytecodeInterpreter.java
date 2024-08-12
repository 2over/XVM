package com.cover.jvm.hotspot.src.share.vm.interpreter;

import com.cover.jvm.hotspot.src.share.vm.memory.StackObj;
import com.cover.jvm.hotspot.src.share.vm.oops.ConstantPool;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaThread;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.cover.jvm.hotspot.src.share.vm.runtime.StackValue;
import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BytecodeInterpreter extends StackObj {
    
    private static Logger logger = LoggerFactory.getLogger(BytecodeInterpreter.class);
    
    public static void run(JavaThread thread, MethodInfo method) {
        
        // 得到字节码指令
        BytecodeStream code = method.getAttributes()[0].getCode();
        
        // 得到栈帧
        JavaVFrame frame = (JavaVFrame)thread.getStack().peek();
        
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
                    frame.getStack().push(new StackValue(BasicType.T_LONG, (long)0));
                    break;
                }
                case Bytecodes.LCONST_1: {
                    logger.info("执行指令: LCONST_1");
                    /**
                     * 这里一定要强转成long,否则会当成int处理
                     */
                    frame.getStack().push(new StackValue(BasicType.T_LONG, (long)1));
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
                            float f = (float)method.getBelongKlass().getConstantPool().getDataMap().get(operand);
                            
                            // 压入栈
                            frame.getStack().push(new StackValue(BasicType.T_FLOAT, f));
                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_String: {
                            int index = (int) method.getBelongKlass().getConstantPool().getDataMap().get(operand);
                            
                            String content = (String)method.getBelongKlass().getConstantPool().getDataMap().get(index);
                            
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
                
            }
        }
        
    }
}
