/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Gene implements StringRepresentable {
/* 309 */   NORMAL(0, "normal", false),
/* 310 */   LAZY(1, "lazy", false),
/* 311 */   WORRIED(2, "worried", false),
/* 312 */   PLAYFUL(3, "playful", false),
/* 313 */   BROWN(4, "brown", true),
/* 314 */   WEAK(5, "weak", true),
/* 315 */   AGGRESSIVE(6, "aggressive", false);
/*     */   
/*     */   public static final Codec<Gene> CODEC;
/*     */   
/*     */   private static final IntFunction<Gene> BY_ID;
/*     */   
/*     */   private static final int MAX_GENE = 6;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean isRecessive;
/*     */   
/*     */   static {
/* 317 */     CODEC = (Codec<Gene>)StringRepresentable.fromEnum(Gene::values);
/* 319 */     BY_ID = ByIdMap.continuous(Gene::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   
/*     */   Gene(int id, String name, boolean isRecessive) {
/* 327 */     this.id = id;
/* 328 */     this.name = name;
/* 329 */     this.isRecessive = isRecessive;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 333 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 338 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isRecessive() {
/* 342 */     return this.isRecessive;
/*     */   }
/*     */   
/*     */   private static Gene getVariantFromGenes(Gene mainGene, Gene hiddenGene) {
/* 346 */     if (mainGene.isRecessive()) {
/* 347 */       if (mainGene == hiddenGene)
/* 348 */         return mainGene; 
/* 350 */       return NORMAL;
/*     */     } 
/* 354 */     return mainGene;
/*     */   }
/*     */   
/*     */   public static Gene byId(int id) {
/* 358 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public static Gene getRandom(RandomSource random) {
/* 362 */     int nextInt = random.nextInt(16);
/* 363 */     if (nextInt == 0)
/* 364 */       return LAZY; 
/* 366 */     if (nextInt == 1)
/* 367 */       return WORRIED; 
/* 369 */     if (nextInt == 2)
/* 370 */       return PLAYFUL; 
/* 372 */     if (nextInt == 4)
/* 373 */       return AGGRESSIVE; 
/* 375 */     if (nextInt < 9)
/* 376 */       return WEAK; 
/* 378 */     if (nextInt < 11)
/* 379 */       return BROWN; 
/* 382 */     return NORMAL;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$Gene.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */