/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.TypedInstance;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public abstract class DebugEntryLookingAt implements DebugScreenEntry {
/*     */   private static final int RANGE = 20;
/*     */   
/*  27 */   private static final Identifier BLOCK_GROUP = Identifier.withDefaultNamespace("looking_at_block");
/*     */   
/*  28 */   private static final Identifier FLUID_GROUP = Identifier.withDefaultNamespace("looking_at_fluid");
/*     */   
/*     */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/*  32 */     Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
/*  33 */     Level clientOrServerLevel = SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES ? serverOrClientLevel : (Level)(Minecraft.getInstance()).level;
/*  34 */     if (cameraEntity == null || clientOrServerLevel == null)
/*     */       return; 
/*  38 */     HitResult block = getHitResult(cameraEntity);
/*  39 */     List<String> result = new ArrayList<>();
/*  41 */     if (block.getType() == HitResult.Type.BLOCK) {
/*  42 */       BlockPos pos = ((BlockHitResult)block).getBlockPos();
/*  43 */       extractInfo(result, clientOrServerLevel, pos);
/*     */     } 
/*  47 */     displayer.addToGroup(group(), result);
/*     */   }
/*     */   
/*     */   public abstract HitResult getHitResult(Entity paramEntity);
/*     */   
/*     */   public abstract void extractInfo(List<String> paramList, Level paramLevel, BlockPos paramBlockPos);
/*     */   
/*     */   public abstract Identifier group();
/*     */   
/*     */   public static abstract class DebugEntryLookingAtState<OwnerType, StateType extends StateHolder<OwnerType, StateType> & TypedInstance<OwnerType>> extends DebugEntryLookingAt {
/*     */     private final String prefix;
/*     */     
/*     */     protected DebugEntryLookingAtState(String prefix) {
/*  60 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public void extractInfo(List<String> result, Level level, BlockPos pos) {
/*  67 */       StateType stateInstance = getInstance(level, pos);
/*  69 */       result.add(String.valueOf(ChatFormatting.UNDERLINE) + String.valueOf(ChatFormatting.UNDERLINE) + ": " + this.prefix + ", " + pos.getX() + ", " + pos.getY());
/*  70 */       result.add(((TypedInstance)stateInstance).typeHolder().getRegisteredName());
/*  72 */       addStateProperties(result, (StateHolder<?, ?>)stateInstance);
/*     */     }
/*     */     
/*     */     private static void addStateProperties(List<String> result, StateHolder<?, ?> stateHolder) {
/*  76 */       stateHolder.getValues().forEach(entry -> result.add(getPropertyValueString(entry)));
/*     */     }
/*     */     
/*     */     private static String getPropertyValueString(Property.Value<?> entry) {
/*  80 */       String valueString = entry.valueName();
/*  82 */       if (Boolean.TRUE.equals(entry.value())) {
/*  83 */         valueString = String.valueOf(ChatFormatting.GREEN) + String.valueOf(ChatFormatting.GREEN);
/*  84 */       } else if (Boolean.FALSE.equals(entry.value())) {
/*  85 */         valueString = String.valueOf(ChatFormatting.RED) + String.valueOf(ChatFormatting.RED);
/*     */       } 
/*  88 */       return entry.property().getName() + ": " + entry.property().getName();
/*     */     }
/*     */     
/*     */     protected abstract StateType getInstance(Level param1Level, BlockPos param1BlockPos);
/*     */   }
/*     */   
/*     */   public static abstract class DebugEntryLookingAtTags<T extends TypedInstance<?>> extends DebugEntryLookingAt {
/*     */     protected abstract T getInstance(Level param1Level, BlockPos param1BlockPos);
/*     */     
/*     */     public void extractInfo(List<String> result, Level level, BlockPos pos) {
/*  97 */       T instance = getInstance(level, pos);
/*  98 */       addTagEntries(result, (TypedInstance<?>)instance);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockStateInfo extends DebugEntryLookingAtState<Block, BlockState> {
/*     */     protected BlockStateInfo() {
/* 104 */       super("Targeted Block");
/*     */     }
/*     */     
/*     */     public HitResult getHitResult(Entity cameraEntity) {
/* 109 */       return cameraEntity.pick(20.0D, 0.0F, false);
/*     */     }
/*     */     
/*     */     public BlockState getInstance(Level level, BlockPos pos) {
/* 114 */       return level.getBlockState(pos);
/*     */     }
/*     */     
/*     */     public Identifier group() {
/* 119 */       return DebugEntryLookingAt.BLOCK_GROUP;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockTagInfo extends DebugEntryLookingAtTags<BlockState> {
/*     */     public HitResult getHitResult(Entity cameraEntity) {
/* 126 */       return cameraEntity.pick(20.0D, 0.0F, false);
/*     */     }
/*     */     
/*     */     public BlockState getInstance(Level level, BlockPos pos) {
/* 131 */       return level.getBlockState(pos);
/*     */     }
/*     */     
/*     */     public Identifier group() {
/* 136 */       return DebugEntryLookingAt.BLOCK_GROUP;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FluidStateInfo extends DebugEntryLookingAtState<Fluid, FluidState> {
/*     */     protected FluidStateInfo() {
/* 142 */       super("Targeted Fluid");
/*     */     }
/*     */     
/*     */     public HitResult getHitResult(Entity cameraEntity) {
/* 147 */       return cameraEntity.pick(20.0D, 0.0F, true);
/*     */     }
/*     */     
/*     */     public FluidState getInstance(Level level, BlockPos pos) {
/* 152 */       return level.getFluidState(pos);
/*     */     }
/*     */     
/*     */     public Identifier group() {
/* 157 */       return DebugEntryLookingAt.FLUID_GROUP;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FluidTagInfo extends DebugEntryLookingAtTags<FluidState> {
/*     */     public HitResult getHitResult(Entity cameraEntity) {
/* 164 */       return cameraEntity.pick(20.0D, 0.0F, true);
/*     */     }
/*     */     
/*     */     public FluidState getInstance(Level level, BlockPos pos) {
/* 169 */       return level.getFluidState(pos);
/*     */     }
/*     */     
/*     */     public Identifier group() {
/* 174 */       return DebugEntryLookingAt.FLUID_GROUP;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addTagEntries(List<String> result, TypedInstance<?> instance) {
/* 179 */     Objects.requireNonNull(result);
/* 179 */     instance.tags().map(e -> "#" + String.valueOf(e.location())).forEach(result::add);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */