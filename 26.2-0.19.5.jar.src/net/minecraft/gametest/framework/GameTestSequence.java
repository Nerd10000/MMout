/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class GameTestSequence {
/*     */   private final GameTestInfo parent;
/*     */   
/*     */   public class Condition {
/*     */     private static final int NOT_TRIGGERED = -1;
/*     */     
/*  15 */     private int triggerTime = -1;
/*     */     
/*     */     public void trigger(int time) {
/*  18 */       if (this.triggerTime != -1)
/*  19 */         throw new IllegalStateException("Condition already triggered at " + this.triggerTime); 
/*  21 */       this.triggerTime = time;
/*     */     }
/*     */     
/*     */     public void assertTriggeredThisTick() {
/*  25 */       int tick = GameTestSequence.this.parent.getTick();
/*  26 */       if (this.triggerTime != tick) {
/*  27 */         if (this.triggerTime == -1)
/*  28 */           throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_not_triggered"), tick); 
/*  30 */         throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_already_triggered", new Object[] { Integer.valueOf(this.triggerTime) }), tick);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*  37 */   private final List<GameTestEvent> events = Lists.newArrayList();
/*     */   
/*     */   private int lastTick;
/*     */   
/*     */   public GameTestSequence(GameTestInfo parent) {
/*  41 */     this.parent = parent;
/*  42 */     this.lastTick = parent.getTick();
/*     */   }
/*     */   
/*     */   public GameTestSequence thenWaitUntil(Runnable assertion) {
/*  47 */     this.events.add(GameTestEvent.create(assertion));
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenWaitUntil(long expectedDelay, Runnable assertion) {
/*  53 */     this.events.add(GameTestEvent.create(expectedDelay, assertion));
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenWaitAtLeast(long minimumDelay, Runnable assertion) {
/*  59 */     this.events.add(GameTestEvent.createWithMinimumDelay(minimumDelay, assertion));
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenIdle(int delta) {
/*  64 */     return thenExecuteAfter(delta, () -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public GameTestSequence thenExecute(Runnable assertion) {
/*  68 */     this.events.add(GameTestEvent.create(() -> executeWithoutFail(assertion)));
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenExecuteAfter(int delta, Runnable after) {
/*  73 */     this.events.add(GameTestEvent.create(() -> {
/*     */             if (this.parent.getTick() < this.lastTick + delta)
/*     */               throw new GameTestAssertException(Component.translatable("test.error.sequence.not_completed"), this.parent.getTick()); 
/*     */             executeWithoutFail(after);
/*     */           }));
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenExecuteFor(int delta, Runnable check) {
/*  83 */     this.events.add(GameTestEvent.create(() -> {
/*     */             if (this.parent.getTick() < this.lastTick + delta) {
/*     */               executeWithoutFail(check);
/*     */               throw new GameTestAssertException(Component.translatable("test.error.sequence.not_completed"), this.parent.getTick());
/*     */             } 
/*     */           }));
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public void thenSucceed() {
/*  93 */     Objects.requireNonNull(this.parent);
/*  93 */     this.events.add(GameTestEvent.create(this.parent::succeed));
/*     */   }
/*     */   
/*     */   public void thenFail(Supplier<GameTestException> e) {
/*  97 */     this.events.add(GameTestEvent.create(() -> this.parent.fail(e.get())));
/*     */   }
/*     */   
/*     */   public Condition thenTrigger() {
/* 101 */     Condition result = new Condition();
/* 102 */     this.events.add(GameTestEvent.create(() -> result.trigger(this.parent.getTick())));
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   public void tickAndContinue(int tick) {
/*     */     try {
/* 108 */       tick(tick);
/* 109 */     } catch (GameTestAssertException gameTestAssertException) {}
/*     */   }
/*     */   
/*     */   public void tickAndFailIfNotComplete(int tick) {
/*     */     try {
/* 115 */       tick(tick);
/* 116 */     } catch (GameTestAssertException e) {
/* 117 */       this.parent.fail(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeWithoutFail(Runnable assertion) {
/*     */     try {
/* 123 */       assertion.run();
/* 124 */     } catch (GameTestAssertException e) {
/* 125 */       this.parent.fail(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tick(int tick) {
/* 130 */     Iterator<GameTestEvent> iterator = this.events.iterator();
/* 131 */     while (iterator.hasNext()) {
/* 132 */       GameTestEvent event = iterator.next();
/* 133 */       event.assertion.run();
/* 134 */       iterator.remove();
/* 135 */       int delay = tick - this.lastTick;
/* 136 */       int prevTick = this.lastTick;
/* 137 */       this.lastTick = tick;
/* 138 */       if (event.minimumDelay != null && event.minimumDelay.longValue() > delay) {
/* 139 */         this.parent.fail(new GameTestAssertException((Component)Component.translatable("test.error.sequence.minimum_tick", new Object[] { Long.valueOf(prevTick + event.minimumDelay.longValue()) }), tick));
/*     */         break;
/*     */       } 
/* 142 */       if (event.expectedDelay != null && event.expectedDelay.longValue() != delay) {
/* 143 */         this.parent.fail(new GameTestAssertException((Component)Component.translatable("test.error.sequence.invalid_tick", new Object[] { Long.valueOf(prevTick + event.expectedDelay.longValue()) }), tick));
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestSequence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */