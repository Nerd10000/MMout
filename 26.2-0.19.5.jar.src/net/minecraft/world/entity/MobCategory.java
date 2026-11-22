/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MobCategory implements StringRepresentable {
/*  7 */   MONSTER("monster", "MO", 70, false, false, 128),
/*  8 */   CREATURE("creature", "C", 10, true, true, 128),
/*  9 */   AMBIENT("ambient", "AM", 15, true, false, 128),
/* 11 */   AXOLOTLS("axolotls", "AX", 5, true, false, 128),
/* 12 */   UNDERGROUND_WATER_CREATURE("underground_water_creature", "UWC", 5, true, false, 128),
/* 13 */   WATER_CREATURE("water_creature", "WC", 5, true, false, 128),
/* 14 */   WATER_AMBIENT("water_ambient", "WA", 20, true, false, 64),
/* 15 */   MISC("misc", "MI", -1, true, true, 128);
/*    */   
/*    */   static {
/* 18 */     CODEC = (Codec<MobCategory>)StringRepresentable.fromEnum(MobCategory::values);
/*    */   }
/*    */   
/* 25 */   private final int noDespawnDistance = 32;
/*    */   
/*    */   public static final Codec<MobCategory> CODEC;
/*    */   
/*    */   private final int max;
/*    */   
/*    */   private final boolean isFriendly;
/*    */   
/*    */   private final boolean isPersistent;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final String debugAbbreviation;
/*    */   
/*    */   private final int despawnDistance;
/*    */   
/*    */   MobCategory(String name, String debugAbbreviation, int max, boolean isFriendly, boolean isPersistent, int despawnDistance) {
/* 29 */     this.name = name;
/* 30 */     this.debugAbbreviation = debugAbbreviation;
/* 31 */     this.max = max;
/* 32 */     this.isFriendly = isFriendly;
/* 33 */     this.isPersistent = isPersistent;
/* 34 */     this.despawnDistance = despawnDistance;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getDebugAbbreviation() {
/* 42 */     return this.debugAbbreviation;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 47 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getMaxInstancesPerChunk() {
/* 51 */     return this.max;
/*    */   }
/*    */   
/*    */   public boolean isFriendly() {
/* 55 */     return this.isFriendly;
/*    */   }
/*    */   
/*    */   public boolean isPersistent() {
/* 59 */     return this.isPersistent;
/*    */   }
/*    */   
/*    */   public int getDespawnDistance() {
/* 63 */     return this.despawnDistance;
/*    */   }
/*    */   
/*    */   public int getNoDespawnDistance() {
/* 67 */     return 32;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\MobCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */