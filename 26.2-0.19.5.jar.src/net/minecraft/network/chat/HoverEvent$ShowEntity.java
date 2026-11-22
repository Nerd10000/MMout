/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class ShowEntity extends Record implements HoverEvent {
/*    */   private final HoverEvent.EntityTooltipInfo entity;
/*    */   
/*    */   public static final MapCodec<ShowEntity> CODEC;
/*    */   
/*    */   public ShowEntity(HoverEvent.EntityTooltipInfo entity) {
/* 45 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HoverEvent.EntityTooltipInfo entity() {
/* 45 */     return this.entity;
/*    */   }
/*    */   
/*    */   static {
/* 46 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)HoverEvent.EntityTooltipInfo.CODEC.forGetter(ShowEntity::entity)).apply((Applicative)i, ShowEntity::new));
/*    */   }
/*    */   
/*    */   public HoverEvent.Action action() {
/* 52 */     return HoverEvent.Action.SHOW_ENTITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\HoverEvent$ShowEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */