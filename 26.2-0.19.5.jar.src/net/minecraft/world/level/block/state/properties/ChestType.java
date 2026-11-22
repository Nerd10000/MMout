/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ChestType implements StringRepresentable {
/*  7 */   SINGLE("single"),
/*  8 */   LEFT("left"),
/*  9 */   RIGHT("right");
/*    */   
/*    */   public static final Codec<ChestType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 12 */     CODEC = (Codec<ChestType>)StringRepresentable.fromEnum(ChestType::values);
/*    */   }
/*    */   
/*    */   ChestType(String name) {
/* 17 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.name;
/*    */   }
/*    */   
/*    */   public ChestType getOpposite() {
/* 26 */     switch (ordinal()) {
/*    */       default:
/* 26 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 26 */     return 
/*    */       
/* 29 */       LEFT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\ChestType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */