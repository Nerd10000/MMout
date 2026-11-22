/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.TriState;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ 
/*    */ public interface AttributeTypes {
/* 18 */   public static final AttributeType<Boolean> BOOLEAN = register("boolean", AttributeType.ofNotInterpolated((Codec<Boolean>)Codec.BOOL, AttributeModifier.BOOLEAN_LIBRARY));
/*    */   
/* 19 */   public static final AttributeType<TriState> TRI_STATE = register("tri_state", AttributeType.ofNotInterpolated(TriState.CODEC));
/*    */   
/*    */   public static final AttributeType<Float> FLOAT;
/*    */   
/*    */   public static final AttributeType<Float> ANGLE_DEGREES;
/*    */   
/*    */   static {
/* 20 */     FLOAT = register("float", AttributeType.ofInterpolated((Codec<Float>)Codec.FLOAT, AttributeModifier.FLOAT_LIBRARY, 
/*    */           
/* 23 */           LerpFunction.ofFloat(), 
/* 24 */           LerpFunction.ofFloat(), value -> value.floatValue()));
/* 27 */     ANGLE_DEGREES = register("angle_degrees", AttributeType.ofInterpolated((Codec<Float>)Codec.FLOAT, AttributeModifier.FLOAT_LIBRARY, 
/*    */           
/* 30 */           LerpFunction.ofFloat(), 
/* 31 */           LerpFunction.ofDegrees(90.0F), value -> value.floatValue()));
/*    */   }
/*    */   
/* 34 */   public static final AttributeType<Integer> RGB_COLOR = register("rgb_color", AttributeType.ofInterpolated(ExtraCodecs.STRING_RGB_COLOR, AttributeModifier.RGB_COLOR_LIBRARY, 
/*    */         
/* 37 */         LerpFunction.ofColor()));
/*    */   
/* 39 */   public static final AttributeType<Integer> ARGB_COLOR = register("argb_color", AttributeType.ofInterpolated(ExtraCodecs.STRING_ARGB_COLOR, AttributeModifier.ARGB_COLOR_LIBRARY, 
/*    */         
/* 42 */         LerpFunction.ofColor()));
/*    */   
/*    */   public static final AttributeType<Integer> INTEGER;
/*    */   
/*    */   static {
/* 44 */     INTEGER = register("integer", AttributeType.ofInterpolated((Codec<Integer>)Codec.INT, AttributeModifier.INTEGER_LIBRARY, 
/*    */           
/* 47 */           LerpFunction.ofInteger(), 
/* 48 */           LerpFunction.ofInteger(), value -> value.intValue()));
/*    */   }
/*    */   
/* 52 */   public static final AttributeType<MoonPhase> MOON_PHASE = register("moon_phase", AttributeType.ofNotInterpolated(MoonPhase.CODEC));
/*    */   
/* 53 */   public static final AttributeType<Activity> ACTIVITY = register("activity", AttributeType.ofNotInterpolated(BuiltInRegistries.ACTIVITY.byNameCodec()));
/*    */   
/* 54 */   public static final AttributeType<BedRule> BED_RULE = register("bed_rule", AttributeType.ofNotInterpolated(BedRule.CODEC));
/*    */   
/* 55 */   public static final AttributeType<ParticleOptions> PARTICLE = register("particle", AttributeType.ofNotInterpolated(ParticleTypes.CODEC));
/*    */   
/* 56 */   public static final AttributeType<List<AmbientParticle>> AMBIENT_PARTICLES = register("ambient_particles", AttributeType.ofNotInterpolated(AmbientParticle.CODEC.listOf()));
/*    */   
/* 57 */   public static final AttributeType<BackgroundMusic> BACKGROUND_MUSIC = register("background_music", AttributeType.ofNotInterpolated(BackgroundMusic.CODEC));
/*    */   
/* 58 */   public static final AttributeType<AmbientSounds> AMBIENT_SOUNDS = register("ambient_sounds", AttributeType.ofNotInterpolated(AmbientSounds.CODEC));
/*    */   
/* 60 */   public static final Codec<AttributeType<?>> CODEC = BuiltInRegistries.ATTRIBUTE_TYPE.byNameCodec();
/*    */   
/*    */   static AttributeType<?> bootstrap(Registry<AttributeType<?>> registry) {
/* 63 */     return BOOLEAN;
/*    */   }
/*    */   
/*    */   static <Value> AttributeType<Value> register(String name, AttributeType<Value> type) {
/* 67 */     Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Identifier.withDefaultNamespace(name), type);
/* 68 */     return type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AttributeTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */