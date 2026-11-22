/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Pose implements StringRepresentable {
/* 161 */   STANDING("standing"),
/* 162 */   SITTING("sitting"),
/* 163 */   RUNNING("running"),
/* 164 */   STAR("star");
/*     */   
/*     */   public static final IntFunction<Pose> BY_ID;
/*     */   
/*     */   public static final Codec<Pose> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 166 */     BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 167 */     CODEC = (Codec<Pose>)StringRepresentable.fromEnum(Pose::values);
/*     */   }
/*     */   
/*     */   Pose(String name) {
/* 171 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 176 */     return this.name;
/*     */   }
/*     */   
/*     */   public Pose getNextPose() {
/* 180 */     return BY_ID.apply(ordinal() + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CopperGolemStatueBlock$Pose.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */