/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ 
/*     */ class null implements DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction> {
/*     */   public Float2FloatFunction acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 302 */     return partialTickTime -> Math.max(first.getOpenNess(partialTickTime), second.getOpenNess(partialTickTime));
/*     */   }
/*     */   
/*     */   public Float2FloatFunction acceptSingle(ChestBlockEntity single) {
/* 307 */     Objects.requireNonNull(single);
/* 307 */     return single::getOpenNess;
/*     */   }
/*     */   
/*     */   public Float2FloatFunction acceptNone() {
/* 312 */     Objects.requireNonNull(entity);
/* 312 */     return entity::getOpenNess;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChestBlock$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */