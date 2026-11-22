/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public final class FeatureFlagSet {
/*  11 */   private static final FeatureFlagSet EMPTY = new FeatureFlagSet(null, 0L);
/*     */   
/*     */   public static final int MAX_CONTAINER_SIZE = 64;
/*     */   
/*     */   private final FeatureFlagUniverse universe;
/*     */   
/*     */   private final long mask;
/*     */   
/*     */   private FeatureFlagSet(FeatureFlagUniverse universe, long mask) {
/*  19 */     this.universe = universe;
/*  20 */     this.mask = mask;
/*     */   }
/*     */   
/*     */   static FeatureFlagSet create(FeatureFlagUniverse universe, Collection<FeatureFlag> flags) {
/*  25 */     if (flags.isEmpty())
/*  26 */       return EMPTY; 
/*  28 */     long mask = computeMask(universe, 0L, flags);
/*  29 */     return new FeatureFlagSet(universe, mask);
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of() {
/*  33 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of(FeatureFlag flag) {
/*  37 */     return new FeatureFlagSet(flag.universe, flag.mask);
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of(FeatureFlag flag, FeatureFlag... flags) {
/*  41 */     long mask = (flags.length == 0) ? flag.mask : computeMask(flag.universe, flag.mask, Arrays.asList(flags));
/*  42 */     return new FeatureFlagSet(flag.universe, mask);
/*     */   }
/*     */   
/*     */   private static long computeMask(FeatureFlagUniverse universe, long mask, Iterable<FeatureFlag> flags) {
/*  46 */     for (FeatureFlag f : flags) {
/*  47 */       if (universe != f.universe)
/*  48 */         throw new IllegalStateException("Mismatched feature universe, expected '" + String.valueOf(universe) + "', but got '" + String.valueOf(f.universe) + "'"); 
/*  50 */       mask |= f.mask;
/*     */     } 
/*  52 */     return mask;
/*     */   }
/*     */   
/*     */   public boolean contains(FeatureFlag flag) {
/*  56 */     if (this.universe != flag.universe)
/*  57 */       return false; 
/*  59 */     return ((this.mask & flag.mask) != 0L);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  63 */     return equals(EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isSubsetOf(FeatureFlagSet set) {
/*  67 */     if (this.universe == null)
/*  68 */       return true; 
/*  70 */     if (this.universe != set.universe)
/*  71 */       return false; 
/*  73 */     return ((this.mask & (set.mask ^ 0xFFFFFFFFFFFFFFFFL)) == 0L);
/*     */   }
/*     */   
/*     */   public boolean intersects(FeatureFlagSet set) {
/*  77 */     if (this.universe == null || set.universe == null || this.universe != set.universe)
/*  78 */       return false; 
/*  80 */     return ((this.mask & set.mask) != 0L);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet join(FeatureFlagSet other) {
/*  84 */     if (this.universe == null)
/*  85 */       return other; 
/*  87 */     if (other.universe == null)
/*  88 */       return this; 
/*  90 */     if (this.universe != other.universe)
/*  91 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(other.universe) + "'"); 
/*  93 */     return new FeatureFlagSet(this.universe, this.mask | other.mask);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet subtract(FeatureFlagSet other) {
/*  97 */     if (this.universe == null || other.universe == null)
/*  98 */       return this; 
/* 100 */     if (this.universe != other.universe)
/* 101 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(other.universe) + "'"); 
/* 103 */     long newMask = this.mask & (other.mask ^ 0xFFFFFFFFFFFFFFFFL);
/* 104 */     if (newMask == 0L)
/* 105 */       return EMPTY; 
/* 107 */     return new FeatureFlagSet(this.universe, newMask);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 112 */     if (this == o)
/* 113 */       return true; 
/* 115 */     if (o instanceof FeatureFlagSet) {
/* 115 */       FeatureFlagSet that = (FeatureFlagSet)o;
/* 115 */       if (this.universe == that.universe && this.mask == that.mask);
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 120 */     return (int)HashCommon.mix(this.mask);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\flag\FeatureFlagSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */