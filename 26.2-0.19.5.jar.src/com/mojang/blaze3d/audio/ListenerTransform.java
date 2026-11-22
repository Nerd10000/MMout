/*   */ package com.mojang.blaze3d.audio;
/*   */ 
/*   */ import net.minecraft.world.phys.Vec3;
/*   */ 
/*   */ public final class ListenerTransform extends Record {
/*   */   private final Vec3 position;
/*   */   
/*   */   private final Vec3 forward;
/*   */   
/*   */   private final Vec3 up;
/*   */   
/*   */   public ListenerTransform(Vec3 position, Vec3 forward, Vec3 up) {
/* 5 */     this.position = position;
/* 5 */     this.forward = forward;
/* 5 */     this.up = up;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/audio/ListenerTransform;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/audio/ListenerTransform;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/audio/ListenerTransform;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Vec3 position() {
/* 5 */     return this.position;
/*   */   }
/*   */   
/*   */   public Vec3 forward() {
/* 5 */     return this.forward;
/*   */   }
/*   */   
/*   */   public Vec3 up() {
/* 5 */     return this.up;
/*   */   }
/*   */   
/* 6 */   public static final ListenerTransform INITIAL = new ListenerTransform(Vec3.ZERO, new Vec3(0.0D, 0.0D, -1.0D), new Vec3(0.0D, 1.0D, 0.0D));
/*   */   
/*   */   public Vec3 right() {
/* 9 */     return this.forward.cross(this.up);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\ListenerTransform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */