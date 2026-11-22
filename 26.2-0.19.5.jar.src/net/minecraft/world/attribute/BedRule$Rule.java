/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public enum Rule implements StringRepresentable {
/* 47 */   ALWAYS("always"),
/* 50 */   WHEN_DARK("when_dark"),
/* 51 */   NEVER("never");
/*    */   
/*    */   public static final Codec<Rule> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 54 */     CODEC = (Codec<Rule>)StringRepresentable.fromEnum(Rule::values);
/*    */   }
/*    */   
/*    */   Rule(String name) {
/* 59 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean test(Level level) {
/* 63 */     switch (ordinal()) {
/*    */       default:
/* 63 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 72 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\BedRule$Rule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */