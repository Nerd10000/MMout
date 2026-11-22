/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ @Immutable
/*    */ public class DifficultyInstance {
/*    */   private static final float DIFFICULTY_TIME_GLOBAL_OFFSET = -72000.0F;
/*    */   
/*    */   private static final float MAX_DIFFICULTY_TIME_GLOBAL = 1440000.0F;
/*    */   
/*    */   private static final float MAX_DIFFICULTY_TIME_LOCAL = 3600000.0F;
/*    */   
/*    */   private final Difficulty base;
/*    */   
/*    */   private final float effectiveDifficulty;
/*    */   
/*    */   public DifficultyInstance(Difficulty base, long totalGameTime, long localGameTime, float moonBrightness) {
/* 20 */     this.base = base;
/* 21 */     this.effectiveDifficulty = calculateDifficulty(base, totalGameTime, localGameTime, moonBrightness);
/*    */   }
/*    */   
/*    */   public Difficulty getDifficulty() {
/* 25 */     return this.base;
/*    */   }
/*    */   
/*    */   public float getEffectiveDifficulty() {
/* 29 */     return this.effectiveDifficulty;
/*    */   }
/*    */   
/*    */   public boolean isHard() {
/* 33 */     return (this.effectiveDifficulty >= Difficulty.HARD.ordinal());
/*    */   }
/*    */   
/*    */   public boolean isHarderThan(float requiredDifficulty) {
/* 37 */     return (this.effectiveDifficulty > requiredDifficulty);
/*    */   }
/*    */   
/*    */   public float getSpecialMultiplier() {
/* 48 */     if (this.effectiveDifficulty < 2.0F)
/* 49 */       return 0.0F; 
/* 51 */     if (this.effectiveDifficulty > 4.0F)
/* 52 */       return 1.0F; 
/* 54 */     return (this.effectiveDifficulty - 2.0F) / 2.0F;
/*    */   }
/*    */   
/*    */   private float calculateDifficulty(Difficulty base, long totalGameTime, long localGameTime, float moonBrightness) {
/* 58 */     if (base == Difficulty.PEACEFUL)
/* 59 */       return 0.0F; 
/* 62 */     boolean isHard = (base == Difficulty.HARD);
/* 63 */     float scale = 0.75F;
/* 66 */     float globalScale = Mth.clamp(((float)totalGameTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
/* 67 */     scale += globalScale;
/* 69 */     float localScale = 0.0F;
/* 72 */     localScale += Mth.clamp((float)localGameTime / 3600000.0F, 0.0F, 1.0F) * (isHard ? 1.0F : 0.75F);
/* 73 */     localScale += Mth.clamp(moonBrightness * 0.25F, 0.0F, globalScale);
/* 75 */     if (base == Difficulty.EASY)
/* 76 */       localScale *= 0.5F; 
/* 78 */     scale += localScale;
/* 83 */     return base.getId() * scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\DifficultyInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */