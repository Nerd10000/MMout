/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class CatVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ClientAsset.ResourceTexture adultAssetInfo;
/*    */   
/*    */   private final ClientAsset.ResourceTexture babyAssetInfo;
/*    */   
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/*    */   public static final Codec<CatVariant> DIRECT_CODEC;
/*    */   
/*    */   public static final Codec<CatVariant> NETWORK_CODEC;
/*    */   
/*    */   public CatVariant(ClientAsset.ResourceTexture adultAssetInfo, ClientAsset.ResourceTexture babyAssetInfo, SpawnPrioritySelectors spawnConditions) {
/* 19 */     this.adultAssetInfo = adultAssetInfo;
/* 19 */     this.babyAssetInfo = babyAssetInfo;
/* 19 */     this.spawnConditions = spawnConditions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/feline/CatVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture adultAssetInfo() {
/* 19 */     return this.adultAssetInfo;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture babyAssetInfo() {
/* 19 */     return this.babyAssetInfo;
/*    */   }
/*    */   
/*    */   public SpawnPrioritySelectors spawnConditions() {
/* 19 */     return this.spawnConditions;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(CatVariant::adultAssetInfo), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(CatVariant::babyAssetInfo), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CatVariant::spawnConditions)).apply((Applicative)i, CatVariant::new));
/* 31 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(CatVariant::adultAssetInfo), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(CatVariant::babyAssetInfo)).apply((Applicative)i, CatVariant::new));
/*    */   }
/*    */   
/* 36 */   public static final Codec<Holder<CatVariant>> CODEC = (Codec<Holder<CatVariant>>)RegistryFixedCodec.create(Registries.CAT_VARIANT);
/*    */   
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CatVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.CAT_VARIANT);
/*    */   
/*    */   private CatVariant(ClientAsset.ResourceTexture adultAssetInfo, ClientAsset.ResourceTexture babyAssetInfo) {
/* 40 */     this(adultAssetInfo, babyAssetInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */   
/*    */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 45 */     return this.spawnConditions.selectors();
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture assetInfo(boolean isBaby) {
/* 49 */     return isBaby ? this.babyAssetInfo : this.adultAssetInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\CatVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */