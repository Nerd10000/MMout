/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PermissionLevel implements StringRepresentable {
/* 11 */   ALL("all", 0),
/* 12 */   MODERATORS("moderators", 1),
/* 13 */   GAMEMASTERS("gamemasters", 2),
/* 14 */   ADMINS("admins", 3),
/* 15 */   OWNERS("owners", 4);
/*    */   
/*    */   public static final Codec<PermissionLevel> CODEC;
/*    */   
/*    */   private static final IntFunction<PermissionLevel> BY_ID;
/*    */   
/*    */   public static final Codec<PermissionLevel> INT_CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 18 */     CODEC = (Codec<PermissionLevel>)StringRepresentable.fromEnum(PermissionLevel::values);
/* 20 */     BY_ID = ByIdMap.continuous(level -> level.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 21 */     Objects.requireNonNull(BY_ID);
/* 21 */     INT_CODEC = Codec.INT.xmap(BY_ID::apply, level -> Integer.valueOf(level.id));
/*    */   }
/*    */   
/*    */   PermissionLevel(String name, int id) {
/* 27 */     this.name = name;
/* 28 */     this.id = id;
/*    */   }
/*    */   
/*    */   public boolean isEqualOrHigherThan(PermissionLevel other) {
/* 32 */     return (this.id >= other.id);
/*    */   }
/*    */   
/*    */   public static PermissionLevel byId(int level) {
/* 36 */     return BY_ID.apply(level);
/*    */   }
/*    */   
/*    */   public int id() {
/* 40 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */