/*    */ package net.minecraft.world.entity.animal.wolf;
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
/*    */ public final class WolfVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final AssetInfo adultInfo;
/*    */   
/*    */   private final AssetInfo babyInfo;
/*    */   
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/*    */   public static final Codec<WolfVariant> DIRECT_CODEC;
/*    */   
/*    */   public static final Codec<WolfVariant> NETWORK_CODEC;
/*    */   
/*    */   public WolfVariant(AssetInfo adultInfo, AssetInfo babyInfo, SpawnPrioritySelectors spawnConditions) {
/* 19 */     this.adultInfo = adultInfo;
/* 19 */     this.babyInfo = babyInfo;
/* 19 */     this.spawnConditions = spawnConditions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public AssetInfo adultInfo() {
/* 19 */     return this.adultInfo;
/*    */   }
/*    */   
/*    */   public AssetInfo babyInfo() {
/* 19 */     return this.babyInfo;
/*    */   }
/*    */   
/*    */   public SpawnPrioritySelectors spawnConditions() {
/* 19 */     return this.spawnConditions;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::adultInfo), (App)AssetInfo.CODEC.fieldOf("baby_assets").forGetter(WolfVariant::babyInfo), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(WolfVariant::spawnConditions)).apply((Applicative)i, WolfVariant::new));
/* 31 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::adultInfo), (App)AssetInfo.CODEC.fieldOf("baby_assets").forGetter(WolfVariant::babyInfo)).apply((Applicative)i, WolfVariant::new));
/*    */   }
/*    */   
/* 36 */   public static final Codec<Holder<WolfVariant>> CODEC = (Codec<Holder<WolfVariant>>)RegistryFixedCodec.create(Registries.WOLF_VARIANT);
/*    */   
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_VARIANT);
/*    */   
/*    */   private WolfVariant(AssetInfo adultInfo, AssetInfo babyInfo) {
/* 40 */     this(adultInfo, babyInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */   
/*    */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 45 */     return this.spawnConditions.selectors();
/*    */   }
/*    */   
/*    */   public static final class AssetInfo extends Record {
/*    */     private final ClientAsset.ResourceTexture wild;
/*    */     
/*    */     private final ClientAsset.ResourceTexture tame;
/*    */     
/*    */     private final ClientAsset.ResourceTexture angry;
/*    */     
/*    */     public static final Codec<AssetInfo> CODEC;
/*    */     
/*    */     public AssetInfo(ClientAsset.ResourceTexture wild, ClientAsset.ResourceTexture tame, ClientAsset.ResourceTexture angry) {
/* 48 */       this.wild = wild;
/* 48 */       this.tame = tame;
/* 48 */       this.angry = angry;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ClientAsset.ResourceTexture wild() {
/* 48 */       return this.wild;
/*    */     }
/*    */     
/*    */     public ClientAsset.ResourceTexture tame() {
/* 48 */       return this.tame;
/*    */     }
/*    */     
/*    */     public ClientAsset.ResourceTexture angry() {
/* 48 */       return this.angry;
/*    */     }
/*    */     
/*    */     static {
/* 53 */       CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ClientAsset.ResourceTexture.CODEC.fieldOf("wild").forGetter(AssetInfo::wild), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("tame").forGetter(AssetInfo::tame), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)).apply((Applicative)instance, AssetInfo::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\WolfVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */