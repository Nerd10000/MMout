/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class DebugEntityNameGenerator {
/* 15 */   private static final String[] NAMES_FIRST_PART = new String[] { 
/* 15 */       "Slim", "Far", "River", "Silly", "Fat", "Thin", "Fish", "Bat", "Dark", "Oak", 
/* 15 */       "Sly", "Bush", "Zen", "Bark", "Cry", "Slack", "Soup", "Grim", "Hook", "Dirt", 
/* 15 */       "Mud", "Sad", "Hard", "Crook", "Sneak", "Stink", "Weird", "Fire", "Soot", "Soft", 
/* 15 */       "Rough", "Cling", "Scar" };
/*    */   
/* 22 */   private static final String[] NAMES_SECOND_PART = new String[] { 
/* 22 */       "Fox", "Tail", "Jaw", "Whisper", "Twig", "Root", "Finder", "Nose", "Brow", "Blade", 
/* 22 */       "Fry", "Seek", "Wart", "Tooth", "Foot", "Leaf", "Stone", "Fall", "Face", "Tongue", 
/* 22 */       "Voice", "Lip", "Mouth", "Snail", "Toe", "Ear", "Hair", "Beard", "Shirt", "Fist" };
/*    */   
/*    */   public static String getEntityName(Entity entity) {
/* 29 */     if (entity instanceof net.minecraft.world.entity.player.Player)
/* 30 */       return entity.getPlainTextName(); 
/* 32 */     Component customName = entity.getCustomName();
/* 33 */     if (customName != null)
/* 34 */       return customName.getString(); 
/* 36 */     return getEntityName(entity.getUUID());
/*    */   }
/*    */   
/*    */   public static String getEntityName(UUID uuid) {
/* 44 */     RandomSource random = RandomSource.createThreadLocalInstance((uuid.hashCode() >> 2));
/* 45 */     return getRandomString(random, NAMES_FIRST_PART) + getRandomString(random, NAMES_FIRST_PART);
/*    */   }
/*    */   
/*    */   private static String getRandomString(RandomSource random, String[] names) {
/* 49 */     return (String)Util.getRandom((Object[])names, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\DebugEntityNameGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */