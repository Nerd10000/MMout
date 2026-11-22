/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ public class GoalSelector {
/*  14 */   private static final WrappedGoal NO_GOAL = new WrappedGoal(2147483647, new Goal() {
/*     */         public boolean canUse() {
/*  17 */           return false;
/*     */         }
/*     */       }) {
/*     */       public boolean isRunning() {
/*  22 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  26 */   private final Map<Goal.Flag, WrappedGoal> lockedFlags = new EnumMap<>(Goal.Flag.class);
/*     */   
/*  27 */   private final Set<WrappedGoal> availableGoals = (Set<WrappedGoal>)new ObjectLinkedOpenHashSet();
/*     */   
/*  28 */   private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);
/*     */   
/*     */   public void addGoal(int prio, Goal goal) {
/*  35 */     this.availableGoals.add(new WrappedGoal(prio, goal));
/*     */   }
/*     */   
/*     */   public void removeAllGoals(Predicate<Goal> predicate) {
/*  39 */     for (WrappedGoal availableGoal : this.availableGoals) {
/*  40 */       if (predicate.test(availableGoal.getGoal()) && availableGoal.isRunning())
/*  41 */         availableGoal.stop(); 
/*     */     } 
/*  44 */     this.availableGoals.removeIf(goal -> predicate.test(goal.getGoal()));
/*     */   }
/*     */   
/*     */   public void removeGoal(Goal toRemove) {
/*  48 */     removeAllGoals(goal -> (goal == toRemove));
/*     */   }
/*     */   
/*     */   private static boolean goalContainsAnyFlags(WrappedGoal goal, EnumSet<Goal.Flag> disabledFlags) {
/*  52 */     for (Goal.Flag flag : goal.getFlags()) {
/*  53 */       if (disabledFlags.contains(flag))
/*  54 */         return true; 
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean goalCanBeReplacedForAllFlags(WrappedGoal goal, Map<Goal.Flag, WrappedGoal> lockedFlags) {
/*  61 */     for (Goal.Flag flag : goal.getFlags()) {
/*  62 */       if (!((WrappedGoal)lockedFlags.getOrDefault(flag, NO_GOAL)).canBeReplacedBy(goal))
/*  63 */         return false; 
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  71 */     ProfilerFiller profiler = Profiler.get();
/*  73 */     profiler.push("goalCleanup");
/*  74 */     for (WrappedGoal goal : this.availableGoals) {
/*  75 */       if (goal.isRunning() && (goalContainsAnyFlags(goal, this.disabledFlags) || !goal.canContinueToUse()))
/*  76 */         goal.stop(); 
/*     */     } 
/*  79 */     this.lockedFlags.entrySet().removeIf(entry -> !((WrappedGoal)entry.getValue()).isRunning());
/*  80 */     profiler.pop();
/*  82 */     profiler.push("goalUpdate");
/*  83 */     for (WrappedGoal goal : this.availableGoals) {
/*  84 */       if (goal.isRunning() || goalContainsAnyFlags(goal, this.disabledFlags) || !goalCanBeReplacedForAllFlags(goal, this.lockedFlags) || !goal.canUse())
/*     */         continue; 
/*  87 */       for (Goal.Flag flag : goal.getFlags()) {
/*  88 */         WrappedGoal currentGoal = this.lockedFlags.getOrDefault(flag, NO_GOAL);
/*  89 */         currentGoal.stop();
/*  90 */         this.lockedFlags.put(flag, goal);
/*     */       } 
/*  92 */       goal.start();
/*     */     } 
/*  94 */     profiler.pop();
/*  96 */     tickRunningGoals(true);
/*     */   }
/*     */   
/*     */   public void tickRunningGoals(boolean forceTickAllRunningGoals) {
/* 100 */     ProfilerFiller profiler = Profiler.get();
/* 102 */     profiler.push("goalTick");
/* 103 */     for (WrappedGoal goal : this.availableGoals) {
/* 104 */       if (goal.isRunning() && (forceTickAllRunningGoals || goal.requiresUpdateEveryTick()))
/* 105 */         goal.tick(); 
/*     */     } 
/* 108 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public Set<WrappedGoal> getAvailableGoals() {
/* 112 */     return this.availableGoals;
/*     */   }
/*     */   
/*     */   public void disableControlFlag(Goal.Flag flag) {
/* 116 */     this.disabledFlags.add(flag);
/*     */   }
/*     */   
/*     */   public void enableControlFlag(Goal.Flag flag) {
/* 120 */     this.disabledFlags.remove(flag);
/*     */   }
/*     */   
/*     */   public void setControlFlag(Goal.Flag flag, boolean enabled) {
/* 124 */     if (enabled) {
/* 125 */       enableControlFlag(flag);
/*     */     } else {
/* 127 */       disableControlFlag(flag);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\GoalSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */