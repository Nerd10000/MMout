/*     */ package com.mojang.blaze3d.buffers;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector2fc;
/*     */ import org.joml.Vector2ic;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector3ic;
/*     */ import org.joml.Vector4fc;
/*     */ import org.joml.Vector4ic;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ 
/*     */ public class Std140Builder {
/*     */   private final ByteBuffer buffer;
/*     */   
/*     */   private final int start;
/*     */   
/*     */   private Std140Builder(ByteBuffer buffer) {
/*  23 */     this.buffer = buffer;
/*  24 */     this.start = buffer.position();
/*     */   }
/*     */   
/*     */   public static Std140Builder intoBuffer(ByteBuffer buffer) {
/*  28 */     return new Std140Builder(buffer);
/*     */   }
/*     */   
/*     */   public static Std140Builder onStack(MemoryStack stack, int size) {
/*  32 */     return new Std140Builder(stack.malloc(size));
/*     */   }
/*     */   
/*     */   public ByteBuffer get() {
/*  36 */     return this.buffer.flip();
/*     */   }
/*     */   
/*     */   public Std140Builder align(int alignment) {
/*  40 */     int position = this.buffer.position();
/*  41 */     this.buffer.position(this.start + Mth.roundToward(position - this.start, alignment));
/*  42 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putFloat(float value) {
/*  46 */     align(4);
/*  47 */     this.buffer.putFloat(value);
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putInt(int value) {
/*  52 */     align(4);
/*  53 */     this.buffer.putInt(value);
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec2(float x, float y) {
/*  58 */     align(8);
/*  59 */     this.buffer.putFloat(x);
/*  60 */     this.buffer.putFloat(y);
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec2(Vector2fc vec) {
/*  65 */     align(8);
/*  66 */     vec.get(this.buffer);
/*  67 */     this.buffer.position(this.buffer.position() + 8);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec2(int x, int y) {
/*  72 */     align(8);
/*  73 */     this.buffer.putInt(x);
/*  74 */     this.buffer.putInt(y);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec2(Vector2ic vec) {
/*  79 */     align(8);
/*  80 */     vec.get(this.buffer);
/*  81 */     this.buffer.position(this.buffer.position() + 8);
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec3(float x, float y, float z) {
/*  86 */     align(16);
/*  87 */     this.buffer.putFloat(x);
/*  88 */     this.buffer.putFloat(y);
/*  89 */     this.buffer.putFloat(z);
/*  90 */     this.buffer.position(this.buffer.position() + 4);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec3(Vector3fc vec) {
/*  95 */     align(16);
/*  96 */     vec.get(this.buffer);
/*  97 */     this.buffer.position(this.buffer.position() + 16);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec3(int x, int y, int z) {
/* 102 */     align(16);
/* 103 */     this.buffer.putInt(x);
/* 104 */     this.buffer.putInt(y);
/* 105 */     this.buffer.putInt(z);
/* 106 */     this.buffer.position(this.buffer.position() + 4);
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec3(Vector3ic vec) {
/* 111 */     align(16);
/* 112 */     vec.get(this.buffer);
/* 113 */     this.buffer.position(this.buffer.position() + 16);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec4(float x, float y, float z, float w) {
/* 118 */     align(16);
/* 119 */     this.buffer.putFloat(x);
/* 120 */     this.buffer.putFloat(y);
/* 121 */     this.buffer.putFloat(z);
/* 122 */     this.buffer.putFloat(w);
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putVec4(Vector4fc vec) {
/* 127 */     align(16);
/* 128 */     vec.get(this.buffer);
/* 129 */     this.buffer.position(this.buffer.position() + 16);
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec4(int x, int y, int z, int w) {
/* 134 */     align(16);
/* 135 */     this.buffer.putInt(x);
/* 136 */     this.buffer.putInt(y);
/* 137 */     this.buffer.putInt(z);
/* 138 */     this.buffer.putInt(w);
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putIVec4(Vector4ic vec) {
/* 143 */     align(16);
/* 144 */     vec.get(this.buffer);
/* 145 */     this.buffer.position(this.buffer.position() + 16);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public Std140Builder putMat4f(Matrix4fc vec) {
/* 150 */     align(16);
/* 151 */     vec.get(this.buffer);
/* 152 */     this.buffer.position(this.buffer.position() + 64);
/* 153 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\buffers\Std140Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */