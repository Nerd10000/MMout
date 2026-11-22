/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class WidgetSprites extends Record {
/*    */   private final Identifier enabled;
/*    */   
/*    */   private final Identifier disabled;
/*    */   
/*    */   private final Identifier enabledFocused;
/*    */   
/*    */   private final Identifier disabledFocused;
/*    */   
/*    */   public WidgetSprites(Identifier enabled, Identifier disabled, Identifier enabledFocused, Identifier disabledFocused) {
/*  5 */     this.enabled = enabled;
/*  5 */     this.disabled = disabled;
/*  5 */     this.enabledFocused = enabledFocused;
/*  5 */     this.disabledFocused = disabledFocused;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/WidgetSprites;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/WidgetSprites;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/WidgetSprites;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/WidgetSprites;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/WidgetSprites;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/components/WidgetSprites;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier enabled() {
/*  5 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public Identifier disabled() {
/*  5 */     return this.disabled;
/*    */   }
/*    */   
/*    */   public Identifier enabledFocused() {
/*  5 */     return this.enabledFocused;
/*    */   }
/*    */   
/*    */   public Identifier disabledFocused() {
/*  5 */     return this.disabledFocused;
/*    */   }
/*    */   
/*    */   public WidgetSprites(Identifier sprite) {
/*  7 */     this(sprite, sprite, sprite, sprite);
/*    */   }
/*    */   
/*    */   public WidgetSprites(Identifier sprite, Identifier focused) {
/* 11 */     this(sprite, sprite, focused, focused);
/*    */   }
/*    */   
/*    */   public WidgetSprites(Identifier enabled, Identifier disabled, Identifier focused) {
/* 15 */     this(enabled, disabled, focused, disabled);
/*    */   }
/*    */   
/*    */   public Identifier get(boolean enabled, boolean focused) {
/* 19 */     if (enabled)
/* 20 */       return focused ? this.enabledFocused : this.enabled; 
/* 22 */     return focused ? this.disabledFocused : this.disabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\WidgetSprites.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */