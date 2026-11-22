/*    */ package net.minecraft.util.worldupdate;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class Messages extends Record {
/*    */   private final Component upgrading;
/*    */   
/*    */   private final Component finished;
/*    */   
/*    */   public Messages(Component upgrading, Component finished) {
/* 38 */     this.upgrading = upgrading;
/* 38 */     this.finished = finished;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component upgrading() {
/* 38 */     return this.upgrading;
/*    */   }
/*    */   
/*    */   public Component finished() {
/* 38 */     return this.finished;
/*    */   }
/*    */   
/*    */   public static Messages create(String type) {
/* 40 */     return new Messages(
/* 41 */         (Component)Component.translatable("optimizeWorld.stage.upgrading." + type), 
/* 42 */         (Component)Component.translatable("optimizeWorld.stage.finished." + type));
/*    */   }
/*    */   
/*    */   public Component forStatus(UpgradeProgress.Status status) {
/* 47 */     switch (UpgradeStatusTranslator.null.$SwitchMap$net$minecraft$util$worldupdate$UpgradeProgress$Status[status.ordinal()]) {
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 50 */     throw new IllegalStateException("Invalid Status received: " + String.valueOf(status));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeStatusTranslator$Messages.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */