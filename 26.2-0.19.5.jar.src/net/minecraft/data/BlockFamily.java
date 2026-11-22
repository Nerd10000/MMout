/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class BlockFamily {
/*     */   private final Block baseBlock;
/*     */   
/*  14 */   private final Map<Variant, Block> variants = Maps.newHashMap();
/*     */   
/*     */   private boolean generateModel = true;
/*     */   
/*     */   private boolean generateCraftingRecipe = true;
/*     */   
/*     */   private boolean generateSmeltingRecipe = true;
/*     */   
/*     */   private boolean generateStonecutterRecipe = false;
/*     */   
/*     */   private String recipeGroupPrefix;
/*     */   
/*     */   private String recipeUnlockedBy;
/*     */   
/*     */   public enum Variant {
/*  23 */     BUTTON("button"),
/*  24 */     CHISELED("chiseled") {
/*     */       public Variant getBaseVariantForCrafting() {
/*  27 */         return SLAB;
/*     */       }
/*     */     },
/*  30 */     CRACKED("cracked"),
/*  31 */     CUT("cut") {
/*     */       public String getPrefixedRecipeGroup(String prefix) {
/*  34 */         return prefix;
/*     */       }
/*     */     },
/*  37 */     DOOR("door"),
/*  38 */     CUSTOM_FENCE("fence"),
/*  39 */     FENCE("fence"),
/*  40 */     CUSTOM_FENCE_GATE("fence_gate"),
/*  41 */     FENCE_GATE("fence_gate"),
/*  42 */     CUSTOM_HANGING_SIGN("hanging_sign") {
/*     */       public Variant getBaseVariantForCrafting() {
/*  45 */         return STRIPPED_LOG;
/*     */       }
/*     */     },
/*  48 */     HANGING_SIGN("hanging_sign") {
/*     */       public Variant getBaseVariantForCrafting() {
/*  51 */         return STRIPPED_LOG;
/*     */       }
/*     */     },
/*  54 */     LOG("log"),
/*  55 */     STRIPPED_LOG("stripped_log"),
/*  56 */     MOSAIC("mosaic"),
/*  57 */     SIGN("sign"),
/*  58 */     SLAB("slab"),
/*  59 */     STAIRS("stairs"),
/*  60 */     PRESSURE_PLATE("pressure_plate"),
/*  61 */     POLISHED("polished"),
/*  62 */     TRAPDOOR("trapdoor"),
/*  63 */     WALL("wall"),
/*  64 */     WALL_SIGN("wall_sign"),
/*  65 */     CUSTOM_WALL_HANGING_SIGN("wall_hanging_sign"),
/*  66 */     WALL_HANGING_SIGN("wall_hanging_sign"),
/*  67 */     BRICKS("bricks"),
/*  68 */     COBBLED("cobbled"),
/*  69 */     TILES("tiles"),
/*  70 */     PILLAR("pillar");
/*     */     
/*     */     private final String recipeGroup;
/*     */     
/*     */     Variant(String recipeGroup) {
/*  75 */       this.recipeGroup = recipeGroup;
/*     */     }
/*     */     
/*     */     public String getRecipeGroup() {
/*  79 */       return this.recipeGroup;
/*     */     }
/*     */     
/*     */     public String getPrefixedRecipeGroup(String prefix) {
/*  83 */       return prefix + "_" + prefix;
/*     */     }
/*     */     
/*     */     public Variant getBaseVariantForCrafting() {
/*  87 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public BlockFamily.Variant getBaseVariantForCrafting() {
/*     */       return SLAB;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public String getPrefixedRecipeGroup(String prefix) {
/*     */       return prefix;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public BlockFamily.Variant getBaseVariantForCrafting() {
/*     */       return STRIPPED_LOG;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public BlockFamily.Variant getBaseVariantForCrafting() {
/*     */       return STRIPPED_LOG;
/*     */     }
/*     */   }
/*     */   
/*     */   private BlockFamily(Block baseBlock) {
/*  92 */     this.baseBlock = baseBlock;
/*     */   }
/*     */   
/*     */   public Block getBaseBlock() {
/*  96 */     return this.baseBlock;
/*     */   }
/*     */   
/*     */   public Map<Variant, Block> getVariants() {
/* 100 */     return this.variants;
/*     */   }
/*     */   
/*     */   public Block get(Variant variant) {
/* 104 */     return this.variants.get(variant);
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateModel() {
/* 108 */     return this.generateModel;
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateCraftingRecipe() {
/* 112 */     return this.generateCraftingRecipe;
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateSmeltingRecipe() {
/* 116 */     return this.generateSmeltingRecipe;
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateStonecutterRecipe() {
/* 120 */     return this.generateStonecutterRecipe;
/*     */   }
/*     */   
/*     */   public Optional<String> getRecipeGroupPrefix() {
/* 124 */     if (StringUtil.isBlank(this.recipeGroupPrefix))
/* 125 */       return Optional.empty(); 
/* 127 */     return Optional.of(this.recipeGroupPrefix);
/*     */   }
/*     */   
/*     */   public Optional<String> getRecipeUnlockedBy() {
/* 131 */     if (StringUtil.isBlank(this.recipeUnlockedBy))
/* 132 */       return Optional.empty(); 
/* 134 */     return Optional.of(this.recipeUnlockedBy);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final BlockFamily family;
/*     */     
/*     */     public Builder(Block baseBlock) {
/* 141 */       this.family = new BlockFamily(baseBlock);
/*     */     }
/*     */     
/*     */     public BlockFamily getFamily() {
/* 145 */       return this.family;
/*     */     }
/*     */     
/*     */     public Builder button(Block button) {
/* 149 */       this.family.variants.put(BlockFamily.Variant.BUTTON, button);
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder chiseled(Block chiseled) {
/* 154 */       this.family.variants.put(BlockFamily.Variant.CHISELED, chiseled);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder mosaic(Block mosaic) {
/* 159 */       this.family.variants.put(BlockFamily.Variant.MOSAIC, mosaic);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cracked(Block cracked) {
/* 164 */       this.family.variants.put(BlockFamily.Variant.CRACKED, cracked);
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder tiles(Block tiles) {
/* 169 */       this.family.variants.put(BlockFamily.Variant.TILES, tiles);
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder pillar(Block pillar) {
/* 174 */       this.family.variants.put(BlockFamily.Variant.PILLAR, pillar);
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cut(Block cut) {
/* 179 */       this.family.variants.put(BlockFamily.Variant.CUT, cut);
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder door(Block door) {
/* 184 */       this.family.variants.put(BlockFamily.Variant.DOOR, door);
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder customFence(Block fence) {
/* 189 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE, fence);
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     public Builder fence(Block fence) {
/* 195 */       this.family.variants.put(BlockFamily.Variant.FENCE, fence);
/* 196 */       return this;
/*     */     }
/*     */     
/*     */     public Builder customFenceGate(Block fenceGate) {
/* 200 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE_GATE, fenceGate);
/* 201 */       return this;
/*     */     }
/*     */     
/*     */     public Builder fenceGate(Block fenceGate) {
/* 206 */       this.family.variants.put(BlockFamily.Variant.FENCE_GATE, fenceGate);
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sign(Block sign, Block wallSign) {
/* 211 */       this.family.variants.put(BlockFamily.Variant.SIGN, sign);
/* 212 */       this.family.variants.put(BlockFamily.Variant.WALL_SIGN, wallSign);
/* 213 */       return this;
/*     */     }
/*     */     
/*     */     public Builder customHangingSign(Block sign, Block wallSign) {
/* 217 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_HANGING_SIGN, sign);
/* 218 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_WALL_HANGING_SIGN, wallSign);
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hangingSign(Block sign, Block wallSign) {
/* 223 */       this.family.variants.put(BlockFamily.Variant.HANGING_SIGN, sign);
/* 224 */       this.family.variants.put(BlockFamily.Variant.WALL_HANGING_SIGN, wallSign);
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public Builder log(Block log) {
/* 229 */       this.family.variants.put(BlockFamily.Variant.LOG, log);
/* 230 */       return this;
/*     */     }
/*     */     
/*     */     public Builder strippedLog(Block strippedLog) {
/* 234 */       this.family.variants.put(BlockFamily.Variant.STRIPPED_LOG, strippedLog);
/* 235 */       return this;
/*     */     }
/*     */     
/*     */     public Builder slab(Block slab) {
/* 239 */       this.family.variants.put(BlockFamily.Variant.SLAB, slab);
/* 240 */       return this;
/*     */     }
/*     */     
/*     */     public Builder stairs(Block stairs) {
/* 244 */       this.family.variants.put(BlockFamily.Variant.STAIRS, stairs);
/* 245 */       return this;
/*     */     }
/*     */     
/*     */     public Builder pressurePlate(Block pressurePlate) {
/* 249 */       this.family.variants.put(BlockFamily.Variant.PRESSURE_PLATE, pressurePlate);
/* 250 */       return this;
/*     */     }
/*     */     
/*     */     public Builder polished(Block polished) {
/* 254 */       this.family.variants.put(BlockFamily.Variant.POLISHED, polished);
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     public Builder trapdoor(Block trapdoor) {
/* 259 */       this.family.variants.put(BlockFamily.Variant.TRAPDOOR, trapdoor);
/* 260 */       return this;
/*     */     }
/*     */     
/*     */     public Builder wall(Block wall) {
/* 264 */       this.family.variants.put(BlockFamily.Variant.WALL, wall);
/* 265 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cobbled(Block cobble) {
/* 269 */       this.family.variants.put(BlockFamily.Variant.COBBLED, cobble);
/* 270 */       return this;
/*     */     }
/*     */     
/*     */     public Builder bricks(Block bricks) {
/* 274 */       this.family.variants.put(BlockFamily.Variant.BRICKS, bricks);
/* 275 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dontGenerateModel() {
/* 280 */       this.family.generateModel = false;
/* 281 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dontGenerateCraftingRecipe() {
/* 286 */       this.family.generateCraftingRecipe = false;
/* 287 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dontGenerateSmeltingRecipe() {
/* 292 */       this.family.generateSmeltingRecipe = false;
/* 293 */       return this;
/*     */     }
/*     */     
/*     */     public Builder generateStonecutterRecipe() {
/* 297 */       this.family.generateStonecutterRecipe = true;
/* 298 */       return this;
/*     */     }
/*     */     
/*     */     public Builder recipeGroupPrefix(String recipeGroupPrefix) {
/* 302 */       this.family.recipeGroupPrefix = recipeGroupPrefix;
/* 303 */       return this;
/*     */     }
/*     */     
/*     */     public Builder recipeUnlockedBy(String recipeUnlockedBy) {
/* 307 */       this.family.recipeUnlockedBy = recipeUnlockedBy;
/* 308 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\BlockFamily.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */