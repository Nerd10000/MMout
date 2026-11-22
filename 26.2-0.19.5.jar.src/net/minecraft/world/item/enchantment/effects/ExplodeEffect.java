/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function12;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ExplodeEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final boolean attributeToUser;
/*    */   
/*    */   private final Optional<Holder<DamageType>> damageType;
/*    */   
/*    */   private final Optional<LevelBasedValue> knockbackMultiplier;
/*    */   
/*    */   private final Optional<HolderSet<Block>> immuneBlocks;
/*    */   
/*    */   private final Vec3 offset;
/*    */   
/*    */   private final LevelBasedValue radius;
/*    */   
/*    */   private final boolean createFire;
/*    */   
/*    */   private final Level.ExplosionInteraction blockInteraction;
/*    */   
/*    */   private final ParticleOptions smallParticle;
/*    */   
/*    */   private final ParticleOptions largeParticle;
/*    */   
/*    */   private final WeightedList<ExplosionParticleInfo> blockParticles;
/*    */   
/*    */   private final Holder<SoundEvent> sound;
/*    */   
/*    */   public static final MapCodec<ExplodeEffect> CODEC;
/*    */   
/*    */   public ExplodeEffect(boolean attributeToUser, Optional<Holder<DamageType>> damageType, Optional<LevelBasedValue> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) {
/* 29 */     this.attributeToUser = attributeToUser;
/* 29 */     this.damageType = damageType;
/* 29 */     this.knockbackMultiplier = knockbackMultiplier;
/* 29 */     this.immuneBlocks = immuneBlocks;
/* 29 */     this.offset = offset;
/* 29 */     this.radius = radius;
/* 29 */     this.createFire = createFire;
/* 29 */     this.blockInteraction = blockInteraction;
/* 29 */     this.smallParticle = smallParticle;
/* 29 */     this.largeParticle = largeParticle;
/* 29 */     this.blockParticles = blockParticles;
/* 29 */     this.sound = sound;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean attributeToUser() {
/* 29 */     return this.attributeToUser;
/*    */   }
/*    */   
/*    */   public Optional<Holder<DamageType>> damageType() {
/* 29 */     return this.damageType;
/*    */   }
/*    */   
/*    */   public Optional<LevelBasedValue> knockbackMultiplier() {
/* 29 */     return this.knockbackMultiplier;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<Block>> immuneBlocks() {
/* 29 */     return this.immuneBlocks;
/*    */   }
/*    */   
/*    */   public Vec3 offset() {
/* 29 */     return this.offset;
/*    */   }
/*    */   
/*    */   public LevelBasedValue radius() {
/* 29 */     return this.radius;
/*    */   }
/*    */   
/*    */   public boolean createFire() {
/* 29 */     return this.createFire;
/*    */   }
/*    */   
/*    */   public Level.ExplosionInteraction blockInteraction() {
/* 29 */     return this.blockInteraction;
/*    */   }
/*    */   
/*    */   public ParticleOptions smallParticle() {
/* 29 */     return this.smallParticle;
/*    */   }
/*    */   
/*    */   public ParticleOptions largeParticle() {
/* 29 */     return this.largeParticle;
/*    */   }
/*    */   
/*    */   public WeightedList<ExplosionParticleInfo> blockParticles() {
/* 29 */     return this.blockParticles;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> sound() {
/* 29 */     return this.sound;
/*    */   }
/*    */   
/*    */   static {
/* 43 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("attribute_to_user", Boolean.valueOf(false)).forGetter(ExplodeEffect::attributeToUser), (App)DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType), (App)LevelBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks), (App)Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeEffect::offset), (App)LevelBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeEffect::radius), (App)Codec.BOOL.optionalFieldOf("create_fire", Boolean.valueOf(false)).forGetter(ExplodeEffect::createFire), (App)Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction), (App)ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle), (App)ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle), (App)WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles), (App)SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeEffect::sound)).apply((Applicative)i, ExplodeEffect::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 60 */     Vec3 pos = position.add(this.offset);
/* 61 */     serverLevel.explode(
/* 62 */         this.attributeToUser ? entity : null, 
/* 63 */         getDamageSource(entity, pos), (ExplosionDamageCalculator)new SimpleExplosionDamageCalculator((this.blockInteraction != Level.ExplosionInteraction.NONE), this.damageType
/*    */           
/* 66 */           .isPresent(), this.knockbackMultiplier
/* 67 */           .map(value -> Float.valueOf(value.calculate(enchantmentLevel))), this.immuneBlocks), pos
/*    */         
/* 70 */         .x(), pos
/* 71 */         .y(), pos
/* 72 */         .z(), 
/* 73 */         Math.max(this.radius.calculate(enchantmentLevel), 0.0F), this.createFire, this.blockInteraction, this.smallParticle, this.largeParticle, this.blockParticles, this.sound);
/*    */   }
/*    */   
/*    */   private DamageSource getDamageSource(Entity entity, Vec3 position) {
/* 83 */     if (this.damageType.isEmpty())
/* 84 */       return null; 
/* 86 */     if (this.attributeToUser)
/* 87 */       return new DamageSource(this.damageType.get(), entity); 
/* 89 */     return new DamageSource(this.damageType.get(), position);
/*    */   }
/*    */   
/*    */   public MapCodec<ExplodeEffect> codec() {
/* 94 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ExplodeEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */