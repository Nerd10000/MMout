/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TemplateFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class TemplateFeature extends Feature<TemplateFeatureConfiguration> {
/*    */   public TemplateFeature(Codec<TemplateFeatureConfiguration> codec) {
/* 20 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<TemplateFeatureConfiguration> context) {
/* 25 */     RandomSource random = context.random();
/* 26 */     WorldGenLevel level = context.level();
/* 27 */     TemplateFeatureConfiguration config = context.config();
/* 29 */     TemplateFeatureConfiguration.TemplateEntry templateEntry = (TemplateFeatureConfiguration.TemplateEntry)config.templates().getRandomOrThrow(random);
/* 30 */     Rotation rotation = (Rotation)Util.getRandom(templateEntry.rotations(), random);
/* 32 */     StructureTemplateManager structureTemplateManager = level.getLevel().getServer().getStructureManager();
/* 33 */     StructureTemplate template = structureTemplateManager.getOrCreate(templateEntry.template());
/* 36 */     Vec3i offsetX = getRotatedOffset(rotation, Direction.Axis.X, template);
/* 37 */     Vec3i offsetZ = getRotatedOffset(rotation, Direction.Axis.Z, template);
/* 38 */     BlockPos pos = context.origin().offset(offsetX).offset(offsetZ);
/* 40 */     StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation).setRandom(random);
/* 41 */     return template.placeInWorld((ServerLevelAccessor)level, pos, pos, settings, random, 3);
/*    */   }
/*    */   
/*    */   private Vec3i getRotatedOffset(Rotation rotation, Direction.Axis axis, StructureTemplate template) {
/* 45 */     return rotation.rotate(axis.getNegative()).getUnitVec3i().multiply(template.getSize().get(axis) / 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\TemplateFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */