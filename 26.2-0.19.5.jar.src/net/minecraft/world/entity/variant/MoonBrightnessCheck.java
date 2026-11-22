/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MoonBrightnessCheck extends Record implements SpawnCondition {
/*    */   private final MinMaxBounds.Doubles range;
/*    */   
/*    */   public static final MapCodec<MoonBrightnessCheck> MAP_CODEC;
/*    */   
/*    */   public MoonBrightnessCheck(MinMaxBounds.Doubles range) {
/* 11 */     this.range = range;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles range() {
/* 11 */     return this.range;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(MoonBrightnessCheck::range)).apply((Applicative)i, MoonBrightnessCheck::new));
/*    */   }
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 21 */     MoonPhase moonPhase = (MoonPhase)context.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, Vec3.atCenterOf((Vec3i)context.pos()));
/* 22 */     float moonBrightness = DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()];
/* 23 */     return this.range.matches(moonBrightness);
/*    */   }
/*    */   
/*    */   public MapCodec<MoonBrightnessCheck> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\MoonBrightnessCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */