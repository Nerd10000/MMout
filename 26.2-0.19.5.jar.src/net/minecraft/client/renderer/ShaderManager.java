/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ShaderManager extends SimplePreparableReloadListener<ShaderManager.Configs> implements AutoCloseable {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MAX_LOG_LENGTH = 32768;
/*     */   
/*     */   public static final String SHADER_PATH = "shaders";
/*     */   
/*     */   private static final String SHADER_INCLUDE_PATH = "shaders/include/";
/*     */   
/*  49 */   private static final FileToIdConverter POST_CHAIN_ID_CONVERTER = FileToIdConverter.json("post_effect");
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   
/*     */   private final Consumer<Exception> recoveryHandler;
/*     */   
/*  54 */   private CompilationCache compilationCache = new CompilationCache(Configs.EMPTY);
/*     */   
/*  55 */   private final Projection postChainProjection = new Projection();
/*     */   
/*  56 */   private final ProjectionMatrixBuffer postChainProjectionMatrixBuffer = new ProjectionMatrixBuffer("post");
/*     */   
/*     */   public ShaderManager(TextureManager textureManager, Consumer<Exception> recoveryHandler) {
/*  59 */     this.textureManager = textureManager;
/*  60 */     this.recoveryHandler = recoveryHandler;
/*  63 */     this.postChainProjection.setupOrtho(0.1F, 1000.0F, 1.0F, 1.0F, false);
/*     */   }
/*     */   
/*     */   protected Configs prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  68 */     ImmutableMap.Builder<ShaderSourceKey, String> shaderSources = ImmutableMap.builder();
/*  70 */     Map<Identifier, Resource> files = manager.listResources("shaders", ShaderManager::isShader);
/*  71 */     for (Map.Entry<Identifier, Resource> entry : files.entrySet()) {
/*  72 */       Identifier location = entry.getKey();
/*  73 */       ShaderType shaderType = ShaderType.byLocation(location);
/*  74 */       if (shaderType != null)
/*  75 */         loadShader(location, entry.getValue(), shaderType, files, shaderSources); 
/*     */     } 
/*  80 */     ImmutableMap.Builder<Identifier, PostChainConfig> postChains = ImmutableMap.builder();
/*  81 */     for (Map.Entry<Identifier, Resource> entry : (Iterable<Map.Entry<Identifier, Resource>>)POST_CHAIN_ID_CONVERTER.listMatchingResources(manager).entrySet())
/*  82 */       loadPostChain(entry.getKey(), entry.getValue(), postChains); 
/*  85 */     return new Configs((Map<ShaderSourceKey, String>)shaderSources.build(), (Map<Identifier, PostChainConfig>)postChains.build());
/*     */   }
/*     */   
/*     */   private static void loadShader(Identifier location, Resource resource, ShaderType type, Map<Identifier, Resource> files, ImmutableMap.Builder<ShaderSourceKey, String> output) {
/*  89 */     Identifier id = type.idConverter().fileToId(location);
/*  90 */     GlslPreprocessor preprocessor = createPreprocessor(files, location);
/*     */     try {
/*  91 */       Reader reader = resource.openAsReader();
/*     */       try {
/*  92 */         String source = IOUtils.toString(reader);
/*  93 */         output.put(new ShaderSourceKey(id, type), String.join("", preprocessor.process(source)));
/*  94 */         if (reader != null)
/*  94 */           reader.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (reader != null)
/*     */           try {
/*     */             reader.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  94 */     } catch (IOException e) {
/*  95 */       LOGGER.error("Failed to load shader source at {}", location, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static GlslPreprocessor createPreprocessor(final Map<Identifier, Resource> files, Identifier location) {
/* 100 */     final Identifier parentLocation = location.withPath(FileUtil::getFullResourcePath);
/* 101 */     return new GlslPreprocessor() {
/* 102 */         private final Set<Identifier> importedLocations = (Set<Identifier>)new ObjectArraySet();
/*     */         
/*     */         public String applyImport(boolean isRelative, String path) {
/*     */           Identifier location;
/*     */           try {
/* 108 */             if (isRelative) {
/* 109 */               location = parentLocation.withPath(parentPath -> FileUtil.normalizeResourcePath(parentPath + parentPath));
/*     */             } else {
/* 111 */               location = Identifier.parse(path).withPrefix("shaders/include/");
/*     */             } 
/* 113 */           } catch (IdentifierException e) {
/* 114 */             ShaderManager.LOGGER.error("Malformed GLSL import {}: {}", path, e.getMessage());
/* 115 */             return "#error " + e.getMessage();
/*     */           } 
/* 118 */           if (!this.importedLocations.add(location))
/* 119 */             return null; 
/*     */           try {
/* 122 */             Reader importResource = ((Resource)files.get(location)).openAsReader();
/*     */             try {
/* 123 */               String str = IOUtils.toString(importResource);
/* 124 */               if (importResource != null)
/* 124 */                 importResource.close(); 
/*     */               return str;
/*     */             } catch (Throwable throwable) {
/*     */               if (importResource != null)
/*     */                 try {
/*     */                   importResource.close();
/*     */                 } catch (Throwable throwable1) {
/*     */                   throwable.addSuppressed(throwable1);
/*     */                 }  
/*     */               throw throwable;
/*     */             } 
/* 124 */           } catch (IOException e) {
/* 125 */             ShaderManager.LOGGER.error("Could not open GLSL import {}: {}", location, e.getMessage());
/* 126 */             return "#error " + e.getMessage();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static void loadPostChain(Identifier location, Resource resource, ImmutableMap.Builder<Identifier, PostChainConfig> output) {
/* 133 */     Identifier id = POST_CHAIN_ID_CONVERTER.fileToId(location);
/*     */     try {
/* 134 */       Reader reader = resource.openAsReader();
/*     */       try {
/* 135 */         JsonElement json = StrictJsonParser.parse(reader);
/* 136 */         output.put(id, PostChainConfig.CODEC.parse((DynamicOps)JsonOps.INSTANCE, json).getOrThrow(com.google.gson.JsonSyntaxException::new));
/* 137 */         if (reader != null)
/* 137 */           reader.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (reader != null)
/*     */           try {
/*     */             reader.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 137 */     } catch (IOException|com.google.gson.JsonParseException e) {
/* 138 */       LOGGER.error("Failed to parse post chain at {}", location, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isShader(Identifier location) {
/* 143 */     return (ShaderType.byLocation(location) != null || location.getPath().endsWith(".glsl"));
/*     */   }
/*     */   
/*     */   protected void apply(Configs preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 148 */     CompilationCache newCompilationCache = new CompilationCache(preparations);
/* 149 */     Set<RenderPipeline> pipelinesToPreload = new HashSet<>(RenderPipelines.getStaticPipelines());
/* 150 */     List<Identifier> failedLoads = new ArrayList<>();
/* 152 */     GpuDevice device = RenderSystem.getDevice();
/* 153 */     device.clearPipelineCache();
/* 156 */     for (RenderPipeline pipeline : pipelinesToPreload) {
/* 157 */       Objects.requireNonNull(newCompilationCache);
/* 157 */       CompiledRenderPipeline compiled = device.precompilePipeline(pipeline, newCompilationCache::getShaderSource);
/* 158 */       if (!compiled.isValid())
/* 159 */         failedLoads.add(pipeline.getLocation()); 
/*     */     } 
/* 163 */     if (!failedLoads.isEmpty()) {
/* 165 */       device.clearPipelineCache();
/* 167 */       device.loadCriticalShaders();
/* 168 */       throw new RuntimeException("Failed to load required shader programs:\n" + (String)failedLoads.stream()
/* 169 */           .map(entry -> " - " + String.valueOf(entry))
/* 170 */           .collect(Collectors.joining("\n")));
/*     */     } 
/* 174 */     this.compilationCache.close();
/* 175 */     this.compilationCache = newCompilationCache;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 180 */     return "Shader Loader";
/*     */   }
/*     */   
/*     */   private void tryTriggerRecovery(Exception exception) {
/* 186 */     if (this.compilationCache.triggeredRecovery)
/*     */       return; 
/* 189 */     this.recoveryHandler.accept(exception);
/* 190 */     this.compilationCache.triggeredRecovery = true;
/*     */   }
/*     */   
/*     */   public PostChain getPostChain(Identifier id, Set<Identifier> allowedTargets) {
/*     */     try {
/* 195 */       return this.compilationCache.getOrLoadPostChain(id, allowedTargets);
/* 196 */     } catch (CompilationException e) {
/* 197 */       LOGGER.error("Failed to load post chain: {}", id, e);
/* 199 */       this.compilationCache.postChains.put(id, Optional.empty());
/* 200 */       tryTriggerRecovery(e);
/* 201 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 207 */     this.compilationCache.close();
/* 208 */     this.postChainProjectionMatrixBuffer.close();
/*     */   }
/*     */   
/*     */   public String getShader(Identifier id, ShaderType type) {
/* 212 */     return this.compilationCache.getShaderSource(id, type);
/*     */   }
/*     */   
/*     */   public static final class Configs extends Record {
/*     */     private final Map<ShaderManager.ShaderSourceKey, String> shaderSources;
/*     */     
/*     */     private final Map<Identifier, PostChainConfig> postChains;
/*     */     
/*     */     public Configs(Map<ShaderManager.ShaderSourceKey, String> shaderSources, Map<Identifier, PostChainConfig> postChains) {
/* 215 */       this.shaderSources = shaderSources;
/* 215 */       this.postChains = postChains;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$Configs;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<ShaderManager.ShaderSourceKey, String> shaderSources() {
/* 215 */       return this.shaderSources;
/*     */     }
/*     */     
/*     */     public Map<Identifier, PostChainConfig> postChains() {
/* 215 */       return this.postChains;
/*     */     }
/*     */     
/* 219 */     public static final Configs EMPTY = new Configs(Map.of(), Map.of());
/*     */   }
/*     */   
/*     */   private class CompilationCache implements AutoCloseable {
/*     */     private final ShaderManager.Configs configs;
/*     */     
/* 225 */     private final Map<Identifier, Optional<PostChain>> postChains = new HashMap<>();
/*     */     
/*     */     private boolean triggeredRecovery;
/*     */     
/*     */     private CompilationCache(ShaderManager.Configs configs) {
/* 230 */       this.configs = configs;
/*     */     }
/*     */     
/*     */     public PostChain getOrLoadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 234 */       Optional<PostChain> cached = this.postChains.get(id);
/* 235 */       if (cached != null)
/* 236 */         return cached.orElse(null); 
/* 238 */       PostChain postChain = loadPostChain(id, allowedTargets);
/* 239 */       this.postChains.put(id, Optional.of(postChain));
/* 240 */       return postChain;
/*     */     }
/*     */     
/*     */     private PostChain loadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 244 */       PostChainConfig config = this.configs.postChains.get(id);
/* 245 */       if (config == null)
/* 246 */         throw new ShaderManager.CompilationException("Could not find post chain with id: " + String.valueOf(id)); 
/* 248 */       return PostChain.load(config, ShaderManager.this.textureManager, allowedTargets, id, ShaderManager.this.postChainProjection, ShaderManager.this.postChainProjectionMatrixBuffer);
/*     */     }
/*     */     
/*     */     public void close() {
/* 253 */       this.postChains.values().forEach(chain -> chain.ifPresent(PostChain::close));
/* 254 */       this.postChains.clear();
/*     */     }
/*     */     
/*     */     public String getShaderSource(Identifier id, ShaderType type) {
/* 258 */       return this.configs.shaderSources.get(new ShaderManager.ShaderSourceKey(id, type));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ShaderSourceKey extends Record {
/*     */     private final Identifier id;
/*     */     
/*     */     private final ShaderType type;
/*     */     
/*     */     private ShaderSourceKey(Identifier id, ShaderType type) {
/* 262 */       this.id = id;
/* 262 */       this.type = type;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #262	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #262	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/* 262 */       return this.id;
/*     */     }
/*     */     
/*     */     public ShaderType type() {
/* 262 */       return this.type;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 265 */       return String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CompilationException extends Exception {
/*     */     public CompilationException(String message) {
/* 271 */       super(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */