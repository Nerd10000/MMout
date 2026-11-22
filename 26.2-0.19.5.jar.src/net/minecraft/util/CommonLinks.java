/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.util.UndashedUuid;
/*    */ import java.net.URI;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class CommonLinks {
/* 10 */   public static final URI GDPR = URI.create("https://aka.ms/MinecraftGDPR");
/*    */   
/* 11 */   public static final URI EULA = URI.create("https://aka.ms/MinecraftEULA");
/*    */   
/* 14 */   public static final URI PRIVACY_STATEMENT = URI.create("http://go.microsoft.com/fwlink/?LinkId=521839");
/*    */   
/* 16 */   public static final URI ATTRIBUTION = URI.create("https://aka.ms/MinecraftJavaAttribution");
/*    */   
/* 17 */   public static final URI LICENSES = URI.create("https://aka.ms/MinecraftJavaLicenses");
/*    */   
/* 19 */   public static final URI BUY_MINECRAFT_JAVA = URI.create("https://aka.ms/BuyMinecraftJava");
/*    */   
/* 20 */   public static final URI ACCOUNT_SETTINGS = URI.create("https://aka.ms/JavaAccountSettings");
/*    */   
/* 22 */   public static final URI SNAPSHOT_FEEDBACK = URI.create("https://aka.ms/snapshotfeedback?ref=game");
/*    */   
/* 23 */   public static final URI RELEASE_FEEDBACK = URI.create("https://aka.ms/javafeedback?ref=game");
/*    */   
/* 24 */   public static final URI SNAPSHOT_BUGS_FEEDBACK = URI.create("https://aka.ms/snapshotbugs?ref=game");
/*    */   
/* 26 */   public static final URI GENERAL_HELP = URI.create("https://aka.ms/Minecraft-Support");
/*    */   
/* 27 */   public static final URI ACCESSIBILITY_HELP = URI.create("https://aka.ms/MinecraftJavaAccessibility");
/*    */   
/* 28 */   public static final URI REPORTING_HELP = URI.create("https://aka.ms/aboutjavareporting");
/*    */   
/* 29 */   public static final URI SUSPENSION_HELP = URI.create("https://aka.ms/mcjavamoderation");
/*    */   
/* 30 */   public static final URI BLOCKING_HELP = URI.create("https://aka.ms/javablocking");
/*    */   
/* 31 */   public static final URI SYMLINK_HELP = URI.create("https://aka.ms/MinecraftSymLinks");
/*    */   
/* 33 */   public static final URI PRIVACY_AND_ONLINE_SETTINGS = URI.create("https://aka.ms/MinecraftJavaXboxPrivacyAndSafety");
/*    */   
/* 35 */   public static final URI START_REALMS_TRIAL = URI.create("https://aka.ms/startjavarealmstrial");
/*    */   
/* 36 */   public static final URI BUY_REALMS = URI.create("https://aka.ms/BuyJavaRealms");
/*    */   
/* 37 */   public static final URI REALMS_TERMS = URI.create("https://aka.ms/MinecraftRealmsTerms");
/*    */   
/* 38 */   public static final URI REALMS_CONTENT_CREATION = URI.create("https://aka.ms/MinecraftRealmsContentCreator");
/*    */   
/*    */   public static final String EXTEND_REALMS_LINK = "https://aka.ms/ExtendJavaRealms";
/*    */   
/*    */   public static final String INTENTIONAL_GAME_DESIGN_BUG_ID = "MCPE-28723";
/*    */   
/* 43 */   public static final URI INTENTIONAL_GAME_DESIGN_BUG = URI.create("https://bugs.mojang.com/browse/MCPE-28723");
/*    */   
/*    */   public static String extendRealms(String subscriptionId, UUID profileId, boolean trial) {
/* 46 */     if (subscriptionId == null)
/* 47 */       return "https://aka.ms/ExtendJavaRealms"; 
/* 49 */     return extendRealms(subscriptionId, profileId) + "&ref=" + extendRealms(subscriptionId, profileId);
/*    */   }
/*    */   
/*    */   public static String extendRealms(String subscriptionId, UUID profileId) {
/* 53 */     if (subscriptionId == null)
/* 54 */       return "https://aka.ms/ExtendJavaRealms"; 
/* 56 */     return "https://aka.ms/ExtendJavaRealms?subscriptionId=" + subscriptionId + "&profileId=" + UndashedUuid.toString(profileId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CommonLinks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */