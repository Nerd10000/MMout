/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ public enum SoundSource {
/*  4 */   MASTER("master"),
/*  5 */   MUSIC("music"),
/*  6 */   RECORDS("record"),
/*  7 */   WEATHER("weather"),
/*  8 */   BLOCKS("block"),
/*  9 */   HOSTILE("hostile"),
/* 10 */   NEUTRAL("neutral"),
/* 11 */   PLAYERS("player"),
/* 12 */   AMBIENT("ambient"),
/* 13 */   VOICE("voice"),
/* 14 */   UI("ui");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SoundSource(String name) {
/* 20 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 24 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\sounds\SoundSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */