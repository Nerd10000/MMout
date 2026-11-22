/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.GlyphSource;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ class null extends CacheLoader<FontDescription.PlayerSprite, GlyphSource> {
/*    */   public GlyphSource load(FontDescription.PlayerSprite playerInfo) {
/* 31 */     final Supplier<PlayerSkinRenderCache.RenderInfo> skin = PlayerGlyphProvider.this.playerSkinRenderCache.createLookup(playerInfo.profile());
/* 32 */     final boolean hat = playerInfo.hat();
/* 34 */     return new SingleSpriteSource(new BakedGlyph(this) {
/*    */           public GlyphInfo info() {
/* 37 */             return PlayerGlyphProvider.GLYPH_INFO;
/*    */           }
/*    */           
/*    */           public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/* 42 */             return new PlayerGlyphProvider.Instance(skin, hat, x, y, color, shadowColor, shadowOffset, style);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\PlayerGlyphProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */