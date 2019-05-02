package com.balancedbytes.game.ashes;

import com.jdotsoft.jarloader.JarClassLoader;

public class JarLauncher {
	
    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        try {
            jcl.invokeMain("com.balancedbytes.game.ashes.AshesOfEmpire", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
	}

}
