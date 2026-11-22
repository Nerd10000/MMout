/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class CompilationCache implements AutoCloseable {
/*     */   private final ShaderManager.Configs configs;
/*     */   
/* 225 */   private final Map<Identifier, Optional<PostChain>> postChains = new HashMap<>();
/*     */   
/*     */   private boolean triggeredRecovery;
/*     */   
/*     */   private CompilationCache(ShaderManager.Configs configs) {
/* 230 */     this.configs = configs;
/*     */   }
/*     */   
/*     */   public PostChain getOrLoadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 234 */     Optional<PostChain> cached = this.postChains.get(id);
/* 235 */     if (cached != null)
/* 236 */       return cached.orElse(null); 
/* 238 */     PostChain postChain = loadPostChain(id, allowedTargets);
/* 239 */     this.postChains.put(id, Optional.of(postChain));
/* 240 */     return postChain;
/*     */   }
/*     */   
/*     */   private PostChain loadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 244 */     PostChainConfig config = this.configs.postChains.get(id);
/* 245 */     if (config == null)
/* 246 */       throw new ShaderManager.CompilationException("Could not find post chain with id: " + String.valueOf(id)); 
/* 248 */     return PostChain.load(config, ShaderManager.this.textureManager, allowedTargets, id, ShaderManager.this.postChainProjection, ShaderManager.this.postChainProjectionMatrixBuffer);
/*     */   }
/*     */   
/*     */   public void close() {
/* 253 */     this.postChains.values().forEach(chain -> chain.ifPresent(PostChain::close));
/* 254 */     this.postChains.clear();
/*     */   }
/*     */   
/*     */   public String getShaderSource(Identifier id, ShaderType type) {
/* 258 */     return this.configs.shaderSources.get(new ShaderManager.ShaderSourceKey(id, type));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderManager$CompilationCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */