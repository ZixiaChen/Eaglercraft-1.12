package net.minecraft.client.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Main
{
    public static void main(String[] p_main_0_)
    {

		/*String username = null;
		if(username == null){
			String[] defaultNames = new String[] {
				"Yeeish", "Yeeish", "Yee", "Yee", "Yeer", "Yeeler", "Eagler", "Eagl",
				"Darver", "Darvler", "Vool", "Vigg", "Vigg", "Deev", "Yigg", "Yeeg"
			};
			Random rand = new Random();
			username = defaultNames[rand.nextInt(defaultNames.length)] + defaultNames[rand.nextInt(defaultNames.length)] + (100 + rand.nextInt(900));
		}*/

        GameConfiguration gameconfiguration = new GameConfiguration(
				new GameConfiguration.UserInformation(new Session()),
				new GameConfiguration.DisplayInformation(854, 480, false, true),
                new GameConfiguration.FolderInformation(new File("."), new File(new File("."), "assets/"), new File(new File("."), "resourcepacks/"), "1.8"),
				new GameConfiguration.GameInformation(false, "1.12", "release"),
                new GameConfiguration.ServerInformation(null, 25565));
		PlatformRuntime.setThreadName("Client thread");
        /*Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
        {
            public void run()
            {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");*/
        (new Minecraft(gameconfiguration)).run();
    }

    /**
     * Returns whether a string is either null or empty.
     */
    private static boolean isNullOrEmpty(String str)
    {
        return str != null && !str.isEmpty();
    }
}