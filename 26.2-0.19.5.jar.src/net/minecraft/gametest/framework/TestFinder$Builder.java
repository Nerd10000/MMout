/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ 
/*    */ public class Builder {
/*    */   private final UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper;
/*    */   
/*    */   private final UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper;
/*    */   
/*    */   public Builder() {
/* 32 */     this.testFinderWrapper = (f -> f);
/* 33 */     this.structureBlockPosFinderWrapper = (f -> f);
/*    */   }
/*    */   
/*    */   private Builder(UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper, UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper) {
/* 40 */     this.testFinderWrapper = testFinderWrapper;
/* 41 */     this.structureBlockPosFinderWrapper = structureBlockPosFinderWrapper;
/*    */   }
/*    */   
/*    */   public Builder createMultipleCopies(int amount) {
/* 45 */     return new Builder(createCopies(amount), createCopies(amount));
/*    */   }
/*    */   
/*    */   private static <Q> UnaryOperator<Supplier<Stream<Q>>> createCopies(int amount) {
/* 49 */     return source -> {
/*    */         List<Q> copyList = new LinkedList<>();
/*    */         List<Q> sourceList = ((Stream<Q>)source.get()).toList();
/*    */         for (int i = 0; i < amount; i++)
/*    */           copyList.addAll(sourceList); 
/*    */         Objects.requireNonNull(copyList);
/*    */         return copyList::stream;
/*    */       };
/*    */   }
/*    */   
/*    */   private TestFinder build(CommandSourceStack source, TestInstanceFinder testInstanceFinder, TestPosFinder testPosFinder) {
/* 62 */     Objects.requireNonNull(testInstanceFinder);
/* 62 */     Objects.requireNonNull(this.testFinderWrapper.apply(testInstanceFinder::findTests));
/* 63 */     Objects.requireNonNull(testPosFinder);
/* 63 */     Objects.requireNonNull(this.structureBlockPosFinderWrapper.apply(testPosFinder::findTestPos));
/* 63 */     return new TestFinder(source, (Supplier)this.testFinderWrapper.apply(testInstanceFinder::findTests)::get, (Supplier)this.structureBlockPosFinderWrapper.apply(testPosFinder::findTestPos)::get);
/*    */   }
/*    */   
/*    */   public TestFinder radius(CommandContext<CommandSourceStack> sourceStack, int radius) {
/* 68 */     CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/* 69 */     BlockPos pos = BlockPos.containing((Position)source.getPosition());
/* 70 */     return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(pos, radius, source.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder nearest(CommandContext<CommandSourceStack> sourceStack) {
/* 74 */     CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/* 75 */     BlockPos pos = BlockPos.containing((Position)source.getPosition());
/* 76 */     return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findNearestTest(pos, 15, source.getLevel()).stream());
/*    */   }
/*    */   
/*    */   public TestFinder allNearby(CommandContext<CommandSourceStack> sourceStack) {
/* 80 */     CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/* 81 */     BlockPos pos = BlockPos.containing((Position)source.getPosition());
/* 82 */     return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(pos, 250, source.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder lookedAt(CommandContext<CommandSourceStack> sourceStack) {
/* 86 */     CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/* 87 */     return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.lookedAtTestPos(BlockPos.containing((Position)source.getPosition()), source.getPlayer().getCamera(), source.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder failedTests(CommandContext<CommandSourceStack> sourceStack, boolean onlyRequiredTests) {
/* 91 */     return build((CommandSourceStack)sourceStack.getSource(), () -> FailedTestTracker.getLastFailedTests().filter(()), TestFinder.NO_STRUCTURES);
/*    */   }
/*    */   
/*    */   public TestFinder byResourceSelection(CommandContext<CommandSourceStack> sourceStack, Collection<Holder.Reference<GameTestInstance>> holders) {
/* 95 */     Objects.requireNonNull(holders);
/* 95 */     return build((CommandSourceStack)sourceStack.getSource(), holders::stream, TestFinder.NO_STRUCTURES);
/*    */   }
/*    */   
/*    */   public TestFinder failedTests(CommandContext<CommandSourceStack> sourceStack) {
/* 99 */     return failedTests(sourceStack, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestFinder$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */