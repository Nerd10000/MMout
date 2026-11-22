/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class LegacySinglePoolElement extends SinglePoolElement {
/*    */   public static final MapCodec<LegacySinglePoolElement> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec(), (App)overrideLiquidSettingsCodec()).apply((Applicative)i, LegacySinglePoolElement::new));
/*    */   }
/*    */   
/*    */   protected LegacySinglePoolElement(Either<Identifier, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> liquidSettings) {
/* 29 */     super(template, processors, projection, liquidSettings);
/*    */   }
/*    */   
/*    */   protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox chunkBB, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 34 */     StructurePlaceSettings settings = super.getSettings(rotation, chunkBB, liquidSettings, keepJigsaws);
/* 35 */     settings.popProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 36 */     settings.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 37 */     return settings;
/*    */   }
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 42 */     return StructurePoolElementType.LEGACY;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 47 */     return "LegacySingle[" + String.valueOf(this.template) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\LegacySinglePoolElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */