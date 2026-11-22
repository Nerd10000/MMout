/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class TrimPattern extends Record {
/*    */   private final Identifier assetId;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final boolean decal;
/*    */   
/*    */   public static final Codec<TrimPattern> DIRECT_CODEC;
/*    */   
/*    */   public TrimPattern(Identifier assetId, Component description, boolean decal) {
/* 16 */     this.assetId = assetId;
/* 16 */     this.description = description;
/* 16 */     this.decal = decal;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier assetId() {
/* 16 */     return this.assetId;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 16 */     return this.description;
/*    */   }
/*    */   
/*    */   public boolean decal() {
/* 16 */     return this.decal;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("asset_id").forGetter(TrimPattern::assetId), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(TrimPattern::description), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "decal", Boolean.valueOf(false)).forGetter(TrimPattern::decal)).apply((Applicative)i, TrimPattern::new));
/*    */   }
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrimPattern> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, TrimPattern::assetId, ComponentSerialization.STREAM_CODEC, TrimPattern::description, ByteBufCodecs.BOOL, TrimPattern::decal, TrimPattern::new);
/*    */   
/* 30 */   public static final Codec<Holder<TrimPattern>> CODEC = (Codec<Holder<TrimPattern>>)RegistryFileCodec.create(Registries.TRIM_PATTERN, DIRECT_CODEC);
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TrimPattern>> STREAM_CODEC = ByteBufCodecs.holder(Registries.TRIM_PATTERN, DIRECT_STREAM_CODEC);
/*    */   
/*    */   public Component copyWithStyle(Holder<TrimMaterial> material) {
/* 34 */     return (Component)this.description.copy().withStyle(((TrimMaterial)material.value()).description().getStyle());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\trim\TrimPattern.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */