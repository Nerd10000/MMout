/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MobEffectInstance implements Comparable<MobEffectInstance> {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int INFINITE_DURATION = -1;
/*     */   
/*     */   public static final int MIN_AMPLIFIER = 0;
/*     */   
/*     */   public static final int MAX_AMPLIFIER = 255;
/*     */   
/*     */   public static final Codec<MobEffectInstance> CODEC;
/*     */   
/*     */   static {
/*  34 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("id").forGetter(MobEffectInstance::getEffect), (App)Details.MAP_CODEC.forGetter(MobEffectInstance::asDetails)).apply((Applicative)i, MobEffectInstance::new));
/*     */   }
/*     */   
/*  39 */   public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC, MobEffectInstance::getEffect, Details.STREAM_CODEC, MobEffectInstance::asDetails, MobEffectInstance::new);
/*     */   
/*     */   private final Holder<MobEffect> effect;
/*     */   
/*     */   private int duration;
/*     */   
/*     */   private int amplifier;
/*     */   
/*     */   private boolean ambient;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   private boolean showIcon;
/*     */   
/*     */   private MobEffectInstance hiddenEffect;
/*     */   
/*  54 */   private final BlendState blendState = new BlendState();
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect) {
/*  57 */     this(effect, 0, 0);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration) {
/*  61 */     this(effect, duration, 0);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier) {
/*  65 */     this(effect, duration, amplifier, false, true);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible) {
/*  69 */     this(effect, duration, amplifier, ambient, visible, visible);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
/*  73 */     this(effect, duration, amplifier, ambient, visible, showIcon, null);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, MobEffectInstance hiddenEffect) {
/*  77 */     this.effect = effect;
/*  78 */     this.duration = duration;
/*  79 */     this.amplifier = Mth.clamp(amplifier, 0, 255);
/*  80 */     this.ambient = ambient;
/*  81 */     this.visible = visible;
/*  82 */     this.showIcon = showIcon;
/*  83 */     this.hiddenEffect = hiddenEffect;
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffectInstance copy) {
/*  87 */     this.effect = copy.effect;
/*  88 */     setDetailsFrom(copy);
/*     */   }
/*     */   
/*     */   private MobEffectInstance(Holder<MobEffect> effect, Details details) {
/*  92 */     this(effect, details
/*     */         
/*  94 */         .duration(), details
/*  95 */         .amplifier(), details
/*  96 */         .ambient(), details
/*  97 */         .showParticles(), details
/*  98 */         .showIcon(), details
/*  99 */         .hiddenEffect().<MobEffectInstance>map(hidden -> new MobEffectInstance(effect, hidden)).orElse(null));
/*     */   }
/*     */   
/*     */   private Details asDetails() {
/* 104 */     return new Details(getAmplifier(), getDuration(), isAmbient(), isVisible(), showIcon(), Optional.<MobEffectInstance>ofNullable(this.hiddenEffect).map(MobEffectInstance::asDetails));
/*     */   }
/*     */   
/*     */   public float getBlendFactor(LivingEntity livingEntity, float partialTickTime) {
/* 108 */     return this.blendState.getFactor(livingEntity, partialTickTime);
/*     */   }
/*     */   
/*     */   public ParticleOptions getParticleOptions() {
/* 112 */     return ((MobEffect)this.effect.value()).createParticleOptions(this);
/*     */   }
/*     */   
/*     */   private void setDetailsFrom(MobEffectInstance copy) {
/* 116 */     this.duration = copy.duration;
/* 117 */     this.amplifier = copy.amplifier;
/* 118 */     this.ambient = copy.ambient;
/* 119 */     this.visible = copy.visible;
/* 120 */     this.showIcon = copy.showIcon;
/*     */   }
/*     */   
/*     */   public boolean update(MobEffectInstance takeOver) {
/* 124 */     if (!this.effect.equals(takeOver.effect))
/* 125 */       LOGGER.warn("This method should only be called for matching effects!"); 
/* 128 */     boolean changed = false;
/* 129 */     if (takeOver.amplifier > this.amplifier) {
/* 130 */       if (takeOver.isShorterDurationThan(this)) {
/* 131 */         MobEffectInstance prevHiddenEffect = this.hiddenEffect;
/* 132 */         this.hiddenEffect = new MobEffectInstance(this);
/* 133 */         this.hiddenEffect.hiddenEffect = prevHiddenEffect;
/*     */       } 
/* 135 */       this.amplifier = takeOver.amplifier;
/* 136 */       this.duration = takeOver.duration;
/* 137 */       changed = true;
/* 138 */     } else if (isShorterDurationThan(takeOver)) {
/* 139 */       if (takeOver.amplifier == this.amplifier) {
/* 140 */         this.duration = takeOver.duration;
/* 141 */         changed = true;
/* 143 */       } else if (this.hiddenEffect == null) {
/* 144 */         this.hiddenEffect = new MobEffectInstance(takeOver);
/*     */       } else {
/* 146 */         this.hiddenEffect.update(takeOver);
/*     */       } 
/*     */     } 
/* 150 */     if ((!takeOver.ambient && this.ambient) || changed) {
/* 151 */       this.ambient = takeOver.ambient;
/* 152 */       changed = true;
/*     */     } 
/* 154 */     if (takeOver.visible != this.visible) {
/* 155 */       this.visible = takeOver.visible;
/* 156 */       changed = true;
/*     */     } 
/* 158 */     if (takeOver.showIcon != this.showIcon) {
/* 159 */       this.showIcon = takeOver.showIcon;
/* 160 */       changed = true;
/*     */     } 
/* 163 */     return changed;
/*     */   }
/*     */   
/*     */   private boolean isShorterDurationThan(MobEffectInstance other) {
/* 167 */     return (!isInfiniteDuration() && (this.duration < other.duration || other.isInfiniteDuration()));
/*     */   }
/*     */   
/*     */   public boolean isInfiniteDuration() {
/* 171 */     return (this.duration == -1);
/*     */   }
/*     */   
/*     */   public boolean endsWithin(int ticks) {
/* 175 */     return (!isInfiniteDuration() && this.duration <= ticks);
/*     */   }
/*     */   
/*     */   public MobEffectInstance withScaledDuration(float scale) {
/* 179 */     MobEffectInstance copy = new MobEffectInstance(this);
/* 180 */     copy.duration = copy.mapDuration(duration -> Math.max(Mth.floor(duration * scale), 1));
/* 181 */     return copy;
/*     */   }
/*     */   
/*     */   public int mapDuration(Int2IntFunction mapper) {
/* 185 */     if (isInfiniteDuration() || this.duration == 0)
/* 186 */       return this.duration; 
/* 188 */     return mapper.applyAsInt(this.duration);
/*     */   }
/*     */   
/*     */   public Holder<MobEffect> getEffect() {
/* 192 */     return this.effect;
/*     */   }
/*     */   
/*     */   public int getDuration() {
/* 196 */     return this.duration;
/*     */   }
/*     */   
/*     */   public int getAmplifier() {
/* 200 */     return this.amplifier;
/*     */   }
/*     */   
/*     */   public boolean isAmbient() {
/* 204 */     return this.ambient;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 208 */     return this.visible;
/*     */   }
/*     */   
/*     */   public boolean showIcon() {
/* 212 */     return this.showIcon;
/*     */   }
/*     */   
/*     */   public boolean tickServer(ServerLevel serverLevel, LivingEntity target, Runnable onEffectUpdate) {
/* 219 */     if (!hasRemainingDuration())
/* 220 */       return false; 
/* 222 */     int tickCount = isInfiniteDuration() ? target.tickCount : this.duration;
/* 223 */     if (((MobEffect)this.effect.value()).shouldApplyEffectTickThisTick(tickCount, this.amplifier) && !((MobEffect)this.effect.value()).applyEffectTick(serverLevel, target, this.amplifier))
/* 224 */       return false; 
/* 226 */     tickDownDuration();
/* 227 */     if (downgradeToHiddenEffect())
/* 228 */       onEffectUpdate.run(); 
/* 230 */     return hasRemainingDuration();
/*     */   }
/*     */   
/*     */   public void tickClient() {
/* 234 */     if (hasRemainingDuration()) {
/* 235 */       tickDownDuration();
/* 236 */       downgradeToHiddenEffect();
/*     */     } 
/* 238 */     this.blendState.tick(this);
/*     */   }
/*     */   
/*     */   private boolean hasRemainingDuration() {
/* 242 */     return (isInfiniteDuration() || this.duration > 0);
/*     */   }
/*     */   
/*     */   private void tickDownDuration() {
/* 246 */     if (this.hiddenEffect != null)
/* 247 */       this.hiddenEffect.tickDownDuration(); 
/* 249 */     this.duration = mapDuration(d -> d - 1);
/*     */   }
/*     */   
/*     */   private boolean downgradeToHiddenEffect() {
/* 253 */     if (this.duration == 0 && this.hiddenEffect != null) {
/* 254 */       setDetailsFrom(this.hiddenEffect);
/* 255 */       this.hiddenEffect = this.hiddenEffect.hiddenEffect;
/* 256 */       return true;
/*     */     } 
/* 258 */     return false;
/*     */   }
/*     */   
/*     */   public void onEffectStarted(LivingEntity mob) {
/* 262 */     ((MobEffect)this.effect.value()).onEffectStarted(mob, this.amplifier);
/*     */   }
/*     */   
/*     */   public void onMobRemoved(ServerLevel level, LivingEntity mob, Entity.RemovalReason reason) {
/* 266 */     ((MobEffect)this.effect.value()).onMobRemoved(level, mob, this.amplifier, reason);
/*     */   }
/*     */   
/*     */   public void onMobHurt(ServerLevel level, LivingEntity mob, DamageSource source, float damage) {
/* 270 */     ((MobEffect)this.effect.value()).onMobHurt(level, mob, this.amplifier, source, damage);
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 274 */     return ((MobEffect)this.effect.value()).getDescriptionId();
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     String result;
/* 280 */     if (this.amplifier > 0) {
/* 281 */       result = getDescriptionId() + " x " + getDescriptionId() + ", Duration: " + this.amplifier + 1;
/*     */     } else {
/* 283 */       result = getDescriptionId() + ", Duration: " + getDescriptionId();
/*     */     } 
/* 285 */     if (!this.visible)
/* 286 */       result = result + ", Particles: false"; 
/* 288 */     if (!this.showIcon)
/* 289 */       result = result + ", Show Icon: false"; 
/* 292 */     return result;
/*     */   }
/*     */   
/*     */   private String describeDuration() {
/* 296 */     if (isInfiniteDuration())
/* 297 */       return "infinite"; 
/* 299 */     return Integer.toString(this.duration);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 304 */     if (this == o)
/* 305 */       return true; 
/* 307 */     if (o instanceof MobEffectInstance) {
/* 307 */       MobEffectInstance that = (MobEffectInstance)o;
/* 308 */       return (this.duration == that.duration && this.amplifier == that.amplifier && this.ambient == that.ambient && this.visible == that.visible && this.showIcon == that.showIcon && this.effect.equals(that.effect));
/*     */     } 
/* 310 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 315 */     int result = this.effect.hashCode();
/* 316 */     result = 31 * result + this.duration;
/* 317 */     result = 31 * result + this.amplifier;
/* 318 */     result = 31 * result + (this.ambient ? 1 : 0);
/* 319 */     result = 31 * result + (this.visible ? 1 : 0);
/* 320 */     result = 31 * result + (this.showIcon ? 1 : 0);
/* 321 */     return result;
/*     */   }
/*     */   
/*     */   public int compareTo(MobEffectInstance o) {
/* 326 */     int updateCutOff = 32147;
/* 327 */     if ((getDuration() > 32147 && o.getDuration() > 32147) || (isAmbient() && o.isAmbient()))
/* 329 */       return ComparisonChain.start()
/* 330 */         .compare(Boolean.valueOf(isAmbient()), Boolean.valueOf(o.isAmbient()))
/* 331 */         .compare(((MobEffect)getEffect().value()).getColor(), ((MobEffect)o.getEffect().value()).getColor())
/* 332 */         .result(); 
/* 334 */     return ComparisonChain.start()
/* 335 */       .compareFalseFirst(isAmbient(), o.isAmbient())
/* 336 */       .compareFalseFirst(isInfiniteDuration(), o.isInfiniteDuration())
/* 337 */       .compare(getDuration(), o.getDuration())
/* 338 */       .compare(((MobEffect)getEffect().value()).getColor(), ((MobEffect)o.getEffect().value()).getColor())
/* 339 */       .result();
/*     */   }
/*     */   
/*     */   public void onEffectAdded(LivingEntity livingEntity) {
/* 343 */     ((MobEffect)this.effect.value()).onEffectAdded(livingEntity, this.amplifier);
/*     */   }
/*     */   
/*     */   private static final class Details extends Record {
/*     */     private final int amplifier;
/*     */     
/*     */     private final int duration;
/*     */     
/*     */     private final boolean ambient;
/*     */     
/*     */     private final boolean showParticles;
/*     */     
/*     */     private final boolean showIcon;
/*     */     
/*     */     private final Optional<Details> hiddenEffect;
/*     */     
/*     */     private Details(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<Details> hiddenEffect) {
/* 346 */       this.amplifier = amplifier;
/* 346 */       this.duration = duration;
/* 346 */       this.ambient = ambient;
/* 346 */       this.showParticles = showParticles;
/* 346 */       this.showIcon = showIcon;
/* 346 */       this.hiddenEffect = hiddenEffect;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #346	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #346	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffectInstance$Details;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #346	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int amplifier() {
/* 346 */       return this.amplifier;
/*     */     }
/*     */     
/*     */     public int duration() {
/* 346 */       return this.duration;
/*     */     }
/*     */     
/*     */     public boolean ambient() {
/* 346 */       return this.ambient;
/*     */     }
/*     */     
/*     */     public boolean showParticles() {
/* 346 */       return this.showParticles;
/*     */     }
/*     */     
/*     */     public boolean showIcon() {
/* 346 */       return this.showIcon;
/*     */     }
/*     */     
/*     */     public Optional<Details> hiddenEffect() {
/* 346 */       return this.hiddenEffect;
/*     */     }
/*     */     
/* 347 */     public static final MapCodec<Details> MAP_CODEC = MapCodec.recursive("MobEffectInstance.Details", codec -> RecordCodecBuilder.mapCodec(()));
/*     */     
/*     */     public static final StreamCodec<ByteBuf, Details> STREAM_CODEC;
/*     */     
/*     */     static {
/* 357 */       STREAM_CODEC = StreamCodec.recursive(subCodec -> StreamCodec.composite(ByteBufCodecs.VAR_INT, Details::amplifier, ByteBufCodecs.VAR_INT, Details::duration, ByteBufCodecs.BOOL, Details::ambient, ByteBufCodecs.BOOL, Details::showParticles, ByteBufCodecs.BOOL, Details::showIcon, subCodec.apply(ByteBufCodecs::optional), Details::hiddenEffect, Details::new));
/*     */     }
/*     */     
/*     */     private static Details create(int amplifier, int duration, boolean ambient, boolean showParticles, Optional<Boolean> showIcon, Optional<Details> hiddenEffect) {
/* 368 */       return new Details(amplifier, duration, ambient, showParticles, ((Boolean)showIcon.orElse(Boolean.valueOf(showParticles))).booleanValue(), hiddenEffect);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean is(Holder<MobEffect> effect) {
/* 373 */     return this.effect.equals(effect);
/*     */   }
/*     */   
/*     */   public void copyBlendState(MobEffectInstance instance) {
/* 377 */     this.blendState.copyFrom(instance.blendState);
/*     */   }
/*     */   
/*     */   public void skipBlending() {
/* 381 */     this.blendState.setImmediate(this);
/*     */   }
/*     */   
/*     */   private static class BlendState {
/*     */     private float factor;
/*     */     
/*     */     private float factorPreviousFrame;
/*     */     
/*     */     public void setImmediate(MobEffectInstance instance) {
/* 389 */       this.factor = hasEffect(instance) ? 1.0F : 0.0F;
/* 390 */       this.factorPreviousFrame = this.factor;
/*     */     }
/*     */     
/*     */     public void copyFrom(BlendState other) {
/* 394 */       this.factor = other.factor;
/* 395 */       this.factorPreviousFrame = other.factorPreviousFrame;
/*     */     }
/*     */     
/*     */     public void tick(MobEffectInstance instance) {
/* 399 */       this.factorPreviousFrame = this.factor;
/* 401 */       boolean hasEffect = hasEffect(instance);
/* 402 */       float target = hasEffect ? 1.0F : 0.0F;
/* 403 */       if (this.factor == target)
/*     */         return; 
/* 407 */       MobEffect effect = (MobEffect)instance.getEffect().value();
/* 408 */       int blendDuration = hasEffect ? effect.getBlendInDurationTicks() : effect.getBlendOutDurationTicks();
/* 409 */       if (blendDuration == 0) {
/* 410 */         this.factor = target;
/*     */       } else {
/* 412 */         float maxDeltaPerTick = 1.0F / blendDuration;
/* 413 */         this.factor += Mth.clamp(target - this.factor, -maxDeltaPerTick, maxDeltaPerTick);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static boolean hasEffect(MobEffectInstance instance) {
/* 418 */       return !instance.endsWithin(((MobEffect)instance.getEffect().value()).getBlendOutAdvanceTicks());
/*     */     }
/*     */     
/*     */     public float getFactor(LivingEntity livingEntity, float partialTickTime) {
/* 422 */       if (livingEntity.isRemoved())
/* 426 */         this.factorPreviousFrame = this.factor; 
/* 429 */       return Mth.lerp(partialTickTime, this.factorPreviousFrame, this.factor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\MobEffectInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */