/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null implements BlockTintSource {
/*     */   public int color(BlockState state) {
/* 170 */     return RedStoneWireBlock.getColorForPower(((Integer)state.getValue((Property)RedStoneWireBlock.POWER)).intValue());
/*     */   }
/*     */   
/*     */   public Set<Property<?>> relevantProperties() {
/* 175 */     return (Set)Set.of(RedStoneWireBlock.POWER);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSources$10.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */