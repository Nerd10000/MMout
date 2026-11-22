/*    */ package net.minecraft.world.entity.animal.pig;
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
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class PigVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ModelAndTexture<ModelType> modelAndTexture;
/*    */   
/*    */   private final ClientAsset.ResourceTexture babyTexture;
/*    */   
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/*    */   public static final Codec<PigVariant> DIRECT_CODEC;
/*    */   
/*    */   public static final Codec<PigVariant> NETWORK_CODEC;
/*    */   
/*    */   public PigVariant(ModelAndTexture<ModelType> modelAndTexture, ClientAsset.ResourceTexture babyTexture, SpawnPrioritySelectors spawnConditions) {
/* 21 */     this.modelAndTexture = modelAndTexture;
/* 21 */     this.babyTexture = babyTexture;
/* 21 */     this.spawnConditions = spawnConditions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/pig/PigVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/pig/PigVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/pig/PigVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ModelAndTexture<ModelType> modelAndTexture() {
/* 21 */     return this.modelAndTexture;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture babyTexture() {
/* 21 */     return this.babyTexture;
/*    */   }
/*    */   
/*    */   public SpawnPrioritySelectors spawnConditions() {
/* 21 */     return this.spawnConditions;
/*    */   }
/*    */   
/*    */   static {
/* 27 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(PigVariant::modelAndTexture), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(PigVariant::babyTexture), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(PigVariant::spawnConditions)).apply((Applicative)i, PigVariant::new));
/* 34 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(PigVariant::modelAndTexture), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(PigVariant::babyTexture)).apply((Applicative)i, PigVariant::new));
/*    */   }
/*    */   
/* 39 */   public static final Codec<Holder<PigVariant>> CODEC = (Codec<Holder<PigVariant>>)RegistryFixedCodec.create(Registries.PIG_VARIANT);
/*    */   
/* 40 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.PIG_VARIANT);
/*    */   
/*    */   private PigVariant(ModelAndTexture<ModelType> assetInfo, ClientAsset.ResourceTexture babyTexture) {
/* 43 */     this(assetInfo, babyTexture, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */   
/*    */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 48 */     return this.spawnConditions.selectors();
/*    */   }
/*    */   
/*    */   public enum ModelType implements StringRepresentable {
/* 52 */     NORMAL("normal"),
/* 53 */     COLD("cold");
/*    */     
/* 56 */     public static final Codec<ModelType> CODEC = (Codec<ModelType>)StringRepresentable.fromEnum(ModelType::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     ModelType(String name) {
/* 61 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 66 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\PigVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */