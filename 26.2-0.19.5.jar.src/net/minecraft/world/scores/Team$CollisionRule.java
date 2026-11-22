/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum CollisionRule implements StringRepresentable {
/*  76 */   ALWAYS("always", 0),
/*  77 */   NEVER("never", 1),
/*  78 */   PUSH_OTHER_TEAMS("pushOtherTeams", 2),
/*  79 */   PUSH_OWN_TEAM("pushOwnTeam", 3);
/*     */   
/*     */   public static final Codec<CollisionRule> CODEC;
/*     */   
/*     */   private static final IntFunction<CollisionRule> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, CollisionRule> STREAM_CODEC;
/*     */   
/*     */   public final String name;
/*     */   
/*     */   public final int id;
/*     */   
/*     */   static {
/*  81 */     CODEC = (Codec<CollisionRule>)StringRepresentable.fromEnum(CollisionRule::values);
/*  83 */     BY_ID = ByIdMap.continuous(r -> r.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  84 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*     */   }
/*     */   
/*     */   CollisionRule(String name, int id) {
/*  90 */     this.name = name;
/*  91 */     this.id = id;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  95 */     return (Component)Component.translatable("team.collision." + this.name);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 100 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\Team$CollisionRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */