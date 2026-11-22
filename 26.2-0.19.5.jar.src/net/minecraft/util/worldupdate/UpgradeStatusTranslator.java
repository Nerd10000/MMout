/*    */ package net.minecraft.util.worldupdate;
/*    */ 
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ 
/*    */ public class UpgradeStatusTranslator {
/*    */   private final Map<DataFixTypes, Messages> messages;
/*    */   
/*    */   public UpgradeStatusTranslator() {
/* 11 */     this.messages = (Map<DataFixTypes, Messages>)Util.make(new EnumMap<>(DataFixTypes.class), map -> {
/*    */           map.put(DataFixTypes.CHUNK, Messages.create("chunks"));
/*    */           map.put(DataFixTypes.ENTITY_CHUNK, Messages.create("entities"));
/*    */           map.put(DataFixTypes.POI_CHUNK, Messages.create("poi"));
/*    */         });
/*    */   }
/*    */   
/* 16 */   private static final Component FAILED = (Component)Component.translatable("optimizeWorld.stage.failed");
/*    */   
/* 17 */   private static final Component COUNTING = (Component)Component.translatable("optimizeWorld.stage.counting");
/*    */   
/* 18 */   private static final Component UPGRADING = (Component)Component.translatable("optimizeWorld.stage.upgrading");
/*    */   
/*    */   public Component translate(UpgradeProgress upgradeProgress) {
/* 21 */     UpgradeProgress.Status status = upgradeProgress.getStatus();
/* 22 */     if (status == UpgradeProgress.Status.FAILED)
/* 23 */       return FAILED; 
/* 24 */     if (status == UpgradeProgress.Status.COUNTING)
/* 25 */       return COUNTING; 
/* 27 */     DataFixTypes dataFixType = upgradeProgress.getDataFixType();
/* 28 */     if (dataFixType == null)
/* 29 */       return COUNTING; 
/* 31 */     Messages typeMessages = this.messages.get(dataFixType);
/* 32 */     if (typeMessages == null)
/* 33 */       return UPGRADING; 
/* 35 */     return typeMessages.forStatus(status);
/*    */   }
/*    */   
/*    */   public static final class Messages extends Record {
/*    */     private final Component upgrading;
/*    */     
/*    */     private final Component finished;
/*    */     
/*    */     public Messages(Component upgrading, Component finished) {
/* 38 */       this.upgrading = upgrading;
/* 38 */       this.finished = finished;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Component upgrading() {
/* 38 */       return this.upgrading;
/*    */     }
/*    */     
/*    */     public Component finished() {
/* 38 */       return this.finished;
/*    */     }
/*    */     
/*    */     public static Messages create(String type) {
/* 40 */       return new Messages(
/* 41 */           (Component)Component.translatable("optimizeWorld.stage.upgrading." + type), 
/* 42 */           (Component)Component.translatable("optimizeWorld.stage.finished." + type));
/*    */     }
/*    */     
/*    */     public Component forStatus(UpgradeProgress.Status status) {
/*    */       // Byte code:
/*    */       //   0: getstatic net/minecraft/util/worldupdate/UpgradeStatusTranslator$1.$SwitchMap$net$minecraft$util$worldupdate$UpgradeProgress$Status : [I
/*    */       //   3: aload_1
/*    */       //   4: invokevirtual ordinal : ()I
/*    */       //   7: iaload
/*    */       //   8: lookupswitch default -> 50, 1 -> 36, 2 -> 43
/*    */       //   36: aload_0
/*    */       //   37: getfield upgrading : Lnet/minecraft/network/chat/Component;
/*    */       //   40: goto -> 67
/*    */       //   43: aload_0
/*    */       //   44: getfield finished : Lnet/minecraft/network/chat/Component;
/*    */       //   47: goto -> 67
/*    */       //   50: new java/lang/IllegalStateException
/*    */       //   53: dup
/*    */       //   54: aload_1
/*    */       //   55: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*    */       //   58: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*    */       //   63: invokespecial <init> : (Ljava/lang/String;)V
/*    */       //   66: athrow
/*    */       //   67: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       //   #48	-> 36
/*    */       //   #49	-> 43
/*    */       //   #50	-> 50
/*    */       //   #47	-> 67
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	68	0	this	Lnet/minecraft/util/worldupdate/UpgradeStatusTranslator$Messages;
/*    */       //   0	68	1	status	Lnet/minecraft/util/worldupdate/UpgradeProgress$Status;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeStatusTranslator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */