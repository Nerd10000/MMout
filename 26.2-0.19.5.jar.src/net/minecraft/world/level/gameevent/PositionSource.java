/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface PositionSource {
/* 22 */   public static final Codec<PositionSource> CODEC = BuiltInRegistries.POSITION_SOURCE_TYPE.byNameCodec().dispatch(PositionSource::getType, PositionSourceType::codec);
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, PositionSource> STREAM_CODEC = ByteBufCodecs.registry(Registries.POSITION_SOURCE_TYPE).dispatch(PositionSource::getType, PositionSourceType::streamCodec);
/*    */   
/*    */   Optional<Vec3> getPosition(Level paramLevel);
/*    */   
/*    */   PositionSourceType<? extends PositionSource> getType();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\PositionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */