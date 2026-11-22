/*    */ package net.minecraft.world.entity.decoration.painting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class PaintingVariant extends Record {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final Identifier assetId;
/*    */   
/*    */   private final Optional<Component> title;
/*    */   
/*    */   private final Optional<Component> author;
/*    */   
/*    */   public static final Codec<PaintingVariant> DIRECT_CODEC;
/*    */   
/*    */   public PaintingVariant(int width, int height, Identifier assetId, Optional<Component> title, Optional<Component> author) {
/* 18 */     this.width = width;
/* 18 */     this.height = height;
/* 18 */     this.assetId = assetId;
/* 18 */     this.title = title;
/* 18 */     this.author = author;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/* 18 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 18 */     return this.height;
/*    */   }
/*    */   
/*    */   public Identifier assetId() {
/* 18 */     return this.assetId;
/*    */   }
/*    */   
/*    */   public Optional<Component> title() {
/* 18 */     return this.title;
/*    */   }
/*    */   
/*    */   public Optional<Component> author() {
/* 18 */     return this.author;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.intRange(1, 16).fieldOf("width").forGetter(PaintingVariant::width), (App)ExtraCodecs.intRange(1, 16).fieldOf("height").forGetter(PaintingVariant::height), (App)Identifier.CODEC.fieldOf("asset_id").forGetter(PaintingVariant::assetId), (App)ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(PaintingVariant::title), (App)ComponentSerialization.CODEC.optionalFieldOf("author").forGetter(PaintingVariant::author)).apply((Applicative)i, PaintingVariant::new));
/*    */   }
/*    */   
/* 33 */   public static final StreamCodec<RegistryFriendlyByteBuf, PaintingVariant> DIRECT_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PaintingVariant::width, ByteBufCodecs.VAR_INT, PaintingVariant::height, Identifier.STREAM_CODEC, PaintingVariant::assetId, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::title, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::author, PaintingVariant::new);
/*    */   
/* 42 */   public static final Codec<Holder<PaintingVariant>> CODEC = (Codec<Holder<PaintingVariant>>)RegistryFixedCodec.create(Registries.PAINTING_VARIANT);
/*    */   
/* 44 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PaintingVariant>> STREAM_CODEC = ByteBufCodecs.holder(Registries.PAINTING_VARIANT, DIRECT_STREAM_CODEC);
/*    */   
/*    */   public int area() {
/* 47 */     return width() * height();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\painting\PaintingVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */