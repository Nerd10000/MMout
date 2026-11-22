/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<SetBannerPatternFunction.Builder> {
/* 50 */   private final BannerPatternLayers.Builder patterns = new BannerPatternLayers.Builder();
/*    */   
/*    */   private final boolean append;
/*    */   
/*    */   private Builder(boolean append) {
/* 54 */     this.append = append;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 64 */     return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append);
/*    */   }
/*    */   
/*    */   public Builder addPattern(Holder<BannerPattern> pattern, DyeColor color) {
/* 68 */     this.patterns.add(pattern, color);
/* 69 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetBannerPatternFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */