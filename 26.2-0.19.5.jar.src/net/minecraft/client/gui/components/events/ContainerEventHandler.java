/*     */ package net.minecraft.client.gui.components.events;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenAxis;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import org.joml.Vector2i;
/*     */ 
/*     */ public interface ContainerEventHandler extends GuiEventListener {
/*     */   default Optional<GuiEventListener> getChildAt(double x, double y) {
/*  31 */     for (GuiEventListener child : children()) {
/*  32 */       if (child.isMouseOver(x, y))
/*  33 */         return Optional.of(child); 
/*     */     } 
/*  36 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  41 */     Optional<GuiEventListener> child = getChildAt(event.x(), event.y());
/*  42 */     if (child.isEmpty())
/*  43 */       return false; 
/*  46 */     GuiEventListener widget = child.get();
/*  47 */     if (widget.mouseClicked(event, doubleClick) && 
/*  48 */       widget.shouldTakeFocusAfterInteraction()) {
/*  49 */       setFocused(widget);
/*  50 */       if (event.button() == 0)
/*  51 */         setDragging(true); 
/*     */     } 
/*  55 */     return true;
/*     */   }
/*     */   
/*     */   default boolean mouseReleased(MouseButtonEvent event) {
/*  60 */     if (event.button() == 0 && isDragging()) {
/*  61 */       setDragging(false);
/*  62 */       if (getFocused() != null)
/*  63 */         return getFocused().mouseReleased(event); 
/*     */     } 
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   default boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  71 */     if (getFocused() != null && isDragging() && event.button() == 0)
/*  72 */       return getFocused().mouseDragged(event, dx, dy); 
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   default boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/*  83 */     return getChildAt(x, y).filter(child -> child.mouseScrolled(x, y, scrollX, scrollY)).isPresent();
/*     */   }
/*     */   
/*     */   default boolean keyPressed(KeyEvent event) {
/*  89 */     return (getFocused() != null && getFocused().keyPressed(event));
/*     */   }
/*     */   
/*     */   default boolean keyReleased(KeyEvent event) {
/*  94 */     return (getFocused() != null && getFocused().keyReleased(event));
/*     */   }
/*     */   
/*     */   default boolean charTyped(CharacterEvent event) {
/*  99 */     return (getFocused() != null && getFocused().charTyped(event));
/*     */   }
/*     */   
/*     */   default boolean preeditUpdated(PreeditEvent event) {
/* 104 */     return (getFocused() != null && getFocused().preeditUpdated(event));
/*     */   }
/*     */   
/*     */   default ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 109 */     GuiEventListener focused = getFocused();
/* 110 */     return (focused != null) ? focused.getBorderForArrowNavigation(opposite) : super.getBorderForArrowNavigation(opposite);
/*     */   }
/*     */   
/*     */   default void setFocused(boolean focused) {
/* 119 */     if (!focused)
/* 120 */       setFocused((GuiEventListener)null); 
/*     */   }
/*     */   
/*     */   default boolean isFocused() {
/* 126 */     return (getFocused() != null);
/*     */   }
/*     */   
/*     */   default ComponentPath getCurrentFocusPath() {
/* 131 */     GuiEventListener focused = getFocused();
/* 132 */     if (focused != null)
/* 133 */       return ComponentPath.path(this, focused.getCurrentFocusPath()); 
/* 135 */     return null;
/*     */   }
/*     */   
/*     */   default ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 140 */     GuiEventListener focus = getFocused();
/* 143 */     if (focus != null) {
/* 144 */       ComponentPath focusPath = focus.nextFocusPath(navigationEvent);
/* 145 */       if (focusPath != null)
/* 146 */         return ComponentPath.path(this, focusPath); 
/*     */     } 
/* 151 */     if (navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
/* 151 */       FocusNavigationEvent.TabNavigation tabNavigation = (FocusNavigationEvent.TabNavigation)navigationEvent;
/* 152 */       return handleTabNavigation(tabNavigation);
/*     */     } 
/* 154 */     if (navigationEvent instanceof FocusNavigationEvent.ArrowNavigation) {
/* 154 */       FocusNavigationEvent.ArrowNavigation arrowNavigation = (FocusNavigationEvent.ArrowNavigation)navigationEvent;
/* 155 */       return handleArrowNavigation(arrowNavigation);
/*     */     } 
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   private ComponentPath handleTabNavigation(FocusNavigationEvent.TabNavigation tabNavigation) {
/*     */     int newIndex;
/* 162 */     boolean forward = tabNavigation.forward();
/* 163 */     GuiEventListener focus = getFocused();
/* 164 */     List<? extends GuiEventListener> sortedChildren = new ArrayList<>(children());
/* 165 */     Collections.sort(sortedChildren, Comparator.comparingInt(child -> child.getTabOrderGroup()));
/* 168 */     int index = sortedChildren.indexOf(focus);
/* 169 */     if (focus != null && index >= 0) {
/* 170 */       newIndex = index + (forward ? 1 : 0);
/* 172 */     } else if (forward) {
/* 173 */       newIndex = 0;
/*     */     } else {
/* 175 */       newIndex = sortedChildren.size();
/*     */     } 
/* 179 */     ListIterator<? extends GuiEventListener> iterator = sortedChildren.listIterator(newIndex);
/* 181 */     Objects.requireNonNull(iterator);
/* 181 */     Objects.requireNonNull(iterator);
/* 181 */     BooleanSupplier test = forward ? iterator::hasNext : iterator::hasPrevious;
/* 182 */     Objects.requireNonNull(iterator);
/* 182 */     Objects.requireNonNull(iterator);
/* 182 */     Supplier<? extends GuiEventListener> getter = forward ? iterator::next : iterator::previous;
/* 184 */     while (test.getAsBoolean()) {
/* 185 */       GuiEventListener child = getter.get();
/* 186 */       ComponentPath focusPath = child.nextFocusPath((FocusNavigationEvent)tabNavigation);
/* 187 */       if (focusPath != null)
/* 188 */         return ComponentPath.path(this, focusPath); 
/*     */     } 
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   private ComponentPath handleArrowNavigation(FocusNavigationEvent.ArrowNavigation arrowNavigation) {
/* 195 */     GuiEventListener focus = getFocused();
/* 196 */     ScreenDirection direction = arrowNavigation.direction();
/* 197 */     if (focus == null) {
/* 198 */       if (arrowNavigation.previousFocus() != null)
/* 199 */         return ComponentPath.path(this, nextFocusPathInDirection(arrowNavigation.previousFocus(), arrowNavigation.direction(), null, arrowNavigation)); 
/* 201 */       ScreenRectangle borderRectangle = getBorderForArrowNavigation(direction.getOpposite());
/* 202 */       return ComponentPath.path(this, nextFocusPathInDirection(borderRectangle, direction, null, arrowNavigation));
/*     */     } 
/* 204 */     ScreenRectangle focusedRectangle = focus.getBorderForArrowNavigation(direction);
/* 205 */     return ComponentPath.path(this, nextFocusPathInDirection(focusedRectangle, arrowNavigation.direction(), focus, arrowNavigation.with(focusedRectangle)));
/*     */   }
/*     */   
/*     */   private ComponentPath nextFocusPathInDirection(ScreenRectangle focusedRectangle, ScreenDirection direction, GuiEventListener excluded, FocusNavigationEvent.ArrowNavigation navigationEvent) {
/* 209 */     ScreenAxis axis = direction.getAxis();
/* 210 */     ScreenAxis otherAxis = axis.orthogonal();
/* 211 */     ScreenDirection positiveDirectionOtherAxis = otherAxis.getPositive();
/* 212 */     int focusedFirstBound = focusedRectangle.getBoundInDirection(direction.getOpposite());
/* 214 */     List<GuiEventListener> potentialChildren = new ArrayList<>();
/* 215 */     for (GuiEventListener child : children()) {
/* 216 */       if (child == excluded)
/*     */         continue; 
/* 219 */       ScreenRectangle childRectangle = child.getRectangle();
/* 220 */       if (childRectangle.overlapsInAxis(focusedRectangle, otherAxis)) {
/* 221 */         int childFirstBound = childRectangle.getBoundInDirection(direction.getOpposite());
/* 222 */         if (direction.isAfter(childFirstBound, focusedFirstBound)) {
/* 223 */           potentialChildren.add(child);
/*     */           continue;
/*     */         } 
/* 224 */         if (childFirstBound == focusedFirstBound && 
/* 225 */           direction.isAfter(childRectangle.getBoundInDirection(direction), focusedRectangle.getBoundInDirection(direction)))
/* 226 */           potentialChildren.add(child); 
/*     */       } 
/*     */     } 
/* 232 */     Comparator<GuiEventListener> primaryComparator = Comparator.comparing(child -> Integer.valueOf(child.getRectangle().getBoundInDirection(direction.getOpposite())), (Comparator<?>)direction.coordinateValueComparator());
/* 233 */     Comparator<GuiEventListener> secondaryComparator = Comparator.comparing(child -> Integer.valueOf(child.getRectangle().getBoundInDirection(positiveDirectionOtherAxis.getOpposite())), (Comparator<?>)positiveDirectionOtherAxis.coordinateValueComparator());
/* 234 */     potentialChildren.sort(primaryComparator.thenComparing(secondaryComparator));
/* 235 */     for (GuiEventListener child : potentialChildren) {
/* 236 */       ComponentPath componentPath = child.nextFocusPath((FocusNavigationEvent)navigationEvent);
/* 237 */       if (componentPath != null)
/* 238 */         return componentPath; 
/*     */     } 
/* 241 */     return nextFocusPathVaguelyInDirection(focusedRectangle, direction, excluded, (FocusNavigationEvent)navigationEvent);
/*     */   }
/*     */   
/*     */   private ComponentPath nextFocusPathVaguelyInDirection(ScreenRectangle focusedRectangle, ScreenDirection direction, GuiEventListener excluded, FocusNavigationEvent navigationEvent) {
/* 245 */     ScreenAxis axis = direction.getAxis();
/* 246 */     ScreenAxis otherAxis = axis.orthogonal();
/* 248 */     List<Pair<GuiEventListener, Long>> potentialChildren = new ArrayList<>();
/* 250 */     ScreenPosition focusedSideCenter = ScreenPosition.of(axis, focusedRectangle.getBoundInDirection(direction), focusedRectangle.getCenterInAxis(otherAxis));
/* 251 */     for (GuiEventListener child : children()) {
/* 252 */       if (child == excluded)
/*     */         continue; 
/* 255 */       ScreenRectangle childRectangle = child.getRectangle();
/* 256 */       ScreenPosition childOpposingSideCenter = ScreenPosition.of(axis, childRectangle.getBoundInDirection(direction.getOpposite()), childRectangle.getCenterInAxis(otherAxis));
/* 258 */       if (direction.isAfter(childOpposingSideCenter.getCoordinate(axis), focusedSideCenter.getCoordinate(axis))) {
/* 259 */         long distanceSquared = Vector2i.distanceSquared(focusedSideCenter.x(), focusedSideCenter.y(), childOpposingSideCenter.x(), childOpposingSideCenter.y());
/* 260 */         potentialChildren.add(Pair.of(child, Long.valueOf(distanceSquared)));
/*     */       } 
/*     */     } 
/* 263 */     potentialChildren.sort(Comparator.comparingDouble(Pair::getSecond));
/* 264 */     for (Pair<GuiEventListener, Long> child : potentialChildren) {
/* 265 */       ComponentPath componentPath = ((GuiEventListener)child.getFirst()).nextFocusPath(navigationEvent);
/* 266 */       if (componentPath != null)
/* 267 */         return componentPath; 
/*     */     } 
/* 270 */     return null;
/*     */   }
/*     */   
/*     */   List<? extends GuiEventListener> children();
/*     */   
/*     */   boolean isDragging();
/*     */   
/*     */   void setDragging(boolean paramBoolean);
/*     */   
/*     */   GuiEventListener getFocused();
/*     */   
/*     */   void setFocused(GuiEventListener paramGuiEventListener);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\events\ContainerEventHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */