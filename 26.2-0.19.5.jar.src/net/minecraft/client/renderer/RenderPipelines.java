/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.pipeline.BlendFunction;
/*     */ import com.mojang.blaze3d.pipeline.ColorTargetState;
/*     */ import com.mojang.blaze3d.pipeline.DepthStencilState;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.platform.BlendFactor;
/*     */ import com.mojang.blaze3d.platform.CompareOp;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RenderPipelines {
/*  21 */   private static final Map<Identifier, RenderPipeline> PIPELINES_BY_LOCATION = new HashMap<>();
/*     */   
/*     */   private static final float ALPHA_CUTOUT_THRESHOLD_DEFAULT = 0.1F;
/*     */   
/*     */   private static final float ALPHA_CUTOUT_THRESHOLD_CUTOUT_TERRAIN = 0.5F;
/*     */   
/*     */   private static RenderPipeline register(RenderPipeline pipeline) {
/*  27 */     PIPELINES_BY_LOCATION.put(pipeline.getLocation(), pipeline);
/*  28 */     return pipeline;
/*     */   }
/*     */   
/*     */   public static List<RenderPipeline> getStaticPipelines() {
/*  32 */     return PIPELINES_BY_LOCATION.values().stream().toList();
/*     */   }
/*     */   
/*  35 */   private static final RenderPipeline.Snippet GLOBALS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[0])
/*  36 */     .withBindGroupLayout(BindGroupLayouts.GLOBALS)
/*  37 */     .buildSnippet();
/*     */   
/*  39 */   private static final RenderPipeline.Snippet MATRICES_FOG_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/*  41 */     .withBindGroupLayout(BindGroupLayouts.FOG)
/*  42 */     .buildSnippet();
/*     */   
/*  44 */   private static final RenderPipeline.Snippet MATRICES_FOG_LIGHT_DIR_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withBindGroupLayout(BindGroupLayouts.LIGHTING)
/*  46 */     .buildSnippet();
/*     */   
/*  48 */   private static final RenderPipeline.Snippet GENERIC_BLOCKS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.FOG)
/*  50 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
/*  51 */     .withVertexBinding(0, DefaultVertexFormat.BLOCK)
/*  52 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/*  53 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/*  54 */     .buildSnippet();
/*     */   
/*  56 */   private static final RenderPipeline.Snippet TERRAIN_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GENERIC_BLOCKS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.PROJECTION)
/*  58 */     .withBindGroupLayout(BindGroupLayouts.CHUNK_SECTION)
/*  59 */     .withVertexShader("core/terrain")
/*  60 */     .withFragmentShader("core/terrain")
/*  61 */     .buildSnippet();
/*     */   
/*  63 */   private static final RenderPipeline.Snippet BLOCK_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GENERIC_BLOCKS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/*  65 */     .withVertexShader("core/block")
/*  66 */     .withFragmentShader("core/block")
/*  67 */     .buildSnippet();
/*     */   
/*  69 */   private static final RenderPipeline.Snippet ENTITY_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET }).withVertexShader("core/entity")
/*  71 */     .withFragmentShader("core/entity")
/*  72 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
/*  73 */     .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/*  74 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/*  75 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/*  76 */     .buildSnippet();
/*     */   
/*  78 */   private static final RenderPipeline.Snippet ENTITY_EMISSIVE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET }).withVertexShader("core/entity")
/*  80 */     .withFragmentShader("core/entity")
/*  81 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/*  82 */     .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/*  83 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/*  84 */     .withShaderDefine("EMISSIVE")
/*  85 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/*  86 */     .buildSnippet();
/*     */   
/*  88 */   private static final RenderPipeline.Snippet BEACON_BEAM_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withVertexShader("core/rendertype_beacon_beam")
/*  90 */     .withFragmentShader("core/rendertype_beacon_beam")
/*  91 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/*  92 */     .withVertexBinding(0, DefaultVertexFormat.BLOCK)
/*  93 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/*  94 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/*  95 */     .buildSnippet();
/*     */   
/*  97 */   private static final RenderPipeline.Snippet ITEM_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_LIGHT_DIR_SNIPPET }).withVertexShader("core/item")
/*  99 */     .withFragmentShader("core/item")
/* 100 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1_SAMPLER2)
/* 101 */     .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/* 102 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 103 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 104 */     .buildSnippet();
/*     */   
/* 106 */   private static final RenderPipeline.Snippet TEXT_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 108 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 109 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 110 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 111 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
/* 112 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 113 */     .buildSnippet();
/*     */   
/* 115 */   private static final RenderPipeline.Snippet WORLD_TEXT_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET }).withBindGroupLayout(BindGroupLayouts.FOG)
/* 117 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
/* 118 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR)
/* 119 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 120 */     .buildSnippet();
/*     */   
/* 122 */   private static final RenderPipeline.Snippet END_PORTAL_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 124 */     .withBindGroupLayout(BindGroupLayouts.FOG)
/* 125 */     .withVertexShader("core/rendertype_end_portal")
/* 126 */     .withFragmentShader("core/rendertype_end_portal")
/* 127 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
/* 128 */     .withVertexBinding(0, DefaultVertexFormat.POSITION)
/* 129 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 130 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 131 */     .buildSnippet();
/*     */   
/* 133 */   private static final RenderPipeline.Snippet CLOUDS_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withVertexShader("core/rendertype_clouds")
/* 135 */     .withFragmentShader("core/rendertype_clouds")
/* 136 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 137 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 138 */     .withBindGroupLayout(BindGroupLayouts.CLOUD_INFO)
/* 139 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 140 */     .buildSnippet();
/*     */   
/* 142 */   private static final RenderPipeline.Snippet LINES_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withVertexShader("core/rendertype_lines")
/* 144 */     .withFragmentShader("core/rendertype_lines")
/* 145 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 146 */     .withCull(false)
/* 147 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
/* 148 */     .withPrimitiveTopology(PrimitiveTopology.LINES)
/* 149 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 150 */     .buildSnippet();
/*     */   
/* 152 */   private static final RenderPipeline.Snippet DEBUG_FILLED_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 154 */     .withVertexShader("core/position_color")
/* 155 */     .withFragmentShader("core/position_color")
/* 156 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 157 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 158 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 159 */     .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 160 */     .withCull(false)
/* 161 */     .buildSnippet();
/*     */   
/* 163 */   private static final RenderPipeline.Snippet PARTICLE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withVertexShader("core/particle")
/* 165 */     .withFragmentShader("core/particle")
/* 166 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
/* 167 */     .withVertexBinding(0, DefaultVertexFormat.PARTICLE)
/* 168 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 169 */     .withDepthStencilState(DepthStencilState.DEFAULT)
/* 170 */     .buildSnippet();
/*     */   
/* 172 */   private static final RenderPipeline.Snippet WEATHER_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET }).withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 174 */     .withCull(false)
/* 175 */     .buildSnippet();
/*     */   
/* 177 */   private static final RenderPipeline.Snippet GUI_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 179 */     .withVertexShader("core/gui")
/* 180 */     .withFragmentShader("core/gui")
/* 181 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 182 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 183 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 184 */     .buildSnippet();
/*     */   
/* 186 */   private static final RenderPipeline.Snippet GUI_TEXTURED_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 188 */     .withVertexShader("core/position_tex_color")
/* 189 */     .withFragmentShader("core/position_tex_color")
/* 190 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 191 */     .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 192 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
/* 193 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 194 */     .buildSnippet();
/*     */   
/* 196 */   private static final RenderPipeline.Snippet GUI_TEXT_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET }).withShaderDefine("IS_GUI")
/* 198 */     .withDepthStencilState(Optional.empty())
/* 199 */     .buildSnippet();
/*     */   
/* 201 */   private static final RenderPipeline.Snippet OUTLINE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 203 */     .withVertexShader("core/rendertype_outline")
/* 204 */     .withFragmentShader("core/rendertype_outline")
/* 205 */     .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 206 */     .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
/* 207 */     .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 208 */     .buildSnippet();
/*     */   
/* 210 */   public static final RenderPipeline.Snippet POST_PROCESSING_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 212 */     .buildSnippet();
/*     */   
/* 214 */   public static final RenderPipeline SOLID_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET }).withLocation("pipeline/solid_block")
/* 216 */       .build());
/*     */   
/* 218 */   public static final RenderPipeline SOLID_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET }).withLocation("pipeline/solid_terrain")
/* 220 */       .build());
/*     */   
/* 222 */   public static final RenderPipeline WIREFRAME = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET }).withLocation("pipeline/wireframe")
/* 224 */       .withPolygonMode(PolygonMode.WIREFRAME)
/* 225 */       .build());
/*     */   
/* 227 */   public static final RenderPipeline CUTOUT_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET }).withLocation("pipeline/cutout_block")
/* 229 */       .withShaderDefine("ALPHA_CUTOUT", 0.5F)
/* 230 */       .build());
/*     */   
/* 232 */   public static final RenderPipeline CUTOUT_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET }).withLocation("pipeline/cutout_terrain")
/* 234 */       .withShaderDefine("ALPHA_CUTOUT", 0.5F)
/* 235 */       .build());
/*     */   
/* 240 */   public static final RenderPipeline TRANSLUCENT_TERRAIN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TERRAIN_SNIPPET }).withLocation("pipeline/translucent_terrain")
/* 242 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 243 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 244 */       .build());
/*     */   
/* 246 */   public static final RenderPipeline TRANSLUCENT_BLOCK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BLOCK_SNIPPET }).withLocation("pipeline/translucent_block")
/* 248 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 249 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 250 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 251 */       .build());
/*     */   
/* 253 */   public static final RenderPipeline ARMOR_CUTOUT_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/armor_cutout_no_cull")
/* 255 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 256 */       .withShaderDefine("NO_OVERLAY")
/* 257 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 258 */       .withCull(false)
/* 259 */       .build());
/*     */   
/* 261 */   public static final RenderPipeline ARMOR_DECAL_CUTOUT_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/armor_decal_cutout_no_cull")
/* 263 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 264 */       .withShaderDefine("NO_OVERLAY")
/* 265 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 266 */       .withCull(false)
/* 267 */       .withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
/* 268 */       .build());
/*     */   
/* 270 */   public static final RenderPipeline ARMOR_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/armor_translucent")
/* 272 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 273 */       .withShaderDefine("NO_OVERLAY")
/* 274 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 275 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 276 */       .withCull(false)
/* 277 */       .build());
/*     */   
/* 279 */   public static final RenderPipeline ENTITY_SOLID = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_solid")
/* 281 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 282 */       .build());
/*     */   
/* 284 */   public static final RenderPipeline ENTITY_SOLID_Z_OFFSET_FORWARD = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_solid_offset_forward")
/* 286 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 287 */       .build());
/*     */   
/* 289 */   public static final RenderPipeline ENTITY_CUTOUT_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_cutout_cull")
/* 291 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 292 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 293 */       .build());
/*     */   
/* 295 */   public static final RenderPipeline ENTITY_CUTOUT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_cutout")
/* 297 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 298 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 299 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 300 */       .withCull(false)
/* 301 */       .build());
/*     */   
/* 303 */   public static final RenderPipeline ENTITY_CUTOUT_Z_OFFSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_cutout_z_offset")
/* 305 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 306 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 307 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 308 */       .withCull(false)
/* 309 */       .build());
/*     */   
/* 311 */   public static final RenderPipeline ENTITY_CUTOUT_DISSOLVE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_cutout_dissolve")
/* 313 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 314 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 315 */       .withShaderDefine("DISSOLVE")
/* 316 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 317 */       .withBindGroupLayout(BindGroupLayouts.DISSOLVE_MASK_SAMPLER)
/* 318 */       .withCull(false)
/* 319 */       .build());
/*     */   
/* 321 */   public static final RenderPipeline ENTITY_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_translucent")
/* 323 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 324 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 325 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 326 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 327 */       .withCull(false)
/* 328 */       .build());
/*     */   
/* 330 */   public static final RenderPipeline ENTITY_TRANSLUCENT_EMISSIVE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_EMISSIVE_SNIPPET }).withLocation("pipeline/entity_translucent_emissive")
/* 332 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 333 */       .withShaderDefine("PER_FACE_LIGHTING")
/* 334 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 335 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 336 */       .withCull(false)
/* 337 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 338 */       .build());
/*     */   
/* 340 */   public static final RenderPipeline ENTITY_TRANSLUCENT_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/entity_translucent_cull")
/* 342 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 343 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
/* 344 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 345 */       .build());
/*     */   
/* 347 */   public static final RenderPipeline END_CRYSTAL_BEAM = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/end_crystal_beam")
/* 349 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 350 */       .withShaderDefine("NO_OVERLAY")
/* 351 */       .withCull(false)
/* 352 */       .build());
/*     */   
/* 354 */   public static final RenderPipeline BANNER_PATTERN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/banner_pattern")
/* 356 */       .withShaderDefine("NO_OVERLAY")
/* 357 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 358 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 359 */       .build());
/*     */   
/* 361 */   public static final RenderPipeline BREEZE_WIND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ENTITY_SNIPPET }).withLocation("pipeline/breeze_wind")
/* 363 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 364 */       .withShaderDefine("APPLY_TEXTURE_MATRIX")
/* 365 */       .withShaderDefine("NO_OVERLAY")
/* 366 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 367 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 368 */       .withCull(false)
/* 369 */       .build());
/*     */   
/* 371 */   public static final RenderPipeline ENERGY_SWIRL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/energy_swirl")
/* 373 */       .withVertexShader("core/entity")
/* 374 */       .withFragmentShader("core/entity")
/* 375 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 376 */       .withShaderDefine("EMISSIVE")
/* 377 */       .withShaderDefine("NO_OVERLAY")
/* 378 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 379 */       .withShaderDefine("APPLY_TEXTURE_MATRIX")
/* 380 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 381 */       .withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
/* 382 */       .withCull(false)
/* 383 */       .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/* 384 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 385 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 386 */       .build());
/*     */   
/* 388 */   public static final RenderPipeline EYES = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/eyes")
/* 390 */       .withVertexShader("core/entity")
/* 391 */       .withFragmentShader("core/entity")
/* 392 */       .withShaderDefine("EMISSIVE")
/* 393 */       .withShaderDefine("NO_OVERLAY")
/* 394 */       .withShaderDefine("NO_CARDINAL_LIGHTING")
/* 395 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 396 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 397 */       .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/* 398 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 399 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 400 */       .build());
/*     */   
/* 402 */   public static final RenderPipeline ENTITY_SHADOW = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/entity_shadow")
/* 404 */       .withVertexShader("core/rendertype_entity_shadow")
/* 405 */       .withFragmentShader("core/rendertype_entity_shadow")
/* 406 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 407 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 408 */       .withVertexBinding(0, DefaultVertexFormat.ENTITY)
/* 409 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 410 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 411 */       .build());
/*     */   
/* 413 */   public static final RenderPipeline ITEM_CUTOUT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ITEM_SNIPPET }).withLocation("pipeline/item_cutout")
/* 415 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 416 */       .build());
/*     */   
/* 418 */   public static final RenderPipeline ITEM_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ITEM_SNIPPET }).withLocation("pipeline/item_translucent")
/* 420 */       .withShaderDefine("ALPHA_CUTOUT", 0.1F)
/* 421 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 422 */       .build());
/*     */   
/* 424 */   public static final RenderPipeline BEACON_BEAM_OPAQUE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BEACON_BEAM_SNIPPET }).withLocation("pipeline/beacon_beam_opaque")
/* 426 */       .build());
/*     */   
/* 428 */   public static final RenderPipeline BEACON_BEAM_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { BEACON_BEAM_SNIPPET }).withLocation("pipeline/beacon_beam_translucent")
/* 430 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 431 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 432 */       .build());
/*     */   
/* 434 */   public static final RenderPipeline LEASH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/leash")
/* 436 */       .withVertexShader("core/rendertype_leash")
/* 437 */       .withFragmentShader("core/rendertype_leash")
/* 438 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
/* 439 */       .withCull(false)
/* 440 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
/* 441 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
/* 442 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 443 */       .build());
/*     */   
/* 445 */   public static final RenderPipeline WATER_MASK = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 447 */       .withLocation("pipeline/water_mask")
/* 448 */       .withVertexShader("core/rendertype_water_mask")
/* 449 */       .withFragmentShader("core/rendertype_water_mask")
/* 450 */       .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), GpuFormat.RGBA8_UNORM, 0))
/* 451 */       .withVertexBinding(0, DefaultVertexFormat.POSITION)
/* 452 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 453 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 454 */       .build());
/*     */   
/* 456 */   public static final RenderPipeline GLINT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 458 */       .withBindGroupLayout(BindGroupLayouts.FOG)
/* 459 */       .withLocation("pipeline/glint")
/* 460 */       .withVertexShader("core/glint")
/* 461 */       .withFragmentShader("core/glint")
/* 462 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 463 */       .withCull(false)
/* 464 */       .withColorTargetState(new ColorTargetState(BlendFunction.GLINT))
/* 465 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
/* 466 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 467 */       .withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
/* 468 */       .build());
/*     */   
/* 470 */   public static final RenderPipeline CRUMBLING = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 472 */       .withBindGroupLayout(BindGroupLayouts.FOG)
/* 473 */       .withLocation("pipeline/crumbling")
/* 474 */       .withVertexShader("core/rendertype_crumbling")
/* 475 */       .withFragmentShader("core/rendertype_crumbling")
/* 476 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 477 */       .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.DST_COLOR, BlendFactor.SRC_COLOR, BlendFactor.ONE, BlendFactor.ZERO)))
/* 478 */       .withVertexBinding(0, DefaultVertexFormat.BLOCK)
/* 479 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 480 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false, 1.0F, 10.0F))
/* 481 */       .build());
/*     */   
/* 483 */   public static final RenderPipeline TEXT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WORLD_TEXT_SNIPPET }).withLocation("pipeline/text")
/* 485 */       .withVertexShader("core/text")
/* 486 */       .withFragmentShader("core/text")
/* 487 */       .build());
/*     */   
/* 489 */   public static final RenderPipeline GUI_TEXT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXT_SNIPPET }).withLocation("pipeline/gui_text")
/* 491 */       .withVertexShader("core/text")
/* 492 */       .withFragmentShader("core/text")
/* 493 */       .build());
/*     */   
/* 495 */   public static final RenderPipeline TEXT_BACKGROUND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 497 */       .withBindGroupLayout(BindGroupLayouts.FOG)
/* 498 */       .withLocation("pipeline/text_background")
/* 499 */       .withVertexShader("core/text_background")
/* 500 */       .withFragmentShader("core/text_background")
/* 501 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 502 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 503 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
/* 504 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
/* 505 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 506 */       .build());
/*     */   
/* 508 */   public static final RenderPipeline TEXT_GRAYSCALE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WORLD_TEXT_SNIPPET }).withLocation("pipeline/text_grayscale")
/* 510 */       .withVertexShader("core/text")
/* 511 */       .withFragmentShader("core/text")
/* 512 */       .withShaderDefine("IS_GRAYSCALE")
/* 513 */       .build());
/*     */   
/* 515 */   public static final RenderPipeline GUI_TEXT_GRAYSCALE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXT_SNIPPET }).withLocation("pipeline/gui_text_grayscale")
/* 517 */       .withVertexShader("core/text")
/* 518 */       .withFragmentShader("core/text")
/* 519 */       .withShaderDefine("IS_GRAYSCALE")
/* 520 */       .build());
/*     */   
/* 522 */   public static final RenderPipeline TEXT_POLYGON_OFFSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WORLD_TEXT_SNIPPET }).withLocation("pipeline/text_polygon_offset")
/* 524 */       .withVertexShader("core/text")
/* 525 */       .withFragmentShader("core/text")
/* 526 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true, 1.0F, 10.0F))
/* 527 */       .build());
/*     */   
/* 529 */   public static final RenderPipeline TEXT_GRAYSCALE_POLYGON_OFFSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WORLD_TEXT_SNIPPET }).withLocation("pipeline/text_grayscale_polygon_offset")
/* 531 */       .withVertexShader("core/text")
/* 532 */       .withFragmentShader("core/text")
/* 533 */       .withShaderDefine("IS_GRAYSCALE")
/* 534 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true, 1.0F, 10.0F))
/* 535 */       .build());
/*     */   
/* 537 */   public static final RenderPipeline TEXT_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET }).withLocation("pipeline/text_see_through")
/* 539 */       .withVertexShader("core/text")
/* 540 */       .withFragmentShader("core/text")
/* 541 */       .withShaderDefine("IS_SEE_THROUGH")
/* 542 */       .withDepthStencilState(Optional.empty())
/* 543 */       .build());
/*     */   
/* 545 */   public static final RenderPipeline TEXT_BACKGROUND_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 547 */       .withLocation("pipeline/text_background_see_through")
/* 548 */       .withVertexShader("core/text_background")
/* 549 */       .withFragmentShader("core/text_background")
/* 550 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 551 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 552 */       .withShaderDefine("IS_SEE_THROUGH")
/* 553 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 554 */       .withDepthStencilState(Optional.empty())
/* 555 */       .build());
/*     */   
/* 557 */   public static final RenderPipeline TEXT_GRAYSCALE_SEE_THROUGH = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { TEXT_SNIPPET }).withLocation("pipeline/text_grayscale_see_through")
/* 559 */       .withVertexShader("core/text")
/* 560 */       .withFragmentShader("core/text")
/* 561 */       .withShaderDefine("IS_GRAYSCALE")
/* 562 */       .withShaderDefine("IS_SEE_THROUGH")
/* 563 */       .withDepthStencilState(Optional.empty())
/* 564 */       .build());
/*     */   
/* 566 */   public static final RenderPipeline LIGHTNING = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/lightning")
/* 568 */       .withVertexShader("core/rendertype_lightning")
/* 569 */       .withFragmentShader("core/rendertype_lightning")
/* 570 */       .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
/* 571 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 572 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 573 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 574 */       .build());
/*     */   
/* 576 */   public static final RenderPipeline DRAGON_RAYS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/dragon_rays")
/* 578 */       .withVertexShader("core/rendertype_lightning")
/* 579 */       .withFragmentShader("core/rendertype_lightning")
/* 580 */       .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
/* 581 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 582 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 583 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 584 */       .build());
/*     */   
/* 586 */   public static final RenderPipeline END_PORTAL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { END_PORTAL_SNIPPET }).withLocation("pipeline/end_portal")
/* 588 */       .withShaderDefine("PORTAL_LAYERS", 15)
/* 589 */       .build());
/*     */   
/* 591 */   public static final RenderPipeline END_GATEWAY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { END_PORTAL_SNIPPET }).withLocation("pipeline/end_gateway")
/* 593 */       .withShaderDefine("PORTAL_LAYERS", 16)
/* 594 */       .build());
/*     */   
/* 596 */   public static final RenderPipeline FLAT_CLOUDS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { CLOUDS_SNIPPET }).withLocation("pipeline/flat_clouds")
/* 598 */       .withCull(false)
/* 599 */       .build());
/*     */   
/* 601 */   public static final RenderPipeline CLOUDS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { CLOUDS_SNIPPET }).withLocation("pipeline/clouds")
/* 603 */       .build());
/*     */   
/* 605 */   public static final RenderPipeline LINES = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET }).withLocation("pipeline/lines")
/* 607 */       .build());
/*     */   
/* 609 */   public static final RenderPipeline LINES_TRANSLUCENT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET }).withLocation("pipeline/lines_translucent")
/* 611 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 612 */       .build());
/*     */   
/* 614 */   public static final RenderPipeline LINES_DEPTH_BIAS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET }).withLocation("pipeline/lines_depth_bias")
/* 616 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true, 1.0F, 1.0F))
/* 617 */       .build());
/*     */   
/* 619 */   public static final RenderPipeline SECONDARY_BLOCK_OUTLINE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { LINES_SNIPPET }).withLocation("pipeline/secondary_block_outline")
/* 621 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 622 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 623 */       .build());
/*     */   
/* 625 */   public static final RenderPipeline DEBUG_POINTS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 627 */       .withLocation("pipeline/debug_points")
/* 628 */       .withVertexShader("core/debug_point")
/* 629 */       .withFragmentShader("core/position_color")
/* 630 */       .withCull(false)
/* 631 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_LINE_WIDTH)
/* 632 */       .withPrimitiveTopology(PrimitiveTopology.POINTS)
/* 633 */       .withDepthStencilState(DepthStencilState.DEFAULT)
/* 634 */       .build());
/*     */   
/* 636 */   public static final RenderPipeline DEBUG_FILLED_BOX = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET }).withLocation("pipeline/debug_filled_box")
/* 638 */       .build());
/*     */   
/* 640 */   public static final RenderPipeline DEBUG_QUADS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET }).withLocation("pipeline/debug_quads")
/* 642 */       .withCull(false)
/* 643 */       .build());
/*     */   
/* 645 */   public static final RenderPipeline DEBUG_TRIANGLE_FAN = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { DEBUG_FILLED_SNIPPET }).withLocation("pipeline/debug_triangle_fan")
/* 647 */       .withCull(false)
/* 648 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 649 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
/* 650 */       .build());
/*     */   
/* 652 */   public static final RenderPipeline WORLD_BORDER = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 654 */       .withLocation("pipeline/world_border")
/* 655 */       .withVertexShader("core/rendertype_world_border")
/* 656 */       .withFragmentShader("core/rendertype_world_border")
/* 657 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 658 */       .withColorTargetState(new ColorTargetState(BlendFunction.OVERLAY))
/* 659 */       .withCull(false)
/* 660 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
/* 661 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 662 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true, 3.0F, 3.0F))
/* 663 */       .build());
/*     */   
/* 665 */   public static final RenderPipeline OPAQUE_PARTICLE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET }).withLocation("pipeline/opaque_particle")
/* 667 */       .build());
/*     */   
/* 669 */   public static final RenderPipeline TRANSLUCENT_PARTICLE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { PARTICLE_SNIPPET }).withLocation("pipeline/translucent_particle")
/* 671 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 672 */       .build());
/*     */   
/* 674 */   public static final RenderPipeline WEATHER_DEPTH_WRITE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WEATHER_SNIPPET }).withLocation("pipeline/weather_depth_write")
/* 676 */       .build());
/*     */   
/* 678 */   public static final RenderPipeline WEATHER_NO_DEPTH_WRITE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { WEATHER_SNIPPET }).withLocation("pipeline/weather_no_depth_write")
/* 680 */       .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
/* 681 */       .build());
/*     */   
/* 683 */   public static final RenderPipeline SKY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { MATRICES_FOG_SNIPPET }).withLocation("pipeline/sky")
/* 685 */       .withVertexShader("core/sky")
/* 686 */       .withFragmentShader("core/sky")
/* 687 */       .withVertexBinding(0, DefaultVertexFormat.POSITION)
/* 688 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
/* 689 */       .build());
/*     */   
/* 691 */   public static final RenderPipeline END_SKY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 693 */       .withLocation("pipeline/end_sky")
/* 694 */       .withVertexShader("core/position_tex_color")
/* 695 */       .withFragmentShader("core/position_tex_color")
/* 696 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 697 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 698 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
/* 699 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 700 */       .build());
/*     */   
/* 702 */   public static final RenderPipeline SUNRISE_SUNSET = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 704 */       .withLocation("pipeline/sunrise_sunset")
/* 705 */       .withVertexShader("core/position_color")
/* 706 */       .withFragmentShader("core/position_color")
/* 707 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
/* 708 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
/* 709 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
/* 710 */       .build());
/*     */   
/* 712 */   public static final RenderPipeline STARS = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 714 */       .withLocation("pipeline/stars")
/* 715 */       .withVertexShader("core/stars")
/* 716 */       .withFragmentShader("core/stars")
/* 717 */       .withColorTargetState(new ColorTargetState(BlendFunction.OVERLAY))
/* 718 */       .withVertexBinding(0, DefaultVertexFormat.POSITION)
/* 719 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 720 */       .build());
/*     */   
/* 722 */   public static final RenderPipeline CELESTIAL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 724 */       .withLocation("pipeline/celestial")
/* 725 */       .withVertexShader("core/position_tex")
/* 726 */       .withFragmentShader("core/position_tex")
/* 727 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 728 */       .withColorTargetState(new ColorTargetState(BlendFunction.OVERLAY))
/* 729 */       .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
/* 730 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 731 */       .build());
/*     */   
/* 733 */   public static final RenderPipeline GUI = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET }).withLocation("pipeline/gui")
/* 735 */       .build());
/*     */   
/* 737 */   public static final RenderPipeline GUI_INVERT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET }).withLocation("pipeline/gui_invert")
/* 739 */       .withColorTargetState(new ColorTargetState(BlendFunction.INVERT))
/* 740 */       .build());
/*     */   
/* 742 */   public static final RenderPipeline GUI_TEXT_HIGHLIGHT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_SNIPPET }).withLocation("pipeline/gui_text_highlight")
/* 744 */       .withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
/* 745 */       .build());
/*     */   
/* 747 */   public static final RenderPipeline GUI_TEXTURED = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/gui_textured")
/* 749 */       .build());
/*     */   
/* 751 */   public static final RenderPipeline GUI_TEXTURED_PREMULTIPLIED_ALPHA = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/gui_textured_premultiplied_alpha")
/* 753 */       .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
/* 754 */       .build());
/*     */   
/* 756 */   public static final RenderPipeline BLOCK_SCREEN_EFFECT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/block_screen_effect")
/* 758 */       .build());
/*     */   
/* 760 */   public static final RenderPipeline FIRE_SCREEN_EFFECT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/fire_screen_effect")
/* 762 */       .build());
/*     */   
/* 764 */   public static final RenderPipeline GUI_OPAQUE_TEXTURED_BACKGROUND = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/gui_opaque_textured_background")
/* 766 */       .withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA8_UNORM, 15))
/* 767 */       .build());
/*     */   
/* 769 */   public static final RenderPipeline GUI_NAUSEA_OVERLAY = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/gui_nausea_overlay")
/* 771 */       .withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
/* 772 */       .build());
/*     */   
/* 774 */   public static final RenderPipeline VIGNETTE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/vignette")
/* 776 */       .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.ZERO, BlendFactor.ONE_MINUS_SRC_COLOR, BlendFactor.ZERO, BlendFactor.ONE)))
/* 777 */       .build());
/*     */   
/* 779 */   public static final RenderPipeline CROSSHAIR = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/crosshair")
/* 781 */       .withColorTargetState(new ColorTargetState(BlendFunction.INVERT))
/* 782 */       .build());
/*     */   
/* 784 */   public static final RenderPipeline MOJANG_LOGO = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GUI_TEXTURED_SNIPPET }).withLocation("pipeline/mojang_logo")
/* 786 */       .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE)))
/* 787 */       .build());
/*     */   
/* 789 */   public static final RenderPipeline ENTITY_OUTLINE_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withLocation("pipeline/entity_outline_blit")
/* 791 */       .withVertexShader("core/screenquad")
/* 792 */       .withFragmentShader("core/blit_screen")
/* 793 */       .withBindGroupLayout(BindGroupLayouts.IN_SAMPLER)
/* 794 */       .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.ENTITY_OUTLINE_BLIT), GpuFormat.RGBA8_UNORM, 7))
/* 795 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 796 */       .build());
/*     */   
/* 798 */   public static final RenderPipeline TRACY_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withLocation("pipeline/tracy_blit")
/* 800 */       .withVertexShader("core/screenquad")
/* 801 */       .withFragmentShader("core/blit_screen")
/* 802 */       .withBindGroupLayout(BindGroupLayouts.IN_SAMPLER)
/* 803 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 804 */       .build());
/*     */   
/* 806 */   public static final RenderPipeline PANORAMA = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
/* 808 */       .withLocation("pipeline/panorama")
/* 809 */       .withVertexShader("core/panorama")
/* 810 */       .withFragmentShader("core/panorama")
/* 811 */       .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
/* 812 */       .withVertexBinding(0, DefaultVertexFormat.POSITION)
/* 813 */       .withPrimitiveTopology(PrimitiveTopology.QUADS)
/* 814 */       .build());
/*     */   
/* 816 */   public static final RenderPipeline OUTLINE_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { OUTLINE_SNIPPET }).withLocation("pipeline/outline_cull")
/* 818 */       .build());
/*     */   
/* 820 */   public static final RenderPipeline OUTLINE_NO_CULL = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { OUTLINE_SNIPPET }).withLocation("pipeline/outline_no_cull")
/* 822 */       .withCull(false)
/* 823 */       .build());
/*     */   
/* 825 */   public static final RenderPipeline LIGHTMAP = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withLocation("pipeline/lightmap")
/* 827 */       .withVertexShader("core/screenquad")
/* 828 */       .withFragmentShader("core/lightmap")
/* 829 */       .withBindGroupLayout(BindGroupLayouts.LIGHTMAP_INFO)
/* 830 */       .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 831 */       .build());
/*     */   
/* 833 */   public static final RenderPipeline.Snippet ANIMATE_SPRITE_SNIPPET = RenderPipeline.builder(new RenderPipeline.Snippet[] { GLOBALS_SNIPPET }).withVertexShader("core/animate_sprite")
/* 835 */     .withBindGroupLayout(BindGroupLayouts.SPRITE_ANIMATION_INFO)
/* 836 */     .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
/* 837 */     .buildSnippet();
/*     */   
/* 839 */   public static final RenderPipeline ANIMATE_SPRITE_BLIT = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ANIMATE_SPRITE_SNIPPET }).withFragmentShader("core/animate_sprite_blit")
/* 841 */       .withLocation("pipeline/animate_sprite_blit")
/* 842 */       .withBindGroupLayout(BindGroupLayouts.SPRITE)
/* 843 */       .build());
/*     */   
/* 845 */   public static final RenderPipeline ANIMATE_SPRITE_INTERPOLATE = register(RenderPipeline.builder(new RenderPipeline.Snippet[] { ANIMATE_SPRITE_SNIPPET }).withFragmentShader("core/animate_sprite_interpolate")
/* 847 */       .withLocation("pipeline/animate_sprite_interpolate")
/* 848 */       .withBindGroupLayout(BindGroupLayouts.CURRENT_SPRITE_NEXT_SPRITE)
/* 849 */       .build());
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\RenderPipelines.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */