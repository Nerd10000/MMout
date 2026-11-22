/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ 
/*     */ public class TextureMapping {
/*  19 */   private final Map<TextureSlot, Material> slots = Maps.newHashMap();
/*     */   
/*  20 */   private final Set<TextureSlot> forcedSlots = Sets.newHashSet();
/*     */   
/*     */   public TextureMapping put(TextureSlot slot, Material material) {
/*  26 */     this.slots.put(slot, material);
/*  27 */     return this;
/*     */   }
/*     */   
/*     */   public TextureMapping putForced(TextureSlot slot, Material material) {
/*  31 */     this.slots.put(slot, material);
/*  32 */     this.forcedSlots.add(slot);
/*  33 */     return this;
/*     */   }
/*     */   
/*     */   public Stream<TextureSlot> getForced() {
/*  37 */     return this.forcedSlots.stream();
/*     */   }
/*     */   
/*     */   public TextureMapping copySlot(TextureSlot from, TextureSlot to) {
/*  41 */     return put(to, this.slots.get(from));
/*     */   }
/*     */   
/*     */   public TextureMapping copyForced(TextureSlot from, TextureSlot to) {
/*  45 */     return putForced(to, this.slots.get(from));
/*     */   }
/*     */   
/*     */   public Material get(TextureSlot slot) {
/*  49 */     TextureSlot currentSlot = slot;
/*  50 */     while (currentSlot != null) {
/*  51 */       Material result = this.slots.get(currentSlot);
/*  52 */       if (result != null)
/*  53 */         return result; 
/*  55 */       currentSlot = currentSlot.getParent();
/*     */     } 
/*  57 */     throw new IllegalStateException("Can't find texture for slot " + String.valueOf(slot));
/*     */   }
/*     */   
/*     */   public TextureMapping copyAndUpdate(TextureSlot slot, Material material) {
/*  61 */     TextureMapping result = new TextureMapping();
/*  62 */     result.slots.putAll(this.slots);
/*  63 */     result.forcedSlots.addAll(this.forcedSlots);
/*  64 */     result.put(slot, material);
/*  65 */     return result;
/*     */   }
/*     */   
/*     */   public TextureMapping updateSlots(BiFunction<TextureSlot, Material, Material> mapper) {
/*  69 */     this.slots.replaceAll(mapper);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public TextureMapping forceAllTranslucent() {
/*  74 */     return updateSlots((paramTextureSlot, material) -> material.withForceTranslucent(true));
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(Block block) {
/*  78 */     Material texture = getBlockTexture(block);
/*  79 */     return cube(texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(Block block) {
/*  83 */     Material texture = getBlockTexture(block);
/*  84 */     return defaultTexture(texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(Material texture) {
/*  88 */     return (new TextureMapping()).put(TextureSlot.TEXTURE, texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(Material all) {
/*  92 */     return (new TextureMapping()).put(TextureSlot.ALL, all);
/*     */   }
/*     */   
/*     */   public static TextureMapping cross(Block block) {
/*  96 */     return singleSlot(TextureSlot.CROSS, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping side(Block block) {
/* 100 */     return singleSlot(TextureSlot.SIDE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping crossEmissive(Block block) {
/* 104 */     return (new TextureMapping())
/* 105 */       .put(TextureSlot.CROSS, getBlockTexture(block))
/* 106 */       .put(TextureSlot.CROSS_EMISSIVE, getBlockTexture(block, "_emissive"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cross(Material cross) {
/* 111 */     return singleSlot(TextureSlot.CROSS, cross);
/*     */   }
/*     */   
/*     */   public static TextureMapping plant(Block block) {
/* 115 */     return singleSlot(TextureSlot.PLANT, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping plantEmissive(Block block) {
/* 119 */     return (new TextureMapping())
/* 120 */       .put(TextureSlot.PLANT, getBlockTexture(block))
/* 121 */       .put(TextureSlot.CROSS_EMISSIVE, getBlockTexture(block, "_emissive"));
/*     */   }
/*     */   
/*     */   public static TextureMapping plant(Material plant) {
/* 126 */     return singleSlot(TextureSlot.PLANT, plant);
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(Block block) {
/* 130 */     return singleSlot(TextureSlot.RAIL, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(Material rail) {
/* 134 */     return singleSlot(TextureSlot.RAIL, rail);
/*     */   }
/*     */   
/*     */   public static TextureMapping wool(Block block) {
/* 138 */     return singleSlot(TextureSlot.WOOL, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping flowerbed(Block block) {
/* 142 */     return (new TextureMapping())
/* 143 */       .put(TextureSlot.FLOWERBED, getBlockTexture(block))
/* 144 */       .put(TextureSlot.STEM, getBlockTexture(block, "_stem"));
/*     */   }
/*     */   
/*     */   public static TextureMapping wool(Material cross) {
/* 149 */     return singleSlot(TextureSlot.WOOL, cross);
/*     */   }
/*     */   
/*     */   public static TextureMapping stem(Block block) {
/* 153 */     return singleSlot(TextureSlot.STEM, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping attachedStem(Block stem, Block upperStem) {
/* 157 */     return (new TextureMapping())
/* 158 */       .put(TextureSlot.STEM, getBlockTexture(stem))
/* 159 */       .put(TextureSlot.UPPER_STEM, getBlockTexture(upperStem));
/*     */   }
/*     */   
/*     */   public static TextureMapping pattern(Block block) {
/* 164 */     return singleSlot(TextureSlot.PATTERN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping fan(Block block) {
/* 168 */     return singleSlot(TextureSlot.FAN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping crop(Material id) {
/* 172 */     return singleSlot(TextureSlot.CROP, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping pane(Block body, Block edge) {
/* 176 */     return (new TextureMapping()).put(TextureSlot.PANE, getBlockTexture(body)).put(TextureSlot.EDGE, getBlockTexture(edge, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping singleSlot(TextureSlot slot, Material id) {
/* 180 */     return (new TextureMapping()).put(slot, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping column(Block block) {
/* 184 */     return (new TextureMapping())
/* 185 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 186 */       .put(TextureSlot.END, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeTop(Block block) {
/* 190 */     return (new TextureMapping())
/* 191 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 192 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping pottedAzalea(Block block) {
/* 196 */     return (new TextureMapping())
/* 197 */       .put(TextureSlot.PLANT, getBlockTexture(block, "_plant"))
/* 198 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 199 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping logColumn(Block block) {
/* 204 */     return (new TextureMapping()).put(TextureSlot.SIDE, getBlockTexture(block)).put(TextureSlot.END, getBlockTexture(block, "_top")).put(TextureSlot.PARTICLE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping column(Material side, Material end) {
/* 208 */     return (new TextureMapping()).put(TextureSlot.SIDE, side).put(TextureSlot.END, end);
/*     */   }
/*     */   
/*     */   public static TextureMapping fence(Block block) {
/* 212 */     return (new TextureMapping()).put(TextureSlot.TEXTURE, getBlockTexture(block)).put(TextureSlot.SIDE, getBlockTexture(block, "_side")).put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping customParticle(Block block) {
/* 216 */     return (new TextureMapping()).put(TextureSlot.TEXTURE, getBlockTexture(block)).put(TextureSlot.PARTICLE, getBlockTexture(block, "_particle"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTop(Block block) {
/* 220 */     return (new TextureMapping())
/* 221 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 222 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 223 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTopWithWall(Block block) {
/* 227 */     Material side = getBlockTexture(block);
/* 228 */     return (new TextureMapping())
/* 229 */       .put(TextureSlot.WALL, side)
/* 230 */       .put(TextureSlot.SIDE, side)
/* 231 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 232 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping columnWithWall(Block block) {
/* 236 */     Material side = getBlockTexture(block);
/* 237 */     return (new TextureMapping())
/* 238 */       .put(TextureSlot.TEXTURE, side)
/* 239 */       .put(TextureSlot.WALL, side)
/* 240 */       .put(TextureSlot.SIDE, side)
/* 241 */       .put(TextureSlot.END, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping door(Material top, Material bottom) {
/* 245 */     return (new TextureMapping()).put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom);
/*     */   }
/*     */   
/*     */   public static TextureMapping door(Block block) {
/* 249 */     return (new TextureMapping()).put(TextureSlot.TOP, getBlockTexture(block, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping bed(Block block, BedPart part) {
/* 256 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.UP, getBlockTexture(block, "_" + String.valueOf(part) + "_up")).put(TextureSlot.EAST, getBlockTexture(block, "_" + String.valueOf(part) + "_east")).put(TextureSlot.WEST, getBlockTexture(block, "_" + String.valueOf(part) + "_west"));
/* 257 */     if (part == BedPart.FOOT)
/* 258 */       mapping.put(TextureSlot.SOUTH, getBlockTexture(block, "_" + String.valueOf(part) + "_south")); 
/* 260 */     return mapping;
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(Block block) {
/* 264 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(Material id) {
/* 268 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping fire0(Block block) {
/* 272 */     return (new TextureMapping()).put(TextureSlot.FIRE, getBlockTexture(block, "_0"));
/*     */   }
/*     */   
/*     */   public static TextureMapping fire1(Block block) {
/* 276 */     return (new TextureMapping()).put(TextureSlot.FIRE, getBlockTexture(block, "_1"));
/*     */   }
/*     */   
/*     */   public static TextureMapping lantern(Block block) {
/* 280 */     return (new TextureMapping()).put(TextureSlot.LANTERN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(Block block) {
/* 284 */     return (new TextureMapping()).put(TextureSlot.TORCH, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(Material id) {
/* 288 */     return (new TextureMapping()).put(TextureSlot.TORCH, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping trialSpawner(Block block, String sideSuffix, String topSuffix) {
/* 292 */     return (new TextureMapping())
/* 293 */       .put(TextureSlot.SIDE, getBlockTexture(block, sideSuffix))
/* 294 */       .put(TextureSlot.TOP, getBlockTexture(block, topSuffix))
/* 295 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping vault(Block block, String frontSuffix, String sideSuffix, String topSuffix, String bottomSuffix) {
/* 299 */     return (new TextureMapping())
/* 300 */       .put(TextureSlot.FRONT, getBlockTexture(block, frontSuffix))
/* 301 */       .put(TextureSlot.SIDE, getBlockTexture(block, sideSuffix))
/* 302 */       .put(TextureSlot.TOP, getBlockTexture(block, topSuffix))
/* 303 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, bottomSuffix));
/*     */   }
/*     */   
/*     */   public static TextureMapping particleFromItem(Item item) {
/* 307 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, getItemTexture(item));
/*     */   }
/*     */   
/*     */   public static TextureMapping commandBlock(Block block) {
/* 311 */     return (new TextureMapping())
/* 312 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 313 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 314 */       .put(TextureSlot.BACK, getBlockTexture(block, "_back"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCube(Block block) {
/* 318 */     return (new TextureMapping())
/* 319 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 320 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 321 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 322 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeOnlyTop(Block block) {
/* 326 */     return (new TextureMapping())
/* 327 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 328 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 329 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeSameEnds(Block block) {
/* 333 */     return (new TextureMapping())
/* 334 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 335 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 336 */       .put(TextureSlot.END, getBlockTexture(block, "_end"));
/*     */   }
/*     */   
/*     */   public static TextureMapping top(Block block) {
/* 340 */     return (new TextureMapping()).put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping craftingTable(Block table, Block bottomWood) {
/* 344 */     return (new TextureMapping())
/* 345 */       .put(TextureSlot.PARTICLE, getBlockTexture(table, "_front"))
/* 346 */       .put(TextureSlot.DOWN, getBlockTexture(bottomWood))
/* 347 */       .put(TextureSlot.UP, getBlockTexture(table, "_top"))
/* 348 */       .put(TextureSlot.NORTH, getBlockTexture(table, "_front"))
/* 349 */       .put(TextureSlot.EAST, getBlockTexture(table, "_side"))
/* 350 */       .put(TextureSlot.SOUTH, getBlockTexture(table, "_side"))
/* 351 */       .put(TextureSlot.WEST, getBlockTexture(table, "_front"));
/*     */   }
/*     */   
/*     */   public static TextureMapping fletchingTable(Block table, Block bottomWood) {
/* 356 */     return (new TextureMapping())
/* 357 */       .put(TextureSlot.PARTICLE, getBlockTexture(table, "_front"))
/* 358 */       .put(TextureSlot.DOWN, getBlockTexture(bottomWood))
/* 359 */       .put(TextureSlot.UP, getBlockTexture(table, "_top"))
/* 360 */       .put(TextureSlot.NORTH, getBlockTexture(table, "_front"))
/* 361 */       .put(TextureSlot.SOUTH, getBlockTexture(table, "_front"))
/* 362 */       .put(TextureSlot.EAST, getBlockTexture(table, "_side"))
/* 363 */       .put(TextureSlot.WEST, getBlockTexture(table, "_side"));
/*     */   }
/*     */   
/*     */   public static TextureMapping snifferEgg(String suffix) {
/* 367 */     return (new TextureMapping())
/* 368 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_north"))
/* 369 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_bottom"))
/* 370 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_top"))
/* 371 */       .put(TextureSlot.NORTH, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_north"))
/* 372 */       .put(TextureSlot.SOUTH, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_south"))
/* 373 */       .put(TextureSlot.EAST, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_east"))
/* 374 */       .put(TextureSlot.WEST, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_west"));
/*     */   }
/*     */   
/*     */   public static TextureMapping driedGhast(String suffix) {
/* 378 */     return (new TextureMapping())
/* 379 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_north"))
/* 380 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_bottom"))
/* 381 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_top"))
/* 382 */       .put(TextureSlot.NORTH, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_north"))
/* 383 */       .put(TextureSlot.SOUTH, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_south"))
/* 384 */       .put(TextureSlot.EAST, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_east"))
/* 385 */       .put(TextureSlot.WEST, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_west"))
/* 386 */       .put(TextureSlot.TENTACLES, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_tentacles"));
/*     */   }
/*     */   
/*     */   public static TextureMapping campfire(Block campfire) {
/* 390 */     return (new TextureMapping())
/* 391 */       .put(TextureSlot.LIT_LOG, getBlockTexture(campfire, "_log_lit"))
/* 392 */       .put(TextureSlot.FIRE, getBlockTexture(campfire, "_fire"));
/*     */   }
/*     */   
/*     */   public static TextureMapping candleCake(Block block, boolean lit) {
/* 397 */     return (new TextureMapping())
/* 398 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAKE, "_side"))
/* 399 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAKE, "_bottom"))
/* 400 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.CAKE, "_top"))
/* 401 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.CAKE, "_side"))
/* 402 */       .put(TextureSlot.CANDLE, getBlockTexture(block, lit ? "_lit" : ""));
/*     */   }
/*     */   
/*     */   public static TextureMapping cauldron(Material contentTextureLoc) {
/* 407 */     return (new TextureMapping())
/* 408 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAULDRON, "_side"))
/* 409 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.CAULDRON, "_side"))
/* 410 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.CAULDRON, "_top"))
/* 411 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAULDRON, "_bottom"))
/* 412 */       .put(TextureSlot.INSIDE, getBlockTexture(Blocks.CAULDRON, "_inner"))
/* 413 */       .put(TextureSlot.CONTENT, contentTextureLoc);
/*     */   }
/*     */   
/*     */   public static TextureMapping sculkShrieker(boolean canSummon) {
/* 418 */     String innerTopString = canSummon ? "_can_summon" : "";
/* 419 */     return (new TextureMapping())
/* 420 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom"))
/* 421 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_side"))
/* 422 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, "_top"))
/* 423 */       .put(TextureSlot.INNER_TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, innerTopString + "_inner_top"))
/* 424 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping bars(Block block) {
/* 429 */     return (new TextureMapping())
/* 430 */       .put(TextureSlot.BARS, getBlockTexture(block))
/* 431 */       .put(TextureSlot.EDGE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Item item) {
/* 435 */     return (new TextureMapping()).put(TextureSlot.LAYER0, getItemTexture(item));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Block block) {
/* 439 */     return (new TextureMapping()).put(TextureSlot.LAYER0, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Material id) {
/* 443 */     return (new TextureMapping()).put(TextureSlot.LAYER0, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping layered(Material layer0, Material layer1) {
/* 447 */     return (new TextureMapping()).put(TextureSlot.LAYER0, layer0).put(TextureSlot.LAYER1, layer1);
/*     */   }
/*     */   
/*     */   public static TextureMapping layered(Material layer0, Material layer1, Material layer2) {
/* 451 */     return (new TextureMapping()).put(TextureSlot.LAYER0, layer0).put(TextureSlot.LAYER1, layer1).put(TextureSlot.LAYER2, layer2);
/*     */   }
/*     */   
/*     */   public static Material getBlockTexture(Block block) {
/* 455 */     Identifier id = BuiltInRegistries.BLOCK.getKey(block);
/* 456 */     return new Material(id.withPrefix("block/"));
/*     */   }
/*     */   
/*     */   public static Material getBlockTexture(Block block, String suffix) {
/* 460 */     Identifier id = BuiltInRegistries.BLOCK.getKey(block);
/* 461 */     return getBlockTexture(id, suffix);
/*     */   }
/*     */   
/*     */   private static Material getBlockTexture(Identifier id, String suffix) {
/* 465 */     return new Material(id.withPath(path -> "block/" + path + suffix));
/*     */   }
/*     */   
/*     */   public static Material getItemTexture(Item block) {
/* 469 */     Identifier id = BuiltInRegistries.ITEM.getKey(block);
/* 470 */     return new Material(id.withPrefix("item/"));
/*     */   }
/*     */   
/*     */   public static Material getItemTexture(Item item, String suffix) {
/* 474 */     Identifier id = BuiltInRegistries.ITEM.getKey(item);
/* 475 */     return new Material(id.withPath(path -> "item/" + path + suffix));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\TextureMapping.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */