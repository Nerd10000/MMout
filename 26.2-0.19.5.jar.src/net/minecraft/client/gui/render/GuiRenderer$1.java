/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.renderer.state.gui.GlyphRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ class null implements Font.GlyphVisitor {
/*     */   public void acceptRenderable(TextRenderable renderable) {
/* 258 */     GuiRenderer.this.renderState.addGlyphToCurrentLayer((GuiElementRenderState)new GlyphRenderState(pose, renderable, scissor));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\GuiRenderer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */