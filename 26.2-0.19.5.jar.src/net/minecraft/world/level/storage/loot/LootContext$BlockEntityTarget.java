/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public enum BlockEntityTarget implements StringRepresentable, LootContextArg.SimpleGetter<BlockEntity> {
/* 155 */   BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);
/*     */   
/*     */   private final ContextKey<? extends BlockEntity> param;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   BlockEntityTarget(String name, ContextKey<? extends BlockEntity> param) {
/* 163 */     this.name = name;
/* 164 */     this.param = param;
/*     */   }
/*     */   
/*     */   public ContextKey<? extends BlockEntity> contextParam() {
/* 169 */     return this.param;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 174 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContext$BlockEntityTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */