/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidIds;
/*    */ 
/*    */ public class FluidTagsProvider extends TagsProvider<Fluid> {
/*    */   public FluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.FLUID, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(FluidTags.WATER).add((ResourceKey<Fluid>[])new ResourceKey[] { FluidIds.WATER, FluidIds.FLOWING_WATER });
/* 20 */     tag(FluidTags.LAVA).add((ResourceKey<Fluid>[])new ResourceKey[] { FluidIds.LAVA, FluidIds.FLOWING_LAVA });
/* 21 */     tag(FluidTags.SUPPORTS_SUGAR_CANE_ADJACENTLY).addTag(FluidTags.WATER);
/* 22 */     tag(FluidTags.SUPPORTS_LILY_PAD).add(FluidIds.WATER);
/* 23 */     tag(FluidTags.SUPPORTS_FROGSPAWN).add(FluidIds.WATER);
/* 24 */     tag(FluidTags.BUBBLE_COLUMN_CAN_OCCUPY).add(FluidIds.WATER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\FluidTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */