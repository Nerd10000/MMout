/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public final class Layer extends Record {
/*    */   private final Holder<BannerPattern> pattern;
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   public static final Codec<Layer> CODEC;
/*    */   
/*    */   public Layer(Holder<BannerPattern> pattern, DyeColor color) {
/* 46 */     this.pattern = pattern;
/* 46 */     this.color = color;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<BannerPattern> pattern() {
/* 46 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public DyeColor color() {
/* 46 */     return this.color;
/*    */   }
/*    */   
/*    */   static {
/* 47 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BannerPattern.CODEC.fieldOf("pattern").forGetter(Layer::pattern), (App)DyeColor.CODEC.fieldOf("color").forGetter(Layer::color)).apply((Applicative)i, Layer::new));
/*    */   }
/*    */   
/* 52 */   public static final StreamCodec<RegistryFriendlyByteBuf, Layer> STREAM_CODEC = StreamCodec.composite(BannerPattern.STREAM_CODEC, Layer::pattern, DyeColor.STREAM_CODEC, Layer::color, Layer::new);
/*    */   
/*    */   public MutableComponent description() {
/* 59 */     String prefix = ((BannerPattern)this.pattern.value()).translationKey();
/* 60 */     return Component.translatable(prefix + "." + prefix);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers$Layer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */