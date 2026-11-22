/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class Builder {
/*     */   private final FeatureFlagUniverse universe;
/*     */   
/*     */   private int id;
/*     */   
/*  88 */   private final Map<Identifier, FeatureFlag> flags = new LinkedHashMap<>();
/*     */   
/*     */   public Builder(String universeId) {
/*  91 */     this.universe = new FeatureFlagUniverse(universeId);
/*     */   }
/*     */   
/*     */   public FeatureFlag createVanilla(String name) {
/*  95 */     return create(Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   public FeatureFlag create(Identifier name) {
/*  99 */     if (this.id >= 64)
/* 101 */       throw new IllegalStateException("Too many feature flags"); 
/* 103 */     FeatureFlag result = new FeatureFlag(this.universe, this.id++);
/* 104 */     FeatureFlag previous = this.flags.put(name, result);
/* 105 */     if (previous != null)
/* 106 */       throw new IllegalStateException("Duplicate feature flag " + String.valueOf(name)); 
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   public FeatureFlagRegistry build() {
/* 112 */     FeatureFlagSet allValues = FeatureFlagSet.create(this.universe, this.flags.values());
/* 113 */     return new FeatureFlagRegistry(this.universe, allValues, Map.copyOf(this.flags));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\flag\FeatureFlagRegistry$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */