/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.util.TextRenderingUtils;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 158 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 161 */     WorldTemplatePaginatedList page = startPage;
/* 162 */     RealmsClient client = RealmsClient.getOrCreate();
/* 163 */     while (page != null) {
/* 165 */       Either<WorldTemplatePaginatedList, Exception> result = RealmsSelectWorldTemplateScreen.this.fetchTemplates(page, client);
/* 190 */       page = RealmsSelectWorldTemplateScreen.access$000(RealmsSelectWorldTemplateScreen.this).submit(() -> {
/*     */             if (result.right().isPresent()) {
/*     */               RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates", result.right().get());
/*     */               if (RealmsSelectWorldTemplateScreen.this.worldTemplateList.isEmpty())
/*     */                 RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(I18n.get("mco.template.select.failure", new Object[0]), new TextRenderingUtils.LineSegment[0]); 
/*     */               return null;
/*     */             } 
/*     */             WorldTemplatePaginatedList currentPage = result.left().get();
/*     */             for (WorldTemplate template : currentPage.templates())
/*     */               RealmsSelectWorldTemplateScreen.this.worldTemplateList.addEntry(template); 
/*     */             if (currentPage.templates().isEmpty()) {
/*     */               if (RealmsSelectWorldTemplateScreen.this.worldTemplateList.isEmpty()) {
/*     */                 String withoutLink = I18n.get("mco.template.select.none", new Object[] { "%link" });
/*     */                 TextRenderingUtils.LineSegment link = TextRenderingUtils.LineSegment.link(I18n.get("mco.template.select.none.linkTitle", new Object[0]), CommonLinks.REALMS_CONTENT_CREATION.toString());
/*     */                 RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(withoutLink, new TextRenderingUtils.LineSegment[] { link });
/*     */               } 
/*     */               return null;
/*     */             } 
/*     */             return currentPage;
/* 190 */           }).join();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsSelectWorldTemplateScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */