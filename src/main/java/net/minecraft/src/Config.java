package net.minecraft.src;

import java.awt.Dimension;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;

public class Config
{
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.12";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "C4";
    public static final String VERSION = "OptiFine_1.12_HD_U_C4";
    private static String newRelease = null;
    private static boolean notify64BitJava = false;
    public static String openGlVersion = null;
    public static String openGlRenderer = null;
    public static String openGlVendor = null;
    public static String[] openGlExtensions = null;
    public static GlVersion glVersion = null;
    public static GlVersion glslVersion = null;
    public static int minecraftVersionInt = -1;
    public static boolean fancyFogAvailable = false;
    public static boolean occlusionAvailable = false;
    private static GameSettings gameSettings = null;
    private static Minecraft minecraft = Minecraft.getMinecraft();
    private static boolean initialized = false;
    private static Thread minecraftThread = null;
    private static Display desktopDisplay = null;
    private static Display[] displayModes = new Display[1];
    private static int antialiasingLevel = 0;
    private static int availableProcessors = 0;
    public static boolean zoomMode = false;
    private static int texturePackClouds = 0;
    public static boolean waterOpacityChanged = false;
    private static boolean fullscreenModeChecked = false;
    private static boolean desktopModeChecked = false;
    private static DefaultResourcePack defaultResourcePackLazy = null;
    public static final Float DEF_ALPHA_FUNC_LEVEL = 0.1F;
    private static final Logger LOGGER = LogManager.getLogger();

    public static String getVersion()
    {
        return "OptiFine_1.12_HD_U_C4";
    }

    public static String getVersionDebug()
    {
        StringBuffer stringbuffer = new StringBuffer(32);

        if (isDynamicLights())
        {
            stringbuffer.append("DL: ");
            stringbuffer.append(String.valueOf(DynamicLights.getCount()));
            stringbuffer.append(", ");
        }

        stringbuffer.append("OptiFine_1.12_HD_U_C4");
        String s = "Stock graphics";

        if (s != null)
        {
            stringbuffer.append(", ");
            stringbuffer.append(s);
        }

        return stringbuffer.toString();
    }

    public static void initGameSettings(GameSettings p_initGameSettings_0_)
    {
        if (gameSettings == null)
        {
            gameSettings = p_initGameSettings_0_;
            //desktopDisplay = Display.getDesktopDisplay();
            updateAvailableProcessors();
            ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
        }
    }

    public static void initDisplay()
    {
        checkInitialized();
        antialiasingLevel = gameSettings.ofAaLevel;
        checkDisplaySettings();
        checkDisplay();
        minecraftThread = Thread.currentThread();
        updateThreadPriorities();
    }

    public static void checkInitialized()
    {
        if (!initialized)
        {
            if (true)
            {
                initialized = true;
                checkOpenGlCaps();
                startVersionCheckThread();
            }
        }
    }

    private static void checkOpenGlCaps()
    {
        log("");
        log(getVersion());
        log("Build: " + getBuild());
        log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        log("LWJGL: 3 or whatever Lax is using");
        log("OpenGL: " + RealOpenGLEnums.GL_VERSION + ", version " + RealOpenGLEnums.GL_RENDERER + ", " + RealOpenGLEnums.GL_VENDOR);
        log("OpenGL Version: " + getOpenGlVersionString());

        if (true)
        {
            log("OpenGL Mipmap levels: " + RealOpenGLEnums.GL_TEXTURE_MAX_LEVEL);
        }

        fancyFogAvailable = false;

        if (!fancyFogAvailable)
        {
            log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }

        occlusionAvailable = false;

        if (!occlusionAvailable)
        {
            log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }

        int i = TextureUtils.getGLMaximumTextureSize();
        dbg("Maximum texture size: " + i + "x" + i);
    }

    private static String getBuild()
    {
        try
        {
            InputStream inputstream = Config.class.getResourceAsStream("/buildof.txt");

            if (inputstream == null)
            {
                return null;
            }
            else
            {
                String s = readLines(inputstream)[0];
                return s;
            }
        }
        catch (Exception exception)
        {
            warn("" + exception.getClass().getName() + ": " + exception.getMessage());
            return null;
        }
    }

    public static boolean isFancyFogAvailable()
    {
        return fancyFogAvailable;
    }

    public static boolean isOcclusionAvailable()
    {
        return occlusionAvailable;
    }

    public static int getMinecraftVersionInt()
    {
        if (minecraftVersionInt < 0)
        {
            String[] astring = tokenize("1.12", ".");
            int i = 0;

            if (astring.length > 0)
            {
                i += 10000 * parseInt(astring[0], 0);
            }

            if (astring.length > 1)
            {
                i += 100 * parseInt(astring[1], 0);
            }

            if (astring.length > 2)
            {
                i += 1 * parseInt(astring[2], 0);
            }

            minecraftVersionInt = i;
        }

        return minecraftVersionInt;
    }

    public static String getOpenGlVersionString()
    {
        GlVersion glversion = getGlVersion();
        String s = "" + glversion.getMajor() + "." + glversion.getMinor() + "." + glversion.getRelease();
        return s;
    }

    private static GlVersion getGlVersionLwjgl()
    {
        return new GlVersion(1, 1);
    }

    public static GlVersion getGlVersion()
    {
        if (glVersion == null)
        {
            String s = "7938";
            glVersion = parseGlVersion(s, (GlVersion)null);

            if (glVersion == null)
            {
                glVersion = getGlVersionLwjgl();
            }

            if (glVersion == null)
            {
                glVersion = new GlVersion(1, 0);
            }
        }

        return glVersion;
    }

    public static GlVersion getGlslVersion()
    {
        if (glslVersion == null)
        {
            String s = "35724";
            glslVersion = parseGlVersion(s, (GlVersion)null);

            if (glslVersion == null)
            {
                glslVersion = new GlVersion(1, 10);
            }
        }

        return glslVersion;
    }

    public static GlVersion parseGlVersion(String p_parseGlVersion_0_, GlVersion p_parseGlVersion_1_)
    {
        try
        {
            if (p_parseGlVersion_0_ == null)
            {
                return p_parseGlVersion_1_;
            }
            else
            {
                Pattern pattern = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.+)?");
                Matcher matcher = pattern.matcher(p_parseGlVersion_0_);

                if (!matcher.matches())
                {
                    return p_parseGlVersion_1_;
                }
                else
                {
                    int i = Integer.parseInt(matcher.group(1));
                    int j = Integer.parseInt(matcher.group(2));
                    int k = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;
                    String s = matcher.group(5);
                    return new GlVersion(i, j, k, s);
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return p_parseGlVersion_1_;
        }
    }

    public static String[] getOpenGlExtensions()
    {
        if (openGlExtensions == null)
        {
            openGlExtensions = detectOpenGlExtensions();
        }

        return openGlExtensions;
    }

    private static String[] detectOpenGlExtensions()
    {
        try
        {
            GlVersion glversion = getGlVersion();

            if (glversion.getMajor() >= 3)
            {
                int i = 7939;

                if (i > 0)
                {
                    String[] astring = new String[i];

                    for (int j = 0; j < i; ++j)
                    {
                        astring[j] = "7939";
                    }

                    return astring;
                }
            }
        }
        catch (Exception exception1)
        {
            exception1.printStackTrace();
        }

        try
        {
            String s = "7939";
            String[] astring1 = s.split(" ");
            return astring1;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return new String[0];
        }
    }

    public static void updateThreadPriorities()
    {
        updateAvailableProcessors();
        int i = 8;

        if (isSingleProcessor())
        {
            if (isSmoothWorld())
            {
                minecraftThread.setPriority(10);
                setThreadPriority("Server thread", 1);
            }
            else
            {
                minecraftThread.setPriority(5);
                setThreadPriority("Server thread", 5);
            }
        }
        else
        {
            minecraftThread.setPriority(10);
            setThreadPriority("Server thread", 5);
        }
    }

    private static void setThreadPriority(String p_setThreadPriority_0_, int p_setThreadPriority_1_)
    {
        try
        {
            ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();

            if (threadgroup == null)
            {
                return;
            }

            int i = (threadgroup.activeCount() + 10) * 2;
            Thread[] athread = new Thread[i];
            threadgroup.enumerate(athread, false);

            for (int j = 0; j < athread.length; ++j)
            {
                Thread thread = athread[j];

                if (thread != null && thread.getName().startsWith(p_setThreadPriority_0_))
                {
                    thread.setPriority(p_setThreadPriority_1_);
                }
            }
        }
        catch (Throwable throwable)
        {
            warn(throwable.getClass().getName() + ": " + throwable.getMessage());
        }
    }

    public static boolean isMinecraftThread()
    {
        return Thread.currentThread() == minecraftThread;
    }

    private static void startVersionCheckThread()
    {
        VersionCheckThread versioncheckthread = new VersionCheckThread();
        versioncheckthread.start();
    }

    public static boolean isMipmaps()
    {
        return gameSettings.mipmapLevels > 0;
    }

    public static int getMipmapLevels()
    {
        return gameSettings.mipmapLevels;
    }

    public static int getMipmapType()
    {
        switch (gameSettings.ofMipmapType)
        {
            case 0:
                return 9986;

            case 1:
                return 9986;

            case 2:
                if (isMultiTexture())
                {
                    return 9985;
                }

                return 9986;

            case 3:
                if (isMultiTexture())
                {
                    return 9987;
                }

                return 9986;

            default:
                return 9986;
        }
    }

    public static boolean isUseAlphaFunc()
    {
        float f = getAlphaFuncLevel();
        return f > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
    }

    public static float getAlphaFuncLevel()
    {
        return DEF_ALPHA_FUNC_LEVEL.floatValue();
    }

    public static boolean isFogFancy()
    {
        if (!isFancyFogAvailable())
        {
            return false;
        }
        else
        {
            return gameSettings.ofFogType == 2;
        }
    }

    public static boolean isFogFast()
    {
        return gameSettings.ofFogType == 1;
    }

    public static boolean isFogOff()
    {
        return gameSettings.ofFogType == 3;
    }

    public static float getFogStart()
    {
        return gameSettings.ofFogStart;
    }

    public static void dbg(String p_dbg_0_)
    {
        LOGGER.info("[OptiFine] " + p_dbg_0_);
    }

    public static void warn(String p_warn_0_)
    {
        LOGGER.warn("[OptiFine] " + p_warn_0_);
    }

    public static void error(String p_error_0_)
    {
        LOGGER.error("[OptiFine] " + p_error_0_);
    }

    public static void log(String p_log_0_)
    {
        dbg(p_log_0_);
    }

    public static int getUpdatesPerFrame()
    {
        return gameSettings.ofChunkUpdates;
    }

    public static boolean isDynamicUpdates()
    {
        return gameSettings.ofChunkUpdatesDynamic;
    }

    public static boolean isRainFancy()
    {
        if (gameSettings.ofRain == 0)
        {
            return gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofRain == 2;
        }
    }

    public static boolean isRainOff()
    {
        return gameSettings.ofRain == 3;
    }

    public static boolean isCloudsFancy()
    {
        if (gameSettings.ofClouds != 0)
        {
            return gameSettings.ofClouds == 2;
        }
        else if (texturePackClouds != 0)
        {
            return texturePackClouds == 2;
        }
        else
        {
            return gameSettings.fancyGraphics;
        }
    }

    public static boolean isCloudsOff()
    {
        if (gameSettings.ofClouds != 0)
        {
            return gameSettings.ofClouds == 3;
        }
        else if (texturePackClouds != 0)
        {
            return texturePackClouds == 3;
        }
        else
        {
            return false;
        }
    }

    public static void updateTexturePackClouds()
    {
        texturePackClouds = 0;
        IResourceManager iresourcemanager = getResourceManager();

        if (iresourcemanager != null)
        {
            try
            {
                InputStream inputstream = iresourcemanager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();

                if (inputstream == null)
                {
                    return;
                }

                Properties properties = new Properties();
                properties.load(inputstream);
                inputstream.close();
                String s = properties.getProperty("clouds");

                if (s == null)
                {
                    return;
                }

                dbg("Texture pack clouds: " + s);
                s = s.toLowerCase();

                if (s.equals("fast"))
                {
                    texturePackClouds = 1;
                }

                if (s.equals("fancy"))
                {
                    texturePackClouds = 2;
                }

                if (s.equals("off"))
                {
                    texturePackClouds = 3;
                }
            }
            catch (Exception var4)
            {
                ;
            }
        }
    }

    public static ModelManager getModelManager()
    {
        return minecraft.getRenderItem().modelManager;
    }

    public static boolean isTreesFancy()
    {
        if (gameSettings.ofTrees == 0)
        {
            return gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofTrees != 1;
        }
    }

    public static boolean isTreesSmart()
    {
        return gameSettings.ofTrees == 4;
    }

    public static boolean isCullFacesLeaves()
    {
        if (gameSettings.ofTrees == 0)
        {
            return !gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofTrees == 4;
        }
    }

    public static boolean isDroppedItemsFancy()
    {
        if (gameSettings.ofDroppedItems == 0)
        {
            return gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofDroppedItems == 2;
        }
    }

    public static int limit(int p_limit_0_, int p_limit_1_, int p_limit_2_)
    {
        if (p_limit_0_ < p_limit_1_)
        {
            return p_limit_1_;
        }
        else
        {
            return p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_;
        }
    }

    public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_)
    {
        if (p_limit_0_ < p_limit_1_)
        {
            return p_limit_1_;
        }
        else
        {
            return p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_;
        }
    }

    public static double limit(double p_limit_0_, double p_limit_2_, double p_limit_4_)
    {
        if (p_limit_0_ < p_limit_2_)
        {
            return p_limit_2_;
        }
        else
        {
            return p_limit_0_ > p_limit_4_ ? p_limit_4_ : p_limit_0_;
        }
    }

    public static float limitTo1(float p_limitTo1_0_)
    {
        if (p_limitTo1_0_ < 0.0F)
        {
            return 0.0F;
        }
        else
        {
            return p_limitTo1_0_ > 1.0F ? 1.0F : p_limitTo1_0_;
        }
    }

    public static boolean isAnimatedWater()
    {
        return gameSettings.ofAnimatedWater != 2;
    }

    public static boolean isGeneratedWater()
    {
        return gameSettings.ofAnimatedWater == 1;
    }

    public static boolean isAnimatedPortal()
    {
        return gameSettings.ofAnimatedPortal;
    }

    public static boolean isAnimatedLava()
    {
        return gameSettings.ofAnimatedLava != 2;
    }

    public static boolean isGeneratedLava()
    {
        return gameSettings.ofAnimatedLava == 1;
    }

    public static boolean isAnimatedFire()
    {
        return gameSettings.ofAnimatedFire;
    }

    public static boolean isAnimatedRedstone()
    {
        return gameSettings.ofAnimatedRedstone;
    }

    public static boolean isAnimatedExplosion()
    {
        return gameSettings.ofAnimatedExplosion;
    }

    public static boolean isAnimatedFlame()
    {
        return gameSettings.ofAnimatedFlame;
    }

    public static boolean isAnimatedSmoke()
    {
        return gameSettings.ofAnimatedSmoke;
    }

    public static boolean isVoidParticles()
    {
        return gameSettings.ofVoidParticles;
    }

    public static boolean isWaterParticles()
    {
        return gameSettings.ofWaterParticles;
    }

    public static boolean isRainSplash()
    {
        return gameSettings.ofRainSplash;
    }

    public static boolean isPortalParticles()
    {
        return gameSettings.ofPortalParticles;
    }

    public static boolean isPotionParticles()
    {
        return gameSettings.ofPotionParticles;
    }

    public static boolean isFireworkParticles()
    {
        return gameSettings.ofFireworkParticles;
    }

    public static float getAmbientOcclusionLevel()
    {
        return gameSettings.ofAoLevel;
    }

    public static String arrayToString(Object[] p_arrayToString_0_)
    {
        if (p_arrayToString_0_ == null)
        {
            return "";
        }
        else
        {
            StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);

            for (int i = 0; i < p_arrayToString_0_.length; ++i)
            {
                Object object = p_arrayToString_0_[i];

                if (i > 0)
                {
                    stringbuffer.append(", ");
                }

                stringbuffer.append(String.valueOf(object));
            }

            return stringbuffer.toString();
        }
    }

    public static String arrayToString(int[] p_arrayToString_0_)
    {
        if (p_arrayToString_0_ == null)
        {
            return "";
        }
        else
        {
            StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);

            for (int i = 0; i < p_arrayToString_0_.length; ++i)
            {
                int j = p_arrayToString_0_[i];

                if (i > 0)
                {
                    stringbuffer.append(", ");
                }

                stringbuffer.append(String.valueOf(j));
            }

            return stringbuffer.toString();
        }
    }

    public static Minecraft getMinecraft()
    {
        return minecraft;
    }

    public static TextureManager getTextureManager()
    {
        return minecraft.getTextureManager();
    }

    public static IResourceManager getResourceManager()
    {
        return minecraft.getResourceManager();
    }

    public static InputStream getResourceStream(ResourceLocation p_getResourceStream_0_) throws IOException
    {
        return getResourceStream(minecraft.getResourceManager(), p_getResourceStream_0_);
    }

    public static InputStream getResourceStream(IResourceManager p_getResourceStream_0_, ResourceLocation p_getResourceStream_1_) throws IOException
    {
        IResource iresource = p_getResourceStream_0_.getResource(p_getResourceStream_1_);
        return iresource == null ? null : iresource.getInputStream();
    }

    public static IResource getResource(ResourceLocation p_getResource_0_) throws IOException
    {
        return minecraft.getResourceManager().getResource(p_getResource_0_);
    }

    public static boolean hasResource(ResourceLocation p_hasResource_0_)
    {
        IResourcePack iresourcepack = getDefiningResourcePack(p_hasResource_0_);
        return iresourcepack != null;
    }

    public static boolean hasResource(IResourceManager p_hasResource_0_, ResourceLocation p_hasResource_1_)
    {
        try
        {
            IResource iresource = p_hasResource_0_.getResource(p_hasResource_1_);
            return iresource != null;
        }
        catch (IOException var3)
        {
            return false;
        }
    }

    public static IResourcePack[] getResourcePacks()
    {
        ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
        List list = resourcepackrepository.getRepositoryEntries();
        List list1 = new ArrayList();

        for (Object resourcepackrepository$entry : list)
        {
            list1.add(((ResourcePackRepository.Entry) resourcepackrepository$entry).getResourcePack());
        }

        if (resourcepackrepository.getServerResourcePack() != null)
        {
            list1.add(resourcepackrepository.getServerResourcePack());
        }

        IResourcePack[] airesourcepack = (IResourcePack[])list1.toArray(new IResourcePack[list1.size()]);
        return airesourcepack;
    }

    public static String getResourcePackNames()
    {
        if (minecraft.getResourcePackRepository() == null)
        {
            return "";
        }
        else
        {
            IResourcePack[] airesourcepack = getResourcePacks();

            if (airesourcepack.length <= 0)
            {
                return getDefaultResourcePack().getPackName();
            }
            else
            {
                String[] astring = new String[airesourcepack.length];

                for (int i = 0; i < airesourcepack.length; ++i)
                {
                    astring[i] = airesourcepack[i].getPackName();
                }

                String s = arrayToString((Object[])astring);
                return s;
            }
        }
    }

    public static DefaultResourcePack getDefaultResourcePack()
    {
        if (defaultResourcePackLazy == null)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            defaultResourcePackLazy = (DefaultResourcePack)Reflector.getFieldValue(minecraft, Reflector.Minecraft_defaultResourcePack);

            if (defaultResourcePackLazy == null)
            {
                ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();

                if (resourcepackrepository != null)
                {
                    defaultResourcePackLazy = (DefaultResourcePack)resourcepackrepository.rprDefaultResourcePack;
                }
            }
        }

        return defaultResourcePackLazy;
    }

    public static boolean isFromDefaultResourcePack(ResourceLocation p_isFromDefaultResourcePack_0_)
    {
        IResourcePack iresourcepack = getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
        return iresourcepack == getDefaultResourcePack();
    }

    public static IResourcePack getDefiningResourcePack(ResourceLocation p_getDefiningResourcePack_0_)
    {
        ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
        IResourcePack iresourcepack = resourcepackrepository.getServerResourcePack();

        if (iresourcepack != null && iresourcepack.resourceExists(p_getDefiningResourcePack_0_))
        {
            return iresourcepack;
        }
        else
        {
            List<ResourcePackRepository.Entry> list = resourcepackrepository.repositoryEntries;

            for (int i = list.size() - 1; i >= 0; --i)
            {
                ResourcePackRepository.Entry resourcepackrepository$entry = list.get(i);
                IResourcePack iresourcepack1 = resourcepackrepository$entry.getResourcePack();

                if (iresourcepack1.resourceExists(p_getDefiningResourcePack_0_))
                {
                    return iresourcepack1;
                }
            }

            if (getDefaultResourcePack().resourceExists(p_getDefiningResourcePack_0_))
            {
                return getDefaultResourcePack();
            }
            else
            {
                return null;
            }
        }
    }

    public static RenderGlobal getRenderGlobal()
    {
        return minecraft.renderGlobal;
    }

    public static boolean isBetterGrass()
    {
        return gameSettings.ofBetterGrass != 3;
    }

    public static boolean isBetterGrassFancy()
    {
        return gameSettings.ofBetterGrass == 2;
    }

    public static boolean isWeatherEnabled()
    {
        return gameSettings.ofWeather;
    }

    public static boolean isSkyEnabled()
    {
        return gameSettings.ofSky;
    }

    public static boolean isSunMoonEnabled()
    {
        return gameSettings.ofSunMoon;
    }

    public static boolean isSunTexture()
    {
        if (!isSunMoonEnabled())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isMoonTexture()
    {
        if (!isSunMoonEnabled())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isVignetteEnabled()
    {
        if (gameSettings.ofVignette == 0)
        {
            return gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofVignette == 2;
        }
    }

    public static boolean isStarsEnabled()
    {
        return gameSettings.ofStars;
    }

    public static void sleep(long p_sleep_0_)
    {
        try
        {
            Thread.sleep(p_sleep_0_);
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
    }

    public static boolean isTimeDayOnly()
    {
        return gameSettings.ofTime == 1;
    }

    public static boolean isTimeDefault()
    {
        return gameSettings.ofTime == 0;
    }

    public static boolean isTimeNightOnly()
    {
        return gameSettings.ofTime == 2;
    }

    public static boolean isClearWater()
    {
        return gameSettings.ofClearWater;
    }

    public static int getAnisotropicFilterLevel()
    {
        return gameSettings.ofAfLevel;
    }

    public static boolean isAnisotropicFiltering()
    {
        return getAnisotropicFilterLevel() > 1;
    }

    public static int getAntialiasingLevel()
    {
        return antialiasingLevel;
    }

    public static boolean isAntialiasing()
    {
        return getAntialiasingLevel() > 0;
    }

    public static boolean isAntialiasingConfigured()
    {
        return getGameSettings().ofAaLevel > 0;
    }

    public static boolean isMultiTexture()
    {
        if (getAnisotropicFilterLevel() > 1)
        {
            return true;
        }
        else
        {
            return getAntialiasingLevel() > 0;
        }
    }

    public static boolean between(int p_between_0_, int p_between_1_, int p_between_2_)
    {
        return p_between_0_ >= p_between_1_ && p_between_0_ <= p_between_2_;
    }

    public static boolean isDrippingWaterLava()
    {
        return gameSettings.ofDrippingWaterLava;
    }

    public static boolean isBetterSnow()
    {
        return gameSettings.ofBetterSnow;
    }

    public static Dimension getFullscreenDimension()
    {
        if (desktopDisplay == null)
        {
            return null;
        }
        else if (gameSettings == null)
        {
            return new Dimension(desktopDisplay.getWidth(), desktopDisplay.getHeight());
        }
        else
        {
            String s = gameSettings.ofFullscreenMode;

            if (s.equals("Default"))
            {
                return new Dimension(desktopDisplay.getWidth(), desktopDisplay.getHeight());
            }
            else
            {
                String[] astring = tokenize(s, " x");
                return astring.length < 2 ? new Dimension(desktopDisplay.getWidth(), desktopDisplay.getHeight()) : new Dimension(parseInt(astring[0], -1), parseInt(astring[1], -1));
            }
        }
    }

    public static int parseInt(String p_parseInt_0_, int p_parseInt_1_)
    {
        try
        {
            if (p_parseInt_0_ == null)
            {
                return p_parseInt_1_;
            }
            else
            {
                p_parseInt_0_ = p_parseInt_0_.trim();
                return Integer.parseInt(p_parseInt_0_);
            }
        }
        catch (NumberFormatException var3)
        {
            return p_parseInt_1_;
        }
    }

    public static float parseFloat(String p_parseFloat_0_, float p_parseFloat_1_)
    {
        try
        {
            if (p_parseFloat_0_ == null)
            {
                return p_parseFloat_1_;
            }
            else
            {
                p_parseFloat_0_ = p_parseFloat_0_.trim();
                return Float.parseFloat(p_parseFloat_0_);
            }
        }
        catch (NumberFormatException var3)
        {
            return p_parseFloat_1_;
        }
    }

    public static boolean parseBoolean(String p_parseBoolean_0_, boolean p_parseBoolean_1_)
    {
        try
        {
            if (p_parseBoolean_0_ == null)
            {
                return p_parseBoolean_1_;
            }
            else
            {
                p_parseBoolean_0_ = p_parseBoolean_0_.trim();
                return Boolean.parseBoolean(p_parseBoolean_0_);
            }
        }
        catch (NumberFormatException var3)
        {
            return p_parseBoolean_1_;
        }
    }

    public static String[] tokenize(String p_tokenize_0_, String p_tokenize_1_)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
        List list = new ArrayList();

        while (stringtokenizer.hasMoreTokens())
        {
            String s = stringtokenizer.nextToken();
            list.add(s);
        }

        String[] astring = (String[])list.toArray(new String[list.size()]);
        return astring;
    }

    public static Display getDesktopDisplay()
    {
        return desktopDisplay;
    }

    public static Display[] getDisplays()
    {
        if (displayModes == null)
        {
            try
            {
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                displayModes = new Display[] {desktopDisplay};
            }
        }

        return displayModes;
    }

    public static Display getLargestDisplay()
    {
        Display[] adisplaymode = getDisplays();

        if (adisplaymode != null && adisplaymode.length >= 1)
        {
            Display displaymode = adisplaymode[adisplaymode.length - 1];

            if (desktopDisplay.getWidth() > displaymode.getWidth())
            {
                return desktopDisplay;
            }
            else
            {
                return desktopDisplay.getWidth() == displaymode.getWidth() && desktopDisplay.getHeight() > displaymode.getHeight() ? desktopDisplay : displaymode;
            }
        }
        else
        {
            return desktopDisplay;
        }
    }

    private static Set<Dimension> getDisplayDimensions(Display[] p_getDisplayDimensions_0_)
    {
        Set<Dimension> set = new HashSet<Dimension>();

        for (int i = 0; i < p_getDisplayDimensions_0_.length; ++i)
        {
            Display displaymode = p_getDisplayDimensions_0_[i];
            Dimension dimension = new Dimension(displaymode.getWidth(), displaymode.getHeight());
            set.add(dimension);
        }

        return set;
    }

    private static Display[] getDisplays(Display[] p_getDisplays_0_, Dimension p_getDisplays_1_)
    {
        List list = new ArrayList();

        for (int i = 0; i < p_getDisplays_0_.length; ++i)
        {
            Display displaymode = p_getDisplays_0_[i];

            if ((double)displaymode.getWidth() == p_getDisplays_1_.getWidth() && (double)displaymode.getHeight() == p_getDisplays_1_.getHeight())
            {
                list.add(displaymode);
            }
        }

        Display[] adisplaymode = (Display[])list.toArray(new Display[list.size()]);
        return adisplaymode;
    }

    private static Display getDisplay(Display[] p_getDisplay_0_, Display p_getDisplay_1_)
    {
        if (p_getDisplay_1_ != null)
        {
            for (int i = 0; i < p_getDisplay_0_.length; ++i)
            {
                Display displaymode = p_getDisplay_0_[i];

                if (false/*displaymode.getBitsPerPixel() == p_getDisplay_1_.getBitsPerPixel() && displaymode.getFrequency() == p_getDisplay_1_.getFrequency()*/)
                {
                    return displaymode;
                }
            }
        }

        if (p_getDisplay_0_.length <= 0)
        {
            return null;
        }
        else
        {
            return p_getDisplay_0_[p_getDisplay_0_.length - 1];
        }
    }

    public static String[] getDisplayNames()
    {
        Display[] adisplaymode = getDisplays();
        String[] astring = new String[adisplaymode.length];

        for (int i = 0; i < adisplaymode.length; ++i)
        {
            Display displaymode = adisplaymode[i];
            String s = "" + displaymode.getWidth() + "x" + displaymode.getHeight();
            astring[i] = s;
        }

        return astring;
    }

    public static Display getDisplay(Dimension p_getDisplay_0_) throws Exception
    {
        Display[] adisplaymode = getDisplays();

        for (int i = 0; i < adisplaymode.length; ++i)
        {
            Display displaymode = adisplaymode[i];

            if (displaymode.getWidth() == p_getDisplay_0_.width && displaymode.getHeight() == p_getDisplay_0_.height)
            {
                return displaymode;
            }
        }

        return desktopDisplay;
    }

    public static boolean isAnimatedTerrain()
    {
        return gameSettings.ofAnimatedTerrain;
    }

    public static boolean isAnimatedTextures()
    {
        return gameSettings.ofAnimatedTextures;
    }

    public static boolean isSwampColors()
    {
        return gameSettings.ofSwampColors;
    }

    public static boolean isRandomMobs()
    {
        return gameSettings.ofRandomMobs;
    }

    public static void checkGlError(String p_checkGlError_0_)
    {
        int i = 0;

        if (i != 0)
        {
            String s = "0";
            error("OpenGlError: " + i + " (" + s + "), at: " + p_checkGlError_0_);
        }
    }

    public static boolean isSmoothBiomes()
    {
        return gameSettings.ofSmoothBiomes;
    }

    public static boolean isCustomColors()
    {
        return gameSettings.ofCustomColors;
    }

    public static boolean isCustomSky()
    {
        return gameSettings.ofCustomSky;
    }

    public static boolean isCustomFonts()
    {
        return gameSettings.ofCustomFonts;
    }

    public static boolean isShowCapes()
    {
        return gameSettings.ofShowCapes;
    }

    public static boolean isConnectedTextures()
    {
        return gameSettings.ofConnectedTextures != 3;
    }

    public static boolean isNaturalTextures()
    {
        return gameSettings.ofNaturalTextures;
    }

    public static boolean isConnectedTexturesFancy()
    {
        return gameSettings.ofConnectedTextures == 2;
    }

    public static boolean isFastRender()
    {
        return gameSettings.ofFastRender;
    }

    public static boolean isTranslucentBlocksFancy()
    {
        if (gameSettings.ofTranslucentBlocks == 0)
        {
            return gameSettings.fancyGraphics;
        }
        else
        {
            return gameSettings.ofTranslucentBlocks == 2;
        }
    }

    public static boolean isShaders()
    {
        return false;
    }

    public static String[] readLines(File p_readLines_0_) throws IOException
    {
        FileInputStream fileinputstream = new FileInputStream(p_readLines_0_);
        return readLines(fileinputstream);
    }

    public static String[] readLines(InputStream p_readLines_0_) throws IOException
    {
        List list = new ArrayList();
        InputStreamReader inputstreamreader = new InputStreamReader(p_readLines_0_, "ASCII");
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        while (true)
        {
            String s = bufferedreader.readLine();

            if (s == null)
            {
                String[] astring = (String[])list.toArray(new String[list.size()]);
                return astring;
            }

            list.add(s);
        }
    }

    public static String readFile(File p_readFile_0_) throws IOException
    {
        FileInputStream fileinputstream = new FileInputStream(p_readFile_0_);
        return readInputStream(fileinputstream, "ASCII");
    }

    public static String readInputStream(InputStream p_readInputStream_0_) throws IOException
    {
        return readInputStream(p_readInputStream_0_, "ASCII");
    }

    public static String readInputStream(InputStream p_readInputStream_0_, String p_readInputStream_1_) throws IOException
    {
        InputStreamReader inputstreamreader = new InputStreamReader(p_readInputStream_0_, p_readInputStream_1_);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        StringBuffer stringbuffer = new StringBuffer();

        while (true)
        {
            String s = bufferedreader.readLine();

            if (s == null)
            {
                return stringbuffer.toString();
            }

            stringbuffer.append(s);
            stringbuffer.append("\n");
        }
    }

    public static byte[] readAll(InputStream p_readAll_0_) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] abyte = new byte[1024];

        while (true)
        {
            int i = p_readAll_0_.read(abyte);

            if (i < 0)
            {
                p_readAll_0_.close();
                byte[] abyte1 = bytearrayoutputstream.toByteArray();
                return abyte1;
            }

            bytearrayoutputstream.write(abyte, 0, i);
        }
    }

    public static GameSettings getGameSettings()
    {
        return gameSettings;
    }

    public static String getNewRelease()
    {
        return newRelease;
    }

    public static void setNewRelease(String p_setNewRelease_0_)
    {
        newRelease = p_setNewRelease_0_;
    }

    public static int compareRelease(String p_compareRelease_0_, String p_compareRelease_1_)
    {
        String[] astring = splitRelease(p_compareRelease_0_);
        String[] astring1 = splitRelease(p_compareRelease_1_);
        String s = astring[0];
        String s1 = astring1[0];

        if (!s.equals(s1))
        {
            return s.compareTo(s1);
        }
        else
        {
            int i = parseInt(astring[1], -1);
            int j = parseInt(astring1[1], -1);

            if (i != j)
            {
                return i - j;
            }
            else
            {
                String s2 = astring[2];
                String s3 = astring1[2];

                if (!s2.equals(s3))
                {
                    if (s2.isEmpty())
                    {
                        return 1;
                    }

                    if (s3.isEmpty())
                    {
                        return -1;
                    }
                }

                return s2.compareTo(s3);
            }
        }
    }

    private static String[] splitRelease(String p_splitRelease_0_)
    {
        if (p_splitRelease_0_ != null && p_splitRelease_0_.length() > 0)
        {
            Pattern pattern = Pattern.compile("([A-Z])([0-9]+)(.*)");
            Matcher matcher = pattern.matcher(p_splitRelease_0_);

            if (!matcher.matches())
            {
                return new String[] {"", "", ""};
            }
            else
            {
                String s = normalize(matcher.group(1));
                String s1 = normalize(matcher.group(2));
                String s2 = normalize(matcher.group(3));
                return new String[] {s, s1, s2};
            }
        }
        else
        {
            return new String[] {"", "", ""};
        }
    }

    public static int intHash(int p_intHash_0_)
    {
        p_intHash_0_ = p_intHash_0_ ^ 61 ^ p_intHash_0_ >> 16;
        p_intHash_0_ = p_intHash_0_ + (p_intHash_0_ << 3);
        p_intHash_0_ = p_intHash_0_ ^ p_intHash_0_ >> 4;
        p_intHash_0_ = p_intHash_0_ * 668265261;
        p_intHash_0_ = p_intHash_0_ ^ p_intHash_0_ >> 15;
        return p_intHash_0_;
    }

    public static int getRandom(BlockPos p_getRandom_0_, int p_getRandom_1_)
    {
        int i = intHash(p_getRandom_1_ + 37);
        i = intHash(i + p_getRandom_0_.getX());
        i = intHash(i + p_getRandom_0_.getZ());
        i = intHash(i + p_getRandom_0_.getY());
        return i;
    }

    public static WorldServer getWorldServer()
    {
        World world = minecraft.world;

        if (world == null)
        {
            return null;
        }
        else if (!minecraft.isIntegratedServerRunning())
        {
            return null;
        }
        else
        {
            return null;
        }
    }

    public static int getAvailableProcessors()
    {
        return availableProcessors;
    }

    public static void updateAvailableProcessors()
    {
        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public static boolean isSingleProcessor()
    {
        return getAvailableProcessors() <= 1;
    }

    public static boolean isSmoothWorld()
    {
        return gameSettings.ofSmoothWorld;
    }

    public static boolean isLazyChunkLoading()
    {
        return !isSingleProcessor() ? false : gameSettings.ofLazyChunkLoading;
    }

    public static boolean isDynamicFov()
    {
        return gameSettings.ofDynamicFov;
    }

    public static boolean isAlternateBlocks()
    {
        return gameSettings.ofAlternateBlocks;
    }

    public static int getChunkViewDistance()
    {
        if (gameSettings == null)
        {
            return 10;
        }
        else
        {
            int i = gameSettings.renderDistanceChunks;
            return i;
        }
    }

    public static boolean equals(Object p_equals_0_, Object p_equals_1_)
    {
        if (p_equals_0_ == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_0_ == null ? false : p_equals_0_.equals(p_equals_1_);
        }
    }

    public static boolean equalsOne(Object p_equalsOne_0_, Object[] p_equalsOne_1_)
    {
        if (p_equalsOne_1_ == null)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < p_equalsOne_1_.length; ++i)
            {
                Object object = p_equalsOne_1_[i];

                if (equals(p_equalsOne_0_, object))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isSameOne(Object p_isSameOne_0_, Object[] p_isSameOne_1_)
    {
        if (p_isSameOne_1_ == null)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < p_isSameOne_1_.length; ++i)
            {
                Object object = p_isSameOne_1_[i];

                if (p_isSameOne_0_ == object)
                {
                    return true;
                }
            }

            return false;
        }
    }

    public static String normalize(String p_normalize_0_)
    {
        return p_normalize_0_ == null ? "" : p_normalize_0_;
    }

    public static void checkDisplaySettings()
    {
        int i = getAntialiasingLevel();

        if (i > 0)
        {
            //Display displaymode = Display.getDisplay();
            dbg("FSAA Samples: " + i);

            try
            {
                /*Display.destroy();
                Display.setDisplay(displaymode);
                Display.create((new PixelFormat()).withDepthBits(24).withSamples(i));
                Display.setResizable(false);
                Display.setResizable(true);*/
            }
            catch (Exception lwjglexception2)
            {
                warn("Error setting FSAA: " + i + "x");
                lwjglexception2.printStackTrace();

                try
                {
                    /*Display.setDisplay(displaymode);
                    Display.create((new PixelFormat()).withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);*/
                }
                catch (Exception lwjglexception1)
                {
                    lwjglexception1.printStackTrace();

                    try
                    {
                        /*Display.setDisplay(displaymode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);*/
                    }
                    catch (Exception lwjglexception)
                    {
                        lwjglexception.printStackTrace();
                    }
                }
            }

            if (!Minecraft.IS_RUNNING_ON_MAC && getDefaultResourcePack() != null)
            {
                InputStream inputstream = null;
                InputStream inputstream1 = null;

                try
                {
                    inputstream = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                    inputstream1 = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));

                    if (inputstream != null && inputstream1 != null)
                    {
                        //Display.setIcon(new ByteBuffer[] {readIconImage(inputstream), readIconImage(inputstream1)});
                    }
                }
                catch (IOException ioexception)
                {
                    warn("Error setting window icon: " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
                }
                finally
                {
                    IOUtils.closeQuietly(inputstream);
                    IOUtils.closeQuietly(inputstream1);
                }
            }
        }
    }

    private static ByteBuffer readIconImage(InputStream p_readIconImage_0_) throws IOException
    {
        /*ImageData bufferedimage = ImageIO.read(p_readIconImage_0_);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.width, bufferedimage.height, (int[])null, 0, bufferedimage.height);
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

        for (int i : aint)
        {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;*/
        return null;
    }

    public static void checkDisplay()
    {
        try
        {
            if (minecraft.isFullScreen())
            {
                if (fullscreenModeChecked)
                {
                    return;
                }

                fullscreenModeChecked = true;
                desktopModeChecked = false;
                Display displaymode = desktopDisplay;
                Dimension dimension = getFullscreenDimension();

                if (dimension == null)
                {
                    return;
                }

                if (displaymode.getWidth() == dimension.width && displaymode.getHeight() == dimension.height)
                {
                    return;
                }

                Display displaymode1 = getDisplay(dimension);

                if (displaymode1 == null)
                {
                    return;
                }

                //Display.setDisplay(displaymode1);
                minecraft.displayWidth = desktopDisplay.getWidth();
                minecraft.displayHeight = desktopDisplay.getHeight();

                if (minecraft.displayWidth <= 0)
                {
                    minecraft.displayWidth = 1;
                }

                if (minecraft.displayHeight <= 0)
                {
                    minecraft.displayHeight = 1;
                }

                if (minecraft.currentScreen != null)
                {
                    ScaledResolution scaledresolution = new ScaledResolution(minecraft);
                    int i = scaledresolution.getScaledWidth();
                    int j = scaledresolution.getScaledHeight();
                    minecraft.currentScreen.setWorldAndResolution(minecraft, i, j);
                }

                minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
                updateFramebufferSize();
                //Display.setFullscreen(true);
                minecraft.gameSettings.updateVSync();
                GlStateManager.enableTexture2D();
            }
            else
            {
                if (desktopModeChecked)
                {
                    return;
                }

                desktopModeChecked = true;
                fullscreenModeChecked = false;
                minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.enableTexture2D();
                //Display.setResizable(false);
                //Display.setResizable(true);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            gameSettings.ofFullscreenMode = "Default";
            gameSettings.saveOfOptions();
        }
    }

    public static void updateFramebufferSize()
    {
        minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);

        if (minecraft.entityRenderer != null)
        {
            minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
        }
    }

    public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_)
    {
        if (p_addObjectToArray_0_ == null)
        {
            throw new NullPointerException("The given array is NULL");
        }
        else
        {
            int i = p_addObjectToArray_0_.length;
            int j = i + 1;
            Object[] aobject = (Object[]) Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), j);
            System.arraycopy(p_addObjectToArray_0_, 0, aobject, 0, i);
            aobject[i] = p_addObjectToArray_1_;
            return aobject;
        }
    }

    public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_, int p_addObjectToArray_2_)
    {
        List list = new ArrayList(Arrays.asList(p_addObjectToArray_0_));
        list.add(p_addObjectToArray_2_, p_addObjectToArray_1_);
        Object[] aobject = (Object[]) Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), list.size());
        return list.toArray(aobject);
    }

    public static Object[] addObjectsToArray(Object[] p_addObjectsToArray_0_, Object[] p_addObjectsToArray_1_)
    {
        if (p_addObjectsToArray_0_ == null)
        {
            throw new NullPointerException("The given array is NULL");
        }
        else if (p_addObjectsToArray_1_.length == 0)
        {
            return p_addObjectsToArray_0_;
        }
        else
        {
            int i = p_addObjectsToArray_0_.length;
            int j = i + p_addObjectsToArray_1_.length;
            Object[] aobject = (Object[]) Array.newInstance(p_addObjectsToArray_0_.getClass().getComponentType(), j);
            System.arraycopy(p_addObjectsToArray_0_, 0, aobject, 0, i);
            System.arraycopy(p_addObjectsToArray_1_, 0, aobject, i, p_addObjectsToArray_1_.length);
            return aobject;
        }
    }

    public static boolean isCustomItems()
    {
        return gameSettings.ofCustomItems;
    }

    public static void drawFps()
    {
        int i = Minecraft.getDebugFPS();
        String s = getUpdates(minecraft.debug);
        int j = minecraft.renderGlobal.getCountActiveRenderers();
        int k = minecraft.renderGlobal.getCountEntitiesRendered();
        int l = minecraft.renderGlobal.getCountTileEntitiesRendered();
        String s1 = "" + i + " fps, C: " + j + ", E: " + k + "+" + l + ", U: " + s;
        minecraft.fontRenderer.drawString(s1, 2, 2, -2039584);
    }

    private static String getUpdates(String p_getUpdates_0_)
    {
        int i = p_getUpdates_0_.indexOf(40);

        if (i < 0)
        {
            return "";
        }
        else
        {
            int j = p_getUpdates_0_.indexOf(32, i);
            return j < 0 ? "" : p_getUpdates_0_.substring(i + 1, j);
        }
    }

    public static int getBitsOs()
    {
        String s = System.getenv("ProgramFiles(X86)");
        return s != null ? 64 : 32;
    }

    public static int getBitsJre()
    {
        String[] astring = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            String s1 = System.getProperty(s);

            if (s1 != null && s1.contains("64"))
            {
                return 64;
            }
        }

        return 32;
    }

    public static boolean isNotify64BitJava()
    {
        return notify64BitJava;
    }

    public static void setNotify64BitJava(boolean p_setNotify64BitJava_0_)
    {
        notify64BitJava = p_setNotify64BitJava_0_;
    }

    public static boolean isConnectedModels()
    {
        return false;
    }

    public static void showGuiMessage(String p_showGuiMessage_0_, String p_showGuiMessage_1_)
    {
        GuiMessage guimessage = new GuiMessage(minecraft.currentScreen, p_showGuiMessage_0_, p_showGuiMessage_1_);
        minecraft.displayGuiScreen(guimessage);
    }

    public static int[] addIntToArray(int[] p_addIntToArray_0_, int p_addIntToArray_1_)
    {
        return addIntsToArray(p_addIntToArray_0_, new int[] {p_addIntToArray_1_});
    }

    public static int[] addIntsToArray(int[] p_addIntsToArray_0_, int[] p_addIntsToArray_1_)
    {
        if (p_addIntsToArray_0_ != null && p_addIntsToArray_1_ != null)
        {
            int i = p_addIntsToArray_0_.length;
            int j = i + p_addIntsToArray_1_.length;
            int[] aint = new int[j];
            System.arraycopy(p_addIntsToArray_0_, 0, aint, 0, i);

            for (int k = 0; k < p_addIntsToArray_1_.length; ++k)
            {
                aint[k + i] = p_addIntsToArray_1_[k];
            }

            return aint;
        }
        else
        {
            throw new NullPointerException("The given array is NULL");
        }
    }

    public static DynamicTexture getMojangLogoTexture(DynamicTexture p_getMojangLogoTexture_0_)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation("textures/gui/title/eagtek.png");
            InputStream inputstream = getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return p_getMojangLogoTexture_0_;
            }
            else
            {
                ImageData bufferedimage = null;

                if (bufferedimage == null)
                {
                    return p_getMojangLogoTexture_0_;
                }
                else
                {
                    DynamicTexture dynamictexture = new DynamicTexture(bufferedimage);
                    return dynamictexture;
                }
            }
        }
        catch (Exception exception)
        {
            warn(exception.getClass().getName() + ": " + exception.getMessage());
            return p_getMojangLogoTexture_0_;
        }
    }

    public static void writeFile(File p_writeFile_0_, String p_writeFile_1_) throws IOException
    {
        FileOutputStream fileoutputstream = new FileOutputStream(p_writeFile_0_);
        byte[] abyte = p_writeFile_1_.getBytes("ASCII");
        fileoutputstream.write(abyte);
        fileoutputstream.close();
    }

    public static TextureMap getTextureMap()
    {
        return getMinecraft().getTextureMapBlocks();
    }

    public static boolean isDynamicLights()
    {
        return gameSettings.ofDynamicLights != 3;
    }

    public static boolean isDynamicLightsFast()
    {
        return gameSettings.ofDynamicLights == 1;
    }

    public static boolean isDynamicHandLight()
    {
        if (!isDynamicLights())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isCustomEntityModels()
    {
        return gameSettings.ofCustomEntityModels;
    }

    public static int getScreenshotSize()
    {
        return gameSettings.ofScreenshotSize;
    }
}
