/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class StepBasedCollector implements InsideBlockEffectApplier {
/*  43 */   private static final InsideBlockEffectType[] APPLY_ORDER = InsideBlockEffectType.values();
/*     */   
/*     */   private static final int NO_STEP = -1;
/*     */   
/*  46 */   private final Set<InsideBlockEffectType> effectsInStep = EnumSet.noneOf(InsideBlockEffectType.class);
/*     */   
/*  47 */   private final Map<InsideBlockEffectType, List<Consumer<Entity>>> beforeEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, type -> new ArrayList());
/*     */   
/*  48 */   private final Map<InsideBlockEffectType, List<Consumer<Entity>>> afterEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, type -> new ArrayList());
/*     */   
/*  50 */   private final List<Consumer<Entity>> finalEffects = new ArrayList<>();
/*     */   
/*  52 */   private int lastStep = -1;
/*     */   
/*     */   public void advanceStep(int step) {
/*  55 */     if (this.lastStep != step) {
/*  56 */       this.lastStep = step;
/*  57 */       flushStep();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyAndClear(Entity entity) {
/*  62 */     flushStep();
/*  63 */     for (Consumer<Entity> effect : this.finalEffects) {
/*  64 */       if (!entity.isAlive())
/*     */         break; 
/*  67 */       effect.accept(entity);
/*     */     } 
/*  69 */     this.finalEffects.clear();
/*  70 */     this.lastStep = -1;
/*     */   }
/*     */   
/*     */   private void flushStep() {
/*  74 */     for (InsideBlockEffectType type : APPLY_ORDER) {
/*  75 */       List<Consumer<Entity>> beforeEffects = this.beforeEffectsInStep.get(type);
/*  76 */       this.finalEffects.addAll(beforeEffects);
/*  77 */       beforeEffects.clear();
/*  79 */       if (this.effectsInStep.remove(type))
/*  80 */         this.finalEffects.add(type.effect()); 
/*  83 */       List<Consumer<Entity>> afterEffects = this.afterEffectsInStep.get(type);
/*  84 */       this.finalEffects.addAll(afterEffects);
/*  85 */       afterEffects.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void apply(InsideBlockEffectType type) {
/*  91 */     this.effectsInStep.add(type);
/*     */   }
/*     */   
/*     */   public void runBefore(InsideBlockEffectType type, Consumer<Entity> effect) {
/*  96 */     ((List<Consumer<Entity>>)this.beforeEffectsInStep.get(type)).add(effect);
/*     */   }
/*     */   
/*     */   public void runAfter(InsideBlockEffectType type, Consumer<Entity> effect) {
/* 101 */     ((List<Consumer<Entity>>)this.afterEffectsInStep.get(type)).add(effect);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\InsideBlockEffectApplier$StepBasedCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */