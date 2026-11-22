/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
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
/*    */ public final class FrogVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ClientAsset.ResourceTexture assetInfo;
/*    */   
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/*    */   public static final Codec<FrogVariant> DIRECT_CODEC;
/*    */   
/*    */   public static final Codec<FrogVariant> NETWORK_CODEC;
/*    */   
/*    */   public FrogVariant(ClientAsset.ResourceTexture assetInfo, SpawnPrioritySelectors spawnConditions) {
/* 19 */     this.assetInfo = assetInfo;
/* 19 */     this.spawnConditions = spawnConditions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture assetInfo() {
/* 19 */     return this.assetInfo;
/*    */   }
/*    */   
/*    */   public SpawnPrioritySelectors spawnConditions() {
/* 19 */     return this.spawnConditions;
/*    */   }
/*    */   
/*    */   static {
/* 23 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(FrogVariant::assetInfo), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(FrogVariant::spawnConditions)).apply((Applicative)i, FrogVariant::new));
/* 29 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(FrogVariant::assetInfo)).apply((Applicative)i, FrogVariant::new));
/*    */   }
/*    */   
/* 33 */   public static final Codec<Holder<FrogVariant>> CODEC = (Codec<Holder<FrogVariant>>)RegistryFixedCodec.create(Registries.FROG_VARIANT);
/*    */   
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FrogVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FROG_VARIANT);
/*    */   
/*    */   private FrogVariant(ClientAsset.ResourceTexture assetInfo) {
/* 37 */     this(assetInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */   
/*    */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 42 */     return this.spawnConditions.selectors();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\FrogVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */