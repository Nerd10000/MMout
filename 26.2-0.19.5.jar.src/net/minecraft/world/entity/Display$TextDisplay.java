/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class TextDisplay extends Display {
/*     */   public static final String TAG_TEXT = "text";
/*     */   
/*     */   private static final String TAG_LINE_WIDTH = "line_width";
/*     */   
/*     */   private static final String TAG_TEXT_OPACITY = "text_opacity";
/*     */   
/*     */   private static final String TAG_BACKGROUND_COLOR = "background";
/*     */   
/*     */   private static final String TAG_SHADOW = "shadow";
/*     */   
/*     */   private static final String TAG_SEE_THROUGH = "see_through";
/*     */   
/*     */   private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background";
/*     */   
/*     */   private static final String TAG_ALIGNMENT = "alignment";
/*     */   
/*     */   public static final byte FLAG_SHADOW = 1;
/*     */   
/*     */   public static final byte FLAG_SEE_THROUGH = 2;
/*     */   
/*     */   public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4;
/*     */   
/*     */   public static final byte FLAG_ALIGN_LEFT = 8;
/*     */   
/*     */   public static final byte FLAG_ALIGN_RIGHT = 16;
/*     */   
/*     */   private static final byte INITIAL_TEXT_OPACITY = -1;
/*     */   
/*     */   public static final int INITIAL_BACKGROUND = 1073741824;
/*     */   
/*     */   private static final int INITIAL_LINE_WIDTH = 200;
/*     */   
/*     */   public enum Align implements StringRepresentable {
/* 659 */     CENTER("center"),
/* 660 */     LEFT("left"),
/* 661 */     RIGHT("right");
/*     */     
/* 663 */     public static final Codec<Align> CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Align(String name) {
/* 668 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 673 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/* 696 */   private static final EntityDataAccessor<Component> DATA_TEXT_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.COMPONENT);
/*     */   
/* 697 */   private static final EntityDataAccessor<Integer> DATA_LINE_WIDTH_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/*     */   
/* 698 */   private static final EntityDataAccessor<Integer> DATA_BACKGROUND_COLOR_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/*     */   
/* 699 */   private static final EntityDataAccessor<Byte> DATA_TEXT_OPACITY_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */   
/* 700 */   private static final EntityDataAccessor<Byte> DATA_STYLE_FLAGS_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */   
/* 702 */   private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(new int[] { DATA_TEXT_ID
/* 703 */         .id(), DATA_LINE_WIDTH_ID
/* 704 */         .id(), DATA_BACKGROUND_COLOR_ID
/* 705 */         .id(), DATA_TEXT_OPACITY_ID
/* 706 */         .id(), DATA_STYLE_FLAGS_ID
/* 707 */         .id() });
/*     */   
/*     */   private CachedInfo clientDisplayCache;
/*     */   
/*     */   private TextRenderState textRenderState;
/*     */   
/*     */   public TextDisplay(EntityType<?> type, Level level) {
/* 715 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 720 */     super.defineSynchedData(entityData);
/* 721 */     entityData.define(DATA_TEXT_ID, Component.empty());
/* 722 */     entityData.define(DATA_LINE_WIDTH_ID, Integer.valueOf(200));
/* 723 */     entityData.define(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(1073741824));
/* 724 */     entityData.define(DATA_TEXT_OPACITY_ID, Byte.valueOf((byte)-1));
/* 725 */     entityData.define(DATA_STYLE_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 730 */     super.onSyncedDataUpdated(accessor);
/* 732 */     if (TEXT_RENDER_STATE_IDS.contains(accessor.id()))
/* 733 */       this.updateRenderState = true; 
/*     */   }
/*     */   
/*     */   private Component getText() {
/* 738 */     return (Component)this.entityData.get(DATA_TEXT_ID);
/*     */   }
/*     */   
/*     */   private void setText(Component text) {
/* 742 */     this.entityData.set(DATA_TEXT_ID, text);
/*     */   }
/*     */   
/*     */   private int getLineWidth() {
/* 746 */     return ((Integer)this.entityData.get(DATA_LINE_WIDTH_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setLineWidth(int width) {
/* 750 */     this.entityData.set(DATA_LINE_WIDTH_ID, Integer.valueOf(width));
/*     */   }
/*     */   
/*     */   private byte getTextOpacity() {
/* 754 */     return ((Byte)this.entityData.get(DATA_TEXT_OPACITY_ID)).byteValue();
/*     */   }
/*     */   
/*     */   private void setTextOpacity(byte opacity) {
/* 758 */     this.entityData.set(DATA_TEXT_OPACITY_ID, Byte.valueOf(opacity));
/*     */   }
/*     */   
/*     */   private int getBackgroundColor() {
/* 762 */     return ((Integer)this.entityData.get(DATA_BACKGROUND_COLOR_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setBackgroundColor(int color) {
/* 766 */     this.entityData.set(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(color));
/*     */   }
/*     */   
/*     */   private byte getFlags() {
/* 770 */     return ((Byte)this.entityData.get(DATA_STYLE_FLAGS_ID)).byteValue();
/*     */   }
/*     */   
/*     */   private void setFlags(byte flags) {
/* 774 */     this.entityData.set(DATA_STYLE_FLAGS_ID, Byte.valueOf(flags));
/*     */   }
/*     */   
/*     */   private static byte loadFlag(byte flags, ValueInput input, String id, byte mask) {
/* 778 */     if (input.getBooleanOr(id, false))
/* 779 */       return (byte)(flags | mask); 
/* 781 */     return flags;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 786 */     super.readAdditionalSaveData(input);
/* 788 */     setLineWidth(input.getIntOr("line_width", 200));
/* 789 */     setTextOpacity(input.getByteOr("text_opacity", (byte)-1));
/* 790 */     setBackgroundColor(input.getIntOr("background", 1073741824));
/* 793 */     byte flags = loadFlag((byte)0, input, "shadow", (byte)1);
/* 794 */     flags = loadFlag(flags, input, "see_through", (byte)2);
/* 795 */     flags = loadFlag(flags, input, "default_background", (byte)4);
/* 797 */     Optional<Align> alignment = input.read("alignment", Align.CODEC);
/* 798 */     if (alignment.isPresent()) {
/* 799 */       switch (((Align)alignment.get()).ordinal()) {
/*     */         default:
/* 799 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */           break;
/*     */       } 
/* 799 */       flags = 
/*     */         
/* 802 */         (byte)(flags | 0x10);
/*     */     } 
/* 806 */     setFlags(flags);
/* 808 */     Optional<Component> text = input.read("text", ComponentSerialization.CODEC);
/* 809 */     if (text.isPresent())
/*     */       try {
/* 811 */         Level level = level();
/* 811 */         if (level instanceof ServerLevel) {
/* 811 */           ServerLevel serverLevel = (ServerLevel)level;
/* 812 */           CommandSourceStack context = createCommandSourceStackForNameResolution(serverLevel).withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/* 813 */           MutableComponent mutableComponent = ComponentUtils.resolve(ResolutionContext.create(context), text.get());
/* 814 */           setText((Component)mutableComponent);
/*     */         } else {
/* 816 */           setText((Component)Component.empty());
/*     */         } 
/* 818 */       } catch (Exception e) {
/* 819 */         Display.LOGGER.warn("Failed to parse display entity text {}", text, e);
/*     */       }  
/*     */   }
/*     */   
/*     */   private static void storeFlag(byte flags, ValueOutput output, String id, byte mask) {
/* 825 */     output.putBoolean(id, ((flags & mask) != 0));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 830 */     super.addAdditionalSaveData(output);
/* 831 */     output.store("text", ComponentSerialization.CODEC, getText());
/* 832 */     output.putInt("line_width", getLineWidth());
/* 833 */     output.putInt("background", getBackgroundColor());
/* 834 */     output.putByte("text_opacity", getTextOpacity());
/* 836 */     byte flags = getFlags();
/* 837 */     storeFlag(flags, output, "shadow", (byte)1);
/* 838 */     storeFlag(flags, output, "see_through", (byte)2);
/* 839 */     storeFlag(flags, output, "default_background", (byte)4);
/* 840 */     output.store("alignment", Align.CODEC, getAlign(flags));
/*     */   }
/*     */   
/*     */   protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 845 */     if (shouldInterpolate && this.textRenderState != null) {
/* 846 */       this.textRenderState = createInterpolatedTextRenderState(this.textRenderState, progress);
/*     */     } else {
/* 848 */       this.textRenderState = createFreshTextRenderState();
/*     */     } 
/* 850 */     this.clientDisplayCache = null;
/*     */   }
/*     */   
/*     */   public TextRenderState textRenderState() {
/* 854 */     return this.textRenderState;
/*     */   }
/*     */   
/*     */   private TextRenderState createFreshTextRenderState() {
/* 858 */     return new TextRenderState(
/* 859 */         getText(), 
/* 860 */         getLineWidth(), 
/* 861 */         Display.IntInterpolator.constant(getTextOpacity()), 
/* 862 */         Display.IntInterpolator.constant(getBackgroundColor()), 
/* 863 */         getFlags());
/*     */   }
/*     */   
/*     */   private TextRenderState createInterpolatedTextRenderState(TextRenderState previous, float progress) {
/* 868 */     int currentBackground = previous.backgroundColor.get(progress);
/* 869 */     int currentOpacity = previous.textOpacity.get(progress);
/* 871 */     return new TextRenderState(
/* 872 */         getText(), 
/* 873 */         getLineWidth(), new Display.LinearIntInterpolator(currentOpacity, 
/* 874 */           getTextOpacity()), new Display.ColorInterpolator(currentBackground, 
/* 875 */           getBackgroundColor()), 
/* 876 */         getFlags());
/*     */   }
/*     */   
/*     */   public static final class CachedLine extends Record {
/*     */     private final FormattedCharSequence contents;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     public CachedLine(FormattedCharSequence contents, int width) {
/* 880 */       this.contents = contents;
/* 880 */       this.width = width;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FormattedCharSequence contents() {
/* 880 */       return this.contents;
/*     */     }
/*     */     
/*     */     public int width() {
/* 880 */       return this.width;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CachedInfo extends Record {
/*     */     private final List<Display.TextDisplay.CachedLine> lines;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) {
/* 882 */       this.lines = lines;
/* 882 */       this.width = width;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Display.TextDisplay.CachedLine> lines() {
/* 882 */       return this.lines;
/*     */     }
/*     */     
/*     */     public int width() {
/* 882 */       return this.width;
/*     */     }
/*     */   }
/*     */   
/*     */   public CachedInfo cacheDisplay(LineSplitter splitter) {
/* 890 */     if (this.clientDisplayCache == null)
/* 891 */       if (this.textRenderState != null) {
/* 892 */         this.clientDisplayCache = splitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
/*     */       } else {
/* 894 */         this.clientDisplayCache = new CachedInfo(List.of(), 0);
/*     */       }  
/* 898 */     return this.clientDisplayCache;
/*     */   }
/*     */   
/*     */   public static Align getAlign(byte flags) {
/* 902 */     if ((flags & 0x8) != 0)
/* 903 */       return Align.LEFT; 
/* 905 */     if ((flags & 0x10) != 0)
/* 906 */       return Align.RIGHT; 
/* 908 */     return Align.CENTER;
/*     */   }
/*     */   
/*     */   public static final class TextRenderState extends Record {
/*     */     private final Component text;
/*     */     
/*     */     private final int lineWidth;
/*     */     
/*     */     private final Display.IntInterpolator textOpacity;
/*     */     
/*     */     private final Display.IntInterpolator backgroundColor;
/*     */     
/*     */     private final byte flags;
/*     */     
/*     */     public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) {
/* 911 */       this.text = text;
/* 911 */       this.lineWidth = lineWidth;
/* 911 */       this.textOpacity = textOpacity;
/* 911 */       this.backgroundColor = backgroundColor;
/* 911 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component text() {
/* 911 */       return this.text;
/*     */     }
/*     */     
/*     */     public int lineWidth() {
/* 911 */       return this.lineWidth;
/*     */     }
/*     */     
/*     */     public Display.IntInterpolator textOpacity() {
/* 911 */       return this.textOpacity;
/*     */     }
/*     */     
/*     */     public Display.IntInterpolator backgroundColor() {
/* 911 */       return this.backgroundColor;
/*     */     }
/*     */     
/*     */     public byte flags() {
/* 911 */       return this.flags;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LineSplitter {
/*     */     Display.TextDisplay.CachedInfo split(Component param2Component, int param2Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$TextDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */