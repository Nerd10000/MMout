/*    */ package net.minecraft.world.entity.raid;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ enum RaidStatus implements StringRepresentable {
/* 72 */   ONGOING("ongoing"),
/* 73 */   VICTORY("victory"),
/* 74 */   LOSS("loss"),
/* 75 */   STOPPED("stopped");
/*    */   
/*    */   public static final Codec<RaidStatus> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 77 */     CODEC = (Codec<RaidStatus>)StringRepresentable.fromEnum(RaidStatus::values);
/*    */   }
/*    */   
/*    */   RaidStatus(String name) {
/* 82 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 87 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raid$RaidStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */