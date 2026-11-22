/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ class SimplePalette implements Iterable<BlockState> {
/* 587 */   public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */   
/* 589 */   private final IdMapper<BlockState> ids = new IdMapper(16);
/*     */   
/*     */   private int lastId;
/*     */   
/*     */   public int idFor(BlockState state) {
/* 593 */     int id = this.ids.getId(state);
/* 594 */     if (id == -1) {
/* 595 */       id = this.lastId++;
/* 596 */       this.ids.addMapping(state, id);
/*     */     } 
/* 599 */     return id;
/*     */   }
/*     */   
/*     */   public BlockState stateFor(int index) {
/* 603 */     BlockState blockState = (BlockState)this.ids.byId(index);
/* 604 */     return (blockState == null) ? DEFAULT_BLOCK_STATE : blockState;
/*     */   }
/*     */   
/*     */   public Iterator<BlockState> iterator() {
/* 609 */     return this.ids.iterator();
/*     */   }
/*     */   
/*     */   public void addMapping(BlockState state, int id) {
/* 613 */     this.ids.addMapping(state, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate$SimplePalette.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */