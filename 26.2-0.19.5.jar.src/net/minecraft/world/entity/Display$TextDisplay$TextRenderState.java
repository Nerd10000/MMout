/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class TextRenderState extends Record {
/*     */   private final Component text;
/*     */   
/*     */   private final int lineWidth;
/*     */   
/*     */   private final Display.IntInterpolator textOpacity;
/*     */   
/*     */   private final Display.IntInterpolator backgroundColor;
/*     */   
/*     */   private final byte flags;
/*     */   
/*     */   public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) {
/* 911 */     this.text = text;
/* 911 */     this.lineWidth = lineWidth;
/* 911 */     this.textOpacity = textOpacity;
/* 911 */     this.backgroundColor = backgroundColor;
/* 911 */     this.flags = flags;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #911	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #911	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #911	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component text() {
/* 911 */     return this.text;
/*     */   }
/*     */   
/*     */   public int lineWidth() {
/* 911 */     return this.lineWidth;
/*     */   }
/*     */   
/*     */   public Display.IntInterpolator textOpacity() {
/* 911 */     return this.textOpacity;
/*     */   }
/*     */   
/*     */   public Display.IntInterpolator backgroundColor() {
/* 911 */     return this.backgroundColor;
/*     */   }
/*     */   
/*     */   public byte flags() {
/* 911 */     return this.flags;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$TextDisplay$TextRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */