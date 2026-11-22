/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.data.models.MultiVariant;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class Empty {
/*     */   private final Block block;
/*     */   
/*     */   public Empty(Block block) {
/*  96 */     this.block = block;
/*     */   }
/*     */   
/*     */   public MultiVariantGenerator with(PropertyDispatch<MultiVariant> newStage) {
/* 100 */     Set<Property<?>> newSeenProperties = MultiVariantGenerator.validateAndExpandProperties(Set.of(), this.block, newStage);
/* 101 */     List<MultiVariantGenerator.Entry> newEntries = newStage.getEntries().entrySet().stream().map(e -> new MultiVariantGenerator.Entry((PropertyValueList)e.getKey(), (MultiVariant)e.getValue())).toList();
/* 102 */     return new MultiVariantGenerator(this.block, newEntries, newSeenProperties);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\MultiVariantGenerator$Empty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */