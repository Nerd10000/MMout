/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ 
/*     */ final class NullListBuilder extends AbstractListBuilder<Unit, Unit> {
/*     */   public NullListBuilder(DynamicOps<Unit> ops) {
/* 265 */     super(ops);
/*     */   }
/*     */   
/*     */   protected Unit initBuilder() {
/* 270 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   protected Unit append(Unit builder, Unit value) {
/* 275 */     return builder;
/*     */   }
/*     */   
/*     */   protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 280 */     return DataResult.success(builder);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NullOps$NullListBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */