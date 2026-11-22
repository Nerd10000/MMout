/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.gameevent.PositionSource;
/*    */ 
/*    */ public class VibrationParticleOption implements ParticleOptions {
/*    */   private static final Codec<PositionSource> SAFE_POSITION_SOURCE_CODEC;
/*    */   
/*    */   public static final MapCodec<VibrationParticleOption> CODEC;
/*    */   
/*    */   static {
/* 16 */     SAFE_POSITION_SOURCE_CODEC = PositionSource.CODEC.validate(e -> (e instanceof net.minecraft.world.level.gameevent.EntityPositionSource) ? DataResult.error(()) : DataResult.success(e));
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SAFE_POSITION_SOURCE_CODEC.fieldOf("destination").forGetter(VibrationParticleOption::getDestination), (App)Codec.INT.fieldOf("arrival_in_ticks").forGetter(VibrationParticleOption::getArrivalInTicks)).apply((Applicative)i, VibrationParticleOption::new));
/*    */   }
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, VibrationParticleOption> STREAM_CODEC = StreamCodec.composite(PositionSource.STREAM_CODEC, VibrationParticleOption::getDestination, ByteBufCodecs.VAR_INT, VibrationParticleOption::getArrivalInTicks, VibrationParticleOption::new);
/*    */   
/*    */   private final PositionSource destination;
/*    */   
/*    */   private final int arrivalInTicks;
/*    */   
/*    */   public VibrationParticleOption(PositionSource destination, int arrivalInTicks) {
/* 33 */     this.destination = destination;
/* 34 */     this.arrivalInTicks = arrivalInTicks;
/*    */   }
/*    */   
/*    */   public ParticleType<VibrationParticleOption> getType() {
/* 39 */     return ParticleTypes.VIBRATION;
/*    */   }
/*    */   
/*    */   public PositionSource getDestination() {
/* 43 */     return this.destination;
/*    */   }
/*    */   
/*    */   public int getArrivalInTicks() {
/* 47 */     return this.arrivalInTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\VibrationParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */