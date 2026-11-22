/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DisplaySlot implements StringRepresentable {
/*  9 */   LIST(0, "list"),
/* 10 */   SIDEBAR(1, "sidebar"),
/* 11 */   BELOW_NAME(2, "below_name"),
/* 12 */   TEAM_BLACK(3, "sidebar.team.black"),
/* 13 */   TEAM_DARK_BLUE(4, "sidebar.team.dark_blue"),
/* 14 */   TEAM_DARK_GREEN(5, "sidebar.team.dark_green"),
/* 15 */   TEAM_DARK_AQUA(6, "sidebar.team.dark_aqua"),
/* 16 */   TEAM_DARK_RED(7, "sidebar.team.dark_red"),
/* 17 */   TEAM_DARK_PURPLE(8, "sidebar.team.dark_purple"),
/* 18 */   TEAM_GOLD(9, "sidebar.team.gold"),
/* 19 */   TEAM_GRAY(10, "sidebar.team.gray"),
/* 20 */   TEAM_DARK_GRAY(11, "sidebar.team.dark_gray"),
/* 21 */   TEAM_BLUE(12, "sidebar.team.blue"),
/* 22 */   TEAM_GREEN(13, "sidebar.team.green"),
/* 23 */   TEAM_AQUA(14, "sidebar.team.aqua"),
/* 24 */   TEAM_RED(15, "sidebar.team.red"),
/* 25 */   TEAM_LIGHT_PURPLE(16, "sidebar.team.light_purple"),
/* 26 */   TEAM_YELLOW(17, "sidebar.team.yellow"),
/* 27 */   TEAM_WHITE(18, "sidebar.team.white");
/*    */   
/*    */   public static final StringRepresentable.EnumCodec<DisplaySlot> CODEC;
/*    */   
/*    */   public static final IntFunction<DisplaySlot> BY_ID;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 30 */     CODEC = StringRepresentable.fromEnum(DisplaySlot::values);
/* 31 */     BY_ID = ByIdMap.continuous(DisplaySlot::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/*    */   
/*    */   DisplaySlot(int id, String name) {
/* 37 */     this.id = id;
/* 38 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int id() {
/* 42 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 47 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\DisplaySlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */