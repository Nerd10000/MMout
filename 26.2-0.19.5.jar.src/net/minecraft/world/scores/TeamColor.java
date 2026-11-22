/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.TextColor;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum TeamColor implements StringRepresentable {
/* 15 */   BLACK(0, "black", TextColor.BLACK, DisplaySlot.TEAM_BLACK),
/* 16 */   DARK_BLUE(1, "dark_blue", TextColor.DARK_BLUE, DisplaySlot.TEAM_DARK_BLUE),
/* 17 */   DARK_GREEN(2, "dark_green", TextColor.DARK_GREEN, DisplaySlot.TEAM_DARK_GREEN),
/* 18 */   DARK_AQUA(3, "dark_aqua", TextColor.DARK_AQUA, DisplaySlot.TEAM_DARK_AQUA),
/* 19 */   DARK_RED(4, "dark_red", TextColor.DARK_RED, DisplaySlot.TEAM_DARK_RED),
/* 20 */   DARK_PURPLE(5, "dark_purple", TextColor.DARK_PURPLE, DisplaySlot.TEAM_DARK_PURPLE),
/* 21 */   GOLD(6, "gold", TextColor.GOLD, DisplaySlot.TEAM_GOLD),
/* 22 */   GRAY(7, "gray", TextColor.GRAY, DisplaySlot.TEAM_GRAY),
/* 23 */   DARK_GRAY(8, "dark_gray", TextColor.DARK_GRAY, DisplaySlot.TEAM_DARK_GRAY),
/* 24 */   BLUE(9, "blue", TextColor.BLUE, DisplaySlot.TEAM_BLUE),
/* 25 */   GREEN(10, "green", TextColor.GREEN, DisplaySlot.TEAM_GREEN),
/* 26 */   AQUA(11, "aqua", TextColor.AQUA, DisplaySlot.TEAM_AQUA),
/* 27 */   RED(12, "red", TextColor.RED, DisplaySlot.TEAM_RED),
/* 28 */   LIGHT_PURPLE(13, "light_purple", TextColor.LIGHT_PURPLE, DisplaySlot.TEAM_LIGHT_PURPLE),
/* 29 */   YELLOW(14, "yellow", TextColor.YELLOW, DisplaySlot.TEAM_YELLOW),
/* 30 */   WHITE(15, "white", TextColor.WHITE, DisplaySlot.TEAM_WHITE);
/*    */   
/*    */   public static final List<TeamColor> VALUES;
/*    */   
/*    */   public static final StringRepresentable.EnumCodec<TeamColor> CODEC;
/*    */   
/*    */   private static final IntFunction<TeamColor> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, TeamColor> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final TextColor format;
/*    */   
/*    */   private final DisplaySlot displaySlot;
/*    */   
/*    */   static {
/* 33 */     VALUES = List.of(values());
/* 35 */     CODEC = StringRepresentable.fromEnum(TeamColor::values);
/* 37 */     BY_ID = ByIdMap.continuous(v -> v.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 38 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
/*    */   }
/*    */   
/*    */   TeamColor(int id, String name, TextColor format, DisplaySlot displaySlot) {
/* 46 */     this.id = id;
/* 47 */     this.name = name;
/* 48 */     this.format = format;
/* 49 */     this.displaySlot = displaySlot;
/*    */   }
/*    */   
/*    */   public static TeamColor byName(String name) {
/* 53 */     return (TeamColor)CODEC.byName(name);
/*    */   }
/*    */   
/*    */   public TextColor textColor() {
/* 57 */     return this.format;
/*    */   }
/*    */   
/*    */   public DisplaySlot displaySlot() {
/* 61 */     return this.displaySlot;
/*    */   }
/*    */   
/*    */   public int rgb() {
/* 65 */     return this.format.getValue();
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 70 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\TeamColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */