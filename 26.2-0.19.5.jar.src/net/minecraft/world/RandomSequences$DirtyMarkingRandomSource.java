/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ 
/*     */ class DirtyMarkingRandomSource implements RandomSource {
/*     */   private final RandomSource random;
/*     */   
/*     */   private DirtyMarkingRandomSource(RandomSource random) {
/*  52 */     this.random = random;
/*     */   }
/*     */   
/*     */   public RandomSource fork() {
/*  57 */     RandomSequences.this.setDirty();
/*  58 */     return this.random.fork();
/*     */   }
/*     */   
/*     */   public PositionalRandomFactory forkPositional() {
/*  63 */     RandomSequences.this.setDirty();
/*  64 */     return this.random.forkPositional();
/*     */   }
/*     */   
/*     */   public void setSeed(long seed) {
/*  69 */     RandomSequences.this.setDirty();
/*  70 */     this.random.setSeed(seed);
/*     */   }
/*     */   
/*     */   public int nextInt() {
/*  75 */     RandomSequences.this.setDirty();
/*  76 */     return this.random.nextInt();
/*     */   }
/*     */   
/*     */   public int nextInt(int bound) {
/*  81 */     RandomSequences.this.setDirty();
/*  82 */     return this.random.nextInt(bound);
/*     */   }
/*     */   
/*     */   public long nextLong() {
/*  87 */     RandomSequences.this.setDirty();
/*  88 */     return this.random.nextLong();
/*     */   }
/*     */   
/*     */   public boolean nextBoolean() {
/*  93 */     RandomSequences.this.setDirty();
/*  94 */     return this.random.nextBoolean();
/*     */   }
/*     */   
/*     */   public float nextFloat() {
/*  99 */     RandomSequences.this.setDirty();
/* 100 */     return this.random.nextFloat();
/*     */   }
/*     */   
/*     */   public double nextDouble() {
/* 105 */     RandomSequences.this.setDirty();
/* 106 */     return this.random.nextDouble();
/*     */   }
/*     */   
/*     */   public double nextGaussian() {
/* 111 */     RandomSequences.this.setDirty();
/* 112 */     return this.random.nextGaussian();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 117 */     if (this == obj)
/* 118 */       return true; 
/* 120 */     if (obj instanceof DirtyMarkingRandomSource) {
/* 120 */       DirtyMarkingRandomSource other = (DirtyMarkingRandomSource)obj;
/* 121 */       return this.random.equals(other.random);
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 128 */     return Objects.hashCode(this.random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\RandomSequences$DirtyMarkingRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */