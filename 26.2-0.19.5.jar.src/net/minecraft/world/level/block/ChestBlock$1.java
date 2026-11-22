/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ 
/*     */ class null implements DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> {
/*     */   public Optional<Container> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 219 */     return (Optional)Optional.of(new CompoundContainer((Container)first, (Container)second));
/*     */   }
/*     */   
/*     */   public Optional<Container> acceptSingle(ChestBlockEntity single) {
/* 224 */     return (Optional)Optional.of(single);
/*     */   }
/*     */   
/*     */   public Optional<Container> acceptNone() {
/* 229 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChestBlock$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */