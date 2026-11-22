/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.RandomizableContainer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.monster.Shulker;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class EndCityPiece extends TemplateStructurePiece {
/*    */   public EndCityPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, boolean overwrite) {
/* 45 */     super(StructurePieceType.END_CITY_PIECE, 0, structureTemplateManager, makeIdentifier(templateName), templateName, makeSettings(overwrite, rotation), position);
/*    */   }
/*    */   
/*    */   public EndCityPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/* 49 */     super(StructurePieceType.END_CITY_PIECE, tag, structureTemplateManager, location -> makeSettings(tag.getBooleanOr("OW", false), tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*    */   }
/*    */   
/*    */   private static StructurePlaceSettings makeSettings(boolean overwrite, Rotation rotation) {
/* 53 */     BlockIgnoreProcessor processor = overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/* 54 */     return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor((StructureProcessor)processor).setRotation(rotation);
/*    */   }
/*    */   
/*    */   protected Identifier makeTemplateLocation() {
/* 59 */     return makeIdentifier(this.templateName);
/*    */   }
/*    */   
/*    */   private static Identifier makeIdentifier(String templateName) {
/* 63 */     return Identifier.withDefaultNamespace("end_city/" + templateName);
/*    */   }
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 68 */     super.addAdditionalSaveData(context, tag);
/* 70 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 71 */     tag.putBoolean("OW", (this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK));
/*    */   }
/*    */   
/*    */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/* 76 */     if (markerId.startsWith("Chest")) {
/* 77 */       BlockPos chestPosition = position.below();
/* 79 */       if (chunkBB.isInside((Vec3i)chestPosition))
/* 80 */         RandomizableContainer.setBlockEntityLootTable((BlockGetter)level, random, chestPosition, BuiltInLootTables.END_CITY_TREASURE); 
/* 82 */     } else if (chunkBB.isInside((Vec3i)position) && Level.isInSpawnableBounds(position)) {
/* 83 */       if (markerId.startsWith("Sentry")) {
/* 84 */         Shulker sentry = (Shulker)EntityTypes.SHULKER.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 85 */         if (sentry != null) {
/* 86 */           sentry.setPos(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D);
/* 87 */           level.addFreshEntity((Entity)sentry);
/*    */         } 
/* 89 */       } else if (markerId.startsWith("Elytra")) {
/* 90 */         ItemFrame itemFrame = new ItemFrame((Level)level.getLevel(), position, this.placeSettings.getRotation().rotate(Direction.SOUTH));
/* 91 */         itemFrame.setItem(new ItemStack((ItemLike)Items.ELYTRA), false);
/* 92 */         level.addFreshEntity((Entity)itemFrame);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$EndCityPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */