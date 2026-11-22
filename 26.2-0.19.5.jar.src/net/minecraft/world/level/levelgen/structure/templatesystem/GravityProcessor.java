/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class GravityProcessor implements StructureProcessor {
/*    */   public static final MapCodec<GravityProcessor> MAP_CODEC;
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   
/*    */   private final int offset;
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.optionalFieldOf("heightmap", Heightmap.Types.WORLD_SURFACE_WG).forGetter(()), (App)Codec.INT.optionalFieldOf("offset", Integer.valueOf(0)).forGetter(())).apply((Applicative)i, GravityProcessor::new));
/*    */   }
/*    */   
/*    */   public GravityProcessor(Heightmap.Types heightmap, int offset) {
/* 22 */     this.heightmap = heightmap;
/* 23 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/*    */     Heightmap.Types heightmap;
/* 29 */     if (level instanceof net.minecraft.server.level.ServerLevel) {
/* 31 */       if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
/* 32 */         heightmap = Heightmap.Types.WORLD_SURFACE;
/* 33 */       } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 34 */         heightmap = Heightmap.Types.OCEAN_FLOOR;
/*    */       } else {
/* 36 */         heightmap = this.heightmap;
/*    */       } 
/*    */     } else {
/* 39 */       heightmap = this.heightmap;
/*    */     } 
/* 41 */     BlockPos pos = processedBlockInfo.pos();
/* 42 */     int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + this.offset;
/* 43 */     int delta = templateRelativePos.getY();
/* 44 */     return new StructureTemplate.StructureBlockInfo(new BlockPos(pos.getX(), height + delta, pos.getZ()), processedBlockInfo.state(), processedBlockInfo.nbt());
/*    */   }
/*    */   
/*    */   public MapCodec<GravityProcessor> codec() {
/* 49 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\GravityProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */