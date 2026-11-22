/*     */ package com.mojang.math;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class Transformation {
/*  33 */   private static final Vector3fc ZERO_TRANSLATION = (Vector3fc)new Vector3f();
/*     */   
/*  34 */   private static final Vector3fc UNIT_SCALE = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*     */   
/*  35 */   private static final Quaternionfc ZERO_ROTATION = (Quaternionfc)new Quaternionf();
/*     */   
/*     */   private final Matrix4fc matrix;
/*     */   
/*     */   public static final Codec<Transformation> CODEC;
/*     */   
/*     */   static {
/*  39 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.VECTOR3F.fieldOf("translation").forGetter(Transformation::translation), (App)ExtraCodecs.QUATERNIONF.fieldOf("left_rotation").forGetter(Transformation::leftRotation), (App)ExtraCodecs.VECTOR3F.fieldOf("scale").forGetter(Transformation::scale), (App)ExtraCodecs.QUATERNIONF.fieldOf("right_rotation").forGetter(Transformation::rightRotation)).apply((Applicative)i, Transformation::new));
/*     */   }
/*     */   
/*  47 */   public static final Codec<Transformation> EXTENDED_CODEC = Codec.withAlternative(CODEC, ExtraCodecs.MATRIX4F
/*     */       
/*  49 */       .xmap(Transformation::new, Transformation::getMatrix));
/*     */   
/*     */   private boolean decomposed;
/*     */   
/*     */   private Vector3fc translation;
/*     */   
/*     */   private Quaternionfc leftRotation;
/*     */   
/*     */   private Vector3fc scale;
/*     */   
/*     */   private Quaternionfc rightRotation;
/*     */   
/*     */   public static final Transformation IDENTITY;
/*     */   
/*     */   public Transformation(Matrix4fc matrix) {
/*  59 */     this.matrix = matrix;
/*     */   }
/*     */   
/*     */   public Transformation(Vector3fc translation, Quaternionfc leftRotation, Vector3fc scale, Quaternionfc rightRotation) {
/*  63 */     this.matrix = (Matrix4fc)compose(translation, leftRotation, scale, rightRotation);
/*  64 */     this.translation = Objects.<Vector3fc>requireNonNullElse(translation, ZERO_TRANSLATION);
/*  65 */     this.leftRotation = Objects.<Quaternionfc>requireNonNullElse(leftRotation, ZERO_ROTATION);
/*  66 */     this.scale = Objects.<Vector3fc>requireNonNullElse(scale, UNIT_SCALE);
/*  67 */     this.rightRotation = Objects.<Quaternionfc>requireNonNullElse(rightRotation, ZERO_ROTATION);
/*  68 */     this.decomposed = true;
/*     */   }
/*     */   
/*     */   static {
/*  71 */     IDENTITY = (Transformation)Util.make(() -> {
/*     */           Transformation identity = new Transformation((Matrix4fc)new Matrix4f());
/*     */           identity.translation = ZERO_TRANSLATION;
/*     */           identity.leftRotation = ZERO_ROTATION;
/*     */           identity.scale = UNIT_SCALE;
/*     */           identity.rightRotation = ZERO_ROTATION;
/*     */           identity.decomposed = true;
/*     */           return identity;
/*     */         });
/*     */   }
/*     */   
/*     */   public Transformation compose(Transformation that) {
/*  82 */     Matrix4f matrix = getMatrixCopy();
/*  83 */     matrix.mul(that.getMatrix());
/*  84 */     return new Transformation((Matrix4fc)matrix);
/*     */   }
/*     */   
/*     */   public Transformation inverse() {
/*  88 */     if (this == IDENTITY)
/*  89 */       return this; 
/*  91 */     Matrix4f matrix = getMatrixCopy().invertAffine();
/*  92 */     if (matrix.isFinite())
/*  93 */       return new Transformation((Matrix4fc)matrix); 
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   private void ensureDecomposed() {
/*  99 */     if (!this.decomposed) {
/* 100 */       Vector3f translation = new Vector3f();
/* 101 */       Quaternionf leftRotation = new Quaternionf();
/* 102 */       Vector3f scale = new Vector3f();
/* 103 */       Quaternionf rightRotation = new Quaternionf();
/* 104 */       MatrixUtil.svdDecompose(this.matrix, translation, leftRotation, scale, rightRotation);
/* 105 */       this.translation = (Vector3fc)translation;
/* 106 */       this.leftRotation = (Quaternionfc)leftRotation;
/* 107 */       this.scale = (Vector3fc)scale;
/* 108 */       this.rightRotation = (Quaternionfc)rightRotation;
/* 109 */       this.decomposed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Matrix4f compose(Vector3fc translation, Quaternionfc leftRotation, Vector3fc scale, Quaternionfc rightRotation) {
/* 114 */     Matrix4f result = new Matrix4f();
/* 115 */     if (translation != null)
/* 116 */       result.translation(translation); 
/* 118 */     if (leftRotation != null)
/* 119 */       result.rotate(leftRotation); 
/* 121 */     if (scale != null)
/* 122 */       result.scale(scale); 
/* 124 */     if (rightRotation != null)
/* 125 */       result.rotate(rightRotation); 
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public Matrix4fc getMatrix() {
/* 131 */     return this.matrix;
/*     */   }
/*     */   
/*     */   public Matrix4f getMatrixCopy() {
/* 135 */     return new Matrix4f(this.matrix);
/*     */   }
/*     */   
/*     */   public Vector3fc translation() {
/* 140 */     ensureDecomposed();
/* 141 */     return this.translation;
/*     */   }
/*     */   
/*     */   public Quaternionfc leftRotation() {
/* 146 */     ensureDecomposed();
/* 147 */     return this.leftRotation;
/*     */   }
/*     */   
/*     */   public Vector3fc scale() {
/* 152 */     ensureDecomposed();
/* 153 */     return this.scale;
/*     */   }
/*     */   
/*     */   public Quaternionfc rightRotation() {
/* 158 */     ensureDecomposed();
/* 159 */     return this.rightRotation;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 164 */     if (this == o)
/* 165 */       return true; 
/* 167 */     if (o == null || getClass() != o.getClass())
/* 168 */       return false; 
/* 170 */     Transformation that = (Transformation)o;
/* 171 */     return Objects.equals(this.matrix, that.matrix);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 176 */     return Objects.hash(new Object[] { this.matrix });
/*     */   }
/*     */   
/*     */   public Transformation slerp(Transformation that, float progress) {
/* 180 */     return new Transformation((Vector3fc)
/* 181 */         translation().lerp(that.translation(), progress, new Vector3f()), (Quaternionfc)
/* 182 */         leftRotation().slerp(that.leftRotation(), progress, new Quaternionf()), (Vector3fc)
/* 183 */         scale().lerp(that.scale(), progress, new Vector3f()), (Quaternionfc)
/* 184 */         rightRotation().slerp(that.rightRotation(), progress, new Quaternionf()));
/*     */   }
/*     */   
/*     */   public static Matrix4fc compose(Matrix4fc parent, Optional<Transformation> transform) {
/* 189 */     return transform.isPresent() ? (Matrix4fc)parent.mul(((Transformation)transform.get()).getMatrix(), new Matrix4f()) : parent;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\Transformation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */