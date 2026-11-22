/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.QuadInstance;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.client.renderer.block.BlockModelLighter;
/*     */ import net.minecraft.client.renderer.block.BlockQuadOutput;
/*     */ import net.minecraft.client.renderer.block.BlockStateModelSet;
/*     */ import net.minecraft.client.renderer.block.FluidRenderer;
/*     */ import net.minecraft.client.renderer.block.FluidStateModelSet;
/*     */ import net.minecraft.client.renderer.block.ModelBlockRenderer;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class SectionCompiler {
/*     */   private final boolean ambientOcclusion;
/*     */   
/*     */   private final boolean cutoutLeaves;
/*     */   
/*     */   private final BlockStateModelSet blockModelSet;
/*     */   
/*     */   private final FluidStateModelSet fluidModelSet;
/*     */   
/*     */   private final BlockColors blockColors;
/*     */   
/*     */   public SectionCompiler(boolean ambientOcclusion, boolean cutoutLeaves, BlockStateModelSet blockModelSet, FluidStateModelSet fluidModelSet, BlockColors blockColors) {
/*  40 */     this.ambientOcclusion = ambientOcclusion;
/*  41 */     this.cutoutLeaves = cutoutLeaves;
/*  42 */     this.blockModelSet = blockModelSet;
/*  43 */     this.fluidModelSet = fluidModelSet;
/*  44 */     this.blockColors = blockColors;
/*     */   }
/*     */   
/*     */   public Results compile(SectionPos sectionPos, RenderSectionRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack builders) {
/*  48 */     Results results = new Results();
/*  50 */     BlockPos minPos = sectionPos.origin();
/*  51 */     BlockPos maxPos = minPos.offset(15, 15, 15);
/*  53 */     VisGraph visGraph = new VisGraph();
/*  55 */     BlockModelLighter.enableCaching();
/*  57 */     ModelBlockRenderer blockRenderer = new ModelBlockRenderer(this.ambientOcclusion, true, this.blockColors);
/*  58 */     FluidRenderer fluidRenderer = new FluidRenderer(this.fluidModelSet);
/*  60 */     Map<ChunkSectionLayer, BufferBuilder> startedLayers = new EnumMap<>(ChunkSectionLayer.class);
/*  62 */     BlockQuadOutput quadOutput = (x, y, z, quad, instance) -> {
/*     */         BufferBuilder builder = getOrBeginLayer(startedLayers, builders, quad.materialInfo().layer());
/*     */         builder.putBlockBakedQuad(x, y, z, quad, instance);
/*     */       };
/*  68 */     BlockQuadOutput opaqueQuadOutput = (x, y, z, quad, instance) -> {
/*     */         BufferBuilder builder = getOrBeginLayer(startedLayers, builders, ChunkSectionLayer.SOLID);
/*     */         builder.putBlockBakedQuad(x, y, z, quad, instance);
/*     */       };
/*  73 */     FluidRenderer.Output fluidOutput = layer -> getOrBeginLayer(startedLayers, builders, layer);
/*  75 */     for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
/*  76 */       BlockState blockState = region.getBlockState(pos);
/*  77 */       if (blockState.isAir())
/*     */         continue; 
/*     */       try {
/*  82 */         if (blockState.isSolidRender())
/*  83 */           visGraph.setOpaque(pos); 
/*  86 */         if (blockState.hasBlockEntity()) {
/*  87 */           BlockEntity blockEntity = region.getBlockEntity(pos);
/*  88 */           if (blockEntity != null)
/*  89 */             handleBlockEntity(results, blockEntity); 
/*     */         } 
/*  93 */         FluidState fluidState = blockState.getFluidState();
/*  94 */         if (!fluidState.isEmpty())
/*  95 */           fluidRenderer.tesselate(region, pos, fluidOutput, blockState, fluidState); 
/*  98 */         if (blockState.getRenderShape() == RenderShape.MODEL)
/*  99 */           blockRenderer.tesselateBlock(
/* 100 */               ModelBlockRenderer.forceOpaque(this.cutoutLeaves, blockState) ? opaqueQuadOutput : quadOutput, 
/* 101 */               SectionPos.sectionRelative(pos.getX()), 
/* 102 */               SectionPos.sectionRelative(pos.getY()), 
/* 103 */               SectionPos.sectionRelative(pos.getZ()), region, pos, blockState, this.blockModelSet
/*     */               
/* 107 */               .get(blockState), blockState
/* 108 */               .getSeed(pos)); 
/* 111 */       } catch (Throwable t) {
/* 112 */         CrashReport report = CrashReport.forThrowable(t, "Tesselating block in world");
/* 113 */         CrashReportCategory category = report.addCategory("Block being tesselated");
/* 115 */         CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)region, pos, blockState);
/* 117 */         throw new ReportedException(report);
/*     */       } 
/*     */     } 
/* 121 */     for (Map.Entry<ChunkSectionLayer, BufferBuilder> entry : startedLayers.entrySet()) {
/* 122 */       ChunkSectionLayer layer = entry.getKey();
/* 123 */       MeshData mesh = ((BufferBuilder)entry.getValue()).build();
/* 124 */       if (mesh != null) {
/* 125 */         if (layer == ChunkSectionLayer.TRANSLUCENT)
/* 126 */           results.transparencyState = mesh.sortQuads(builders.buffer(layer), vertexSorting); 
/* 128 */         results.renderedLayers.put(layer, mesh);
/*     */       } 
/*     */     } 
/* 132 */     BlockModelLighter.clearCache();
/* 134 */     results.visibilitySet = visGraph.resolve();
/* 136 */     return results;
/*     */   }
/*     */   
/*     */   private BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> startedLayers, SectionBufferBuilderPack buffers, ChunkSectionLayer layer) {
/* 140 */     BufferBuilder builder = startedLayers.get(layer);
/* 141 */     if (builder == null) {
/* 142 */       ByteBufferBuilder buffer = buffers.buffer(layer);
/* 143 */       builder = new BufferBuilder(buffer, PrimitiveTopology.QUADS, layer.vertexFormat());
/* 144 */       startedLayers.put(layer, builder);
/*     */     } 
/* 146 */     return builder;
/*     */   }
/*     */   
/*     */   private <E extends BlockEntity> void handleBlockEntity(Results results, E blockEntity) {
/* 150 */     results.blockEntities.add((BlockEntity)blockEntity);
/*     */   }
/*     */   
/*     */   public static final class Results {
/* 154 */     public final List<BlockEntity> blockEntities = new ArrayList<>();
/*     */     
/* 155 */     public final Map<ChunkSectionLayer, MeshData> renderedLayers = new EnumMap<>(ChunkSectionLayer.class);
/*     */     
/* 156 */     public VisibilitySet visibilitySet = new VisibilitySet();
/*     */     
/*     */     public MeshData.SortState transparencyState;
/*     */     
/*     */     public void release() {
/* 160 */       this.renderedLayers.values().forEach(MeshData::close);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionCompiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */