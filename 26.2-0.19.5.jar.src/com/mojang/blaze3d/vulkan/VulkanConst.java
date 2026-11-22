/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.pipeline.ColorTargetState;
/*     */ import com.mojang.blaze3d.platform.BlendFactor;
/*     */ import com.mojang.blaze3d.platform.BlendOp;
/*     */ import com.mojang.blaze3d.platform.CompareOp;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.systems.GpuSurface;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ 
/*     */ public final class VulkanConst {
/*     */   public static int textureUsageToVk(int usage, GpuFormat format) {
/*  21 */     int vkUsage = 0;
/*  23 */     if ((usage & 0x8) != 0) {
/*  24 */       if (format.hasColorAspect())
/*  25 */         vkUsage |= 0x10; 
/*  27 */       if (format.hasDepthAspect())
/*  28 */         vkUsage |= 0x20; 
/*     */     } 
/*  31 */     if ((usage & 0x4) != 0)
/*  32 */       vkUsage |= 0x4; 
/*  34 */     if ((usage & 0x1) != 0)
/*  35 */       vkUsage |= 0x2; 
/*  37 */     if ((usage & 0x2) != 0)
/*  38 */       vkUsage |= 0x1; 
/*  41 */     return vkUsage;
/*     */   }
/*     */   
/*     */   public static int bufferUsageToVk(int usage) {
/*  45 */     int result = 0;
/*  47 */     if ((usage & 0x8) != 0)
/*  48 */       result |= 0x2; 
/*  50 */     if ((usage & 0x10) != 0)
/*  51 */       result |= 0x1; 
/*  53 */     if ((usage & 0x20) != 0)
/*  54 */       result |= 0x80; 
/*  56 */     if ((usage & 0x40) != 0)
/*  57 */       result |= 0x40; 
/*  59 */     if ((usage & 0x80) != 0)
/*  60 */       result |= 0x10; 
/*  62 */     if ((usage & 0x100) != 0)
/*  63 */       result |= 0x4; 
/*  65 */     if ((usage & 0x200) != 0)
/*  66 */       result |= 0x100; 
/*  69 */     return result;
/*     */   }
/*     */   
/*     */   public static int formatAspectMask(GpuFormat format) {
/*  73 */     int aspectMask = 0;
/*  74 */     if (format.hasColorAspect())
/*  75 */       aspectMask |= 0x1; 
/*  77 */     if (format.hasDepthAspect())
/*  78 */       aspectMask |= 0x2; 
/*  80 */     if (format.hasStencilAspect())
/*  81 */       aspectMask |= 0x4; 
/*  83 */     return aspectMask;
/*     */   }
/*     */   
/*     */   public static int toVk(AddressMode addressMode) {
/*  87 */     switch (addressMode) {
/*     */       default:
/*  87 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */         break;
/*     */     } 
/*  87 */     return 
/*     */       
/*  89 */       2;
/*     */   }
/*     */   
/*     */   public static int toVk(FilterMode filter) {
/*  94 */     switch (filter) {
/*     */       default:
/*  94 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */         break;
/*     */     } 
/*  94 */     return 
/*     */       
/*  96 */       1;
/*     */   }
/*     */   
/*     */   public static int toVk(GpuFormat format) {
/* 101 */     switch (format) {
/*     */       default:
/* 101 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 101 */     return 
/*     */       
/* 157 */       127;
/*     */   }
/*     */   
/*     */   public static int toVk(BlendFactor factor) {
/* 162 */     switch (factor) {
/*     */       default:
/* 162 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 162 */     return 
/*     */       
/* 177 */       0;
/*     */   }
/*     */   
/*     */   public static int toVk(BlendOp blendOp) {
/* 182 */     switch (blendOp) {
/*     */       default:
/* 182 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 182 */     return 
/*     */       
/* 187 */       4;
/*     */   }
/*     */   
/*     */   public static int toVk(CompareOp op) {
/* 192 */     switch (op) {
/*     */       default:
/* 192 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 192 */     return 
/*     */       
/* 200 */       0;
/*     */   }
/*     */   
/*     */   public static int toVk(PolygonMode polygonMode) {
/* 205 */     switch (polygonMode) {
/*     */       default:
/* 205 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */         break;
/*     */     } 
/* 205 */     return 
/*     */       
/* 207 */       1;
/*     */   }
/*     */   
/*     */   public static int toVk(PrimitiveTopology primitiveTopology) {
/* 212 */     switch (primitiveTopology) {
/*     */       default:
/* 212 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 212 */     return 
/*     */       
/* 220 */       3;
/*     */   }
/*     */   
/*     */   public static int toVk(ColorTargetState colorTargetState) {
/* 225 */     int result = 0;
/* 226 */     if (colorTargetState.writeAlpha())
/* 227 */       result |= 0x8; 
/* 229 */     if (colorTargetState.writeRed())
/* 230 */       result |= 0x1; 
/* 232 */     if (colorTargetState.writeGreen())
/* 233 */       result |= 0x2; 
/* 235 */     if (colorTargetState.writeBlue())
/* 236 */       result |= 0x4; 
/* 238 */     return result;
/*     */   }
/*     */   
/*     */   public static int toVk(GpuSurface.PresentMode mode) {
/* 242 */     switch (mode) {
/*     */       default:
/* 242 */         throw new MatchException(null, null);
/*     */       case IMMEDIATE:
/*     */       
/*     */       case MAILBOX:
/*     */       
/*     */       case FIFO:
/*     */       
/*     */       case FIFO_RELAXED:
/*     */         break;
/*     */     } 
/* 242 */     return 
/*     */       
/* 246 */       3;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanConst.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */