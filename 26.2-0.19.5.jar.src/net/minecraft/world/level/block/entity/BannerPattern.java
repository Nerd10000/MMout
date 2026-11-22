/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public final class BannerPattern extends Record {
/*    */   private final Identifier assetId;
/*    */   
/*    */   private final String translationKey;
/*    */   
/*    */   public static final Codec<BannerPattern> DIRECT_CODEC;
/*    */   
/*    */   public BannerPattern(Identifier assetId, String translationKey) {
/* 13 */     this.assetId = assetId;
/* 13 */     this.translationKey = translationKey;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPattern;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPattern;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPattern;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier assetId() {
/* 13 */     return this.assetId;
/*    */   }
/*    */   
/*    */   public String translationKey() {
/* 13 */     return this.translationKey;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("asset_id").forGetter(BannerPattern::assetId), (App)Codec.STRING.fieldOf("translation_key").forGetter(BannerPattern::translationKey)).apply((Applicative)i, BannerPattern::new));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPattern> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, BannerPattern::assetId, ByteBufCodecs.STRING_UTF8, BannerPattern::translationKey, BannerPattern::new);
/*    */   
/* 27 */   public static final Codec<Holder<BannerPattern>> CODEC = (Codec<Holder<BannerPattern>>)RegistryFileCodec.create(Registries.BANNER_PATTERN, DIRECT_CODEC);
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BannerPattern>> STREAM_CODEC = ByteBufCodecs.holder(Registries.BANNER_PATTERN, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BannerPattern.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */