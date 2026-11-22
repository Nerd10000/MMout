/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ public final class Results {
/* 154 */   public final List<BlockEntity> blockEntities = new ArrayList<>();
/*     */   
/* 155 */   public final Map<ChunkSectionLayer, MeshData> renderedLayers = new EnumMap<>(ChunkSectionLayer.class);
/*     */   
/* 156 */   public VisibilitySet visibilitySet = new VisibilitySet();
/*     */   
/*     */   public MeshData.SortState transparencyState;
/*     */   
/*     */   public void release() {
/* 160 */     this.renderedLayers.values().forEach(MeshData::close);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionCompiler$Results.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */