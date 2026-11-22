/*    */ package net.minecraft.world.scores;
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
/*    */ public enum Visibility implements StringRepresentable {
/* 47 */   ALWAYS("always", 0),
/* 48 */   NEVER("never", 1),
/* 49 */   HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/* 50 */   HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
/*    */   
/*    */   public static final Codec<Visibility> CODEC;
/*    */   
/*    */   private static final IntFunction<Visibility> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Visibility> STREAM_CODEC;
/*    */   
/*    */   public final String name;
/*    */   
/*    */   public final int id;
/*    */   
/*    */   static {
/* 52 */     CODEC = (Codec<Visibility>)StringRepresentable.fromEnum(Visibility::values);
/* 54 */     BY_ID = ByIdMap.continuous(v -> v.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 55 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
/*    */   }
/*    */   
/*    */   Visibility(String name, int id) {
/* 61 */     this.name = name;
/* 62 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 66 */     return (Component)Component.translatable("team.visibility." + this.name);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 71 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\Team$Visibility.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */