/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<CopyComponentsFunction.Builder> {
/*     */   private final LootContextArg<DataComponentGetter> source;
/*     */   
/*  97 */   private Optional<ImmutableList.Builder<DataComponentType<?>>> include = Optional.empty();
/*     */   
/*  98 */   private Optional<ImmutableList.Builder<DataComponentType<?>>> exclude = Optional.empty();
/*     */   
/*     */   private Builder(LootContextArg<DataComponentGetter> source) {
/* 101 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Builder include(DataComponentType<?> type) {
/* 105 */     if (this.include.isEmpty())
/* 106 */       this.include = Optional.of(ImmutableList.builder()); 
/* 108 */     ((ImmutableList.Builder)this.include.get()).add(type);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public Builder exclude(DataComponentType<?> type) {
/* 113 */     if (this.exclude.isEmpty())
/* 114 */       this.exclude = Optional.of(ImmutableList.builder()); 
/* 117 */     ((ImmutableList.Builder)this.exclude.get()).add(type);
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   protected Builder getThis() {
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 128 */     return new CopyComponentsFunction(
/* 129 */         getConditions(), this.source, this.include
/*     */         
/* 131 */         .map(ImmutableList.Builder::build), this.exclude
/* 132 */         .map(ImmutableList.Builder::build));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyComponentsFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */