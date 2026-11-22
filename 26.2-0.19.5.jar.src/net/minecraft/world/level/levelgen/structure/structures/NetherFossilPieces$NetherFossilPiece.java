/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class NetherFossilPiece extends TemplateStructurePiece {
/*    */   public NetherFossilPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation) {
/* 51 */     super(StructurePieceType.NETHER_FOSSIL, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation), position);
/*    */   }
/*    */   
/*    */   public NetherFossilPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/* 55 */     super(StructurePieceType.NETHER_FOSSIL, tag, structureTemplateManager, location -> makeSettings(tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*    */   }
/*    */   
/*    */   private static StructurePlaceSettings makeSettings(Rotation rotation) {
/* 59 */     return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*    */   }
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 64 */     super.addAdditionalSaveData(context, tag);
/* 65 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*    */   }
/*    */   
/*    */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {}
/*    */   
/*    */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 74 */     BoundingBox fossilBB = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 75 */     chunkBB.encapsulate(fossilBB);
/* 76 */     super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/* 77 */     placeDriedGhast(level, random, fossilBB, chunkBB);
/*    */   }
/*    */   
/*    */   private void placeDriedGhast(WorldGenLevel level, RandomSource random, BoundingBox fossilBB, BoundingBox chunkBB) {
/* 81 */     RandomSource positionalRandom = RandomSource.createThreadLocalInstance(level.getSeed()).forkPositional().at(fossilBB.getCenter());
/* 82 */     if (positionalRandom.nextFloat() < 0.5F) {
/* 83 */       int x = fossilBB.minX() + positionalRandom.nextInt(fossilBB.getXSpan());
/* 84 */       int y = fossilBB.minY();
/* 85 */       int z = fossilBB.minZ() + positionalRandom.nextInt(fossilBB.getZSpan());
/* 86 */       BlockPos randomPos = new BlockPos(x, y, z);
/* 88 */       if (level.getBlockState(randomPos).isAir() && chunkBB.isInside((Vec3i)randomPos))
/* 89 */         level.setBlock(randomPos, Blocks.DRIED_GHAST.defaultBlockState().rotate(Rotation.getRandom(positionalRandom)), 2); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilPieces$NetherFossilPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */