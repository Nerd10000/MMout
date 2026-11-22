/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public final class Palette {
/*     */   private final List<StructureTemplate.StructureBlockInfo> blocks;
/*     */   
/* 819 */   private final Map<Block, List<StructureTemplate.StructureBlockInfo>> cache = Maps.newHashMap();
/*     */   
/*     */   private List<StructureTemplate.JigsawBlockInfo> cachedJigsaws;
/*     */   
/*     */   private Palette(List<StructureTemplate.StructureBlockInfo> blocks) {
/* 823 */     this.blocks = blocks;
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> jigsaws() {
/* 827 */     if (this.cachedJigsaws == null)
/* 828 */       this
/*     */         
/* 830 */         .cachedJigsaws = blocks(Blocks.JIGSAW).stream().map(StructureTemplate.JigsawBlockInfo::of).toList(); 
/* 832 */     return this.cachedJigsaws;
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> blocks() {
/* 836 */     return this.blocks;
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> blocks(Block filter) {
/* 840 */     return this.cache.computeIfAbsent(filter, block -> (List)this.blocks.stream().filter(()).collect(Collectors.toList()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate$Palette.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */