/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum TestBlockMode implements StringRepresentable {
/* 14 */   START(0, "start"),
/* 15 */   LOG(1, "log"),
/* 16 */   FAIL(2, "fail"),
/* 17 */   ACCEPT(3, "accept");
/*    */   
/*    */   private static final IntFunction<TestBlockMode> BY_ID;
/*    */   
/*    */   public static final Codec<TestBlockMode> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, TestBlockMode> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Component displayName;
/*    */   
/*    */   private final Component detailedMessage;
/*    */   
/*    */   static {
/* 20 */     BY_ID = ByIdMap.continuous(mode -> mode.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 22 */     CODEC = (Codec<TestBlockMode>)StringRepresentable.fromEnum(TestBlockMode::values);
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, mode -> mode.id);
/*    */   }
/*    */   
/*    */   TestBlockMode(int id, String name) {
/* 31 */     this.id = id;
/* 32 */     this.name = name;
/* 33 */     this.displayName = (Component)Component.translatable("test_block.mode." + name);
/* 34 */     this.detailedMessage = (Component)Component.translatable("test_block.mode_info." + name);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 43 */     return this.displayName;
/*    */   }
/*    */   
/*    */   public Component getDetailedMessage() {
/* 47 */     return this.detailedMessage;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\TestBlockMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */