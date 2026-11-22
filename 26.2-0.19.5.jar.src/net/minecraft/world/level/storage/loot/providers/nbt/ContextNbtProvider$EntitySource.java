/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import net.minecraft.advancements.predicates.NbtPredicate;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ final class EntitySource extends Record implements LootContextArg.Getter<Entity, Tag> {
/*    */   private final ContextKey<? extends Entity> contextParam;
/*    */   
/*    */   private EntitySource(ContextKey<? extends Entity> contextParam) {
/* 63 */     this.contextParam = contextParam;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ContextKey<? extends Entity> contextParam() {
/* 63 */     return this.contextParam;
/*    */   }
/*    */   
/*    */   public Tag get(Entity entity) {
/* 68 */     return (Tag)NbtPredicate.getEntityTagToCompare(entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\nbt\ContextNbtProvider$EntitySource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */