/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ 
/*    */ public final class DamageType extends Record {
/*    */   private final String msgId;
/*    */   
/*    */   private final DamageScaling scaling;
/*    */   
/*    */   private final float exhaustion;
/*    */   
/*    */   private final DamageEffects effects;
/*    */   
/*    */   private final DeathMessageType deathMessageType;
/*    */   
/*    */   public static final Codec<DamageType> DIRECT_CODEC;
/*    */   
/*    */   public DamageType(String msgId, DamageScaling scaling, float exhaustion, DamageEffects effects, DeathMessageType deathMessageType) {
/* 12 */     this.msgId = msgId;
/* 12 */     this.scaling = scaling;
/* 12 */     this.exhaustion = exhaustion;
/* 12 */     this.effects = effects;
/* 12 */     this.deathMessageType = deathMessageType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/DamageType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/DamageType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/DamageType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/damagesource/DamageType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String msgId() {
/* 12 */     return this.msgId;
/*    */   }
/*    */   
/*    */   public DamageScaling scaling() {
/* 12 */     return this.scaling;
/*    */   }
/*    */   
/*    */   public float exhaustion() {
/* 12 */     return this.exhaustion;
/*    */   }
/*    */   
/*    */   public DamageEffects effects() {
/* 12 */     return this.effects;
/*    */   }
/*    */   
/*    */   public DeathMessageType deathMessageType() {
/* 12 */     return this.deathMessageType;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("message_id").forGetter(DamageType::msgId), (App)DamageScaling.CODEC.fieldOf("scaling").forGetter(DamageType::scaling), (App)Codec.FLOAT.fieldOf("exhaustion").forGetter(DamageType::exhaustion), (App)DamageEffects.CODEC.optionalFieldOf("effects", DamageEffects.HURT).forGetter(DamageType::effects), (App)DeathMessageType.CODEC.optionalFieldOf("death_message_type", DeathMessageType.DEFAULT).forGetter(DamageType::deathMessageType)).apply((Applicative)i, DamageType::new));
/*    */   }
/*    */   
/* 27 */   public static final Codec<Holder<DamageType>> CODEC = (Codec<Holder<DamageType>>)RegistryFixedCodec.create(Registries.DAMAGE_TYPE);
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DamageType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DAMAGE_TYPE);
/*    */   
/*    */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion) {
/* 31 */     this(msgdId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion, DamageEffects effects) {
/* 35 */     this(msgdId, scaling, exhaustion, effects, DeathMessageType.DEFAULT);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, float exhaustion, DamageEffects effects) {
/* 39 */     this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, effects);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, float exhaustion) {
/* 43 */     this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\damagesource\DamageType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */