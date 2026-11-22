/*    */ package net.minecraft.world.entity;
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
/*    */ public enum HumanoidArm implements StringRepresentable {
/* 14 */   LEFT(0, "left", "options.mainHand.left"),
/* 15 */   RIGHT(1, "right", "options.mainHand.right");
/*    */   
/*    */   public static final Codec<HumanoidArm> CODEC;
/*    */   
/*    */   private static final IntFunction<HumanoidArm> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, HumanoidArm> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 18 */     CODEC = (Codec<HumanoidArm>)StringRepresentable.fromEnum(HumanoidArm::values);
/* 19 */     BY_ID = ByIdMap.continuous(a -> a.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, a -> a.id);
/*    */   }
/*    */   
/*    */   HumanoidArm(int id, String name, String translationKey) {
/* 27 */     this.id = id;
/* 28 */     this.name = name;
/* 29 */     this.caption = (Component)Component.translatable(translationKey);
/*    */   }
/*    */   
/*    */   public HumanoidArm getOpposite() {
/* 33 */     switch (ordinal()) {
/*    */       default:
/* 33 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/* 33 */     return 
/*    */       
/* 35 */       LEFT;
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 40 */     return this.caption;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\HumanoidArm.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */