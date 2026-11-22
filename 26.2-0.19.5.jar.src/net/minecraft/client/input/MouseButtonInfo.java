/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ public final class MouseButtonInfo extends Record implements InputWithModifiers {
/*    */   private final int button;
/*    */   
/*    */   private final int modifiers;
/*    */   
/*    */   public MouseButtonInfo(int button, int modifiers) {
/* 11 */     this.button = button;
/* 11 */     this.modifiers = modifiers;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/MouseButtonInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/MouseButtonInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/MouseButtonInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int button() {
/* 11 */     return this.button;
/*    */   }
/*    */   
/*    */   public int modifiers() {
/* 11 */     return this.modifiers;
/*    */   }
/*    */   
/*    */   public int input() {
/* 17 */     return this.button;
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface MouseButton {}
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface Action {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\MouseButtonInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */