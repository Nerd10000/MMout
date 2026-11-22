/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ModelTemplate {
/*    */   private final Optional<Identifier> model;
/*    */   
/*    */   private final Set<TextureSlot> requiredSlots;
/*    */   
/*    */   private final Optional<String> suffix;
/*    */   
/*    */   public ModelTemplate(Optional<Identifier> model, Optional<String> suffix, TextureSlot... requiredSlots) {
/* 28 */     this.model = model;
/* 29 */     this.suffix = suffix;
/* 30 */     this.requiredSlots = (Set<TextureSlot>)ImmutableSet.copyOf((Object[])requiredSlots);
/*    */   }
/*    */   
/*    */   public Identifier getDefaultModelLocation(Block block) {
/* 34 */     return ModelLocationUtils.getModelLocation(block, this.suffix.orElse(""));
/*    */   }
/*    */   
/*    */   public Identifier create(Block block, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 38 */     return create(ModelLocationUtils.getModelLocation(block, this.suffix.orElse("")), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier createWithSuffix(Block block, String extraSuffix, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 42 */     return create(ModelLocationUtils.getModelLocation(block, extraSuffix + extraSuffix), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier createWithOverride(Block block, String suffixOverride, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 46 */     return create(ModelLocationUtils.getModelLocation(block, suffixOverride), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier create(Item item, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 50 */     return create(ModelLocationUtils.getModelLocation(item, this.suffix.orElse("")), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier create(Identifier target, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 54 */     Map<TextureSlot, Material> slots = createMap(textures);
/* 55 */     output.accept(target, () -> {
/*    */           JsonObject result = new JsonObject();
/*    */           this.model.ifPresent(());
/*    */           if (!slots.isEmpty()) {
/*    */             JsonObject textureObj = new JsonObject();
/*    */             slots.forEach(());
/*    */             result.add("textures", (JsonElement)textureObj);
/*    */           } 
/*    */           return (JsonElement)result;
/*    */         });
/* 68 */     return target;
/*    */   }
/*    */   
/*    */   private Map<TextureSlot, Material> createMap(TextureMapping mapping) {
/* 72 */     Objects.requireNonNull(mapping);
/* 72 */     return (Map<TextureSlot, Material>)Streams.concat(new Stream[] { this.requiredSlots.stream(), mapping.getForced() }).collect(ImmutableMap.toImmutableMap(Function.identity(), mapping::get));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\ModelTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */