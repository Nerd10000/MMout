/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class Builder {
/* 65 */   private final ImmutableList.Builder<BannerPatternLayers.Layer> layers = ImmutableList.builder();
/*    */   
/*    */   @Deprecated
/*    */   public Builder addIfRegistered(HolderGetter<BannerPattern> patternGetter, ResourceKey<BannerPattern> patternKey, DyeColor color) {
/* 70 */     Optional<Holder.Reference<BannerPattern>> pattern = patternGetter.get(patternKey);
/* 71 */     if (pattern.isEmpty()) {
/* 72 */       BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", patternKey.identifier());
/* 73 */       return this;
/*    */     } 
/* 75 */     return add((Holder<BannerPattern>)pattern.get(), color);
/*    */   }
/*    */   
/*    */   public Builder add(Holder<BannerPattern> pattern, DyeColor color) {
/* 79 */     return add(new BannerPatternLayers.Layer(pattern, color));
/*    */   }
/*    */   
/*    */   public Builder add(BannerPatternLayers.Layer layer) {
/* 83 */     this.layers.add(layer);
/* 84 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addAll(BannerPatternLayers layers) {
/* 88 */     this.layers.addAll(layers.layers);
/* 89 */     return this;
/*    */   }
/*    */   
/*    */   public BannerPatternLayers build() {
/* 93 */     return new BannerPatternLayers((List<BannerPatternLayers.Layer>)this.layers.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */