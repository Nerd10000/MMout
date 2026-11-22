/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ 
/*     */ final class NullMapBuilder extends RecordBuilder.AbstractUniversalBuilder<Unit, Unit> {
/*     */   public NullMapBuilder(DynamicOps<Unit> ops) {
/* 244 */     super(ops);
/*     */   }
/*     */   
/*     */   protected Unit initBuilder() {
/* 249 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   protected Unit append(Unit key, Unit value, Unit builder) {
/* 254 */     return builder;
/*     */   }
/*     */   
/*     */   protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 259 */     return DataResult.success(prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NullOps$NullMapBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */