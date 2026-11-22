/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.FlatLevelGeneratorPresetTags;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*    */ 
/*    */ public class FlatLevelGeneratorPresetTagsProvider extends TagsProvider<FlatLevelGeneratorPreset> {
/*    */   public FlatLevelGeneratorPresetTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.FLAT_LEVEL_GENERATOR_PRESET, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(FlatLevelGeneratorPresetTags.VISIBLE)
/* 20 */       .add(FlatLevelGeneratorPresets.CLASSIC_FLAT)
/* 21 */       .add(FlatLevelGeneratorPresets.TUNNELERS_DREAM)
/* 22 */       .add(FlatLevelGeneratorPresets.WATER_WORLD)
/* 23 */       .add(FlatLevelGeneratorPresets.OVERWORLD)
/* 24 */       .add(FlatLevelGeneratorPresets.SNOWY_KINGDOM)
/* 25 */       .add(FlatLevelGeneratorPresets.BOTTOMLESS_PIT)
/* 26 */       .add(FlatLevelGeneratorPresets.DESERT)
/* 27 */       .add(FlatLevelGeneratorPresets.REDSTONE_READY)
/* 28 */       .add(FlatLevelGeneratorPresets.THE_VOID);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\FlatLevelGeneratorPresetTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */