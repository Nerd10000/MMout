/*     */ package net.minecraft.world.item.enchantment.effects;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.FloatProviders;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class SpawnParticlesEffect extends Record implements EnchantmentEntityEffect {
/*     */   private final ParticleOptions particle;
/*     */   
/*     */   private final PositionSource horizontalPosition;
/*     */   
/*     */   private final PositionSource verticalPosition;
/*     */   
/*     */   private final VelocitySource horizontalVelocity;
/*     */   
/*     */   private final VelocitySource verticalVelocity;
/*     */   
/*     */   private final FloatProvider speed;
/*     */   
/*     */   public static final MapCodec<SpawnParticlesEffect> CODEC;
/*     */   
/*     */   public SpawnParticlesEffect(ParticleOptions particle, PositionSource horizontalPosition, PositionSource verticalPosition, VelocitySource horizontalVelocity, VelocitySource verticalVelocity, FloatProvider speed) {
/*  20 */     this.particle = particle;
/*  20 */     this.horizontalPosition = horizontalPosition;
/*  20 */     this.verticalPosition = verticalPosition;
/*  20 */     this.horizontalVelocity = horizontalVelocity;
/*  20 */     this.verticalVelocity = verticalVelocity;
/*  20 */     this.speed = speed;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ParticleOptions particle() {
/*  20 */     return this.particle;
/*     */   }
/*     */   
/*     */   public PositionSource horizontalPosition() {
/*  20 */     return this.horizontalPosition;
/*     */   }
/*     */   
/*     */   public PositionSource verticalPosition() {
/*  20 */     return this.verticalPosition;
/*     */   }
/*     */   
/*     */   public VelocitySource horizontalVelocity() {
/*  20 */     return this.horizontalVelocity;
/*     */   }
/*     */   
/*     */   public VelocitySource verticalVelocity() {
/*  20 */     return this.verticalVelocity;
/*     */   }
/*     */   
/*     */   public FloatProvider speed() {
/*  20 */     return this.speed;
/*     */   }
/*     */   
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle), (App)PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition), (App)PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition), (App)VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity), (App)VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity), (App)FloatProviders.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)).apply((Applicative)i, SpawnParticlesEffect::new));
/*     */   }
/*     */   
/*     */   public enum PositionSourceType implements StringRepresentable {
/*     */     ENTITY_POSITION, BOUNDING_BOX;
/*     */     
/*     */     public static final Codec<PositionSourceType> CODEC;
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final CoordinateSource source;
/*     */     
/*     */     static {
/*  38 */       ENTITY_POSITION = new PositionSourceType("ENTITY_POSITION", 0, "entity_position", (pos, center, bbSpan, random) -> pos);
/*  39 */       BOUNDING_BOX = new PositionSourceType("BOUNDING_BOX", 1, "in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5D) * bbSpan);
/*     */     }
/*     */     
/*     */     static {
/*  42 */       CODEC = (Codec<PositionSourceType>)StringRepresentable.fromEnum(PositionSourceType::values);
/*     */     }
/*     */     
/*     */     PositionSourceType(String id, CoordinateSource source) {
/*  53 */       this.id = id;
/*  54 */       this.source = source;
/*     */     }
/*     */     
/*     */     public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/*  58 */       return this.source.getCoordinate(position, center, boundingBoxSpan, random);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  63 */       return this.id;
/*     */     }
/*     */     
/*     */     @FunctionalInterface
/*     */     private static interface CoordinateSource {
/*     */       double getCoordinate(double param2Double1, double param2Double2, float param2Float, RandomSource param2RandomSource);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class PositionSource extends Record {
/*     */     private final SpawnParticlesEffect.PositionSourceType type;
/*     */     
/*     */     private final float offset;
/*     */     
/*     */     private final float scale;
/*     */     
/*     */     public static final MapCodec<PositionSource> CODEC;
/*     */     
/*     */     public PositionSource(SpawnParticlesEffect.PositionSourceType type, float offset, float scale) {
/*  67 */       this.type = type;
/*  67 */       this.offset = offset;
/*  67 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SpawnParticlesEffect.PositionSourceType type() {
/*  67 */       return this.type;
/*     */     }
/*     */     
/*     */     public float offset() {
/*  67 */       return this.offset;
/*     */     }
/*     */     
/*     */     public float scale() {
/*  67 */       return this.scale;
/*     */     }
/*     */     
/*     */     static {
/*  76 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SpawnParticlesEffect.PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type), (App)Codec.FLOAT.optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(PositionSource::offset), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(PositionSource::scale)).apply((Applicative)i, PositionSource::new)).validate(positioning -> 
/*  77 */           (positioning.type() == SpawnParticlesEffect.PositionSourceType.ENTITY_POSITION && positioning.scale() != 1.0F) ? DataResult.error(()) : DataResult.success(positioning));
/*     */     }
/*     */     
/*     */     public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/*  84 */       return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
/*     */     }
/*     */   }
/*     */   
/*     */   public static PositionSource offsetFromEntityPosition(float offset) {
/*  89 */     return new PositionSource(PositionSourceType.ENTITY_POSITION, offset, 1.0F);
/*     */   }
/*     */   
/*     */   public static PositionSource inBoundingBox() {
/*  93 */     return new PositionSource(PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public static final class VelocitySource extends Record {
/*     */     private final float movementScale;
/*     */     
/*     */     private final FloatProvider base;
/*     */     
/*     */     public static final MapCodec<VelocitySource> CODEC;
/*     */     
/*     */     public VelocitySource(float movementScale, FloatProvider base) {
/*  96 */       this.movementScale = movementScale;
/*  96 */       this.base = base;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float movementScale() {
/*  96 */       return this.movementScale;
/*     */     }
/*     */     
/*     */     public FloatProvider base() {
/*  96 */       return this.base;
/*     */     }
/*     */     
/*     */     static {
/* 100 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.optionalFieldOf("movement_scale", Float.valueOf(0.0F)).forGetter(VelocitySource::movementScale), (App)FloatProviders.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base)).apply((Applicative)i, VelocitySource::new));
/*     */     }
/*     */     
/*     */     public double getVelocity(double movement, RandomSource random) {
/* 106 */       return movement * this.movementScale + this.base.sample(random);
/*     */     }
/*     */   }
/*     */   
/*     */   public static VelocitySource movementScaled(float scale) {
/* 111 */     return new VelocitySource(scale, (FloatProvider)ConstantFloat.ZERO);
/*     */   }
/*     */   
/*     */   public static VelocitySource fixedVelocity(FloatProvider provider) {
/* 115 */     return new VelocitySource(0.0F, provider);
/*     */   }
/*     */   
/*     */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 120 */     RandomSource random = entity.getRandom();
/* 121 */     Vec3 movement = entity.getKnownMovement();
/* 122 */     float bbWidth = entity.getBbWidth();
/* 123 */     float bbHeight = entity.getBbHeight();
/* 124 */     serverLevel.sendParticles(this.particle, this.horizontalPosition
/*     */         
/* 126 */         .getCoordinate(position.x(), position.x(), bbWidth, random), this.verticalPosition
/* 127 */         .getCoordinate(position.y(), position.y() + (bbHeight / 2.0F), bbHeight, random), this.horizontalPosition
/* 128 */         .getCoordinate(position.z(), position.z(), bbWidth, random), 0, this.horizontalVelocity
/*     */         
/* 130 */         .getVelocity(movement.x(), random), this.verticalVelocity
/* 131 */         .getVelocity(movement.y(), random), this.horizontalVelocity
/* 132 */         .getVelocity(movement.z(), random), this.speed
/* 133 */         .sample(random));
/*     */   }
/*     */   
/*     */   public MapCodec<SpawnParticlesEffect> codec() {
/* 139 */     return CODEC;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface CoordinateSource {
/*     */     double getCoordinate(double param1Double1, double param1Double2, float param1Float, RandomSource param1RandomSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */