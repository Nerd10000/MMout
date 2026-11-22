/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<SetAttributesFunction.Builder> {
/*     */   private final boolean replace;
/*     */   
/* 107 */   private final List<SetAttributesFunction.Modifier> modifiers = Lists.newArrayList();
/*     */   
/*     */   public Builder(boolean replace) {
/* 110 */     this.replace = replace;
/*     */   }
/*     */   
/*     */   public Builder() {
/* 114 */     this(false);
/*     */   }
/*     */   
/*     */   protected Builder getThis() {
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withModifier(SetAttributesFunction.ModifierBuilder modifier) {
/* 123 */     this.modifiers.add(modifier.build());
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 129 */     return new SetAttributesFunction(getConditions(), this.modifiers, this.replace);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */