/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null implements BlockTintSource {
/*     */   public int color(BlockState state) {
/* 184 */     int age = ((Integer)state.getValue((Property)StemBlock.AGE)).intValue();
/* 185 */     return ARGB.color(age * 32, 255 - age * 8, age * 4);
/*     */   }
/*     */   
/*     */   public Set<Property<?>> relevantProperties() {
/* 190 */     return (Set)Set.of(StemBlock.AGE);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSources$11.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */