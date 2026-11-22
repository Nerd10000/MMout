/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class MultipleTestTracker {
/*     */   private static final char NOT_STARTED_TEST_CHAR = ' ';
/*     */   
/*     */   private static final char ONGOING_TEST_CHAR = '_';
/*     */   
/*     */   private static final char SUCCESSFUL_TEST_CHAR = '+';
/*     */   
/*     */   private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';
/*     */   
/*     */   private static final char FAILED_REQUIRED_TEST_CHAR = 'X';
/*     */   
/*  16 */   private final Collection<GameTestInfo> tests = Lists.newArrayList();
/*     */   
/*  17 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */   
/*     */   public MultipleTestTracker(Collection<GameTestInfo> tests) {
/*  23 */     this.tests.addAll(tests);
/*     */   }
/*     */   
/*     */   public void addTestToTrack(GameTestInfo testInfo) {
/*  27 */     this.tests.add(testInfo);
/*  28 */     Objects.requireNonNull(testInfo);
/*  28 */     this.listeners.forEach(testInfo::addListener);
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener listener) {
/*  32 */     this.listeners.add(listener);
/*  33 */     this.tests.forEach(testInfo -> testInfo.addListener(listener));
/*     */   }
/*     */   
/*     */   public void addFailureListener(final Consumer<GameTestInfo> listener) {
/*  37 */     addListener(new GameTestListener(this) {
/*     */           public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */           
/*     */           public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {}
/*     */           
/*     */           public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/*  48 */             listener.accept(testInfo);
/*     */           }
/*     */           
/*     */           public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */         });
/*     */   }
/*     */   
/*     */   public int getFailedRequiredCount() {
/*  58 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).count();
/*     */   }
/*     */   
/*     */   public int getFailedOptionalCount() {
/*  62 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).count();
/*     */   }
/*     */   
/*     */   public int getDoneCount() {
/*  66 */     return (int)this.tests.stream().filter(GameTestInfo::isDone).count();
/*     */   }
/*     */   
/*     */   public boolean hasFailedRequired() {
/*  70 */     return (getFailedRequiredCount() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasFailedOptional() {
/*  74 */     return (getFailedOptionalCount() > 0);
/*     */   }
/*     */   
/*     */   public Collection<GameTestInfo> getFailedRequired() {
/*  78 */     return (Collection<GameTestInfo>)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public Collection<GameTestInfo> getFailedOptional() {
/*  82 */     return (Collection<GameTestInfo>)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public int getTotalCount() {
/*  86 */     return this.tests.size();
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  90 */     return (getDoneCount() == getTotalCount());
/*     */   }
/*     */   
/*     */   public String getProgressBar() {
/*  94 */     StringBuffer buf = new StringBuffer();
/*  95 */     buf.append('[');
/*  96 */     this.tests.forEach(test -> {
/*     */           if (!test.hasStarted()) {
/*     */             buf.append(' ');
/*     */           } else if (test.hasSucceeded()) {
/*     */             buf.append('+');
/*     */           } else if (test.hasFailed()) {
/*     */             buf.append(test.isRequired() ? 88 : 120);
/*     */           } else {
/*     */             buf.append('_');
/*     */           } 
/*     */         });
/* 107 */     buf.append(']');
/* 108 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 113 */     return getProgressBar();
/*     */   }
/*     */   
/*     */   public void remove(GameTestInfo testInfo) {
/* 117 */     this.tests.remove(testInfo);
/*     */   }
/*     */   
/*     */   public MultipleTestTracker() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\MultipleTestTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */