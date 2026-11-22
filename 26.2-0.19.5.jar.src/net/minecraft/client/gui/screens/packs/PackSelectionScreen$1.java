/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.server.packs.repository.PackDetector;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ class null extends PackDetector<Path> {
/*     */   null(PackSelectionScreen this$0, DirectoryValidator validator) {
/* 250 */     super(validator);
/*     */   }
/*     */   
/*     */   protected Path createZipPack(Path content) {
/* 253 */     return content;
/*     */   }
/*     */   
/*     */   protected Path createDirectoryPack(Path content) {
/* 258 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */