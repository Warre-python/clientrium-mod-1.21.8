package be.warrox.clientrium;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clientrium implements ModInitializer {
	public static final String MOD_ID = "clientrium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializeing ModInitialize");
	}
}