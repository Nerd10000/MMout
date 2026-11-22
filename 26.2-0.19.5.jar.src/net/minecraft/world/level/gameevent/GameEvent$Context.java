/*     */ package net.minecraft.world.level.gameevent;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class Context extends Record {
/*     */   private final Entity sourceEntity;
/*     */   
/*     */   private final BlockState affectedState;
/*     */   
/*     */   public Context(Entity sourceEntity, BlockState affectedState) {
/* 105 */     this.sourceEntity = sourceEntity;
/* 105 */     this.affectedState = affectedState;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Entity sourceEntity() {
/* 105 */     return this.sourceEntity;
/*     */   }
/*     */   
/*     */   public BlockState affectedState() {
/* 105 */     return this.affectedState;
/*     */   }
/*     */   
/*     */   public static Context of(Entity sourceEntity) {
/* 107 */     return new Context(sourceEntity, null);
/*     */   }
/*     */   
/*     */   public static Context of(BlockState state) {
/* 111 */     return new Context(null, state);
/*     */   }
/*     */   
/*     */   public static Context of(Entity sourceEntity, BlockState state) {
/* 115 */     return new Context(sourceEntity, state);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEvent$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */