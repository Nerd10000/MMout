/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockParticleOption implements ParticleOptions {
/* 13 */   private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative(BlockState.CODEC, BuiltInRegistries.BLOCK
/*    */       
/* 15 */       .byNameCodec(), Block::defaultBlockState);
/*    */   
/*    */   private final ParticleType<BlockParticleOption> type;
/*    */   
/*    */   private final BlockState state;
/*    */   
/*    */   public static MapCodec<BlockParticleOption> codec(ParticleType<BlockParticleOption> type) {
/* 19 */     return BLOCK_STATE_CODEC.xmap(state -> new BlockParticleOption(type, state), o -> o.state).fieldOf("block_state");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> streamCodec(ParticleType<BlockParticleOption> type) {
/* 23 */     return ByteBufCodecs.idMapper((IdMap)Block.BLOCK_STATE_REGISTRY).map(state -> new BlockParticleOption(type, state), o -> o.state);
/*    */   }
/*    */   
/*    */   public BlockParticleOption(ParticleType<BlockParticleOption> type, BlockState state) {
/* 30 */     this.type = type;
/* 31 */     this.state = state;
/*    */   }
/*    */   
/*    */   public ParticleType<BlockParticleOption> getType() {
/* 36 */     return this.type;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 40 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\BlockParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */