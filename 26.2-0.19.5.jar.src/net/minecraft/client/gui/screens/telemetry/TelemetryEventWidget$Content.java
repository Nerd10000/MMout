/*     */ package net.minecraft.client.gui.screens.telemetry;
/*     */ 
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class Content extends Record {
/*     */   private final Layout container;
/*     */   
/*     */   private final Component narration;
/*     */   
/*     */   private Content(Layout container, Component narration) {
/* 173 */     this.container = container;
/* 173 */     this.narration = narration;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Layout container() {
/* 173 */     return this.container;
/*     */   }
/*     */   
/*     */   public Component narration() {
/* 173 */     return this.narration;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\telemetry\TelemetryEventWidget$Content.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */