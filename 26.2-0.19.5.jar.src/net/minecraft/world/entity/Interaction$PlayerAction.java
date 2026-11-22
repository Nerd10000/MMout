/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ 
/*    */ final class PlayerAction extends Record {
/*    */   private final UUID player;
/*    */   
/*    */   private final long timestamp;
/*    */   
/*    */   public static final Codec<PlayerAction> CODEC;
/*    */   
/*    */   private PlayerAction(UUID player, long timestamp) {
/* 41 */     this.player = player;
/* 41 */     this.timestamp = timestamp;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Interaction$PlayerAction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Interaction$PlayerAction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Interaction$PlayerAction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID player() {
/* 41 */     return this.player;
/*    */   }
/*    */   
/*    */   public long timestamp() {
/* 41 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   static {
/* 42 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC.fieldOf("player").forGetter(PlayerAction::player), (App)Codec.LONG.fieldOf("timestamp").forGetter(PlayerAction::timestamp)).apply((Applicative)i, PlayerAction::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Interaction$PlayerAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */