/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class Entry<E extends ContainerObjectSelectionList.Entry<E>> extends AbstractSelectionList.Entry<E> implements ContainerEventHandler {
/*     */   private GuiEventListener focused;
/*     */   
/*     */   private NarratableEntry lastNarratable;
/*     */   
/*     */   private boolean dragging;
/*     */   
/*     */   public boolean isDragging() {
/* 126 */     return this.dragging;
/*     */   }
/*     */   
/*     */   public void setDragging(boolean dragging) {
/* 131 */     this.dragging = dragging;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 136 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 141 */     if (this.focused != null)
/* 142 */       this.focused.setFocused(false); 
/* 144 */     if (focused != null)
/* 145 */       focused.setFocused(true); 
/* 147 */     this.focused = focused;
/*     */   }
/*     */   
/*     */   public GuiEventListener getFocused() {
/* 152 */     return this.focused;
/*     */   }
/*     */   
/*     */   public ComponentPath focusPathAtIndex(FocusNavigationEvent navigationEvent, int currentIndex) {
/* 156 */     if (children().isEmpty())
/* 157 */       return null; 
/* 159 */     ComponentPath componentPath = ((GuiEventListener)children().get(Math.min(currentIndex, children().size() - 1))).nextFocusPath(navigationEvent);
/* 160 */     return ComponentPath.path(this, componentPath);
/*     */   }
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 165 */     if (navigationEvent instanceof FocusNavigationEvent.ArrowNavigation) {
/* 165 */       FocusNavigationEvent.ArrowNavigation arrowNavigation = (FocusNavigationEvent.ArrowNavigation)navigationEvent;
/* 166 */       switch (ContainerObjectSelectionList.null.$SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection[arrowNavigation.direction().ordinal()]) {
/*     */         default:
/* 166 */           throw new MatchException(null, null);
/*     */         case 3:
/*     */         case 4:
/*     */         
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */           break;
/*     */       } 
/* 166 */       int delta = 
/*     */         
/* 169 */         1;
/* 171 */       if (delta == 0)
/* 172 */         return null; 
/* 174 */       int index = Mth.clamp(delta + children().indexOf(getFocused()), 0, children().size() - 1);
/*     */       int i;
/* 175 */       for (i = index; i >= 0 && i < children().size(); i += delta) {
/* 176 */         GuiEventListener child = children().get(i);
/* 177 */         ComponentPath componentPath = child.nextFocusPath(navigationEvent);
/* 178 */         if (componentPath != null)
/* 179 */           return ComponentPath.path(this, componentPath); 
/*     */       } 
/*     */     } 
/* 184 */     return super.nextFocusPath(navigationEvent);
/*     */   }
/*     */   
/*     */   void updateNarration(NarrationElementOutput output) {
/* 191 */     List<? extends NarratableEntry> narratables = narratables();
/* 192 */     Screen.NarratableSearchResult result = Screen.findNarratableWidget(narratables, this.lastNarratable);
/* 193 */     if (result != null) {
/* 194 */       if (result.priority().isTerminal())
/* 195 */         this.lastNarratable = result.entry(); 
/* 197 */       if (narratables.size() > 1)
/* 198 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.object_list", new Object[] { Integer.valueOf(result.index() + 1), Integer.valueOf(narratables.size()) })); 
/* 200 */       result.entry().updateNarration(output.nest());
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract List<? extends NarratableEntry> narratables();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ContainerObjectSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */