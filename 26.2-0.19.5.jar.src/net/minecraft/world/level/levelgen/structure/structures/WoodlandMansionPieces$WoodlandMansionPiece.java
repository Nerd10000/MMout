/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class WoodlandMansionPiece extends TemplateStructurePiece {
/*     */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation) {
/*  37 */     this(structureTemplateManager, templateName, position, rotation, Mirror.NONE);
/*     */   }
/*     */   
/*     */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, Mirror mirror) {
/*  41 */     super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, structureTemplateManager, makeLocation(templateName), templateName, makeSettings(mirror, rotation), position);
/*     */   }
/*     */   
/*     */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*  45 */     super(StructurePieceType.WOODLAND_MANSION_PIECE, tag, structureTemplateManager, location -> makeSettings(tag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */   }
/*     */   
/*     */   protected Identifier makeTemplateLocation() {
/*  50 */     return makeLocation(this.templateName);
/*     */   }
/*     */   
/*     */   private static Identifier makeLocation(String templateName) {
/*  54 */     return Identifier.withDefaultNamespace("woodland_mansion/" + templateName);
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation) {
/*  58 */     return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(mirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  63 */     super.addAdditionalSaveData(context, tag);
/*  65 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  66 */     tag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*     */   }
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*  71 */     if (markerId.startsWith("Chest")) {
/*  72 */       Rotation rot = this.placeSettings.getRotation();
/*  73 */       BlockState chestState = Blocks.CHEST.defaultBlockState();
/*  74 */       if ("ChestWest".equals(markerId)) {
/*  75 */         chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.WEST));
/*  76 */       } else if ("ChestEast".equals(markerId)) {
/*  77 */         chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.EAST));
/*  78 */       } else if ("ChestSouth".equals(markerId)) {
/*  79 */         chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.SOUTH));
/*  80 */       } else if ("ChestNorth".equals(markerId)) {
/*  81 */         chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.NORTH));
/*     */       } 
/*  83 */       createChest(level, chunkBB, random, position, BuiltInLootTables.WOODLAND_MANSION, chestState);
/*     */     } else {
/*     */       int numberOfAllays, i;
/*  85 */       List<Mob> mobs = new ArrayList<>();
/*  86 */       switch (markerId) {
/*     */         case "Mage":
/*  87 */           mobs.add((Mob)EntityTypes.EVOKER.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Warrior":
/*  88 */           mobs.add((Mob)EntityTypes.VINDICATOR.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Group of Allays":
/*  90 */           numberOfAllays = level.getRandom().nextInt(3) + 1;
/*  91 */           for (i = 0; i < numberOfAllays; i++)
/*  92 */             mobs.add((Mob)EntityTypes.ALLAY.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE)); 
/*     */           break;
/*     */         default:
/*     */           return;
/*     */       } 
/* 100 */       for (Mob mob : mobs) {
/* 101 */         if (mob == null)
/*     */           continue; 
/* 104 */         mob.setPersistenceRequired();
/* 105 */         mob.snapTo(position, 0.0F, 0.0F);
/* 106 */         mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/* 107 */         level.addFreshEntityWithPassengers((Entity)mob);
/* 108 */         level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$WoodlandMansionPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */