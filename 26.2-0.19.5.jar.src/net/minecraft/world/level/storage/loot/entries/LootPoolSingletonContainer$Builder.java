/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ 
/*     */ public abstract class Builder<T extends LootPoolSingletonContainer.Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> {
/*  86 */   protected int weight = 1;
/*     */   
/*  87 */   protected int quality = 0;
/*     */   
/*  89 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */   
/*     */   public T apply(LootItemFunction.Builder function) {
/*  93 */     this.functions.add(function.build());
/*  94 */     return getThis();
/*     */   }
/*     */   
/*     */   protected List<LootItemFunction> getFunctions() {
/*  98 */     return (List<LootItemFunction>)this.functions.build();
/*     */   }
/*     */   
/*     */   public T setWeight(int weight) {
/* 102 */     this.weight = weight;
/* 103 */     return getThis();
/*     */   }
/*     */   
/*     */   public T setQuality(int quality) {
/* 107 */     this.quality = quality;
/* 108 */     return getThis();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */