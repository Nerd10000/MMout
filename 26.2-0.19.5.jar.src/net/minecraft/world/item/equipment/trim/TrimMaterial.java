/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public final class TrimMaterial extends Record {
/*    */   private final MaterialAssetGroup assets;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   public static final Codec<TrimMaterial> DIRECT_CODEC;
/*    */   
/*    */   public TrimMaterial(MaterialAssetGroup assets, Component description) {
/* 14 */     this.assets = assets;
/* 14 */     this.description = description;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MaterialAssetGroup assets() {
/* 14 */     return this.assets;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 14 */     return this.description;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)MaterialAssetGroup.MAP_CODEC.forGetter(TrimMaterial::assets), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(TrimMaterial::description)).apply((Applicative)i, TrimMaterial::new));
/*    */   }
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrimMaterial> DIRECT_STREAM_CODEC = StreamCodec.composite(MaterialAssetGroup.STREAM_CODEC, TrimMaterial::assets, ComponentSerialization.STREAM_CODEC, TrimMaterial::description, TrimMaterial::new);
/*    */   
/* 26 */   public static final Codec<Holder<TrimMaterial>> CODEC = (Codec<Holder<TrimMaterial>>)RegistryFileCodec.create(Registries.TRIM_MATERIAL, DIRECT_CODEC);
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TrimMaterial>> STREAM_CODEC = ByteBufCodecs.holder(Registries.TRIM_MATERIAL, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\trim\TrimMaterial.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */