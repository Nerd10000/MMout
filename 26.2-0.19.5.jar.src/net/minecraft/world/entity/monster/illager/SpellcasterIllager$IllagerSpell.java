/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ 
/*     */ public enum IllagerSpell {
/* 215 */   NONE(0, 0.0D, 0.0D, 0.0D),
/* 216 */   SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
/* 217 */   FANGS(2, 0.4D, 0.3D, 0.35D),
/* 218 */   WOLOLO(3, 0.7D, 0.5D, 0.2D),
/* 219 */   DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
/* 220 */   BLINDNESS(5, 0.1D, 0.1D, 0.2D);
/*     */   
/*     */   private static final IntFunction<IllagerSpell> BY_ID;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final double[] spellColor;
/*     */   
/*     */   static {
/* 223 */     BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   
/*     */   IllagerSpell(int id, double red, double green, double blue) {
/* 229 */     this.id = id;
/* 230 */     this.spellColor = new double[] { red, green, blue };
/*     */   }
/*     */   
/*     */   public static IllagerSpell byId(int id) {
/* 234 */     return BY_ID.apply(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\SpellcasterIllager$IllagerSpell.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */