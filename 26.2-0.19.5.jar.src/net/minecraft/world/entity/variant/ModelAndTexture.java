/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ModelAndTexture<T> extends Record {
/*    */   private final T model;
/*    */   
/*    */   private final ClientAsset.ResourceTexture asset;
/*    */   
/*    */   public ModelAndTexture(T model, ClientAsset.ResourceTexture asset) {
/* 11 */     this.model = model;
/* 11 */     this.asset = asset;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/ModelAndTexture;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>;
/*    */   }
/*    */   
/*    */   public T model() {
/* 11 */     return this.model;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture asset() {
/* 11 */     return this.asset;
/*    */   }
/*    */   
/*    */   public ModelAndTexture(T model, Identifier assetId) {
/* 16 */     this(model, new ClientAsset.ResourceTexture(assetId));
/*    */   }
/*    */   
/*    */   public static <T> MapCodec<ModelAndTexture<T>> codec(Codec<T> modelCodec, T defaultModel) {
/* 20 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)modelCodec.optionalFieldOf("model", defaultModel).forGetter(ModelAndTexture::model), (App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(ModelAndTexture::asset)).apply((Applicative)i, ModelAndTexture::new));
/*    */   }
/*    */   
/*    */   public static <T> StreamCodec<RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, T> modelCodec) {
/* 27 */     return StreamCodec.composite(modelCodec, ModelAndTexture::model, ClientAsset.ResourceTexture.STREAM_CODEC, ModelAndTexture::asset, ModelAndTexture::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\ModelAndTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */