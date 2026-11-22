/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.VarInt;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Optional<BlockState>> {
/*    */   public void encode(ByteBuf output, Optional<BlockState> value) {
/* 86 */     if (value.isPresent()) {
/* 87 */       VarInt.write(output, Block.getId(value.get()));
/*    */     } else {
/* 89 */       VarInt.write(output, 0);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Optional<BlockState> decode(ByteBuf input) {
/* 95 */     int id = VarInt.read(input);
/* 96 */     if (id == 0)
/* 97 */       return Optional.empty(); 
/* 99 */     return Optional.of(Block.stateById(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializers$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */