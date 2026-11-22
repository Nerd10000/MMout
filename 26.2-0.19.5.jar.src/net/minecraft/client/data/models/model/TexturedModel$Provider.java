/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Provider {
/*     */   TexturedModel get(Block paramBlock);
/*     */   
/*     */   default Identifier create(Block block, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  99 */     return get(block).create(block, modelOutput);
/*     */   }
/*     */   
/*     */   default Identifier createWithSuffix(Block block, String suffix, BiConsumer<Identifier, ModelInstance> modelOutput) {
/* 103 */     return get(block).createWithSuffix(block, suffix, modelOutput);
/*     */   }
/*     */   
/*     */   default Provider updateTexture(Consumer<TextureMapping> mutator) {
/* 107 */     return block -> get(block).updateTextures(mutator);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\TexturedModel$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */