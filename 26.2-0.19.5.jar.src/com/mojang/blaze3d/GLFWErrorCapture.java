/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ public class GLFWErrorCapture implements GLFWErrorCallbackI, Iterable<GLFWErrorCapture.Error> {
/*    */   private List<Error> errors;
/*    */   
/*    */   public void invoke(int error, long description) {
/* 19 */     if (this.errors == null)
/* 20 */       this.errors = new ArrayList<>(); 
/* 22 */     this.errors.add(new Error(error, MemoryUtil.memUTF8(description)));
/*    */   }
/*    */   
/*    */   public Iterator<Error> iterator() {
/* 27 */     return (this.errors == null) ? Collections.<Error>emptyIterator() : this.errors.iterator();
/*    */   }
/*    */   
/*    */   public Error firstError() {
/* 31 */     return (this.errors == null) ? null : this.errors.getFirst();
/*    */   }
/*    */   
/*    */   public static final class Error extends Record {
/*    */     private final int error;
/*    */     
/*    */     private final String description;
/*    */     
/*    */     public Error(int error, String description) {
/* 34 */       this.error = error;
/* 34 */       this.description = description;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/GLFWErrorCapture$Error;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/GLFWErrorCapture$Error;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/GLFWErrorCapture$Error;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/GLFWErrorCapture$Error;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int error() {
/* 34 */       return this.error;
/*    */     }
/*    */     
/*    */     public String description() {
/* 34 */       return this.description;
/*    */     }
/*    */     
/*    */     public String toString() {
/* 37 */       return String.format(Locale.ROOT, "[GLFW 0x%X] %s", new Object[] { Integer.valueOf(this.error), this.description });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\GLFWErrorCapture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */