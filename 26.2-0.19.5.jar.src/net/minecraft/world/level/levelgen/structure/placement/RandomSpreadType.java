/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum RandomSpreadType implements StringRepresentable {
/*  8 */   LINEAR("linear"),
/*  9 */   TRIANGULAR("triangular");
/*    */   
/*    */   public static final Codec<RandomSpreadType> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 11 */     CODEC = (Codec<RandomSpreadType>)StringRepresentable.fromEnum(RandomSpreadType::values);
/*    */   }
/*    */   
/*    */   RandomSpreadType(String id) {
/* 16 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 21 */     return this.id;
/*    */   }
/*    */   
/*    */   public int evaluate(RandomSource random, int limit) {
/* 25 */     switch (ordinal()) {
/*    */       default:
/* 25 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/* 25 */     return (
/*    */       
/* 27 */       random.nextInt(limit) + random.nextInt(limit)) / 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\RandomSpreadType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */