/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import net.minecraft.world.item.component.TooltipProvider;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class BannerPatternLayers extends Record implements TooltipProvider {
/*    */   private final List<Layer> layers;
/*    */   
/*    */   public BannerPatternLayers(List<Layer> layers) {
/* 27 */     this.layers = layers;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Layer> layers() {
/* 27 */     return this.layers;
/*    */   }
/*    */   
/* 28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 30 */   public static final BannerPatternLayers EMPTY = new BannerPatternLayers(List.of());
/*    */   
/* 32 */   public static final Codec<BannerPatternLayers> CODEC = Layer.CODEC.listOf().xmap(BannerPatternLayers::new, BannerPatternLayers::layers);
/*    */   
/* 33 */   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPatternLayers> STREAM_CODEC = Layer.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BannerPatternLayers::new, BannerPatternLayers::layers);
/*    */   
/*    */   public BannerPatternLayers removeLast() {
/* 36 */     return new BannerPatternLayers(List.copyOf(this.layers.subList(0, this.layers.size() - 1)));
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 41 */     for (int i = 0; i < Math.min(layers().size(), 6); i++)
/* 42 */       consumer.accept(((Layer)layers().get(i)).description().withStyle(ChatFormatting.GRAY)); 
/*    */   }
/*    */   
/*    */   public static final class Layer extends Record {
/*    */     private final Holder<BannerPattern> pattern;
/*    */     
/*    */     private final DyeColor color;
/*    */     
/*    */     public static final Codec<Layer> CODEC;
/*    */     
/*    */     public Layer(Holder<BannerPattern> pattern, DyeColor color) {
/* 46 */       this.pattern = pattern;
/* 46 */       this.color = color;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<BannerPattern> pattern() {
/* 46 */       return this.pattern;
/*    */     }
/*    */     
/*    */     public DyeColor color() {
/* 46 */       return this.color;
/*    */     }
/*    */     
/*    */     static {
/* 47 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BannerPattern.CODEC.fieldOf("pattern").forGetter(Layer::pattern), (App)DyeColor.CODEC.fieldOf("color").forGetter(Layer::color)).apply((Applicative)i, Layer::new));
/*    */     }
/*    */     
/* 52 */     public static final StreamCodec<RegistryFriendlyByteBuf, Layer> STREAM_CODEC = StreamCodec.composite(BannerPattern.STREAM_CODEC, Layer::pattern, DyeColor.STREAM_CODEC, Layer::color, Layer::new);
/*    */     
/*    */     public MutableComponent description() {
/* 59 */       String prefix = ((BannerPattern)this.pattern.value()).translationKey();
/* 60 */       return Component.translatable(prefix + "." + prefix);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private final ImmutableList.Builder<BannerPatternLayers.Layer> layers;
/*    */     
/*    */     public Builder() {
/* 65 */       this.layers = ImmutableList.builder();
/*    */     }
/*    */     
/*    */     @Deprecated
/*    */     public Builder addIfRegistered(HolderGetter<BannerPattern> patternGetter, ResourceKey<BannerPattern> patternKey, DyeColor color) {
/* 70 */       Optional<Holder.Reference<BannerPattern>> pattern = patternGetter.get(patternKey);
/* 71 */       if (pattern.isEmpty()) {
/* 72 */         BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", patternKey.identifier());
/* 73 */         return this;
/*    */       } 
/* 75 */       return add((Holder<BannerPattern>)pattern.get(), color);
/*    */     }
/*    */     
/*    */     public Builder add(Holder<BannerPattern> pattern, DyeColor color) {
/* 79 */       return add(new BannerPatternLayers.Layer(pattern, color));
/*    */     }
/*    */     
/*    */     public Builder add(BannerPatternLayers.Layer layer) {
/* 83 */       this.layers.add(layer);
/* 84 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addAll(BannerPatternLayers layers) {
/* 88 */       this.layers.addAll(layers.layers);
/* 89 */       return this;
/*    */     }
/*    */     
/*    */     public BannerPatternLayers build() {
/* 93 */       return new BannerPatternLayers((List<BannerPatternLayers.Layer>)this.layers.build());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */