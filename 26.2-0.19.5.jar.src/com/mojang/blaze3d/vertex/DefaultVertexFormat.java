/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ 
/*     */ public class DefaultVertexFormat {
/*     */   public static final String POSITION_SEMANTIC_NAME = "Position";
/*     */   
/*     */   public static final String COLOR_SEMANTIC_NAME = "Color";
/*     */   
/*     */   public static final String UV0_SEMANTIC_NAME = "UV0";
/*     */   
/*     */   public static final String UV1_SEMANTIC_NAME = "UV1";
/*     */   
/*     */   public static final String UV2_SEMANTIC_NAME = "UV2";
/*     */   
/*     */   public static final String NORMAL_SEMANTIC_NAME = "Normal";
/*     */   
/*     */   public static final String LINE_WIDTH_SEMANTIC_NAME = "LineWidth";
/*     */   
/*  15 */   private static final GpuFormat POSITION_FORMAT = GpuFormat.RGB32_FLOAT;
/*     */   
/*  16 */   private static final GpuFormat COLOR_FORMAT = GpuFormat.RGBA8_UNORM;
/*     */   
/*  17 */   private static final GpuFormat UV0_FORMAT = GpuFormat.RG32_FLOAT;
/*     */   
/*  18 */   private static final GpuFormat UV1_FORMAT = GpuFormat.RG16_SINT;
/*     */   
/*  19 */   private static final GpuFormat UV2_FORMAT = GpuFormat.RG16_SINT;
/*     */   
/*  20 */   private static final GpuFormat NORMAL_FORMAT = GpuFormat.RGBA8_SNORM;
/*     */   
/*  21 */   private static final GpuFormat LINE_WIDTH_FORMAT = GpuFormat.R32_FLOAT;
/*     */   
/*  25 */   public static final VertexFormat BLOCK = VertexFormat.builder(0)
/*  26 */     .addAttribute("Position", POSITION_FORMAT)
/*  27 */     .addAttribute("Color", COLOR_FORMAT)
/*  28 */     .addAttribute("UV0", UV0_FORMAT)
/*  29 */     .addAttribute("UV2", UV2_FORMAT)
/*  30 */     .build();
/*     */   
/*  32 */   public static final VertexFormat ENTITY = VertexFormat.builder(0)
/*  33 */     .addAttribute("Position", POSITION_FORMAT)
/*  34 */     .addAttribute("Color", COLOR_FORMAT)
/*  35 */     .addAttribute("UV0", UV0_FORMAT)
/*  36 */     .addAttribute("UV1", UV1_FORMAT)
/*  37 */     .addAttribute("UV2", UV2_FORMAT)
/*  38 */     .addAttribute("Normal", NORMAL_FORMAT)
/*  39 */     .build();
/*     */   
/*  41 */   public static final VertexFormat PARTICLE = VertexFormat.builder(0)
/*  42 */     .addAttribute("Position", POSITION_FORMAT)
/*  43 */     .addAttribute("UV0", UV0_FORMAT)
/*  44 */     .addAttribute("Color", COLOR_FORMAT)
/*  45 */     .addAttribute("UV2", UV2_FORMAT)
/*  46 */     .build();
/*     */   
/*  48 */   public static final VertexFormat POSITION = VertexFormat.builder(0)
/*  49 */     .addAttribute("Position", POSITION_FORMAT)
/*  50 */     .build();
/*     */   
/*  52 */   public static final VertexFormat POSITION_COLOR = VertexFormat.builder(0)
/*  53 */     .addAttribute("Position", POSITION_FORMAT)
/*  54 */     .addAttribute("Color", COLOR_FORMAT)
/*  55 */     .build();
/*     */   
/*  57 */   public static final VertexFormat POSITION_COLOR_NORMAL = VertexFormat.builder(0)
/*  58 */     .addAttribute("Position", POSITION_FORMAT)
/*  59 */     .addAttribute("Color", COLOR_FORMAT)
/*  60 */     .addAttribute("Normal", NORMAL_FORMAT)
/*  61 */     .build();
/*     */   
/*  63 */   public static final VertexFormat POSITION_COLOR_LIGHTMAP = VertexFormat.builder(0)
/*  64 */     .addAttribute("Position", POSITION_FORMAT)
/*  65 */     .addAttribute("Color", COLOR_FORMAT)
/*  66 */     .addAttribute("UV2", UV2_FORMAT)
/*  67 */     .build();
/*     */   
/*  69 */   public static final VertexFormat POSITION_TEX = VertexFormat.builder(0)
/*  70 */     .addAttribute("Position", POSITION_FORMAT)
/*  71 */     .addAttribute("UV0", UV0_FORMAT)
/*  72 */     .build();
/*     */   
/*  74 */   public static final VertexFormat POSITION_TEX_COLOR = VertexFormat.builder(0)
/*  75 */     .addAttribute("Position", POSITION_FORMAT)
/*  76 */     .addAttribute("UV0", UV0_FORMAT)
/*  77 */     .addAttribute("Color", COLOR_FORMAT)
/*  78 */     .build();
/*     */   
/*  80 */   public static final VertexFormat POSITION_COLOR_TEX_LIGHTMAP = VertexFormat.builder(0)
/*  81 */     .addAttribute("Position", POSITION_FORMAT)
/*  82 */     .addAttribute("Color", COLOR_FORMAT)
/*  83 */     .addAttribute("UV0", UV0_FORMAT)
/*  84 */     .addAttribute("UV2", UV2_FORMAT)
/*  85 */     .build();
/*     */   
/*  87 */   public static final VertexFormat POSITION_TEX_LIGHTMAP_COLOR = VertexFormat.builder(0)
/*  88 */     .addAttribute("Position", POSITION_FORMAT)
/*  89 */     .addAttribute("UV0", UV0_FORMAT)
/*  90 */     .addAttribute("UV2", UV2_FORMAT)
/*  91 */     .addAttribute("Color", COLOR_FORMAT)
/*  92 */     .build();
/*     */   
/*  94 */   public static final VertexFormat POSITION_TEX_COLOR_NORMAL = VertexFormat.builder(0)
/*  95 */     .addAttribute("Position", POSITION_FORMAT)
/*  96 */     .addAttribute("UV0", UV0_FORMAT)
/*  97 */     .addAttribute("Color", COLOR_FORMAT)
/*  98 */     .addAttribute("Normal", NORMAL_FORMAT)
/*  99 */     .build();
/*     */   
/* 101 */   public static final VertexFormat POSITION_COLOR_LINE_WIDTH = VertexFormat.builder(0)
/* 102 */     .addAttribute("Position", POSITION_FORMAT)
/* 103 */     .addAttribute("Color", COLOR_FORMAT)
/* 104 */     .addAttribute("LineWidth", LINE_WIDTH_FORMAT)
/* 105 */     .build();
/*     */   
/* 107 */   public static final VertexFormat POSITION_COLOR_NORMAL_LINE_WIDTH = VertexFormat.builder(0)
/* 108 */     .addAttribute("Position", POSITION_FORMAT)
/* 109 */     .addAttribute("Color", COLOR_FORMAT)
/* 110 */     .addAttribute("Normal", NORMAL_FORMAT)
/* 111 */     .addAttribute("LineWidth", LINE_WIDTH_FORMAT)
/* 112 */     .build();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\DefaultVertexFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */